package br.com.devmaker.mapsteste;

import android.*;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Locale;

import br.com.devmaker.mapsteste.model.Street;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap;
    public static final int  REQUEST_PERMISSIONS_CODE = 128;
    private GoogleApiClient mGoogleApiClient;
    Location lastLocation;
    String error = "";
    Street street;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        Intent it = getIntent();
        if(it.hasExtra("obj")){
             street = (Street) it.getSerializableExtra("obj");
        }


        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED ){
            ActivityCompat.requestPermissions( this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSIONS_CODE );
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch( requestCode ){
            case  128:
                for( int i = 0; i < permissions.length; i++ ){
                    if(permissions[i].equalsIgnoreCase( android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                        if(grantResults[i] == PackageManager.PERMISSION_GRANTED){
                            callConnection();
                            Toast.makeText(MapsActivity.this, "Permissão ok", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getBaseContext(), "A permissão dever concedida para utilizar o aplicativo!", Toast.LENGTH_LONG).show();
                            ActivityCompat.requestPermissions( this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSIONS_CODE );
                        }
                    }
                    return;
                }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
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
        mMap = googleMap;
        callConnection();

    }


    private synchronized void callConnection(){
        Log.i("LOG", "LastLocationActivity.callConnection()");
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addOnConnectionFailedListener(this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    public void requestPermissions(@NonNull String[] permissions, int requestCode)
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.


                //requestPermissions(,123);
//
//                ActivityCompat.requestPermissions(this, new String[]{
//                                android.Manifest.permission.ACCESS_FINE_LOCATION },
//                        REQUEST_PERMISSIONS_CODE);
//                return;
            }else{
                lastLocation = LocationServices
                        .FusedLocationApi
                        .getLastLocation(mGoogleApiClient);
            }
        }else{
            lastLocation = LocationServices
                    .FusedLocationApi
                    .getLastLocation(mGoogleApiClient);
        }

        // Add a marker in Sydney and move the camera
        if(lastLocation != null) {
            LatLng location = new LatLng(Double.parseDouble(street.getLatitude()), Double.parseDouble(street.getLongitude()));
            mMap.setMyLocationEnabled(true);
            mMap.addMarker(new MarkerOptions().position(location).title("Essa é a posição do carro"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location,13));

            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {


                    String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?saddr=%f,%f(%s)&daddr=%f,%f (%s)", lastLocation.getLatitude(), lastLocation.getLongitude(), "Localização atual", Double.parseDouble(street.getLatitude()), Double.parseDouble(street.getLongitude()), "Seu destino");
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                    startActivity(intent);

                }
            });

        }else{
            LatLng location = new LatLng(Double.parseDouble(street.getLatitude()), Double.parseDouble(street.getLongitude()));
            mMap.setMyLocationEnabled(true);
            mMap.addMarker(new MarkerOptions().position(location).title("Essa é a posição do carro"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location,13));
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }



}
