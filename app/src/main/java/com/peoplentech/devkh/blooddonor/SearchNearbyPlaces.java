package com.peoplentech.devkh.blooddonor;

import android.Manifest;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;

import android.graphics.Color;
import android.location.Location;

import android.location.LocationManager;
import android.os.Build;

import android.support.annotation.NonNull;

import android.support.annotation.Nullable;

import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;

import android.support.v4.content.ContextCompat;

import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;

import android.util.Log;

import android.view.View;

import android.widget.Button;

import android.widget.Toast;


import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Route;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.google.android.gms.common.ConnectionResult;

import com.google.android.gms.common.GoogleApiAvailability;

import com.google.android.gms.common.api.GoogleApiClient;

import com.google.android.gms.location.LocationListener;

import com.google.android.gms.location.LocationRequest;

import com.google.android.gms.location.LocationServices;

import com.google.android.gms.maps.CameraUpdateFactory;

import com.google.android.gms.maps.GoogleMap;

import com.google.android.gms.maps.OnMapReadyCallback;

import com.google.android.gms.maps.SupportMapFragment;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import com.google.android.gms.maps.model.LatLng;

import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;

import com.google.android.gms.maps.model.MarkerOptions;
import com.peoplentech.devkh.blooddonor.maps.NearByApiResponse;
import com.peoplentech.devkh.blooddonor.model.MyApplication;
import com.peoplentech.devkh.blooddonor.remote.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

import retrofit2.Callback;

import retrofit2.Response;

public class SearchNearbyPlaces extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener {



    private GoogleMap googleMap;

    private GoogleApiClient mGoogleApiClient;

    private Button btnRestorentFind,btnHospitalFind;

    private LocationRequest mLocationRequest;

    private Location location;

    private int PROXIMITY_RADIUS = 5000;

    private LatLng destination;

    private LatLng pnt;

    private String[] colors = {"#7fff7272", "#7f31c7c5", "#7fff8a00"};

