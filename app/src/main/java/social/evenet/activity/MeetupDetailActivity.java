package social.evenet.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.koushikdutta.async.ByteBufferList;
import com.koushikdutta.async.DataEmitter;
import com.koushikdutta.async.callback.CompletedCallback;
import com.koushikdutta.async.callback.DataCallback;
import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.WebSocket;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedFile;
import social.evenet.BL.ChatBL;
import social.evenet.R;
import social.evenet.adapter.ChatAdapter;
import social.evenet.db.Event;
import social.evenet.db.MainAttachment;
import social.evenet.db.Meetups;
import social.evenet.db.Place;
import social.evenet.db.UserInfo;
import social.evenet.helper.Connectivity;
import social.evenet.helper.SupportInfo;
import social.evenet.helper.Util;
import social.evenet.model.ChatModel;
import social.evenet.multi.Action;
import social.evenet.widget.AccessibleTextView;
import social.evenet.widget.HapticFeedbackController;
import social.evenet.widget.RadialPickerLayout;

public class MeetupDetailActivity extends HashActivity implements RadialPickerLayout.OnValueSelectedListener, TimePickerDialog.OnTimeSetListener /*implements SlidingTray.OnDrawerOpenListener,
        SlidingTray.OnDrawerCloseListener*/ {


    private List<ChatModel> item;
    private ChatAdapter adapter;
    private ListView listView;
    private Button send;
    private ImageView add;
    private EditText chat_edit;
    private ChatBL chatBL;
    private Place choose_place;
    //   private WebSocket webSoc;
    private byte[] icon;
    private Button updload;
    private Event e;
    private Place place;
    private ArrayList<byte[]> bytes = new ArrayList<byte[]>();
    final int STATIC_INTEGER_VALUE = 101;
    private String user_id = "";
    private ChatModel model;
    private LinkedList<ChatModel> list_item = new LinkedList<ChatModel>();
    private static int count_foto;


    private Meetups meetups;
    //    private TextView time;
    private Button start_chat;
    private TextView name_meetup;
    private TextView address;
    private ImageView meetup_icon_title;
    private ImageView not_know;
    private Button going;
    private Button not_going;
    private String[] param;
    private ChatBL bl;
    private String url_meet_status;
    private Intent intent;
    private Button edit_meetup;
    private ImageView icon_address;
    private String meetup_id;
    private Intent intent1;
    private TextView numberfriends;
    private ImageView friends_going;
    //private SlidingTray mSlidingTray;
    private ArrayList<UserInfo> user_going = new ArrayList<UserInfo>();
    private ArrayList<UserInfo> user_not = new ArrayList<UserInfo>();
    private ArrayList<UserInfo> user_not_know = new ArrayList<UserInfo>();
    private ImageView mArrowImage;
    private SlidingUpPanelLayout sliding;
    private LinearLayout content;
    String TAG = "test";
    private LinearLayout calendar1;
    private ImageView icon_panel;
    private ImageView edit_name;
    private ImageView edit_place;
    private ImageView venue;
    private ImageView location;


    private LinearLayout edit_date;
    private LinearLayout edit_name_layout;
    private LinearLayout edit_place_layout;
    private LinearLayout place_layout;
    private LinearLayout name_layout;
    private LinearLayout address_save;
    private LinearLayout address_before;
    private LinearLayout date_content;

    private ImageView accept_name;
    private ImageView accept_place;

    private EditText editText;
    private LinearLayout time1;
    private ImageView cancel_edit;
    private ImageView cancel_place;

    private TextView edit_address_text;
    //  private TextView address;
    private TextView date_text;
    private double lat;
    private double lon;
    private CaldroidFragment caldroidFragment;

    private Context context;


    private TimePickerDialog.OnTimeSetListener mCallback;

    private HapticFeedbackController mHapticFeedbackController;
    private static final String KEY_HOUR_OF_DAY = "hour_of_day";
    private static final String KEY_MINUTE = "minute";
    private static final String KEY_IS_24_HOUR_VIEW = "is_24_hour_view";
    private static final String KEY_CURRENT_ITEM_SHOWING = "current_item_showing";
    private static final String KEY_IN_KB_MODE = "in_kb_mode";
    private static final String KEY_TYPED_TIMES = "typed_times";
    private static final String KEY_DARK_THEME = "dark_theme";

    public static final int HOUR_INDEX = 0;
    public static final int MINUTE_INDEX = 1;
    // NOT a real index for the purpose of what's showing.
    public static final int AMPM_INDEX = 2;
    // Also NOT a real index, just used for keyboard mode.
    public static final int ENABLE_PICKER_INDEX = 3;
    public static final int AM = 0;
    public static final int PM = 1;

    // Delay before starting the pulse animation, in ms.
    private static final int PULSE_ANIMATOR_DELAY = 300;
    private TextView mDoneButton;
    private TextView mHourView;
    private TextView mHourSpaceView;
    private TextView mMinuteView;
    private TextView mMinuteSpaceView;
    private TextView mAmPmTextView;
    private View mAmPmHitspace;
    private RadialPickerLayout mTimePicker;

    private AccessibleTextView time;
    private AccessibleTextView minute;
    private WebSocket webSoc;

    private int mSelectedColor;
    private int mUnselectedColor;
    private String mAmText;
    private String mPmText;
    private String mDoneText;

    private boolean mAllowAutoAdvance = true;
    private int mInitialHourOfDay;
    private int mInitialMinute;
    private boolean mIs24HourMode = false;
    private boolean mThemeDark;
    private static String url_change = SupportInfo.url + "meetup.setInfo.php";
    // For hardware IME input.
    private char mPlaceholderText;
    private String mDoublePlaceholderText;
    private String mDeletedKeyFormat;
    private boolean mInKbMode;
    private ArrayList<Integer> mTypedTimes;
    private Node mLegalTimesTree;
    private int mAmKeyCode;
    private int mPmKeyCode;
    private TextView ampm;
    private String mHourPickerDescription;
    private String mSelectHours;
    private String mMinutePickerDescription;
    private String mSelectMinutes;
    private ImageView accept;
    private ImageView disable;
    private Map<String,String> params;
    private TextView time_meetup;

    DisplayImageOptions options = new DisplayImageOptions.Builder()
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .considerExifParams(true).build();

    public void initialize(TimePickerDialog.OnTimeSetListener callback,
                           int hourOfDay, int minute, boolean is24HourMode) {
        mCallback = callback;

        mInitialHourOfDay = hourOfDay;
        mInitialMinute = minute;
        mIs24HourMode = is24HourMode;
        mInKbMode = false;
        mThemeDark = false;
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meetup_panel);
        setActionBar();
        context = this;
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {

                ChatModel model = (ChatModel) msg.obj;

                getUserPhoto(model, true, 0);

            }
        };

        sliding = (SlidingUpPanelLayout) findViewById(R.id.sliding_top);

        edit_name = (ImageView) findViewById(R.id.edit_name);

        edit_place = (ImageView) findViewById(R.id.edit_address);

        edit_date = (LinearLayout) findViewById(R.id.edit_data);

        edit_name_layout = (LinearLayout) findViewById(R.id.edit_name_layout);

        name_layout = (LinearLayout) findViewById(R.id.name_layout);

        place_layout = (LinearLayout) findViewById(R.id.place_layout);

        edit_place_layout = (LinearLayout) findViewById(R.id.edit_place_layout);

        address_save = (LinearLayout) findViewById(R.id.address_save);

        address_before = (LinearLayout) findViewById(R.id.address_before);

        address_save = (LinearLayout) findViewById(R.id.address_save);

        address_before = (LinearLayout) findViewById(R.id.address_before);

        edit_address_text = (TextView) findViewById(R.id.edit_address_text);

        editText = (EditText) findViewById(R.id.change_name);

        cancel_edit = (ImageView) findViewById(R.id.cancel_edit);

        accept_name = (ImageView) findViewById(R.id.accept_name);

        venue = (ImageView) findViewById(R.id.venue);

        location = (ImageView) findViewById(R.id.location);

        cancel_place = (ImageView) findViewById(R.id.cancel_place);

        name_meetup = (TextView) findViewById(R.id.name_meetup);

        address = (TextView) findViewById(R.id.address);

        date_text = (TextView) findViewById(R.id.date);

        content = (LinearLayout) findViewById(R.id.content);

        date_content = (LinearLayout) findViewById(R.id.date_content);

        accept_place = (ImageView) findViewById(R.id.accept_place);

        time1 = (LinearLayout) findViewById(R.id.time);

        calendar1 = (LinearLayout) findViewById(R.id.calendar1);

        edit_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                content.setVisibility(View.GONE);
                date_content.setVisibility(View.VISIBLE);
            }
        });

        final SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM, yyyy");
        caldroidFragment = new CaldroidFragment();

        time = (AccessibleTextView) findViewById(R.id.include_content).findViewById(R.id.hours);

        time_meetup = (TextView) findViewById(R.id.time_meetup);

        minute = (AccessibleTextView) findViewById(R.id.include_content).findViewById(R.id.minutes);
        KeyboardListener keyboardListener = new KeyboardListener();
        ampm = (TextView) findViewById(R.id.include_content).findViewById(R.id.ampm_label);
        ampm.setText("AM");
        mAmPmHitspace = findViewById(R.id.ampm_hitspace);
        mHourView = (TextView) findViewById(R.id.hours);
        mHourView.setOnKeyListener(keyboardListener);
        mHourSpaceView = (TextView) findViewById(R.id.hour_space);
        mMinuteSpaceView = (TextView) findViewById(R.id.minutes_space);
        mMinuteView = (TextView) findViewById(R.id.minutes);
        mMinuteView.setOnKeyListener(keyboardListener);
        mAmPmTextView = (TextView) findViewById(R.id.ampm_label);
        mAmPmTextView.setOnKeyListener(keyboardListener);
        String[] amPmTexts = new DateFormatSymbols().getAmPmStrings();
        mAmText = amPmTexts[0];
        mPmText = amPmTexts[1];

        mHapticFeedbackController = new HapticFeedbackController(this);

        mTimePicker = (RadialPickerLayout) findViewById(R.id.time_picker);
        mTimePicker.setOnValueSelectedListener(this);
        mTimePicker.setOnKeyListener(keyboardListener);
        mTimePicker.initialize(this, mHapticFeedbackController, mInitialHourOfDay,
                mInitialMinute, mIs24HourMode);

        int currentItemShowing = HOUR_INDEX;
        if (savedInstanceState != null &&
                savedInstanceState.containsKey(KEY_CURRENT_ITEM_SHOWING)) {
            currentItemShowing = savedInstanceState.getInt(KEY_CURRENT_ITEM_SHOWING);
        }
        setCurrentItemShowing(currentItemShowing, false, true, true);
        mTimePicker.invalidate();

        mHourView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCurrentItemShowing(HOUR_INDEX, true, false, true);
                tryVibrate();
            }
        });
        mMinuteView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCurrentItemShowing(MINUTE_INDEX, true, false, true);
                tryVibrate();
            }
        });

        disable = (ImageView) findViewById(R.id.disable);

        disable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar1.setVisibility(View.VISIBLE);
                time1.setVisibility(View.GONE);
            }
        });

        accept = (ImageView) findViewById(R.id.accept);

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour = Integer.parseInt(time.getText().toString());
                if (ampm.getText().equals("PM")) {

                    hour = 12 + hour;
                    time.setText("" + hour);
                }
                time_meetup.setText(time.getText().toString() + " : " + minute.getText().toString());
                content.setVisibility(View.VISIBLE);
                calendar1.setVisibility(View.VISIBLE);
                time1.setVisibility(View.GONE);
                date_content.setVisibility(View.GONE);
            }
        });


        Bundle args = new Bundle();
        Calendar cal = Calendar.getInstance();
        args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
        args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
        args.putBoolean(CaldroidFragment.ENABLE_SWIPE, true);
        args.putBoolean(CaldroidFragment.SIX_WEEKS_IN_CALENDAR, true);
        caldroidFragment.setArguments(args);
        caldroidFragment.setCaldroidListener(new CaldroidListener() {
            @Override
            public void onSelectDate(Date date, View view) {
                String s = formatter.format(date);
                Calendar cal = Calendar.getInstance();
                date_text.setText(s);
                // Min date is last 7 days
                time1.setVisibility(View.VISIBLE);
                calendar1.setVisibility(View.GONE);
                cal.add(Calendar.DATE, -18);
                Date blueDate = cal.getTime();
                Date greenDate = date;

                if (caldroidFragment != null) {

                    caldroidFragment.setBackgroundResourceForDate(R.color.green,
                            blueDate);

                    caldroidFragment.setTextColorForDate(R.color.white, blueDate);
                }

            }
        });
        //setCustomResourceForDates();

        // Attach to the activity
        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.calendar1, caldroidFragment);
        t.commit();


        cancel_place.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                place_layout.setVisibility(View.VISIBLE);
                edit_place_layout.setVisibility(View.GONE);
            }
        });


        accept_place.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                params = new LinkedHashMap<String, String>();
                params.put("access_token", getSharedPreferences("token_info", MODE_PRIVATE).getString("token", ""));
                params.put("meetup_id", meetup_id);
                if (choose_place != null)
                    params.put("place_id", ""+choose_place.getPlace_id());
                else {
                    params.put("address", edit_address_text.getText().toString());
                    params.put("latitude", ""+lat);
                    params.put("longitude", ""+lon);
                }

                changePost(params);
                if (change_success) {
                    address.setText(edit_address_text.getText().toString());
                    change_success = false;
                }
                place_layout.setVisibility(View.VISIBLE);
                edit_place_layout.setVisibility(View.GONE);
            }
        });

        venue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, SearchPlaceActivity.class);
                intent.putExtra("detail", "detail");
                startActivityForResult(intent, 104);
                //   place_layout.setVisibility(View.VISIBLE);
                //   edit_place_layout.setVisibility(View.GONE);
            }
        });

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, MapsActivity.class);
                startActivityForResult(intent, 103);
                //   place_layout.setVisibility(View.VISIBLE);
                //   edit_place_layout.setVisibility(View.GONE);
            }
        });

        edit_place.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                place_layout.setVisibility(View.GONE);
                edit_place_layout.setVisibility(View.VISIBLE);
                address_before.setVisibility(View.VISIBLE);
                address_save.setVisibility(View.GONE);
            }
        });


        cancel_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name_layout.setVisibility(View.VISIBLE);
                edit_name_layout.setVisibility(View.GONE);
                hideKeyboard();
            }
        });

        accept_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                params = new LinkedHashMap<String, String>();
                params.put("access_token", getSharedPreferences("token_info", MODE_PRIVATE).getString("token", ""));
                params.put("meetup_id", meetup_id);
                params.put("name", editText.getText().toString());


                changePost(params);


                name_layout.setVisibility(View.VISIBLE);
                edit_name_layout.setVisibility(View.GONE);
                hideKeyboard();
                if (change_success) {
                    name_meetup.setText(editText.getText().toString());
                    change_success = false;
                }
            }
        });


        edit_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name_layout.setVisibility(View.GONE);
                edit_name_layout.setVisibility(View.VISIBLE);
                editText.setText(name_meetup.getText().toString());
                editText.requestFocus();
            }
        });

        sliding.setPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                Log.i(TAG, "onPanelSlide, offset " + slideOffset);
            }

            @Override
            public void onPanelExpanded(View panel) {
                Log.i(TAG, "onPanelExpanded");

            }

            @Override
            public void onPanelCollapsed(View panel) {
                Log.i(TAG, "onPanelCollapsed");

            }

            @Override
            public void onPanelAnchored(View panel) {
                Log.i(TAG, "onPanelAnchored");
            }

            @Override
            public void onPanelHidden(View panel) {

                Log.i(TAG, "onPanelHidden");
            }
        });

        icon_panel = (ImageView) findViewById(R.id.panel);
        icon_panel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sliding.expandPanel();
            }
        });


        user_id = getSharedPreferences("token_info", MODE_PRIVATE).getString("user_id", "");
        meetup_id = "" + getIntent().getIntExtra("meetup_id", 0);


        Map<String ,String> param_info=new LinkedHashMap<>();
        param_info.put("access_token",getSharedPreferences("token_info", MODE_PRIVATE).getString("token", ""));
        param_info.put("meetup_id", meetup_id);

        App.getApi().meetupInfo(param_info,new Callback<JSONArray>() {
            @Override
            public void success(JSONArray jsonArray, Response response) {
                meetups = new Meetups();


                try {

                    for (int i = 0; i < jsonArray.length(); i++) {
                        Place place = new Place();

                        JSONObject jsonobject = jsonArray.getJSONObject(i);
                        meetups.setMeetup_id(jsonobject.getInt("meetup_id"));

                        if (!jsonobject.isNull("start"))
                            meetups.setStart(jsonobject.getInt("start"));
                        if (!jsonobject.isNull("end")) meetups.setEnd(jsonobject.getInt("end"));
                        meetups.setMeetup_name(jsonobject.getString("meetup_name"));



                        meetups.setUsers_count(jsonobject.getInt("users_count"));
                        if (!jsonobject.isNull("point")) {
                            if (!jsonobject.getJSONObject("point").isNull("place")) {
                                place.setPlace_id(jsonobject.getJSONObject("point").getJSONObject("place").getInt("place_id"));
                                place.setPlace_title(jsonobject.getJSONObject("point").getJSONObject("place").getString("place_title"));
                                place.setPlace_address(jsonobject.getJSONObject("point").getJSONObject("place").getString("place_address"));
                            } else if (!jsonobject.getJSONObject("point").isNull("address")) {
                                place.setPlace_address(jsonobject.getJSONObject("point").getString("address"));
                            }
                        }
                        meetups.setPlace(place);
                    }

                } catch (JSONException e1) {
                    e1.printStackTrace();
                }

                if (meetups.getPlace() != null) {
                    address.setText(meetups.getPlace().getPlace_address());
                }
                name_meetup.setText(meetups.getMeetup_name());
                count_user.setText("" + meetups.getUsers_count());
                time.setText("" + meetups.getStart());
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });





        listView = (ListView) findViewById(R.id.content_message);
        send = (Button) findViewById(R.id.send_message);
        sendChat();


        add = (ImageView) findViewById(R.id.add_content);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_dialog();
            }
        });
        chat_edit = (EditText) findViewById(R.id.edit_message);
        chatBL = new ChatBL(this);
        item = chatBL.getChat(Integer.parseInt(meetup_id));
        for (ChatModel model : item) {
            list_item.addLast(model);
        }


        adapter = new ChatAdapter(this, item);
        listView.setAdapter(adapter);
        param = new String[5];
        param[0] = getSharedPreferences("token_info", MODE_PRIVATE).getString("token", "");


        param[1] = "" + meetup_id;

        if (Connectivity.isConnectedFast(this)) {
            param[2] = "100";
            param[3] = "" + 0;
        } else {
            param[2] = "40";
            param[3] = "" + 0;
        }


        Map<String ,String> chat_param=new LinkedHashMap<>();
        chat_param.put("access_token", param[0]);
        chat_param.put("meetup_id", param[1]);
        chat_param.put("count", param[2]);
        chat_param.put("offset", param[3]);


        App.getApi().chatMessage(chat_param,new Callback<JSONArray>() {
            @Override
            public void success(JSONArray jsonArray, Response response) {
                item = new ArrayList<ChatModel>();


                try {
                    jsonArray = jsonArray.getJSONObject(0).getJSONArray("messages");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        ChatModel chatModel = new ChatModel();
                        Event event=new Event();
                        Place p=new Place();
                        MainAttachment mainAttachment = new MainAttachment();
                        chatModel.setMessage_id(jsonArray.getJSONObject(i).getInt("message_id"));
                        chatModel.setUser_id(jsonArray.getJSONObject(i).getInt("user_id"));
                        String user_mes = "" + chatModel.getUser_id();
                        if (user_mes.equals(user_id)) chatModel.setSend(true);
                        else chatModel.setSend(false);
                        chatModel.setTime(jsonArray.getJSONObject(i).getString("date"));
                        chatModel.setMessage(jsonArray.getJSONObject(i).getString("message"));
                        chatModel.setIs_new(jsonArray.getJSONObject(i).getInt("is_new"));
                        chatModel.setIs_read(jsonArray.getJSONObject(i).getInt("is_read"));
                        chatModel.setIs_read(jsonArray.getJSONObject(i).getInt("iknow"));
                        if (!jsonArray.getJSONObject(i).isNull("attachments")) {
                            JSONArray objs = jsonArray.getJSONObject(i).getJSONArray("attachments");

                            mainAttachment.setType(objs.getJSONObject(0).getString("type"));
                            if(mainAttachment.getType().equals("event")){
                                event.setEvent_title(objs.getJSONObject(0).getString("event_title"));
                                event.setEvent_id(objs.getJSONObject(0).getInt("event_id"));
                                chatModel.setEvent(event);
                            }
                            else if(mainAttachment.getType().equals("place")){
                                p.setPlace_address(objs.getJSONObject(0).getString("place_address"));
                                p.setPlace_category(objs.getJSONObject(0).getString("category_name"));
                                p.setPlace_title(objs.getJSONObject(0).getString("place_title"));
                            }
                            else {

                                mainAttachment.setId(objs.getJSONObject(0).getInt("attachment_id"));
                                mainAttachment.setUrl(objs.getJSONObject(0).getJSONObject("photo_big").getString("url"));
                                chatModel.setAttachment(mainAttachment);
                            }
                        }

                        item.add(chatModel);
                    }
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
                count_foto = item.size();

                for (int i = 0; i < item.size(); i++) {
                    getUserPhoto(item.get(i), false, i);
                }
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });



        if (webSoc == null) {

            String url = SupportInfo.chat_url + meetup_id;
            AsyncHttpClient.getDefaultInstance().websocket(url, "http", new AsyncHttpClient.WebSocketConnectCallback() {
                @Override
                public void onCompleted(Exception ex, WebSocket webSocket) {
                    if (ex != null) {
                        ex.printStackTrace();
                        return;
                    }
                    webSoc = webSocket;

                    webSoc.setStringCallback(new WebSocket.StringCallback() {
                        public void onStringAvailable(String s) {

                            ChatModel chatModel = new ChatModel();
                            try {
                                JSONObject object = new JSONObject(s).getJSONObject("new_message");
                                chatModel.setMeetup_id(Integer.parseInt(meetup_id));
                                chatModel.setMessage(object.getString("message"));
                                chatModel.setTime(object.getString("date"));
                                chatModel.setUser_id(object.getInt("user_id"));
                                String user_mes = "" + chatModel.getUser_id();
                                if (user_mes.equals(user_id)) chatModel.setSend(true);

                                if (item.size() == 100) {
                                    chatBL.deleteItem(Integer.parseInt(meetup_id));
                                }
                                //    chatBL.saveChatItem(chatModel);

                                //  item.add(chatModel);

                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }

                            Message message = new Message();
                            message.obj = chatModel;
                            handler.sendMessage(message);
                        }
                    });

                    webSoc.setClosedCallback(new CompletedCallback() {
                        @Override
                        public void onCompleted(Exception e) {
                            System.out.print("");

                        }
                    });

                    webSoc.setDataCallback(new DataCallback() {
                        public void onDataAvailable(ByteBufferList byteBufferList) {
                            System.out.println("I got some bytes!");

                            // note that this data has been read
                            byteBufferList.recycle();
                        }

                        @Override
                        public void onDataAvailable(DataEmitter emitter, ByteBufferList bb) {
                            System.out.print("");
                        }
                    });
                }
            });

        }

        adapter.notifyDataSetChanged();
    }


    public void getUserPhoto(final ChatModel itemmodel, boolean b, final int position) {
        final boolean flag = b;
        model = itemmodel;
        Map<String,String> options=new LinkedHashMap<>();

        options.put("access_token", param[0]);
        options.put("user_id", ""+model.getUser_id());

        App.getApi().userInfo(options,new Callback<JSONArray>() {
            @Override
            public void success(JSONArray jsonArray, Response response) {
                JSONObject jsonobject;
                try {
                    jsonobject = jsonArray.getJSONObject(0);
                    UserInfo userInfo = new UserInfo();
                    userInfo.setName(jsonobject.getString("name"));
                    userInfo.setSurname(jsonobject.getString("surname"));
                    userInfo.setFollowers_count(jsonobject.getInt("followers_count"));
                    userInfo.setFollowing_count(jsonobject.getInt("following_count"));
                    userInfo.setFriends_count(jsonobject.getInt("friends_count"));
                    userInfo.setFeedback_notification_count(jsonobject.getInt("feedback_notifications_count"));
                    userInfo.setPhoto_small(jsonobject.getString("photo_small"));
                    userInfo.setReputation(jsonobject.getInt("reputation"));
                    userInfo.setGender(jsonobject.getString("gender"));
                    if (!jsonobject.isNull("phone_number"))
                        userInfo.setPhone_number(jsonobject.getInt("phone_number"));
                    else {
                        userInfo.setPhone_number(0);
                    }
                    userInfo.setLast_seen(jsonobject.getString("last_seen"));
                    userInfo.setNative_lang(jsonobject.getString("native_lang"));
                    userInfo.setHome_city(jsonobject.getString("home_city"));
                    userInfo.setEvent_created(jsonobject.getInt("events_created"));
                    userInfo.setPhoto_big(jsonobject.getString("photo_big"));
                    userInfo.setRelation(jsonobject.getInt("relation"));
                    itemmodel.setIcon(userInfo.getPhoto_small());
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
                if (flag) {
                    if (item.size() == 0) {
                        item.add(0, itemmodel);
                        adapter = new ChatAdapter(context, item);
                        listView.setAdapter(adapter);
                    } else {
                        item.add(0, itemmodel);
                        adapter.notifyDataSetChanged();
                        listView.requestFocusFromTouch();
                        listView.setSelection(item.size() - 1);
                    }
                } else {
                    item.set(position, itemmodel);
                    count_foto--;
                    if (count_foto == 0) {

                        adapter = new ChatAdapter(context, item);
                        listView.setAdapter(adapter);
                        listView.requestFocusFromTouch();
                        listView.setSelection(item.size() - 1);
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });



    }

    @Override
    protected void onDestroy() {
        if (webSoc != null) {
            webSoc.getServer().stop();
            webSoc.getSocket().close();
        }

        super.onDestroy();
    }


    public void openGallery(int req_code) {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select file to upload "), req_code);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        switch (requestCode) {
            case (103): {
                if (resultCode == Activity.RESULT_OK) {
                    //   String newText = data.getStringExtra(PUBLIC_STATIC_STRING_IDENTIFIER);
                    String[] addr = data.getStringArrayExtra("address");
                    String ad = "";
                    if (addr[0] != null) {
                        ad = ad + " " + addr[0];
                    }
                    if (addr[1] != null) {
                        ad = ad + " " + addr[1];
                    }
                    if (ad.length() > 17) {
                        String arr = ad.substring(0, 14) + "...";
                        edit_address_text.setText(arr);

                    } else {
                        edit_address_text.setText(ad);
                    }

                    lat = data.getDoubleExtra("lat", 0);
                    lon = data.getDoubleExtra("lon", 0);
                    address_before.setVisibility(View.GONE);
                    address_save.setVisibility(View.VISIBLE);
                }
                break;
            }
            case (104): {

                if (resultCode == Activity.RESULT_OK) {
                    //   String newText = data.getStringExtra(PUBLIC_STATIC_STRING_IDENTIFIER);
                    choose_place = data.getParcelableExtra("data");
                    if (choose_place.getPlace_title().length() > 17) {
                        String arr = choose_place.getPlace_title().substring(0, 14) + "...";
                        edit_address_text.setText(arr);

                    } else {
                        edit_address_text.setText(choose_place.getPlace_title());
                    }
                    edit_address_text.setText(choose_place.getPlace_title());
                    lat = choose_place.getLatitude();
                    lon = choose_place.getLongitude();
                    address_before.setVisibility(View.GONE);
                    address_save.setVisibility(View.VISIBLE);

                }

                break;
            }
            case (STATIC_INTEGER_VALUE): {
                if (resultCode == Activity.RESULT_OK) {
                    //   String newText = data.getStringExtra(PUBLIC_STATIC_STRING_IDENTIFIER);
                    e = data.getParcelableExtra("item");
                    String text = e.getEvent_title();
                    // TODO Update your TextView.
                }
                break;
            }
            case 200:
                String[] all_path = data.getStringArrayExtra("all_path");

                for (int i = 0; i < all_path.length; i++) {
                    //byte[] byteArray=null;
                    final BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 8;
                    Bitmap bMap = BitmapFactory.decodeFile(all_path[i], options);
                    // Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.icon);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bMap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
                    byte[] byteArray = new byte[stream.toByteArray().length];
                    byteArray = stream.toByteArray();
                    bytes.add(byteArray);


                    ChatModel chatModel = new ChatModel();
                    chatModel.setMeetup_id(Integer.parseInt(meetup_id));
                    chatModel.setName(getSharedPreferences("token_info", MODE_PRIVATE).getString("username", "") + " " + getSharedPreferences("token_info", MODE_PRIVATE).getString("surname", ""));
                    chatModel.setIcon(getSharedPreferences("token_info", MODE_PRIVATE).getString("foto", ""));
                    chatModel.setSend(true);
                    chatModel.setImage(byteArray);
                    item.add(0, chatModel);
                    adapter.notifyDataSetChanged();
                    listView.requestFocusFromTouch();
                    listView.setSelection(item.size() - 1);

                    TypedFile  typedFile = new TypedFile("image/jpg", new File(all_path[i]));
                    Map<String,String> params = new LinkedHashMap<>();
                    params.put("access_token", param[0]);
                    params.put("chat", "1");
                    App.getApi().chatAddUpload(params,typedFile,new Callback<JSONArray>() {
                        @Override
                        public void success(JSONArray jsonArray, Response response) {
                            JSONObject object=jsonArray.optJSONObject(0);
                            object.optJSONObject("photo_big").optString("url");
                            object.optJSONObject("photo_small").optString("url");

                            System.out.print(jsonArray);

                            Map<String,String> params = new LinkedHashMap<String, String>();
                            params.put("access_token", param[0]);
                            params.put("meetup_id", ""+param[1]);
                            params.put("message","");
                            params.put("attachments",object.optString("attachment_id"));
                            App.getApi().chatAddMessage(params,new Callback<JSONArray>() {
                                @Override
                                public void success(JSONArray jsonArray, Response response) {
                                     System.out.print(jsonArray);
                                }

                                @Override
                                public void failure(RetrofitError error) {
                                    System.out.print(error);
                                }
                            });
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            System.out.print(error);
                        }
                    });
                  /*  RequestParams params = new RequestParams();
                    params.put("access_token", param[0]);
                    params.put("chat", "1");
                    params.put("file", new ByteArrayInputStream(byteArray), "file.png");
                    //    params.put("file",byteArray);
                    //   params.put("filename","file.jpg");
                    // params.put("file.разрешение","jpg");
                    String url_post = SupportInfo.url + "photos.upload.php";


                    com.loopj.android.http.AsyncHttpClient asyncHttpClient = new com.loopj.android.http.AsyncHttpClient();
                    asyncHttpClient.post(url_post, params, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            // If the response is JSONObject instead of expected JSONArray
                            System.out.print("");

                        }

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONArray timeline) {
                            // Pull out the first event on the public timeline
                            //   JSONObject firstEvent = timeline.get(0);
                            //   String tweetText = firstEvent.getString("text");


                            JSONObject jsonobject;
                            try {
                                jsonobject = timeline.getJSONObject(0);
                                UserInfo userInfo = new UserInfo();

                                userInfo.setName(jsonobject.getString("name"));
                                userInfo.setSurname(jsonobject.getString("surname"));
                                userInfo.setFollowers_count(jsonobject.getInt("followers_count"));
                                userInfo.setFollowing_count(jsonobject.getInt("following_count"));
                                userInfo.setFriends_count(jsonobject.getInt("friends_count"));
                                userInfo.setFeedback_notification_count(jsonobject.getInt("feedback_notifications_count"));
                                userInfo.setPhoto_small(jsonobject.getString("photo_small"));
                                userInfo.setReputation(jsonobject.getInt("reputation"));
                                userInfo.setGender(jsonobject.getString("gender"));
                                if (!jsonobject.isNull("phone_number"))
                                    userInfo.setPhone_number(jsonobject.getInt("phone_number"));
                                else {
                                    userInfo.setPhone_number(0);
                                }
                                userInfo.setLast_seen(jsonobject.getString("last_seen"));
                                userInfo.setNative_lang(jsonobject.getString("native_lang"));
                                userInfo.setHome_city(jsonobject.getString("home_city"));
                                userInfo.setEvent_created(jsonobject.getInt("events_created"));
                                userInfo.setPhoto_big(jsonobject.getString("photo_big"));
                                userInfo.setRelation(jsonobject.getInt("relation"));

                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }


                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                            super.onFailure(statusCode, headers, responseString, throwable);
                        }
                    });
*/
                }


                break;


        }
    }


    public void show_dialog() {
        final Dialog dia = new Dialog(this, android.R.style.Theme_Translucent);
        dia.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dia.setCancelable(true);
        dia.setContentView(R.layout.add_item_dialog_chat);

        Button foto = (Button) dia.findViewById(R.id.add_foto);

        foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Action.ACTION_MULTIPLE_PICK);
                startActivityForResult(i, 200);
                dia.dismiss();
            }
        });

        Button event = (Button) dia.findViewById(R.id.add_event);


        event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent search = new Intent(context, SearchEventActivity.class);
                startActivityForResult(search, STATIC_INTEGER_VALUE);
                dia.dismiss();
            }
        });


        Button cancel = (Button) dia.findViewById(R.id.cancel_button);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dia.dismiss();
            }
        });

        dia.show();

    }


    private Handler handler;





    public void sendChat() {
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //     webSoc.getClosedCallback().onCompleted(new Exception());

                ChatModel chatModel = new ChatModel();
                chatModel.setMeetup_id(Integer.parseInt(meetup_id));
                chatModel.setMessage(chat_edit.getText().toString());
                chatModel.setName(getSharedPreferences("token_info", MODE_PRIVATE).getString("username", "") + " " + getSharedPreferences("token_info", MODE_PRIVATE).getString("surname", ""));
                chatModel.setIcon(getSharedPreferences("token_info", MODE_PRIVATE).getString("foto", ""));
                chatModel.setSend(true);

                //chatModel.setUpload_first(byteArray.toString());
                if (bytes.size() != 0) {
                    chatModel.setImage(bytes.get(0));
                }

                if (bytes.size() > 1)
                    chatModel.setImage_second(bytes.get(1));

                if (bytes.size() > 2)
                    chatModel.setImage_third(bytes.get(2));
                if (bytes.size() > 3)
                    chatModel.setImage_four(bytes.get(3));

                if (bytes.size() > 4)
                    chatModel.setImage_five(bytes.get(4));

                if (item.size() == 100) {
                    chatBL.deleteItem(Integer.parseInt(meetup_id));
                }
                if (e != null) {
                    chatModel.setEvent_id(e.getEvent_id());
                }



                bytes.clear();


                Map<String,String> params = new LinkedHashMap<String, String>();
                params.put("access_token", param[0]);
                params.put("meetup_id", ""+param[1]);
                params.put("message", chat_edit.getText().toString());
                chat_edit.setText("");

                if (e != null) {

                    params.put("events", ""+e.getEvent_id());
                    e = null;

                }
                if (place != null) {

                    params.put("places", ""+place.getPlace_id());
                    place = null;
                }

                 App.getApi().chatAddMessage(params,new Callback<JSONArray>() {
                     @Override
                     public void success(JSONArray jsonArray, Response response) {

                     }

                     @Override
                     public void failure(RetrofitError error) {

                     }
                 });


            }
        });

    }

    private class KeyboardListener implements View.OnKeyListener {

        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (event.getAction() == KeyEvent.ACTION_UP) {
                return processKeyUp(keyCode);
            }
            return false;
        }
    }

    private boolean processKeyUp(int keyCode) {
        if (keyCode == KeyEvent.KEYCODE_ESCAPE || keyCode == KeyEvent.KEYCODE_BACK) {

            return true;
        } else if (keyCode == KeyEvent.KEYCODE_TAB) {
            if (mInKbMode) {
                if (isTypedTimeFullyLegal()) {
                    finishKbMode(true);
                }
                return true;
            }
        } else if (keyCode == KeyEvent.KEYCODE_ENTER) {
            if (mInKbMode) {
                if (!isTypedTimeFullyLegal()) {
                    return true;
                }
                finishKbMode(false);
            }
            if (mCallback != null) {
                /*mCallback.onTimeSet(MeetupCreateFragmentFour,
                        mTimePicker.getHours(), mTimePicker.getMinutes());*/
            }

            return true;
        } else if (keyCode == KeyEvent.KEYCODE_DEL) {
            if (mInKbMode) {
                if (!mTypedTimes.isEmpty()) {
                    int deleted = deleteLastTypedKey();
                    String deletedKeyStr;
                    if (deleted == getAmOrPmKeyCode(AM)) {
                        deletedKeyStr = mAmText;
                    } else if (deleted == getAmOrPmKeyCode(PM)) {
                        deletedKeyStr = mPmText;
                    } else {
                        deletedKeyStr = String.format("%d", getValFromKeyCode(deleted));
                    }
                    Util.tryAccessibilityAnnounce(mTimePicker,
                            String.format(mDeletedKeyFormat, deletedKeyStr));
                    updateDisplay(true);
                }
            }
        } else if (keyCode == KeyEvent.KEYCODE_0 || keyCode == KeyEvent.KEYCODE_1
                || keyCode == KeyEvent.KEYCODE_2 || keyCode == KeyEvent.KEYCODE_3
                || keyCode == KeyEvent.KEYCODE_4 || keyCode == KeyEvent.KEYCODE_5
                || keyCode == KeyEvent.KEYCODE_6 || keyCode == KeyEvent.KEYCODE_7
                || keyCode == KeyEvent.KEYCODE_8 || keyCode == KeyEvent.KEYCODE_9
                || (!mIs24HourMode &&
                (keyCode == getAmOrPmKeyCode(AM) || keyCode == getAmOrPmKeyCode(PM)))) {
            if (!mInKbMode) {
                if (mTimePicker == null) {
                    // Something's wrong, because time picker should definitely not be null.

                    return true;
                }
                mTypedTimes.clear();
                tryStartingKbMode(keyCode);
                return true;
            }
            // We're already in keyboard mode.
            if (addKeyIfLegal(keyCode)) {
                updateDisplay(false);
            }
            return true;
        }
        return false;
    }

    private void setHour(int value, boolean announce) {
        String format;
        if (mIs24HourMode) {
            format = "%02d";
        } else {
            format = "%d";
            value = value % 12;
            if (value == 0) {
                value = 12;
            }
        }

        CharSequence text = String.format(format, value);
        mHourView.setText(text);
        mHourSpaceView.setText(text);
        if (announce) {
            Util.tryAccessibilityAnnounce(mTimePicker, text);
        }
    }

    private void setMinute(int value) {
        if (value == 60) {
            value = 0;
        }
        CharSequence text = String.format(Locale.getDefault(), "%02d", value);
        Util.tryAccessibilityAnnounce(mTimePicker, text);
        mMinuteView.setText(text);
        mMinuteSpaceView.setText(text);
    }

    private void updateAmPmDisplay(int amOrPm) {
        if (amOrPm == AM) {
            mAmPmTextView.setText(mAmText);
            Util.tryAccessibilityAnnounce(mTimePicker, mAmText);
            mAmPmHitspace.setContentDescription(mAmText);
        } else if (amOrPm == PM) {
            mAmPmTextView.setText(mPmText);
            Util.tryAccessibilityAnnounce(mTimePicker, mPmText);
            mAmPmHitspace.setContentDescription(mPmText);
        } else {
            mAmPmTextView.setText(mDoublePlaceholderText);
        }
    }

    private void updateDisplay(boolean allowEmptyDisplay) {
        if (!allowEmptyDisplay && mTypedTimes.isEmpty()) {
            int hour = mTimePicker.getHours();
            int minute = mTimePicker.getMinutes();
            setHour(hour, true);
            setMinute(minute);
            if (!mIs24HourMode) {
                updateAmPmDisplay(hour < 12 ? AM : PM);
            }
            setCurrentItemShowing(mTimePicker.getCurrentItemShowing(), true, true, true);
            mDoneButton.setEnabled(true);
        } else {
            Boolean[] enteredZeros = {false, false};
            int[] values = getEnteredTime(enteredZeros);
            String hourFormat = enteredZeros[0] ? "%02d" : "%2d";
            String minuteFormat = (enteredZeros[1]) ? "%02d" : "%2d";
            String hourStr = (values[0] == -1) ? mDoublePlaceholderText :
                    String.format(hourFormat, values[0]).replace(' ', mPlaceholderText);
            String minuteStr = (values[1] == -1) ? mDoublePlaceholderText :
                    String.format(minuteFormat, values[1]).replace(' ', mPlaceholderText);
            mHourView.setText(hourStr);
            mHourSpaceView.setText(hourStr);
            mHourView.setTextColor(mUnselectedColor);
            mMinuteView.setText(minuteStr);
            mMinuteSpaceView.setText(minuteStr);
            mMinuteView.setTextColor(mUnselectedColor);
            if (!mIs24HourMode) {
                updateAmPmDisplay(values[2]);
            }
        }
    }


    private void setCurrentItemShowing(int index, boolean animateCircle, boolean delayLabelAnimate,
                                       boolean announce) {
        mTimePicker.setCurrentItemShowing(index, animateCircle);

        TextView labelToAnimate;
        if (index == HOUR_INDEX) {
            int hours = mTimePicker.getHours();
            if (!mIs24HourMode) {
                hours = hours % 12;
            }
            mTimePicker.setContentDescription(mHourPickerDescription + ": " + hours);
            if (announce) {
                Util.tryAccessibilityAnnounce(mTimePicker, mSelectHours);
            }
            labelToAnimate = mHourView;
        } else {
            int minutes = mTimePicker.getMinutes();
            mTimePicker.setContentDescription(mMinutePickerDescription + ": " + minutes);
            if (announce) {
                Util.tryAccessibilityAnnounce(mTimePicker, mSelectMinutes);
            }
            labelToAnimate = mMinuteView;
        }


        mHourView.setTextColor(getResources().getColor(R.color.white));
        mMinuteView.setTextColor(getResources().getColor(R.color.white));

        ObjectAnimator pulseAnimator = Util.getPulseAnimator(labelToAnimate, 0.85f, 1.1f);
        if (delayLabelAnimate) {
            pulseAnimator.setStartDelay(PULSE_ANIMATOR_DELAY);
        }
        pulseAnimator.start();/**/
    }

    public void hideKeyboard() {
        InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(editText
                        .getApplicationWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

    }

    @Override
    public void onValueSelected(int pickerIndex, int newValue, boolean autoAdvance) {
        if (pickerIndex == HOUR_INDEX) {
            setHour(newValue, false);
            String announcement = String.format("%d", newValue);
            if (mAllowAutoAdvance && autoAdvance) {
                setCurrentItemShowing(MINUTE_INDEX, true, true, false);
                announcement += ". " + mSelectMinutes;
            } else {
                mTimePicker.setContentDescription(mHourPickerDescription + ": " + newValue);
            }
            Util.tryAccessibilityAnnounce(mTimePicker, announcement);
        } else if (pickerIndex == MINUTE_INDEX) {
            setMinute(newValue);
            mTimePicker.setContentDescription(mMinutePickerDescription + ": " + newValue);
        } else if (pickerIndex == AMPM_INDEX) {
            updateAmPmDisplay(newValue);
        } else if (pickerIndex == ENABLE_PICKER_INDEX) {
            if (!isTypedTimeFullyLegal()) {
                mTypedTimes.clear();
            }
            finishKbMode(true);
        }
    }

    private void finishKbMode(boolean updateDisplays) {
        mInKbMode = false;
        if (!mTypedTimes.isEmpty()) {
            int values[] = getEnteredTime(null);
            mTimePicker.setTime(values[0], values[1]);
            if (!mIs24HourMode) {
                mTimePicker.setAmOrPm(values[2]);
            }
            mTypedTimes.clear();
        }
        if (updateDisplays) {
            updateDisplay(false);
            mTimePicker.trySettingInputEnabled(true);
        }
    }

    private class Node {

        private int[] mLegalKeys;
        private ArrayList<Node> mChildren;

        public Node(int... legalKeys) {
            mLegalKeys = legalKeys;
            mChildren = new ArrayList<Node>();
        }

        public void addChild(Node child) {
            mChildren.add(child);
        }

        public boolean containsKey(int key) {
            for (int i = 0; i < mLegalKeys.length; i++) {
                if (mLegalKeys[i] == key) {
                    return true;
                }
            }
            return false;
        }

        public Node canReach(int key) {
            if (mChildren == null) {
                return null;
            }
            for (Node child : mChildren) {
                if (child.containsKey(key)) {
                    return child;
                }
            }
            return null;
        }
    }

    private boolean isTypedTimeFullyLegal() {
        if (mIs24HourMode) {
            // For 24-hour mode, the time is legal if the hours and minutes are each legal. Note:
            // getEnteredTime() will ONLY call isTypedTimeFullyLegal() when NOT in 24hour mode.
            int[] values = getEnteredTime(null);
            return (values[0] >= 0 && values[1] >= 0 && values[1] < 60);
        } else {
            // For AM/PM mode, the time is legal if it contains an AM or PM, as those can only be
            // legally added at specific times based on the tree's algorithm.
            return (mTypedTimes.contains(getAmOrPmKeyCode(AM)) ||
                    mTypedTimes.contains(getAmOrPmKeyCode(PM)));
        }
    }

    private int deleteLastTypedKey() {
        int deleted = mTypedTimes.remove(mTypedTimes.size() - 1);
        if (!isTypedTimeFullyLegal()) {
            mDoneButton.setEnabled(false);
        }
        return deleted;
    }

    private void tryStartingKbMode(int keyCode) {
        if (mTimePicker.trySettingInputEnabled(false) &&
                (keyCode == -1 || addKeyIfLegal(keyCode))) {
            mInKbMode = true;
            mDoneButton.setEnabled(false);
            updateDisplay(false);
        }
    }

    private int getAmOrPmKeyCode(int amOrPm) {
        // Cache the codes.
        if (mAmKeyCode == -1 || mPmKeyCode == -1) {
            // Find the first character in the AM/PM text that is unique.
            KeyCharacterMap kcm = KeyCharacterMap.load(KeyCharacterMap.VIRTUAL_KEYBOARD);
            char amChar;
            char pmChar;
            for (int i = 0; i < Math.max(mAmText.length(), mPmText.length()); i++) {
                amChar = mAmText.toLowerCase(Locale.getDefault()).charAt(i);
                pmChar = mPmText.toLowerCase(Locale.getDefault()).charAt(i);
                if (amChar != pmChar) {
                    KeyEvent[] events = kcm.getEvents(new char[]{amChar, pmChar});
                    // There should be 4 events: a down and up for both AM and PM.
                    if (events != null && events.length == 4) {
                        mAmKeyCode = events[0].getKeyCode();
                        mPmKeyCode = events[2].getKeyCode();
                    } else {

                    }
                    break;
                }
            }
        }
        if (amOrPm == AM) {
            return mAmKeyCode;
        } else if (amOrPm == PM) {
            return mPmKeyCode;
        }

        return -1;
    }


    private void generateLegalTimesTree() {
        // Create a quick cache of numbers to their keycodes.
        int k0 = KeyEvent.KEYCODE_0;
        int k1 = KeyEvent.KEYCODE_1;
        int k2 = KeyEvent.KEYCODE_2;
        int k3 = KeyEvent.KEYCODE_3;
        int k4 = KeyEvent.KEYCODE_4;
        int k5 = KeyEvent.KEYCODE_5;
        int k6 = KeyEvent.KEYCODE_6;
        int k7 = KeyEvent.KEYCODE_7;
        int k8 = KeyEvent.KEYCODE_8;
        int k9 = KeyEvent.KEYCODE_9;

        // The root of the tree doesn't contain any numbers.
        mLegalTimesTree = new Node();
        if (mIs24HourMode) {
            // We'll be re-using these nodes, so we'll save them.
            Node minuteFirstDigit = new Node(k0, k1, k2, k3, k4, k5);
            Node minuteSecondDigit = new Node(k0, k1, k2, k3, k4, k5, k6, k7, k8, k9);
            // The first digit must be followed by the second digit.
            minuteFirstDigit.addChild(minuteSecondDigit);

            // The first digit may be 0-1.
            Node firstDigit = new Node(k0, k1);
            mLegalTimesTree.addChild(firstDigit);

            // When the first digit is 0-1, the second digit may be 0-5.
            Node secondDigit = new Node(k0, k1, k2, k3, k4, k5);
            firstDigit.addChild(secondDigit);
            // We may now be followed by the first minute digit. E.g. 00:09, 15:58.
            secondDigit.addChild(minuteFirstDigit);

            // When the first digit is 0-1, and the second digit is 0-5, the third digit may be 6-9.
            Node thirdDigit = new Node(k6, k7, k8, k9);
            // The time must now be finished. E.g. 0:55, 1:08.
            secondDigit.addChild(thirdDigit);

            // When the first digit is 0-1, the second digit may be 6-9.
            secondDigit = new Node(k6, k7, k8, k9);
            firstDigit.addChild(secondDigit);
            // We must now be followed by the first minute digit. E.g. 06:50, 18:20.
            secondDigit.addChild(minuteFirstDigit);

            // The first digit may be 2.
            firstDigit = new Node(k2);
            mLegalTimesTree.addChild(firstDigit);

            // When the first digit is 2, the second digit may be 0-3.
            secondDigit = new Node(k0, k1, k2, k3);
            firstDigit.addChild(secondDigit);
            // We must now be followed by the first minute digit. E.g. 20:50, 23:09.
            secondDigit.addChild(minuteFirstDigit);

            // When the first digit is 2, the second digit may be 4-5.
            secondDigit = new Node(k4, k5);
            firstDigit.addChild(secondDigit);
            // We must now be followd by the last minute digit. E.g. 2:40, 2:53.
            secondDigit.addChild(minuteSecondDigit);

            // The first digit may be 3-9.
            firstDigit = new Node(k3, k4, k5, k6, k7, k8, k9);
            mLegalTimesTree.addChild(firstDigit);
            // We must now be followed by the first minute digit. E.g. 3:57, 8:12.
            firstDigit.addChild(minuteFirstDigit);
        } else {
            // We'll need to use the AM/PM node a lot.
            // Set up AM and PM to respond to "a" and "p".
            Node ampm = new Node(getAmOrPmKeyCode(AM), getAmOrPmKeyCode(PM));

            // The first hour digit may be 1.
            Node firstDigit = new Node(k1);
            mLegalTimesTree.addChild(firstDigit);
            // We'll allow quick input of on-the-hour times. E.g. 1pm.
            firstDigit.addChild(ampm);

            // When the first digit is 1, the second digit may be 0-2.
            Node secondDigit = new Node(k0, k1, k2);
            firstDigit.addChild(secondDigit);
            // Also for quick input of on-the-hour times. E.g. 10pm, 12am.
            secondDigit.addChild(ampm);

            // When the first digit is 1, and the second digit is 0-2, the third digit may be 0-5.
            Node thirdDigit = new Node(k0, k1, k2, k3, k4, k5);
            secondDigit.addChild(thirdDigit);
            // The time may be finished now. E.g. 1:02pm, 1:25am.
            thirdDigit.addChild(ampm);

            // When the first digit is 1, the second digit is 0-2, and the third digit is 0-5,
            // the fourth digit may be 0-9.
            Node fourthDigit = new Node(k0, k1, k2, k3, k4, k5, k6, k7, k8, k9);
            thirdDigit.addChild(fourthDigit);
            // The time must be finished now. E.g. 10:49am, 12:40pm.
            fourthDigit.addChild(ampm);

            // When the first digit is 1, and the second digit is 0-2, the third digit may be 6-9.
            thirdDigit = new Node(k6, k7, k8, k9);
            secondDigit.addChild(thirdDigit);
            // The time must be finished now. E.g. 1:08am, 1:26pm.
            thirdDigit.addChild(ampm);

            // When the first digit is 1, the second digit may be 3-5.
            secondDigit = new Node(k3, k4, k5);
            firstDigit.addChild(secondDigit);

            // When the first digit is 1, and the second digit is 3-5, the third digit may be 0-9.
            thirdDigit = new Node(k0, k1, k2, k3, k4, k5, k6, k7, k8, k9);
            secondDigit.addChild(thirdDigit);
            // The time must be finished now. E.g. 1:39am, 1:50pm.
            thirdDigit.addChild(ampm);

            // The hour digit may be 2-9.
            firstDigit = new Node(k2, k3, k4, k5, k6, k7, k8, k9);
            mLegalTimesTree.addChild(firstDigit);
            // We'll allow quick input of on-the-hour-times. E.g. 2am, 5pm.
            firstDigit.addChild(ampm);

            // When the first digit is 2-9, the second digit may be 0-5.
            secondDigit = new Node(k0, k1, k2, k3, k4, k5);
            firstDigit.addChild(secondDigit);

            // When the first digit is 2-9, and the second digit is 0-5, the third digit may be 0-9.
            thirdDigit = new Node(k0, k1, k2, k3, k4, k5, k6, k7, k8, k9);
            secondDigit.addChild(thirdDigit);
            // The time must be finished now. E.g. 2:57am, 9:30pm.
            thirdDigit.addChild(ampm);
        }
    }

    private int[] getEnteredTime(Boolean[] enteredZeros) {
        int amOrPm = -1;
        int startIndex = 1;
        if (!mIs24HourMode && isTypedTimeFullyLegal()) {
            int keyCode = mTypedTimes.get(mTypedTimes.size() - 1);
            if (keyCode == getAmOrPmKeyCode(AM)) {
                amOrPm = AM;
            } else if (keyCode == getAmOrPmKeyCode(PM)) {
                amOrPm = PM;
            }
            startIndex = 2;
        }
        int minute = -1;
        int hour = -1;
        for (int i = startIndex; i <= mTypedTimes.size(); i++) {
            int val = getValFromKeyCode(mTypedTimes.get(mTypedTimes.size() - i));
            if (i == startIndex) {
                minute = val;
            } else if (i == startIndex + 1) {
                minute += 10 * val;
                if (enteredZeros != null && val == 0) {
                    enteredZeros[1] = true;
                }
            } else if (i == startIndex + 2) {
                hour = val;
            } else if (i == startIndex + 3) {
                hour += 10 * val;
                if (enteredZeros != null && val == 0) {
                    enteredZeros[0] = true;
                }
            }
        }

        int[] ret = {hour, minute, amOrPm};
        return ret;
    }

    private int getValFromKeyCode(int keyCode) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_0:
                return 0;
            case KeyEvent.KEYCODE_1:
                return 1;
            case KeyEvent.KEYCODE_2:
                return 2;
            case KeyEvent.KEYCODE_3:
                return 3;
            case KeyEvent.KEYCODE_4:
                return 4;
            case KeyEvent.KEYCODE_5:
                return 5;
            case KeyEvent.KEYCODE_6:
                return 6;
            case KeyEvent.KEYCODE_7:
                return 7;
            case KeyEvent.KEYCODE_8:
                return 8;
            case KeyEvent.KEYCODE_9:
                return 9;
            default:
                return -1;
        }
    }

    private boolean addKeyIfLegal(int keyCode) {
        // If we're in 24hour mode, we'll need to check if the input is full. If in AM/PM mode,
        // we'll need to see if AM/PM have been typed.
        if ((mIs24HourMode && mTypedTimes.size() == 4) ||
                (!mIs24HourMode && isTypedTimeFullyLegal())) {
            return false;
        }

        mTypedTimes.add(keyCode);
        if (!isTypedTimeLegalSoFar()) {
            deleteLastTypedKey();
            return false;
        }

        int val = getValFromKeyCode(keyCode);
        Util.tryAccessibilityAnnounce(mTimePicker, String.format("%d", val));
        // Automatically fill in 0's if AM or PM was legally entered.
        if (isTypedTimeFullyLegal()) {
            if (!mIs24HourMode && mTypedTimes.size() <= 3) {
                mTypedTimes.add(mTypedTimes.size() - 1, KeyEvent.KEYCODE_0);
                mTypedTimes.add(mTypedTimes.size() - 1, KeyEvent.KEYCODE_0);
            }
            mDoneButton.setEnabled(true);
        }

        return true;
    }

    /**
     * Traverse the tree to see if the keys that have been typed so far are legal as is, or may become legal as more
     * keys are typed (excluding backspace).
     */
    private boolean isTypedTimeLegalSoFar() {
        Node node = mLegalTimesTree;
        for (int keyCode : mTypedTimes) {
            node = node.canReach(keyCode);
            if (node == null) {
                return false;
            }
        }
        return true;
    }

    public void tryVibrate() {
        mHapticFeedbackController.tryVibrate();
    }

    private View customBar;
    private TextView count_user;
    private Intent intent_invite;

    public void setActionBar() {

        Toolbar actionbar = (Toolbar) findViewById(R.id.actionbar);
        LayoutInflater mInflater = LayoutInflater.from(this);
        customBar = mInflater.inflate(R.layout.actionbar_custom_meetup_detail, actionbar);

        ImageView invite = (ImageView) customBar.findViewById(R.id.count_user_actionbar);

        count_user = (TextView) customBar.findViewById(R.id.count_user);

        intent_invite = new Intent(this, InviteFriendsActivity.class);

        invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent_invite);
            }
        });


    }

    public void changePost(Map<String,String> params) {

        App.getApi().meetupChange(params,new Callback<JSONArray>() {
            @Override
            public void success(JSONArray jsonArray, Response response) {

                if (jsonArray.optJSONObject(0).isNull("error_code")) {
                    change_success = true;
                }
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });


    }


    public static boolean change_success = false;
}
