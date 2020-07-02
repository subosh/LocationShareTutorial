package com.example.subosh.locationsharetutorial;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.List;

import static com.example.subosh.locationsharetutorial.Constants.MAPVIEW_BUNDLE_KEY;

public class ActivityTwo extends AppCompatActivity implements OnMapReadyCallback {
    Button locationbutton;
    TextView addressTextView, address;
    double lati, longi;
    MapView mapView;
    List<Address> user = null;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEZT_CODE_LOCATION_PERMISSION = 1;
    GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two);
        locationbutton = findViewById(R.id.getlocation_button);
        addressTextView = findViewById(R.id.getlocation_textview);
        address = findViewById(R.id.addressfull);
        mapView = findViewById(R.id.google_map_view);
        initializeGoogleMap(savedInstanceState);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        locationbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(
                            ActivityTwo.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            REQUEZT_CODE_LOCATION_PERMISSION
                    );
                } else {
                    getLocation();
                }
            }
        });
    }

    private void initializeGoogleMap(Bundle savedInstanceState) {
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }
        mapView.onCreate(mapViewBundle);
        mapView.getMapAsync(this);

    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                if ((task.isSuccessful())) {
                    Location location =(Location) task.getResult();
                    Geocoder geocoder = new Geocoder(ActivityTwo.this);
                    try {
                        assert location != null;
                        user = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        lati = (double) user.get(0).getLatitude();
                        longi = (double) user.get(0).getLongitude();
                        String addressfull = user.get(0).getAddressLine(0);
                        String country = user.get(0).getCountryName();
                        String Postalcode = user.get(0).getPostalCode();
                        String locality = user.get(0).getLocality();
                        addressTextView.setText(String.format("Latitude:%s\nLongitude:%s", lati, longi));
                        address.setText(addressfull);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
       map=googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LatLng location=new LatLng(12.817188199999999,79.6941815);

        map.addMarker(new MarkerOptions().position(location).title("Marker"));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(location,14F));
        map.setMyLocationEnabled(true);

    }

    @Override


    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Bundle mapViewBundle=outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if(mapViewBundle==null){
            mapViewBundle=new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY,mapViewBundle);
        }
        mapView.onSaveInstanceState(mapViewBundle);
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    public void onLowMemory() {
        mapView.onLowMemory();
        super.onLowMemory();
    }

    @Override
    public void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onStop() {
        mapView.onStop();
        super.onStop();
    }

    @Override
    public void onStart() {
        mapView.onStart();
        super.onStart();
    }
}