    private double pntLat;
    private double pntLng;



    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search_nearby_places);



        //To check permissions above M as below it making issue and gives permission denied on samsung and other phones.

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            checkLocationPermission();

        }



        btnRestorentFind = (Button) findViewById(R.id.btnRestorentFind);

        btnHospitalFind = (Button) findViewById(R.id.btnHospitalFind);



        //To check google play service available

        if(!isGooglePlayServicesAvailable()){

            Toast.makeText(this,"Google Play Services not available.",Toast.LENGTH_LONG).show();

            finish();

        }else{

            // when the map is ready to be used.

            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

            mapFragment.getMapAsync(this);



        }

    }



    private boolean isGooglePlayServicesAvailable() {

        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();

        int result = googleAPI.isGooglePlayServicesAvailable(this);

        if (result != ConnectionResult.SUCCESS) {

            if (googleAPI.isUserResolvableError(result)) {

                googleAPI.getErrorDialog(this, result, 0).show();

            }

            return false;

        }

        return true;

    }



    @Override

    public void onMapReady(final GoogleMap googleMap) {

        this.googleMap = googleMap;

        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);



        //Initialize Google Play Services

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                buildGoogleApiClient();

                googleMap.setMyLocationEnabled(true);


            }

        } else {

            buildGoogleApiClient();

            googleMap.setMyLocationEnabled(true);

        }

        //new
        googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            public void onInfoWindowClick(final Marker marker) {
                // Do stuff with the id
                //Toast.makeText(SearchNearbyPlaces.this, "Direction Requesting..." + marker.getPosition(), Toast.LENGTH_SHORT).show();

                pnt = new LatLng(pntLat, pntLng);

                GoogleDirection.withServerKey(Constants.MAPS_API_KEY)

                        .from(pnt)

                        .to(marker.getPosition())

                        .transportMode(TransportMode.DRIVING)

                        .alternativeRoute(true)

                        .execute(new DirectionCallback() {
                            @Override
                            public void onDirectionSuccess(Direction direction, String rawBody) {

                                if (direction.isOK()) {

                                    googleMap.clear();

                                    googleMap.addMarker(new MarkerOptions().position(pnt));

                                    googleMap.addMarker(new MarkerOptions().position(marker.getPosition()));



                                    for (int i = 0; i < direction.getRouteList().size(); i++) {

                                        Route route = direction.getRouteList().get(i);

                                        String color = colors[i % colors.length];

                                        ArrayList<LatLng> directionPositionList = route.getLegList().get(0).getDirectionPoint();

                                        googleMap.addPolyline(DirectionConverter.createPolyline(SearchNearbyPlaces.this, directionPositionList, 5, Color.parseColor(color)));

                                    }

                                    setCameraWithCoordinationBounds(direction.getRouteList().get(0));


                                } else {
                                    Toast.makeText(SearchNearbyPlaces.this, "Error", Toast.LENGTH_LONG).show();
                                }

                            }

                            @Override
                            public void onDirectionFailure(Throwable t) {
                                Toast.makeText(SearchNearbyPlaces.this, "Error " + t.toString(), Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });

    }





    protected synchronized void buildGoogleApiClient() {

        mGoogleApiClient = new GoogleApiClient.Builder(this).
                addConnectionCallbacks(this).addOnConnectionFailedListener(this).
                addApi(LocationServices.API).build();

        mGoogleApiClient.connect();

    }



    public void onRestorentFindClick(View view){

        findBloodBank();

    }



    public void onHospitalsFindClick(View view){

        findPlaces("hospital");

    }



    public void findPlaces(String placeType){

        Call<NearByApiResponse> call = MyApplication.getApp().getApiService().getNearbyPlaces(placeType, location.getLatitude() + "," + location.getLongitude(), PROXIMITY_RADIUS);



        call.enqueue(new Callback<NearByApiResponse>() {

            @Override

            public void onResponse(Call<NearByApiResponse> call, Response<NearByApiResponse> response) {

                try {

                    googleMap.clear();

                    // This loop will go through all the results and add marker on each location.

                    for (int i = 0; i < response.body().getResults().size(); i++) {

                        Double lat = response.body().getResults().get(i).getGeometry().getLocation().getLat();

                        Double lng = response.body().getResults().get(i).getGeometry().getLocation().getLng();

                        String placeName = response.body().getResults().get(i).getName();

                        String vicinity = response.body().getResults().get(i).getVicinity();

                        MarkerOptions markerOptions = new MarkerOptions();

                        LatLng latLng = new LatLng(lat, lng);

                        // Location of Marker on Map

                        markerOptions.position(latLng);

                        // Title for Marker

                        markerOptions.title(placeName + " : " + vicinity);

                        // Color or drawable for marker

                        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));

                        // add marker

                        Marker m = googleMap.addMarker(markerOptions);

                        //destination = m.getPosition();

                        // move map camera

                        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

                        googleMap.animateCamera(CameraUpdateFactory.zoomTo(13));

                    }

                } catch (Exception e) {

                    Log.d("onResponse", "There is an error");

                    e.printStackTrace();

                }

            }



            @Override

            public void onFailure(Call<NearByApiResponse> call, Throwable t) {

                Log.d("onFailure", t.toString());

                t.printStackTrace();

                PROXIMITY_RADIUS += 10000;

            }

        });

    }

    //Method for Blood Bank
    public void findBloodBank() {

            Call<NearByApiResponse> call = MyApplication.getApp().getApiService().getBloodBank( location.getLatitude() + "," + location.getLongitude(), PROXIMITY_RADIUS);



            call.enqueue(new Callback<NearByApiResponse>() {

                @Override

                public void onResponse(Call<NearByApiResponse> call, Response<NearByApiResponse> response) {

                    try {

                        googleMap.clear();

                        // This loop will go through all the results and add marker on each location.

                        for (int i = 0; i < response.body().getResults().size(); i++) {

                            Double lat = response.body().getResults().get(i).getGeometry().getLocation().getLat();

                            Double lng = response.body().getResults().get(i).getGeometry().getLocation().getLng();

                            String placeName = response.body().getResults().get(i).getName();

                            String vicinity = response.body().getResults().get(i).getVicinity();

                            final MarkerOptions markerOptions = new MarkerOptions();

                            LatLng latLng = new LatLng(lat, lng);

                            // Location of Marker on Map

                            markerOptions.position(latLng);

                            // Title for Marker

                            markerOptions.title(placeName + " : " + vicinity);

                            // Color or drawable for marker

                            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));

                            // add marker

                            Marker m = googleMap.addMarker(markerOptions);

                            googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

                            googleMap.animateCamera(CameraUpdateFactory.zoomTo(13));
                        }

                    } catch (Exception e) {

                        Log.d("onResponse", "There is an error");

                        e.printStackTrace();

                    }

                }


                @Override

                public void onFailure(Call<NearByApiResponse> call, Throwable t) {

                    Log.d("onFailure", t.toString());

                    t.printStackTrace();

                    PROXIMITY_RADIUS += 10000;

                }

            });

    }


    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;



    public boolean checkLocationPermission() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {



            // Asking user if explanation is needed

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {



                // Show an explanation to the user *asynchronously* -- don't block

                // this thread waiting for the user's response! After the user

                // sees the explanation, try again to request the permission.


                //Prompt the user once explanation has been shown

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);





            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);

            }

            return false;

        } else {

            return true;

        }

    }



    @Override

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        switch (requestCode) {

            case MY_PERMISSIONS_REQUEST_LOCATION: {

                // If request is cancelled, the result arrays are empty.

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {



                    // permission was granted. Do the

                    // contacts-related task you need to do.

                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {



                        if (mGoogleApiClient == null) {

                            buildGoogleApiClient();

                        }

                        googleMap.setMyLocationEnabled(true);

                    }



                } else {

                    Toast.makeText(this, "Location Permission has been denied, can not search the places you want.", Toast.LENGTH_LONG).show();

                }

                return;

            }

        }

    }



    @Override

    public void onConnected(@Nullable Bundle bundle) {

        startLocationUpdates();

    }



    @Override

    public void onConnectionSuspended(int i) {



    }



    @Override

    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        Toast.makeText(this,"Could not connect google api",Toast.LENGTH_LONG).show();

    }





    @SuppressLint("RestrictedApi")
    protected void startLocationUpdates() {

        mLocationRequest = new LocationRequest();

        mLocationRequest.setInterval(1000);

        mLocationRequest.setFastestInterval(1000);

        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

        }

    }



    @Override

    public void onLocationChanged(Location location) {

        if(location!=null){

            this.location = location;

            pntLat = location.getLatitude();
            pntLng = location.getLongitude();

            if(!btnHospitalFind.isEnabled()){

                LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());

                googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

                googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));

                btnRestorentFind.setEnabled(true);

                btnHospitalFind.setEnabled(true);

            }

        }

    }

    private void setCameraWithCoordinationBounds(Route route) {

        LatLng southwest = route.getBound().getSouthwestCoordination().getCoordination();

        LatLng northeast = route.getBound().getNortheastCoordination().getCoordination();

        LatLngBounds bounds = new LatLngBounds(southwest, northeast);

        googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));

    }
}
