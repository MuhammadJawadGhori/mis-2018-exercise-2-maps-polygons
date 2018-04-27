package com.jawad.jawadmaps;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.maps.android.SphericalUtil;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    GoogleMap mMap;
    LocationManager locationManager;
    //LocationListener locationListener;
    LocationListener locationListener;
    EditText getData;
    //Set<String> demo = new HashSet<String>();
    HashSet<String> demo = new HashSet<String>();
    ArrayList<Marker> markers = new ArrayList<Marker>();
    ArrayList<LatLng> markersList = new ArrayList<LatLng>();
    Button polygonBtn;

    int count=0;

    static final int POLYGON_POINTS=5;
    Polygon shape;
    Polygon polygon;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        try {
            if (requestCode == 1) {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                    }
                }
            }
        }
        catch(Exception e){
            //Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
            onCreateHelper();
        }
    }

    public void onCreateHelper(){
        try {
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                buildAlertMessageNoGps();
            }
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }

            //else{
            //shared preference
            /*SharedPreferences sharedPreferences = this.getSharedPreferences("com.jawad.jawadmaps",Context.MODE_PRIVATE);
            Set abc = sharedPreferences.getStringSet("getdata",new LinkedHashSet<String>());
            Toast.makeText(getApplicationContext(), abc.toString(), Toast.LENGTH_SHORT).show();
            */
            /*
            if(abc!=null) {
                double latitude = Double.parseDouble(String.valueOf(abc.toArray()[0]));
                double longitude= Double.parseDouble(String.valueOf(abc.toArray()[1]));
                Toast.makeText(getApplicationContext(), abc.toArray()[0].toString(), Toast.LENGTH_SHORT).show();
                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(latitude,longitude))
                        .title(abc.toArray()[2].toString())
                        .snippet(getData.getText().toString())
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
            }
            */
            /*
            SharedPreferences sharedPreferences = this.getSharedPreferences("com.jawad.jawadmaps",MODE_PRIVATE);
            Set<String> fetchLat = new HashSet<>();
            Set<String> fetchLng = new HashSet<>();
            Set<String> fetchTextData = new HashSet<String>();

            fetchLat.clear();
            fetchLng.clear();
            fetchTextData.clear();
            try{
                fetchLat = sharedPreferences.getStringSet("getLat",new HashSet<String>());
                fetchLng = sharedPreferences.getStringSet("getLng",new HashSet<String>());
                fetchTextData = sharedPreferences.getStringSet("getTextData",new HashSet<String>());
            }
            catch (Exception e){
                e.printStackTrace();
            }
            if(fetchLat.size()>0 && fetchLng.size()>0){
            for(int i=0; i<fetchLat.size();i++){
                Marker marker = mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(fetchLat(i),fetchLng(i)))
                        .title("Your textbox data")
                        .snippet(getData.getText().toString())
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
            }
            }
            */
            // }
            else{
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();
                        LatLng latLng = new LatLng(latitude, longitude);
                        List<Address> address = null;
                        Geocoder geocoder = new Geocoder(getApplicationContext());
                        try {
                            address = geocoder.getFromLocation(latitude, longitude, 1);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        mMap.addMarker(new MarkerOptions().position(latLng).title(address.get(0).getLocality()));
                        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,30f));
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.5f), 2000, null);

                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {

                    }

                    @Override
                    public void onProviderEnabled(String provider) {

                    }

                    @Override
                    public void onProviderDisabled(String provider) {

                    }
                });
            }
        }
        catch (Exception e){
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        polygonBtn = findViewById(R.id.polygonBtn);
        getData = findViewById(R.id.getData);
        onCreateHelper();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        ///final Set<String> setLat = new HashSet<>();
        ///final Set<String> setLng = new HashSet<>();
        ///final Set<String> setTextData = new HashSet<String>();
        //demo.clear();
        /*if(markers.size()==POLYGON_POINTS){
            removeEverything();
        }*/
        mMap = googleMap;
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {

            @Override
            public void onMapLongClick(LatLng latLng) {
                mMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title("Your textbox data")
                        .snippet(getData.getText().toString())
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));

                count++;
                markersList.add(new LatLng(latLng.latitude, latLng.longitude));
                /*if(count==5) {
                    Polygon polygon = mMap.addPolygon(new PolygonOptions()
                            .add(markersList.get(0), markersList.get(1), markersList.get(2),markersList.get(3),markersList.get(4))
                            .strokeColor(0xFF00AA00)
                            .fillColor(0x2200FFFF)
                            .strokeWidth(5)
                    );
                    mMap.addMarker(new MarkerOptions()
                            .position(getPolygonCenterPoint(markersList))
                            .title("Centroid")
                            .snippet("This is your centroid")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

                    Toast.makeText(getApplicationContext(),String.format(Locale.ENGLISH," Area is %.2f",SphericalUtil.computeArea(markersList)/1000) + " km²",Toast.LENGTH_SHORT).show();
                    ///Math.round(*100)/100.f;
                    markersList.clear();
                    count=0;
                }*/
                //.add(new LatLng(24.873176, 67.061257), new LatLng(24.873298, 67.060146), new LatLng(24.873200, 67.059991), new LatLng(24.873964, 67.060715))

                //Toast.makeText(getApplicationContext(),latLng.latitude +"  "+latLng.longitude,Toast.LENGTH_SHORT).show();

                //if(markersList.isEmpty())
                // Toast.makeText(getApplicationContext(),markersList.toString(),Toast.LENGTH_SHORT).show();
               /* if(markersList.size()>0)
                    Toast.makeText(getApplicationContext(),markersList.size(),Toast.LENGTH_SHORT).show();*/

                ///setTextData.add(getData.getText().toString());
                ///setLat.add(Double.toString(latLng.latitude));
                ///setLng.add(Double.toString(latLng.longitude));
                /*
                                //TODO shared preference
                Collections.addAll(demo,getData.getText().toString(), Double.toString(latLng.latitude),Double.toString(latLng.longitude));
                SharedPreferences sharedPreferences = getSharedPreferences("com.jawad.jawadmaps",Context.MODE_PRIVATE);
                sharedPreferences.edit().putStringSet("getdata",demo).apply();
                */
            }
        });

        // if(markersList.size()>0)
        //  Toast.makeText(getApplicationContext(),markersList.toString(),Toast.LENGTH_SHORT).show();

        //if(markersList.size()==5) {

        //}
      /*  if(markers.size()==5){
            drawPolygon();
        }*/
        ///sharedPreferences.edit().putStringSet("getTextdata",setTextData).apply();
        ///sharedPreferences.edit().putStringSet("getLat",setLat).apply();
        ///sharedPreferences.edit().putStringSet("getLng",setLng).apply();
        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
    private void removeEverything(){
        for(Marker marker:markers){
            marker.remove();
        }
        markers.clear();
        shape.remove();
        shape=null;
    }
    protected void buildAlertMessageNoGps() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please Turn ON your GPS Connection")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    private LatLng getPolygonCenterPoint(ArrayList<LatLng> polygonPointsList){
        LatLng centerLatLng = null;
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for(int i = 0 ; i < polygonPointsList.size() ; i++)
        {
            builder.include(polygonPointsList.get(i));
        }
        LatLngBounds bounds = builder.build();
        centerLatLng =  bounds.getCenter();

        return centerLatLng;
    }
    public void makePolygon(View view) {
        if (polygonBtn.getText().equals("Stop Polygon")) {
            polygon.remove();
            polygonBtn.setText("Start Polygon");
        } else {
            //if (count == 5) {
            polygonBtn.setText("Stop Polygon");
            polygon = mMap.addPolygon(new PolygonOptions()
                    .addAll(markersList)
                    .strokeColor(0xFF00AA00)
                    .fillColor(0x2200FFFF)
                    .strokeWidth(5)
            );
            mMap.addMarker(new MarkerOptions()
                    .position(getPolygonCenterPoint(markersList))
                    .title("Centroid")
                    .snippet("This is your centroid")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

            Toast.makeText(getApplicationContext(), String.format(Locale.ENGLISH, " Area is %.2f", SphericalUtil.computeArea(markersList) / 1000) + " km²", Toast.LENGTH_LONG).show();
            ///Math.round(*100)/100.f;
            markersList.clear();
            count = 0;
            //add(markersList.get(0), markersList.get(1), markersList.get(2), markersList.get(3), markersList.get(4))
            //}
            //else if(count==0 || count<5) {
            //  Toast.makeText(getApplicationContext(), count == 4 ? "Just 1 more marker remaining for Polygon" : "Please make " + (5 - count) + " more markers for Polygon", Toast.LENGTH_SHORT).show();
            //}
        }
    }
}
