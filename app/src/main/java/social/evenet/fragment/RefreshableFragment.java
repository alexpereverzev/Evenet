package social.evenet.fragment;

import java.util.ArrayList;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import social.evenet.activity.App;
import social.evenet.R;



public abstract class RefreshableFragment extends Fragment implements  LocationListener {
	

	
	RelativeLayout progressBar = null;
	
	private Handler mHandler = new Handler();

    public static Location location;
    private boolean isGPSEnabled = false;
    private boolean isNetworkEnabled = false;
    private boolean canGetLocation = false;
    private LocationManager locationManager;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	};
	
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {

		super.onSaveInstanceState(outState);
		
	};
	

	

	

	protected void showProgressBar(){
		if(progressBar == null || progressBar.getVisibility() != View.VISIBLE){
            LayoutInflater vi = (LayoutInflater) App.getInstance().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View v = vi.inflate(R.layout.fragment_progress_bar, null);
			progressBar = (RelativeLayout)v.findViewById(R.id.curtain_loading_rl);
			((ViewGroup)getView()).addView(progressBar);		
		}
	}
	
	protected void hideProgressBar(final String message){
		if (progressBar != null) {
			mHandler.post(new Runnable() {
                public void run() {
                    progressBar.setVisibility(View.GONE);
                    if (!"".equals(message)) {
                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                    }
                }
            });
		}
	}



    public void setActionBar(String title) {
        final Context c = getActivity();
        ActionBar actionBar = ((ActionBarActivity) c).getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        View customBar = View.inflate(c.getApplicationContext(), R.layout.actionbar_custom, null);
        actionBar.setCustomView(customBar);
        TextView textTitle = (TextView) customBar.findViewById(R.id.title);
        textTitle.setText(title);





    }


    public void setImageView(int a, ImageView imageView) {

        switch (a) {
            case 1:
                imageView.setImageResource(R.drawable.ic_first_big);
                break;
            case 2:
                imageView.setImageResource(R.drawable.ic_second_big);
                break;
            case 3:
                imageView.setImageResource(R.drawable.ic_third_big);
                break;
            case 4:
                imageView.setImageResource(R.drawable.ic_four_big);
                break;
            case 5:
                imageView.setImageResource(R.drawable.ic_five_big);
                break;
            case 6:
                imageView.setImageResource(R.drawable.ic_six_big);
                break;
            case 7:
                imageView.setImageResource(R.drawable.ic_seven_big);
                break;
            case 8:
                imageView.setImageResource(R.drawable.ic_eight_big);
                break;

        }

    }


    public Location getLocation() {
        try {
            locationManager = (LocationManager) getActivity()
                    .getSystemService(Context.LOCATION_SERVICE);

            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            isNetworkEnabled = locationManager
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
                    Log.d("Network", "Network Enabled");
                    if (locationManager != null) {
                        location = locationManager
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {

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