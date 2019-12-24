package alarmitron.personalalarmhomepage;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * class that represents the fragment for creating alarms that will
 * go off depending on your location
 * @author The Alarmitron Team
 */
public class GPSFragment extends Fragment implements OnMapReadyCallback, LocationListener {

    public MapView mMapView;
    public View alarmGPSView;
    public GoogleMap mGoogleMap;
    public LocationManager locationManager;
    public double lat;
    public double lon;

    /**
     * method that inflates the view needed for the user to interact with this fragment
     * @param inflater
     *      layout inflater that inflates layout
     * @param container
     *      container for fragment
     * @param savedInstanceState
     * @return
     *      view of fragment
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        alarmGPSView = inflater.inflate(R.layout.fragment_gps, container, false);
        return alarmGPSView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLocation();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    mMapView = new MapView(getContext());
                    mMapView.onCreate(null);
                    mMapView.onPause();
                    mMapView.onDestroy();
                }catch(Exception e){

                }
            }
        }).start();
    }

    public void getLocation(){
        try {
            locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
            Location loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            onLocationChanged(loc);
        }
        catch(SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mMapView = (MapView) alarmGPSView.findViewById(R.id.map);
        if(mMapView != null){
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }
    }

    /**
     * standard toString method that returns name of fragment
     * @return
     *      name of fragment
     */
    @Override
    public String toString(){
        return "GPSFragment";
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
    public void onMapReady(GoogleMap googleMap){
        mGoogleMap = googleMap;
        MapsInitializer.initialize(getContext());
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(lat, lon)).title("Jesus Christ, it's Jason Bourne!").snippet("plus or minus 100ft..."));
        CameraPosition cp = CameraPosition.builder().target(new LatLng(lat, lon)).zoom(16).bearing(0).tilt(45).build();
        mGoogleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cp));
    }


    @Override
    public void onLocationChanged(Location location) {
        MainActivity main = (MainActivity) getActivity();
        main.lat = location.getLatitude();
        main.lon = location.getLongitude();
        lat = location.getLatitude();
        lon = location.getLongitude();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Latitude","status");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("Latitude","enable");
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("Latitude","disable");
    }
}
