package com.oestjacobsen.android.get2gether.view.groups;

import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.oestjacobsen.android.get2gether.Manifest;
import com.oestjacobsen.android.get2gether.R;

import butterknife.ButterKnife;


public class SelectedGroupMapFragment extends SupportMapFragment implements
        OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private static final String TAG = "MAP_FRAGMENT";

    private GoogleApiClient mGoogleAPIClient;
    private LocationRequest mLocationRequest;
    private LatLng mLatLon;
    private GoogleMap mGoogleMap;
    private Marker mCurrLocMarker;
    private static final int MY_PERMISSION_ACCESS_COURSE_LOCATION = 0;









    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_group_map, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    public static SelectedGroupMapFragment newInstance() {
        SelectedGroupMapFragment mapFragment = new SelectedGroupMapFragment();

        return mapFragment;
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.setMyLocationEnabled(true);

        buildGoogleApiClient();

        mGoogleApiClient.connect();

    }

    private void checkPermissions() {
        if ( Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission( getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission( getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions( getActivity(), new String[] {  android.Manifest.permission.ACCESS_COARSE_LOCATION  },
                    SelectedGroupMapFragment.MY_PERMISSION_ACCESS_COURSE_LOCATION );
        }

        if ( ContextCompat.checkSelfPermission( getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {

            ActivityCompat.requestPermissions( getActivity(), new String[] {  android.Manifest.permission.ACCESS_COARSE_LOCATION  },
                    SelectedGroupMapFragment.MY_PERMISSION_ACCESS_COURSE_LOCATION );
        }
    }




    /*
     @RequiresApi(api = Build.VERSION_CODES.M)
     private void checkPermissions() {
         private boolean checkPermission() {
             int permissionCheck = getActivity().checkSelfPermission(getActivity(),
                     Manifest.permission.ACCESS_FINE_LOCATION);

             if (ContextCompat.checkSelfPermission(getActivity(),
                     Manifest.permission.ACCESS_FINE_LOCATION)
                     != PackageManager.PERMISSION_GRANTED) {

                 ActivityCompat.requestPermissions(getActivity(),
                         new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                         );

                 return false;
             } else {
                 return true;
             }
         }
     }*/
}
