package com.oestjacobsen.android.get2gether.services;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.oestjacobsen.android.get2gether.DatabasePicker;
import com.oestjacobsen.android.get2gether.UserManager;
import com.oestjacobsen.android.get2gether.UserManagerImpl;
import com.oestjacobsen.android.get2gether.model.BaseDatabase;
import com.oestjacobsen.android.get2gether.model.RealmDatabase;
import com.oestjacobsen.android.get2gether.model.User;

import org.altbeacon.beacon.BeaconManager;

import io.realm.Realm;


public class LocationService extends Service {

    private static final String TAG = "LocationService";
    public LocationManager mLocationManager;
    public MyLocationListener mLocationListener;
    private static final int TWO_MINUTES = 1000 * 60 * 2;
    public Location mPreviousBestLocation;
    private BaseDatabase mDatabase;
    private Handler mHandler;


    //private static final int COARSE_PERMISSION_REQUEST_CODE = 1111;
    //private static final int FINE_PERMISSION_REQUEST_CODE = 2222;

    UserManager mUserManager;
    User mCurrentUser;
    private String mUserUUID;

    Intent mIntent;
    //int counter = 0;

    @Override
    public void onCreate() {
        super.onCreate();

        mLocationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);
        mLocationListener = new MyLocationListener();
        mHandler = new Handler();
        mDatabase = DatabasePicker.getChosenDatabase(this);


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mUserUUID = intent.getStringExtra("USERUUID");
        checkPermission();

        mLocationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER, 4000, 0, mLocationListener);
        mLocationManager.requestLocationUpdates
                (LocationManager.GPS_PROVIDER, 4000, 0, mLocationListener);

        return START_REDELIVER_INTENT;
    }

    private void runOnUiThread(Runnable runnable) {
        mHandler.post(runnable);
    }

    protected boolean isBetterLocation(Location location) {
        if(mPreviousBestLocation == null) {
            return true;
        }

        long timeDelta = location.getTime() - mPreviousBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
        boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
        boolean isNewer = timeDelta > 0;

        if (isSignificantlyNewer) {
            return true;
            // If the new location is more than two minutes older, it must be worse
        } else if (isSignificantlyOlder) {
            return false;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (location.getAccuracy() - mPreviousBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(location.getProvider(),
                mPreviousBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and accuracy
        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return true;
        }

        return false;
    }

    /** Checks whether two providers are the same */
    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        checkPermission();
        mLocationManager.removeUpdates(mLocationListener);
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= 23) {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Get2Gether needs your permission to get locationUpdates", Toast.LENGTH_SHORT).show();
            }

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Get2Gether needs your permission to get locationUpdates", Toast.LENGTH_SHORT).show();
            }
        }

    }


    public class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            if(isBetterLocation(location)) {
                Log.i(TAG, "Lat: " + location.getLatitude() + " - Lng: " + location.getLongitude());
                try {
                    final Location fLocation = location;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mDatabase.updateUserPosition(mUserUUID, fLocation);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }



                /*Realm realm = Realm.getDefaultInstance();
                if(realm != null) {
                    try {
                        final User fUser = realm.where(User.class).equalTo("mUUID", mUserUUID).findFirst();
                        final Location fLocation = location;

                        if (fUser != null) {
                            realm.executeTransactionAsync(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    fUser.setLatitude(fLocation.getLatitude());
                                    fUser.setLongitude(fLocation.getLongitude());
                                    realm.copyToRealmOrUpdate(fUser);
                                }
                            });
                        }
                    } finally {
                        realm.close();
                    }
                }*/
            }
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    }
}
