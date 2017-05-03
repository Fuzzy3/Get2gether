package com.oestjacobsen.android.get2gether.services;

import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.app.Service;


import com.oestjacobsen.android.get2gether.DatabasePicker;
import com.oestjacobsen.android.get2gether.UserManager;
import com.oestjacobsen.android.get2gether.UserManagerImpl;
import com.oestjacobsen.android.get2gether.model.BaseDatabase;
import com.oestjacobsen.android.get2gether.model.RealmDatabase;
import com.oestjacobsen.android.get2gether.model.User;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.Collection;

import io.realm.Realm;

public class BeaconCollecterServiceAltLib extends Service implements BeaconConsumer {

    private BeaconManager mBeaconManager;
    private static final String TAG = "BeaconCollecterAlt";
    public static final String BROADCAST_INDOOR_ACTION = "BROADCASTINDOORACTION";
    private BaseDatabase mDatabase;
    UserManager mUserManager;
    User mCurrentUser;
    //private Intent mIntent;
    //private String fUUID;
    private Handler mHandler;


    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        mDatabase = DatabasePicker.getChosenDatabase(this);
        mHandler = new Handler();

        //mIntent = new Intent();
        //mIntent.setAction(BROADCAST_INDOOR_ACTION);
    }

    private void setupBeaconCollection() {
        mBeaconManager = BeaconManager.getInstanceForApplication(this);
        mBeaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));
        mBeaconManager.setBackgroundScanPeriod(5000l);

       /*try {
            mBeaconManager.updateScanPeriods();
        } catch (RemoteException e) {
            e.printStackTrace();
        }*/

        mBeaconManager.bind(this);
    }

    private void runOnUiThread(Runnable runnable) {
        mHandler.post(runnable);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mUserManager = UserManagerImpl.get();
        mCurrentUser = mUserManager.getUser();
        //fUUID = mCurrentUser.getUUID();
        setupBeaconCollection();

        Log.i(TAG, "Beacon service started");


        return START_STICKY;
    }

    @Override
    public void onBeaconServiceConnect() {
        Log.i(TAG, "Beacon connected to service ***************************** ");

        mBeaconManager.addRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> collection, Region region) {
                Log.i(TAG, "FOUND BEACONS");
                findNearestBeacon(collection);

            }
        });

        try {
            mBeaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
        } catch (RemoteException e) {    }

        try {
            mBeaconManager.startMonitoringBeaconsInRegion(new Region("myMonitoringUniqueId", null, null, null));
        } catch (RemoteException e) {
            //NOTING RIGHT NOW
        }


        mBeaconManager.addMonitorNotifier(new MonitorNotifier() {

            @Override
            public void didEnterRegion(Region region) {
                Log.i(TAG, "I just saw an beacon for the first time!");

            }

            @Override
            public void didExitRegion(Region region) {
                Log.i(TAG, "I no longer see a beacon");

            }

            @Override
            public void didDetermineStateForRegion(int i, Region region) {
                Log.i(TAG, "I have just switched from seeing/not seeing beacons: " + i);

            }
        });
    }

    private void findNearestBeacon(Collection<Beacon> beacons) {
        Beacon closestBeacon = null;
        Log.i(TAG, "Beacons are in region beaconsCollectionsize: " + beacons.size());

        for (Beacon beacon : beacons) {
            if (closestBeacon == null) {
                closestBeacon = beacon;
            }
            if (beacon.getDistance() < closestBeacon.getDistance()) {
                closestBeacon = beacon;
            }
        }
        if (closestBeacon != null) {
            final Beacon fClosestBeacon = closestBeacon;
            try {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mDatabase.updateUserIndoorPosition(mCurrentUser, beaconLocationToITUArea(fClosestBeacon));
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private String beaconLocationToITUArea(Beacon beacon) {
        String floor = beacon.getId2().toString();
        String minor = beacon.getId3().toString();
        if (minor.length() >= 4) {
            String area = minor.substring(0, 1);
            Log.i(TAG, area);
            switch (area) {
                case "1": {
                    area = "A";
                    break;
                }
                case "2": {
                    area = "B";
                    break;
                }
                case "3": {
                    area = "C";
                    break;
                }
                case "4": {
                    area = "D";
                    break;
                }
                case "5": {
                    area = "E";
                    break;
                }
            }
            String room = minor.substring(1, 3);
            return floor + area + room;
        }
        return "Beacon - major: " + floor + " minor: " + minor;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "JUST GOT DESTROYED");
        mBeaconManager.unbind(this);

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
