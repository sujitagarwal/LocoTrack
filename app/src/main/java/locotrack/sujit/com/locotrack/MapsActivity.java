package locotrack.sujit.com.locotrack;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import androidx.annotation.RequiresPermission;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import locotrack.sujit.com.locotrack.util.RandomLocations;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    private static Map<Integer, LatLng> randomLocations= new HashMap<Integer, LatLng>();
    /* GPS Constant Permission */
    private static final int MY_PERMISSION_ACCESS_COARSE_LOCATION = 11;
    private static final int MY_PERMISSION_ACCESS_FINE_LOCATION = 12;

    private GoogleMap mMap;
    protected LocationManager locationManager;
    protected Location current_location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        if (ContextCompat.checkSelfPermission( this, Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED )
        {
            ActivityCompat.requestPermissions(
                    this,
                    new String [] { Manifest.permission.ACCESS_FINE_LOCATION },
                    MapsActivity.MY_PERMISSION_ACCESS_FINE_LOCATION
            );
        }
        if (ContextCompat.checkSelfPermission( this, Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED )
        {
            ActivityCompat.requestPermissions(
                    this,
                    new String [] { Manifest.permission.ACCESS_COARSE_LOCATION },
                    MapsActivity.MY_PERMISSION_ACCESS_COARSE_LOCATION
            );
        }

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        mapFragment.getMapAsync(this);
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
        mMap.setMinZoomPreference(12);
        markAndMove(RandomLocations.getRandomLocation());
    }

    public void goDurgapur(View view)
    {
        LatLng durgapur = new LatLng(23.5504636,87.2762923);
        markAndMove(durgapur);
    }

    public void goDelhi(View view)
    {
        LatLng delhi = new LatLng(28.6472799,76.8130644);
        markAndMove(delhi);
    }

    public void goMumbai(View view)
    {
        LatLng delhi = new LatLng(19.0825223,72.7410978);
        markAndMove(delhi);
    }

    public void goKolkata(View view)
    {
        LatLng kolkata = new LatLng(22.6763858,88.0495275);
        markAndMove(kolkata);
    }

    public void goChennai(View view)
    {
        LatLng chennai = new LatLng(13.0480438,79.928808);
        markAndMove(chennai);
    }

    public void goRandom(View view)
    {
        markAndMove(RandomLocations.getRandomLocation());
    }

    public void updateCurrentLocation(View view)
    {
        doUpdateCurrentLocation(true);
    }

    public void doUpdateCurrentLocation(boolean useLastLocation)
    {
        if (ContextCompat.checkSelfPermission( this, Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED )
        {
            ActivityCompat.requestPermissions(
                    this,
                    new String [] { Manifest.permission.ACCESS_FINE_LOCATION },
                    MapsActivity.MY_PERMISSION_ACCESS_FINE_LOCATION
            );
        }
        if (ContextCompat.checkSelfPermission( this, Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED )
        {
            ActivityCompat.requestPermissions(
                    this,
                    new String [] { Manifest.permission.ACCESS_COARSE_LOCATION },
                    MapsActivity.MY_PERMISSION_ACCESS_COARSE_LOCATION
            );
        }
        if (useLastLocation)
            this.current_location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        LatLng latLng = new LatLng(this.current_location.getLatitude(), this.current_location.getLongitude());
        markAndMove(latLng);
    }

    @Override
    public void onLocationChanged(Location location) {
        this.current_location = location;
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    public static int getMyPermissionAccessCoarseLocation() {
        return MY_PERMISSION_ACCESS_COARSE_LOCATION;
    }

    public static int getMyPermissionAccessFineLocation() {
        return MY_PERMISSION_ACCESS_FINE_LOCATION;
    }

    public void markAndMove(LatLng latLng)
    {
        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(latLng).title("Marker"));
        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        TextView txtLat = (TextView) findViewById(R.id.currentLocationTxtLat);
        TextView txtLng = (TextView) findViewById(R.id.currentLocationTxtLng);
        TextView txtName = (TextView) findViewById(R.id.currentLocationTxtName);
        txtLat.setText("Latitude: " + latLng.latitude);
        txtLng.setText("Longitude: " + latLng.longitude);
        try {
            Geocoder gcd = new Geocoder(getApplicationContext(), Locale.getDefault());
            List<Address> addresses = gcd.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if (addresses.size() > 0) {
                txtName.setText("Location Name: " + addresses.get(0).getLocality());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
