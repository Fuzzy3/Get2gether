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


import com.oestjacobsen.android.get2gether.model.BaseDatabase;
import com.oestjacobsen.android.get2gether.model.RealmDatabase;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.Collection;

public class BeaconCollecterService extends Service implements BeaconConsumer {

    public BeaconManager mBeaconManager;
    private static final String TAG = BeaconCollecterService.class.getCanonicalName();
    private static final String BROADCAST_INDOOR_ACTION = "BROADCASTINDOORACTION";
    private BaseDatabase mDatabase;
    private Intent mIntent;

    public BeaconCollecterService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mDatabase = RealmDatabase.get(this);
        mIntent = new Intent();
        mIntent.setAction(BROADCAST_INDOOR_ACTION);
        setupBeaconCollection();

    }

    private void setupBeaconCollection() {
        mBeaconManager = BeaconManager.getInstanceForApplication(getApplicationContext());
        mBeaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));
        mBeaconManager.setForegroundScanPeriod(5000l);
        mBeaconManager.bind(this);
    }


    @Override
    public void onBeaconServiceConnect() {
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
                Log.i(TAG, "Number of beacons: " + collection.size());

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

        for(Beacon beacon : beacons) {
            if (closestBeacon == null) {
                closestBeacon = beacon;
            }
            if (beacon.getDistance() < closestBeacon.getDistance()) {


                mDatabase.updateUserIndoorPosition();
            }
        }
    }

    private String beaconLocationToITUArea(Beacon beacon) {
        String floor = beacon.getId2().toString();
        String minor = beacon.getId3().toString();
        String area = minor.substring(0,1);
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
        String room = minor.substring(1,3);
        return floor +
    }

    @Override
    public Context getApplicationContext() {
        return null;
    }

    @Override
    public void unbindService(ServiceConnection serviceConnection) {

    }

    @Override
    public boolean bindService(Intent intent, ServiceConnection serviceConnection, int i) {
        return false;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
