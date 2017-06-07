package com.oestjacobsen.android.get2gether.view.groups;

import android.content.BroadcastReceiver;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.Manifest;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.oestjacobsen.android.get2gether.R;
import com.oestjacobsen.android.get2gether.model.Group;
import com.oestjacobsen.android.get2gether.model.RealmDatabase;
import com.oestjacobsen.android.get2gether.model.User;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SelectedGroupMapFragment extends SelectedGroupParentView implements
        SelectedGroupMapMVP.SelectedGroupMapView {


    @BindView(R.id.selected_group_mapview) MapView mMapView;

    private static final String TAG = "MAP_FRAGMENT";
    private LatLng mLatLon;
    private GoogleMap mGoogleMap;
    private MarkerOptions mCurrentLocationMarker;
    private boolean launchedMapFirstTime;
    private User mCurrentUser;
    private static final int COARSE_PERMISSION_REQUEST_CODE = 1111;
    private static final int FINE_PERMISSION_REQUEST_CODE = 2222;
    private HashMap<String, Boolean> mActiveMap;
    private SelectedGroupMapMVP.SelectedGroupMapPresenter mPresenter;
    private static final String ARGS_GROUP_UUID = "ARGSGROUPUUID";
    private BroadcastReceiver mLocationReciever;
    private Timer mTimer;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_group_map, container, false);
        ButterKnife.bind(this, view);
        mMapView.onCreate(savedInstanceState);
        mPresenter = new SelectedGroupMapPresenterImpl(RealmDatabase.get(getContext()), getArguments().getString(ARGS_GROUP_UUID), this);
        setTimer();
        launchedMapFirstTime = true;
        Log.i(TAG, "Getting map async");
        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                Log.i(TAG, "OnMapReady");
                mGoogleMap = googleMap;
                mPresenter.getCurrentGroup();
            }
        });
        return view;
    }

    public void setCurrentUser(User user) {
        mCurrentUser = user;
        mPresenter.getActiveGroups();
    }

    @Override
    public void setCurrentGroup(Group group) {
        super.setCurrentGroup(group);
        mPresenter.getCurrentUser();
    }

    public void setActiveHashMap(HashMap<String, Boolean> map) {
        mActiveMap = map;
        updateUI();
    }

    private void updateUI() {
        if (mGoogleMap != null) {
            setOwnLocation();
            setParticipantsLocation();
        }
    }

    private void setParticipantsLocation() {
        if(mActiveMap != null) {
            for(User member : mCurrentGroup.getParticipants()) {
                if(mActiveMap.containsKey(member.getUUID())) {
                    if(mActiveMap.get(member.getUUID()) && !member.getUUID().equals(mCurrentUser.getUUID())) {
                        LatLng memberLatLng = new LatLng(member.getLatitude(), member.getLongitude());
                        MarkerOptions locationMarker = new MarkerOptions().position(memberLatLng).title(member.getFullName());
                        mGoogleMap.addMarker(locationMarker);
                    }
                }
            }
        } else {
            mPresenter.getActiveGroups();
        }
    }

    /*private void createLocationBroadcastReciever() {
        mLocationReciever = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                newLocationsAcquired();
            }
        };
    }

    private void newLocationsAcquired() {
        //Log.i("BROADCAST RECIEVED", "TIME TO GET SOME UPDATES");
        updateUI();

    }*/

    private void setTimer() {
        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    if(getActivity() != null) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mPresenter.getActiveGroups();
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 0, 1000);
    }

    private void setOwnLocation() {
        mLatLon = new LatLng(mCurrentUser.getLatitude(), mCurrentUser.getLongitude());
        mCurrentLocationMarker = new MarkerOptions().position(mLatLon).title("You (" + mCurrentUser.getFullName() + ")");
        mGoogleMap.clear();
        mGoogleMap.addMarker(mCurrentLocationMarker);


        if (launchedMapFirstTime) {
            CameraPosition pos = new CameraPosition.Builder()
                    .target(mLatLon)
                    .zoom(17)
                    .build();

            mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(pos));
            launchedMapFirstTime = false;
        }
    }


    public static SelectedGroupMapFragment newInstance(String groupUUID) {
        SelectedGroupMapFragment mapFragment = new SelectedGroupMapFragment();

        Bundle args = new Bundle();
        args.putString(ARGS_GROUP_UUID, groupUUID);
        mapFragment.setArguments(args);

        return mapFragment;
    }




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case COARSE_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Permission granted
                } else {
                    //Permission denied do something
                }
                return;
            }
            case FINE_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Permission granted
                } else {
                    //Permission denied do something
                }
                return;
            }
        }
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            //int fineLocationPermission = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION);
            //int coarseLocationPermission = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION);

            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        FINE_PERMISSION_REQUEST_CODE);

            }

            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        COARSE_PERMISSION_REQUEST_CODE);

            }
        }

    }


    @Override
    public void onStart() {
        super.onStart();
        //mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
     //   mGoogleApiClient.disconnect();
    }


    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
        setTimer();
        //getActivity().registerReceiver(mLocationReciever, new IntentFilter(LocationService.BROADCAST_ACTION));

    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
        if(mTimer != null) {
            mTimer.cancel();
        }
        //getActivity().unregisterReceiver(mLocationReciever);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

}