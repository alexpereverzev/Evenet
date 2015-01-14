package social.evenet.fragment;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import social.evenet.R;
import social.evenet.activity.MeetUpAddActivity;

/**
 * Created by Александр on 09.11.2014.
 */
public class MapsFragment extends Fragment implements LocationListener {

    MapView mMapView;
    private GoogleMap googleMap;
    private Marker marker;
    private ImageView choose;
    private String [] addres=new String[3];
    private String lon;
    private String lat;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_map, container, false);
        mMapView = (MapView) v.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        choose=(ImageView) v.findViewById(R.id.location);

        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b=getArguments();
                b.putStringArray("address",addres);
                b.putString("lat",lat);
                b.putString("lon",lon);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_meetup, MeetupCreateFragmentSix.getMeetupCreateFragmetSix(b)).commit();
            }
        });
        mMapView.onResume();// needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        googleMap = mMapView.getMap();
        getLocation();
        if(location!=null){
            CameraUpdate center=
                    CameraUpdateFactory.newLatLng(new LatLng(latitude,
                            longitude));
            if(latitude!=0){
                System.out.print(latitude);
            }
            System.out.print(latitude);
            System.out.print(longitude);


            CameraUpdate zoom= CameraUpdateFactory.zoomTo(8);


            googleMap.moveCamera(center);
            googleMap.animateCamera(zoom);

            googleMap.addMarker(new MarkerOptions().position(new LatLng(latitude,
                    longitude)).title("Marker"));

        }

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Geocoder geo = new Geocoder(getActivity(), Locale.getDefault());
                List<Address> addresses = null;
                try {
                    addresses = geo.getFromLocation(latLng.latitude, latLng.longitude, 1);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                if (addresses.isEmpty()) {

                }
                else {
                    if (addresses.size() > 0) {

                     addres[0]=addresses.get(0).getThoroughfare();
                     addres[1]=addresses.get(0).getSubAdminArea();
                     addres[2]=addresses.get(0).getLocality();
                    lat=""+latLng.latitude;
                    lon=""+latLng.longitude;

                        if(marker!=null){
                            marker.remove();
                        }
                        marker=googleMap.addMarker(new MarkerOptions().position(latLng).title("choose").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_choose_location)));


                        Toast.makeText(getActivity(), "Address:-  " + addresses.get(0).getFeatureName() + addresses.get(0).getAdminArea() + addresses.get(0).getLocality(), Toast.LENGTH_LONG).show();
                    }
                }
            }


        });

        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                return false;
            }
        });


        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private boolean isGPSEnabled = false;
    private boolean isNetworkEnabled = false;
    private boolean canGetLocation = false;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute
    private Location location;
    private double latitude;
    private double longitude;

    public Location getLocation() {
        try {
          LocationManager  locationManager = (LocationManager) getActivity()
                    .getSystemService(Context.LOCATION_SERVICE);

          boolean  isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

          boolean  isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
            } else {
                this.canGetLocation = true;
                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                    if (locationManager != null) {
                        location = locationManager
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }
                if (isGPSEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        Log.d("GPS", "GPS Enabled");
                        if (locationManager != null) {
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return location;
    }


    @Override
    public void onLocationChanged(Location location) {

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
}
