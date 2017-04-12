package com.oestjacobsen.android.get2gether.services;

import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;
import android.app.Service;
import android.widget.BaseAdapter;


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
import io.realm.RealmResults;

public class BeaconCollecterService extends Service implements BeaconConsumer {

    private BeaconManager mBeaconManager;
    private static final String TAG = BeaconCollecterService.class.getCanonicalName();
    public static final String BROADCAST_INDOOR_ACTION = "BROADCASTINDOORACTION";
    private BaseDatabase mDatabase;
    UserManager mUserManager;
    User mCurrentUser;
    private Intent mIntent;
    private String fUUID;
    public BeaconCollecterService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mDatabase = RealmDatabase.get(this);
        mIntent = new Intent();
        mIntent.setAction(BROADCAST_INDOOR_ACTION);
    }

    private void setupBeaconCollection() {
        mBeaconManager = BeaconManager.getInstanceForApplication(this);
        mBeaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));
        mBeaconManager.setForegroundScanPeriod(5000l);
        mBeaconManager.setBackgroundScanPeriod(50001);
        mBeaconManager.setBackgroundMode(true);
        try {
            mBeaconManager.updateScanPeriods();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        mBeaconManager.bind(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mUserManager = UserManagerImpl.get();
        mCurrentUser = mUserManager.getUser();
        fUUID = mCurrentUser.getUUID();
        setupBeaconCollection();

        Log.i(TAG, "Beacon service started");


        return START_NOT_STICKY;
    }

    @Override
    public void onBeaconServiceConnect() {
        Log.i(TAG, "Beacon connected to service ***************************** ");
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

        try {
            mBeaconManager.startMonitoringBeaconsInRegion(new Region("myMonitoringUniqueId", null, null, null));
        } catch (RemoteException e) {
            //NOTING RIGHT NOW
        }

        mBeaconManager.addRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> collection, Region region) {
                Log.i(TAG, "Beacons are in region");
                findNearestBeacon(collection);

            }
        });

        try {
            mBeaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
        } catch (RemoteException e) {
            //DO NOTHING FOR NOW
        }

    }

    private void findNearestBeacon(Collection<Beacon> beacons) {
        Beacon closestBeacon = null;

        for (Beacon beacon : beacons) {
            if (closestBeacon == null) {
                closestBeacon = beacon;
            }
            if (beacon.getDistance() < closestBeacon.getDistance()) {
                closestBeacon = beacon;
            }
        }
        if (closestBeacon != null) {
            //mDatabase.updateUserIndoorPosition(mCurrentUser, beaconLocationToITUArea(closestBeacon));
            Realm realm = null;
            try {
                realm = Realm.getDefaultInstance();
                final User fUser;
                RealmResults<User> rows = realm.where(User.class).equalTo("mUUID", fUUID).findAll();
                if (rows.size() > 0) {
                    fUser = rows.get(0);
                } else {
                    fUser = null;
                }
                if (fUser != null) {
                    realm.beginTransaction();
                    fUser.setIndoorLocation(beaconLocationToITUArea(closestBeacon));
                    realm.copyToRealmOrUpdate(fUser);
                    realm.commitTransaction();
                }
            } finally {
                if(realm != null) {
                    realm.close(); // important
                }
                for(Beacon beacon : beacons)
                Log.i(TAG, beacon.getId1().toString());
                sendBroadcast(mIntent);
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
        return "Custom beacon - major: " + floor + " minor: " + minor;
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
