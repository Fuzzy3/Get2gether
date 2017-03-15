package com.oestjacobsen.android.get2gether.view.groups;

import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.Manifest;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.oestjacobsen.android.get2gether.R;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SelectedGroupMapFragment extends Fragment {


    @BindView(R.id.selected_group_mapview) MapView mMapView;

    private static final String TAG = "MAP_FRAGMENT";
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private LatLng mLatLon;
    private GoogleMap mGoogleMap;
    private Marker mCurrLocMarker;
    private static final int COARSE_PERMISSION_REQUEST_CODE = 1111;
    private static final int FINE_PERMISSION_REQUEST_CODE = 2222;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_group_map, container, false);
        ButterKnife.bind(this, view);
        mMapView.onCreate(savedInstanceState);
        buildGoogleApiClient();
        checkPermission();
        /*try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        Log.i(TAG, "Getting map async");
        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                Log.i(TAG, "OnMapReady");
                mGoogleMap = googleMap;
                updateUI();
                /*if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }*/
                //mGoogleMap.setMyLocationEnabled(true);
                //mGoogleApiClient.connect();
            }
        });



        return view;
    }

    private void updateUI() {
        Log.i(TAG, "UPDATE UI");
        checkPermission();
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);                   }
        if (mLastLocation != null) {
            //place marker at current position
            //mGoogleMap.clear();
            mLatLon = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            //LatLngBounds bounds = new LatLngBounds.Builder().include(mLatLon).build();
            MarkerOptions currentLocationMarker = new MarkerOptions().position(mLatLon);
            mGoogleMap.clear();
            mGoogleMap.addMarker(currentLocationMarker);
            CameraPosition pos = new CameraPosition.Builder()
                    .target(mLatLon)
                    .zoom(17)
                    .build();

            mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(pos));
        }


    }

    public static SelectedGroupMapFragment newInstance() {
        SelectedGroupMapFragment mapFragment = new SelectedGroupMapFragment();

        return mapFragment;
    }

    /*
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i(TAG, "onConnected");
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Log.i(TAG, "Permissions granted");

        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (mLastLocation != null) {
            //place marker at current position
            //mGoogleMap.clear();
            mLatLon = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(mLatLon);
            markerOptions.title("Current Position");
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
            mCurrLocMarker = mGoogleMap.addMarker(markerOptions);
        }

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(5000); //5 seconds
        mLocationRequest.setFastestInterval(3000); //3 seconds
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        //mLocationRequest.setSmallestDisplacement(0.1F); //1/10 meter

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(getActivity(), "onConnectionSuspended", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(getActivity(), "onConnectionFailed", Toast.LENGTH_SHORT).show();
    }*/

    /*@Override
    public void onLocationChanged(Location location) {
        //place marker at current position
        //mGoogleMap.clear();
        if (mCurrLocMarker != null) {
            mCurrLocMarker.remove();
        }
        mLatLon = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(mLatLon);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        mCurrLocMarker = mGoogleMap.addMarker(markerOptions);

        Toast.makeText(getActivity(), "Location Changed", Toast.LENGTH_SHORT).show();

        //zoom to current position:
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mLatLon, 11));

        //If you only need one location, unregister the listener
        //LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);

    }*/


    protected synchronized void buildGoogleApiClient() {
        Log.i(TAG, "BuildGoogleApiClient");
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(@Nullable Bundle bundle) {
                        checkPermission();
                        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);                   }

                    @Override
                    public void onConnectionSuspended(int i) {
                        //something
                    }
                })
                .build();
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
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }


    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
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