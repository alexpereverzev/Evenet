package social.evenet.activity;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.CalendarContract;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.TimeZone;

import social.evenet.R;
import social.evenet.adapter.AutoCompleteAdapter;
import social.evenet.adapter.CustomAdapter;
import social.evenet.db.Event;
import social.evenet.db.Place;
import social.evenet.db.UserInfo;
import social.evenet.fragment.MeetupCreateFragment;
import social.evenet.fragment.MeetupCreateFragmentSix;

public class MeetUpAddActivity extends HashActivity implements LocationListener {

    private AutoCompleteAdapter<Place> adapter;
    private TimePicker timePicker1;
    private TextView time_meetup;
    private TextView event_meetup;
    private TextView invite_friends;
    private CustomAdapter adapter_custom;
    private ArrayList<Place> ite;
    static final int TIME_DIALOG_ID = 999;
    int mHour;
    int mMinute;
    private Calendar c;
    final int STATIC_INTEGER_VALUE = 100;
    final int STATIC_INTEGER_FRIENDS = 101;
    private Button map;
    private Button create;
    private EditText name_meetup;
    private Event e;
    private SimpleDateFormat df;
    private Calendar ca;
    private int meetup_id;
    private View customBar;

    public View getCustomBar() {
        return customBar;
    }

    private AutoCompleteTextView place_meetup;
    private Handler handler;
    private String[] param;
    private EditText data_meetup;
    private String friends_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meet_create);
        setActionBar("");


        getSupportFragmentManager().beginTransaction().replace(R.id.content_meetup, new MeetupCreateFragment()).commit();

    }




    public void setActionBar(String title) {

        Toolbar actionbar = (Toolbar) findViewById(R.id.actionbar);
        LayoutInflater mInflater = LayoutInflater.from(this);
        customBar   = mInflater.inflate(R.layout.actionbar_custom, actionbar);

        ImageView disable_actionbar=(ImageView)customBar.findViewById(R.id.disable_actionbar);

        TextView done=(TextView) customBar.findViewById(R.id.done_actionbar);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        disable_actionbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if(title.equals("Invite Friends")){
            disable_actionbar.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            //   String newText = data.getStringExtra(PUBLIC_STATIC_STRING_IDENTIFIER);
            Bundle b = data.getParcelableExtra("data");
            getSupportFragmentManager().beginTransaction().replace(R.id.content_meetup, MeetupCreateFragmentSix.getMeetupCreateFragmetSix(b)).commit();

        }
        switch (requestCode) {
            case (103): {
                if (resultCode == Activity.RESULT_OK) {
                    //   String newText = data.getStringExtra(PUBLIC_STATIC_STRING_IDENTIFIER);
                    Bundle b = data.getParcelableExtra("data");
                    getSupportFragmentManager().beginTransaction().replace(R.id.content_meetup, MeetupCreateFragmentSix.getMeetupCreateFragmetSix(b)).commit();

                   }
                break;
            }
            case (STATIC_INTEGER_VALUE): {
                if (resultCode == Activity.RESULT_OK) {
                    //   String newText = data.getStringExtra(PUBLIC_STATIC_STRING_IDENTIFIER);
                    e = data.getParcelableExtra("item");
                    event_meetup.setText(e.getEvent_title());
                    place_meetup.setText(e.getPlace().getPlace_address());
                    // TODO Update your TextView.
                }
                break;
            }
            case (STATIC_INTEGER_FRIENDS): {
                if (resultCode == Activity.RESULT_OK) {
                    String invite_id = "";
                    String invite_name = " ";
                    //   String newText = data.getStringExtra(PUBLIC_STATIC_STRING_IDENTIFIER);
                    ArrayList<UserInfo> e = data.getParcelableArrayListExtra("user");
                    Iterator<UserInfo> iterator = e.iterator();
                    while (iterator.hasNext()) {
                        UserInfo userInfo = iterator.next();
                        invite_name = invite_name + userInfo.getName() + " " + userInfo.getSurname() + " ";
                        if (userInfo.getSurname() != null)
                            invite_id = invite_id + userInfo.getUser_id() + ",";
                    }
                    invite_friends.setText(invite_name);

                    //  if(invite_id.split(",").length>1){
                    invite_id = invite_id.substring(0, invite_id.length() - 1);
                    //}
                    friends_id = invite_id;
                    // TODO Update your TextView.
                }
                break;

            }
        }
    }


    private TimePickerDialog.OnTimeSetListener mTimeSetListener =
            new TimePickerDialog.OnTimeSetListener() {
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    mHour = hourOfDay;
                    mMinute = minute;
                    time_meetup.setText(new StringBuilder()
                            .append(pad(mHour)).append(":")
                            .append(pad(mMinute)));
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.HOUR, mHour);
                    calendar.set(Calendar.MINUTE, mHour);

                    data_meetup.setText(df.format(c.getTime()));

                }
            };


    private static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }


    private int DeleteCalendarEntry(long entryID) {
        int iNumRowsDeleted = 0;
        ContentResolver cr = getContentResolver();
        ContentValues values = new ContentValues();
        Uri deleteUri = null;
        Uri eventsUri = Uri.parse("content://com.android.calendar/events");
        deleteUri = ContentUris.withAppendedId(eventsUri, eventID);
        int rows = getContentResolver().delete(deleteUri, null, null);

        return iNumRowsDeleted;
    }


    public void addEvent1(String[] array) {
        Cursor cursor = getContentResolver().query(Uri.parse("content://com.android.calendar/calendars"), new String[]{"_id", "calendar_displayName"}, null, null, null);
        cursor.moveToFirst();
        // Get calendars name
        String calendarNames[] = new String[cursor.getCount()];
        // Get calendars id
        int[] calendarId = new int[cursor.getCount()];
        for (int i = 0; i < calendarNames.length; i++) {
            calendarId[i] = cursor.getInt(0);
            calendarNames[i] = cursor.getString(1);
            cursor.moveToNext();
        }

        Date d = new Date(Long.parseLong(e.getEnds()) * 1000);
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        ca = Calendar.getInstance();
        try {

            ca.setTime(df.parse(array[1]));
            ca.set(Calendar.HOUR, mHour);
            ca.set(Calendar.MINUTE, mMinute);

        } catch (ParseException e1) {
            e1.printStackTrace();
        }
        long endMillis = 0;
        Calendar beginTime = Calendar.getInstance();
        beginTime.set(2014, Calendar.OCTOBER, 5, 15, 30);
        long startMillis = beginTime.getTimeInMillis();
        Calendar endTime = Calendar.getInstance();
        endTime.set(2014, Calendar.OCTOBER, 8, 8, 45);
        endMillis = endTime.getTimeInMillis();

        ContentValues contentEvent = new ContentValues();
        // Particular Calendar in which we need to add Event
        contentEvent.put("calendar_id", calendarId[0]);
        // Title/Caption of the Event
        contentEvent.put(CalendarContract.Events.TITLE, array[0]);
        // Description of the Event
        contentEvent.put(CalendarContract.Events.DESCRIPTION, array[3]);
        // Venue/Location of the Event
        contentEvent.put(CalendarContract.Events.EVENT_LOCATION, array[4]);
        // Start Date of the Event with Time
        contentEvent.put(CalendarContract.Events.DTSTART, ca.getTimeInMillis());
        // End Date of the Event with Time
        //  contentEvent.put(CalendarContract.Events.DTEND, cal.getTimeInMillis());
        // All Day Event
        contentEvent.put("allDay", 1);

        // Set alarm for this Event
        contentEvent.put("hasAlarm", 1);
        contentEvent.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().getID());

        Uri eventsUri = Uri.parse("content://com.android.calendar/events");
        // event is added successfully
        //  Uri last = getContentResolver().insert(eventsUri, contentEvent);
        // eventID = Long.parseLong(last.getLastPathSegment());

        cursor.close();

    }

    private long eventID;
    public static Location location;
    private LocationManager locationManager;
    private Context mContext;
    private boolean isGPSEnabled = false;
    private boolean isNetworkEnabled = false;
    private boolean canGetLocation = false;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute
    private double latitude;
    private double longitude;


    public Location getLocation() {
        try {
            locationManager = (LocationManager) mContext
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
