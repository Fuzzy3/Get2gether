package com.oestjacobsen.android.get2gether.services;


import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.oestjacobsen.android.get2gether.DatabasePicker;
import com.oestjacobsen.android.get2gether.UserManager;
import com.oestjacobsen.android.get2gether.UserManagerImpl;
import com.oestjacobsen.android.get2gether.model.BaseDatabase;
import com.oestjacobsen.android.get2gether.model.User;

import java.util.List;


public class BeaconCollecterServiceEstimoteLib extends Service {

    private static final String TAG = "BeaconCollecterEstimote";
    private BaseDatabase mDatabase;
    private UserManager mUserManager;
    private User mCurrentUser;
    private Handler mHandler;
    private BeaconManager mBeaconmanager;
    private Region mRegion;

    @Override
    public void onCreate() {
        super.onCreate();
        mDatabase = DatabasePicker.getChosenDatabase(this);
        mHandler = new Handler();
        Log.i(TAG, "Beacon onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mUserManager = UserManagerImpl.get();
        mCurrentUser = mUserManager.getUser();
        setupBeaconCollection();
        Log.i(TAG, "Beacon collecting started");
        return START_STICKY;
    }

    private void setupBeaconCollection() {
        mBeaconmanager = new BeaconManager(this);
        mBeaconmanager.setRangingListener(new BeaconManager.RangingListener() {
            @Override
            public void onBeaconsDiscovered(Region region, List<Beacon> list) {
                if (!list.isEmpty()) {
                    Beacon nearestBeacon = list.get(0);
                    final Beacon fClosestBeacon = nearestBeacon;
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
                    // TODO: update the UI here
                    for(Beacon beacon : list) {
                        Log.i("Main", "major: " + beacon.getMajor() + " - minor: " + beacon.getMinor());
                    }
                }
            }
        });
        mRegion = new Region("no specific region", null, null, null);

        mBeaconmanager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                mBeaconmanager.startRanging(mRegion);
            }
        });
    }

    private void runOnUiThread(Runnable runnable) {
        mHandler.post(runnable);
    }

    private String beaconLocationToITUArea(Beacon beacon) {
        String floor = beacon.getMajor() + "";
        String minor = beacon.getMinor() + "";
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
    public IBinder onBind(Intent intent) {
        return null;
    }


}
