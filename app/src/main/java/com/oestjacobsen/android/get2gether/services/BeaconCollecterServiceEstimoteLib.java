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

import io.realm.Realm;


public class BeaconCollecterServiceEstimoteLib extends Service {

    private static final String TAG = "BeaconCollecterEstimote";
    private BaseDatabase mDatabase;
    private UserManager mUserManager;
    private User mCurrentUser;
    private String mUserUUID;
    //private Handler mHandler;
    private BeaconManager mBeaconmanager;
    private Region mRegion;

    @Override
    public void onCreate() {
        super.onCreate();

        //mHandler = new Handler();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent.getStringExtra("USERUUID") != null) {
            mUserUUID = intent.getStringExtra("USERUUID");
        }
        mDatabase = DatabasePicker.getChosenDatabase(this);
        setupBeaconCollection();
        Log.i(TAG, "Beacon collecting started");
        return START_REDELIVER_INTENT;
    }

    private void setupBeaconCollection() {
        mBeaconmanager = new BeaconManager(this);
        mBeaconmanager.setRangingListener(new BeaconManager.RangingListener() {
            @Override
            public void onBeaconsDiscovered(Region region, List<Beacon> list) {
                Log.i(TAG, "NONE Beacons found");
                if (!list.isEmpty()) {
                    final Beacon fClosestBeacon = list.get(0);
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
        });

        mRegion = new Region("no specific region", null, null, null);
        mBeaconmanager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                mBeaconmanager.startRanging(mRegion);
            }
        });
    }

    /*private void runOnUiThread(Runnable runnable) {
        mHandler.post(runnable);
    }*/

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