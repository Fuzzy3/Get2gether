package com.oestjacobsen.android.get2gether.services;

import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.app.Service;


import com.google.android.gms.nearby.messages.IBeaconId;
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
    private UserManager mUserManager;
    private User mCurrentUser;
    private String mUserUUID;
    //private Intent mIntent;
    //private String fUUID;
    //private Handler mHandler;


    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        mDatabase = DatabasePicker.getChosenDatabase(this);
        //mHandler = new Handler();
        //mUserManager = UserManagerImpl.get();
        //mCurrentUser = mUserManager.getUser();
        //if(mCurrentUser != null) {
        //    mUserUUID = mCurrentUser.getUUID();
        //}

        //mIntent = new Intent();
        //mIntent.setAction(BROADCAST_INDOOR_ACTION);
    }

    private void setupBeaconCollection() {
        mBeaconManager = BeaconManager.getInstanceForApplication(this);
        mBeaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));
        //mBeaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:0-3=4c000215,i:4-19,i:20-21,i:22-23,p:24-24"));
        //mBeaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout(BeaconParser.URI_BEACON_LAYOUT));
        //mBeaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=beac,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));
        //mBeaconManager.setBackgroundScanPeriod(5000l);

       /*try {
            mBeaconManager.updateScanPeriods();
        } catch (RemoteException e) {
            e.printStackTrace();
        }*/

        mBeaconManager.bind(this);
    }

    /*
    private void runOnUiThread(Runnable runnable) {
        mHandler.post(runnable);
    }
       */

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent.getStringExtra("USERUUID") != null) {
            mUserUUID = intent.getStringExtra("USERUUID");
        }
        setupBeaconCollection();
        Log.i(TAG, "Beacon service started");
        return START_REDELIVER_INTENT;
    }

    @Override
    public void onBeaconServiceConnect() {
        mBeaconManager.addRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> collection, Region region) {
                Log.i("AltBeacon", "List of found beacons:");
                for(Beacon beacon : collection) {
                    Log.i("AltBeacon", "major: " + beacon.getId2() + " - minor: " + beacon.getId3());
                }
                findNearestBeacon(collection);

            }
        });

        try {
            mBeaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
        } catch (RemoteException e) {    }
    }

    private void findNearestBeacon(Collection<Beacon> beacons) {
        Beacon closestBeacon = null;
        for (Beacon beacon : beacons) {
            if (closestBeacon == null) {
                closestBeacon = beacon;
            } else if (beacon.getDistance() < closestBeacon.getDistance()) {
                closestBeacon = beacon;
            }
        }
        if (closestBeacon != null) {
            final Beacon fClosestBeacon = closestBeacon;
            Realm realm = null;
            try {
                realm = Realm.getDefaultInstance();
                Log.i(TAG, "Beacons found");
                mDatabase.updateUserIndoorPosition(mUserUUID,
                        beaconLocationToITUArea(fClosestBeacon), realm);
            } finally {
                if (realm != null) {
                    realm.close();
                }
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
