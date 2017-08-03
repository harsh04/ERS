package internship.ibm.com.ers;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnCameraChangeListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap;
    private LatLngBounds India_bound = new LatLngBounds(new LatLng(7.80822, 68.00000), new LatLng(38.20570, 97.00000));
    private LatLng dehradun = new LatLng(30.31666667,78.06666667);
    ProgressDialog progressMap;
    private GoogleApiClient mGoogleApiClient;
    private int PROXIMITY_RADIUS = 100000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        progressMap = ProgressDialog.show(MainActivity.this, "Loading", "Getting Map Ready for you...", true);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setLatLngBoundsForCameraTarget(India_bound);
        mMap.setOnCameraChangeListener(this);
        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                progressMap.dismiss();
                mMap.animateCamera(CameraUpdateFactory.zoomTo(12));
                mMap.moveCamera( CameraUpdateFactory.newLatLngZoom(dehradun, 12) );
                mMap.clear();
                //display hospitals
                String url_hospital = getUrl(dehradun.latitude, dehradun.longitude, "hospital");
                Object[] DataTransfer = new Object[2];
                DataTransfer[0] = mMap;
                DataTransfer[1] = url_hospital;
                Log.d("onClick1", url_hospital);
                GetNearbyHospitalData getNearbyHospitalData = new GetNearbyHospitalData();
                getNearbyHospitalData.execute(DataTransfer);
               //display fire stations
                String url_fireStation = getUrl(dehradun.latitude, dehradun.longitude, "fire_station");
                Object[] DataTrans = new Object[2];
                DataTrans[0] = mMap;
                DataTrans[1] = url_fireStation;
                Log.d("onClick2", url_fireStation);
                GetNearbyFireStationData getNearbyFireStationData = new GetNearbyFireStationData();
                getNearbyFireStationData.execute(DataTrans);
            }
        });
    }

    private String getUrl(double latitude, double longitude, String nearbyPlace) {

        StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlacesUrl.append("location=" + latitude + "," + longitude);
        googlePlacesUrl.append("&radius=" + PROXIMITY_RADIUS);
        googlePlacesUrl.append("&type=" + nearbyPlace);
        googlePlacesUrl.append("&sensor=true");
        googlePlacesUrl.append("&key=" + "AIzaSyAdiZV-RjiNM3bESXxHkVdX59GS_LyBgY0");
        Log.d("getUrl", googlePlacesUrl.toString());
        return (googlePlacesUrl.toString());
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        float minZoom = 4;
        if (cameraPosition.zoom < minZoom) {
            mMap.animateCamera(CameraUpdateFactory.zoomTo(minZoom));
            mMap.moveCamera( CameraUpdateFactory.newLatLngZoom(India_bound.getCenter(), 4) );
        }
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
}
