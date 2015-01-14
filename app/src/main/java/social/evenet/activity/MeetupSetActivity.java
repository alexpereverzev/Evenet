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
import android.os.Message;
import android.provider.CalendarContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import social.evenet.R;
import social.evenet.adapter.AutoCompleteAdapter;
import social.evenet.adapter.CustomAdapter;
import social.evenet.db.Event;
import social.evenet.db.Meetups;
import social.evenet.db.Place;
import social.evenet.db.UserInfo;
import social.evenet.exeption.ContextNullReference;
import social.evenet.helper.Util;

public class MeetupSetActivity extends HashActivity implements LocationListener {

    private AutoCompleteAdapter<Place> adapter;
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
    private EditText name_meetup;
    private Event e;
    private SimpleDateFormat df;
    private Calendar ca;
    private AutoCompleteTextView place_meetup;
    private Handler handler;
    private String[] param;
    private EditText data_meetup;
    private String friends_id;

    private Handler mhandler = new Handler();
    private Meetups meetups;
    private Button save;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meetup_set);
        name_meetup = (EditText) findViewById(R.id.name_meetup);
        mContext = this;
        c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);
        final Context mContext = this;
        time_meetup = (TextView) findViewById(R.id.time_meetup);
        event_meetup = (TextView) findViewById(R.id.event_meetup);
        invite_friends = (TextView) findViewById(R.id.invite_friends);
        place_meetup = (AutoCompleteTextView) findViewById(R.id.place_meetup);
        data_meetup = (EditText) findViewById(R.id.da_meetup);

        final Calendar c = Calendar.getInstance();
        df = new SimpleDateFormat("dd-MM-yyyy");

        if (getIntent().getParcelableExtra("event") != null) {
            e = getIntent().getParcelableExtra("event");
            event_meetup.setText(e.getEvent_title());
            place_meetup.setText(e.getPlace().getPlace_address());
            name_meetup.setText("Meetup_in_" + e.getEvent_title());
            data_meetup.setText(df.format(c.getTime()));

        }

        invite_friends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(mContext, InviteFriendsActivity.class);
                startActivityForResult(i, STATIC_INTEGER_FRIENDS);
            }
        });

        time_meetup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // data_meetup.setText(df.format(c.getTime()));
                new TimePickerDialog(mContext, mTimeSetListener, mHour, mMinute, false).show();
            }
        });

        event_meetup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(mContext, SearchEventActivity.class);
                startActivityForResult(i, STATIC_INTEGER_VALUE);
            }
        });
        adapter = new AutoCompleteAdapter<Place>(this, android.R.layout.simple_spinner_dropdown_item);
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                ArrayList<Place> text = (ArrayList<Place>) msg.obj;
                adapter.addAll(text);
            }
        };


        save = (Button) findViewById(R.id.save_change);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] array_item = new String[7];
                array_item[0] = name_meetup.getText().toString();
                array_item[1] = data_meetup.getText().toString();
                array_item[2] = time_meetup.getText().toString();
                array_item[3] = event_meetup.getText().toString();
                array_item[4] = place_meetup.getText().toString();
                array_item[5] = invite_friends.getText().toString();
                array_item[6] = e.getEnds();
                addEvent1(array_item);
                long timestart = ca.getTimeInMillis() / 1000L;

                location = getLocation();
                param = new String[8];
                param[0] = getSharedPreferences("token_info", MODE_PRIVATE).getString("token", "");
                param[1] = "" + meetups.getMeetup_id();
                param[2] = name_meetup.getText().toString();
                param[3] = "" + timestart;
                if (meetups.getEvent() != null) {
                    param[4] = "" + meetups.getEvent().getEvent_id();
                }
                      param[5] = "" + e.getPlace().getLatitude();
                    param[6] = "" + e.getPlace().getLongitude();


                String place = place_meetup.getText().toString();
                place = place.replace(" ", "0");
                param[7] = place;
                Map<String,String> options=new LinkedHashMap<String, String>();
                options.put("access_token",param[0]);
                options.put("meetup_id",param[1]);
                options.put("name",param[2]);
                options.put("start",param[3]);
                options.put("latitude",param[5]);
                options.put("longitude",param[6]);
                options.put("address",param[7]);

                App.getApi().meetupSet(options, new Callback<JSONArray>() {
                    @Override
                    public void success(JSONArray jsonArray, Response response) {
                            System.out.print(jsonArray);
                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                });
            }
        });

        place_meetup.setAdapter(adapter);

        place_meetup.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // no need to do anything
            }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (((AutoCompleteTextView) place_meetup).isPerformingCompletion()) {
                    return;
                }
                if (charSequence.length() < 2) {
                    return;
                }

                String query = charSequence.toString();
                adapter.clear();

                List<NameValuePair> data = new ArrayList<NameValuePair>();
                data.add(new BasicNameValuePair("keyword", query));

                String url = "http://evenet.me/cgi-bin/search.places.py?count=20&offset=0&zap=" + query;

                ite = new ArrayList<Place>();
                if (query.length() != 0) {





                }


            }

            public void afterTextChanged(Editable editable) {
                //   adapter.notifyDataSetChanged();
            }
        });

        if (getIntent().getParcelableExtra("event") != null) {
            e = getIntent().getParcelableExtra("event");
            event_meetup.setText(e.getEvent_title());
            if (e.getPlace() != null)
                place_meetup.setText(e.getPlace().getPlace_address());
        }

        meetups = getIntent().getParcelableExtra("item");
        name_meetup.setText(meetups.getMeetup_name());
        c.setTimeInMillis(meetups.getStart());
        data_meetup.setText(df.format(c.getTime()));
        if (meetups.getEvent() != null) {
            event_meetup.setText(meetups.getEvent().getEvent_title());
            place_meetup.setText(meetups.getEvent().getPlace().getPlace_address());
        }
        time_meetup.setText(c.get(Calendar.HOUR) + ":" + c.get(Calendar.MINUTE));
    }

    public void AddToAdapter(Place place) {
        if (adapter != null)
            adapter.add(place);
        else {
            adapter = new AutoCompleteAdapter<Place>(this, android.R.layout.simple_spinner_dropdown_item);
            adapter.add(place);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.meet_up_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }


    private int DeleteCalendarEntry(long entryID) {
        int iNumRowsDeleted = 0;


        // long eventID = 201;

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
