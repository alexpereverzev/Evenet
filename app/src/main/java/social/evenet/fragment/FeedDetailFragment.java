package social.evenet.fragment;


import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import social.evenet.R;
import social.evenet.activity.App;
import social.evenet.activity.CommentActivity;
import social.evenet.activity.MeetUpAddActivity;
import social.evenet.activity.PlaceActivity;
import social.evenet.activity.PlaceMenuActivity;
import social.evenet.activity.SharedActivity;
import social.evenet.db.Events;
import social.evenet.helper.Util;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 */
public class FeedDetailFragment extends RefreshableFragment {

    private Events events;
    private TextView title;
    private ImageView icon_feed;
    private TextView when;
    private TextView where;
    private TextView title_text_info;
    private TextView text_info;
    private TextView count_likes;
    private ImageView icon_like;
    private ImageView icon_comment;
    private boolean active = false;
    private ImageView shared;
    private Button create_meetup;
    private String[] param = new String[6];
    private String position;
    private ImageView place;
    private SharedPreferences pref;
    private Button action_button;

    private Button scheldule;

    DisplayImageOptions options = new DisplayImageOptions.Builder()
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .resetViewBeforeLoading(true)
            .considerExifParams(true).build();


    public static FeedDetailFragment getFeedFragment(Bundle b) {
        //bundle.putSerializable("ekg", value);
        FeedDetailFragment fragment = new FeedDetailFragment();
        fragment.setArguments(b);


        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            events = getArguments().getParcelable("events");
            position = getArguments().getString("count");
            pref = getActivity().getSharedPreferences("token_info", getActivity().MODE_PRIVATE);
            SharedPreferences.Editor prefEditor = pref.edit();
            prefEditor.putBoolean("event_near", true);
            prefEditor.commit();
        }
    }

    public FeedDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        Map<String, String> dimensions = new HashMap<String, String>();
        dimensions.put("page", "Event Info");
        dimensions.put("position", position);
        if (getArguments() != null) {
            events = getArguments().getParcelable("events");
        }

        View v;

        v = inflater.inflate(R.layout.fragment_feed_detail, container, false);
        icon_feed = (ImageView) v.findViewById(R.id.icon_feed);
        if (events.getEvent().getMainAttachment() != null) {
            ImageLoader.getInstance().displayImage(events.getEvent().getMainAttachment().getUrl(), icon_feed, options);
        }

        shared = (ImageView) v.findViewById(R.id.shared);

        shared.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SharedActivity.class);
                intent.putExtra("event", events.getEvent());
                startActivity(intent);
            }
        });

        place = (ImageView) v.findViewById(R.id.icon_location);
        place.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PlaceActivity.class);
                intent.putExtra("place_id", events.getEvent().getPlace().getPlace_id());
                startActivity(intent);
            }
        });
        // title = (TextView) v.findViewById(R.id.label_title);
        when = (TextView) v.findViewById(R.id.when);
        where = (TextView) v.findViewById(R.id.where);

        text_info = (TextView) v.findViewById(R.id.text_info);

        title_text_info = (TextView) v.findViewById(R.id.title_text_info);

        count_likes = (TextView) v.findViewById(R.id.count_likes);


        icon_like = (ImageView) v.findViewById(R.id.icon_like);
        icon_comment = (ImageView) v.findViewById(R.id.icon_comment);

        scheldule = (Button) v.findViewById(R.id.scheldule);

        param[0] = getActivity().getSharedPreferences("token_info", getActivity().MODE_PRIVATE).getString("token", "");

        // events.getCurrent_coordinat_latitude();

        create_meetup = (Button) v.findViewById(R.id.create_meetup);

        create_meetup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MeetUpAddActivity.class);
                intent.putExtra("event", events.getEvent());
                startActivity(intent);
            }
        });

        param[1] = "" + 0;
        param[2] = "" + 20;
        param[3] = "" + events.getEvent().getEvent_id();
        if (events.getEvent().getPlace() != null) {
            param[4] = "" + events.getEvent().getPlace().getLatitude();
            param[5] = "" + events.getEvent().getPlace().getLongitude();
        } else {
            scheldule.setVisibility(View.GONE);
        }

        scheldule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PlaceMenuActivity.class);
                intent.putExtra("scheldure", param);
                startActivity(intent);
            }
        });
        if (events.getEvent().getEnds() != null) {
            Date d = new Date(Long.parseLong(events.getEvent().getEnds()) * 1000);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            DateTimeFormatter formatter1 = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
            DateTime endDate = DateTime.parse(sdf.format(new Date(Long.parseLong(events.getEvent().getEnds()) * 1000)), formatter1);
            DateTime startDate = DateTime.parse(sdf.format(new Date(Long.parseLong(events.getEvent().getBegins()) * 1000)), formatter1);
            String begin = Util.convertToWeek(sdf.format(new Date(Long.parseLong(events.getEvent().getBegins()) * 1000)));
            String end = Util.convertToWeek(sdf.format(new Date(Long.parseLong(events.getEvent().getEnds()) * 1000)));
            String[] end_date = end.split(",");
            String[] begin_date = begin.split(",");
            String when_end = " ";
            String when_start = " ";
            for (int i = 0; i < begin_date.length; i++) {
                String s = begin_date[i];
                if ((s.contains("-")) || (s.contains("0")) || (s.length() == 2)) {
                    continue;
                } else {
                    when_start = when_start + s;
                    break;

                }
            }

            for (int i = 0; i < end_date.length; i++) {
                String s = end_date[i];
                if (s.contains("0")) {
                    continue;
                } else if (s.contains("-")) {
                    s = s.replace("-", "");
                    when_end = when_end + s + " ago";
                    break;
                } else {
                    when_end = when_end + s;
                    break;
                }
            }

            if (when_start.length() > 1) {
                if (when_start.contains("1 day")) {
                    int i = startDate.getDayOfWeek();
                    Calendar c = Calendar.getInstance();
                    c.set(2011, 7, 1, 0, 0, 0);
                    c.add(Calendar.DAY_OF_MONTH, i - 1);
                    String s1 = String.format("%tA", c) + ", ";
                    int starth = startDate.getHourOfDay();
                    int startm = startDate.getMinuteOfHour();
                    if (startm < 10)
                        when_start = s1 + "" + starth + "h 0" + startm + "m";
                    else when_start = s1 + "" + starth + "h " + startm + "m";
                }
                if (when_start.contains(" hour")) {
                    when_start = when_start.replace(" hour", "h");
                }
                when.setText("Starts " + when_start);
            } else if (when_end.length() > 1) {
                if (when_end.contains("1 day")) {
                    int i = endDate.getDayOfWeek();
                    Calendar c = Calendar.getInstance();
                    c.set(2011, 7, 1, 0, 0, 0);
                    c.add(Calendar.DAY_OF_MONTH, i - 1);
                    String s1 = String.format("%tA", c) + ", ";
                    int endh = endDate.getHourOfDay();
                    int endm = endDate.getMinuteOfHour();
                    if (endm < 10)
                        when_end = s1 + "" + endh + "h 0" + endm + "m";
                    else when_end = s1 + "" + endh + "h " + endm + "m";
                } else if (when_end.contains("year")) {
                    when.setText("active");
                    active = true;
                }
                if (when_end.contains(" hour")) {
                    when_end = when_end.replace(" hour", "h");

                }
                if (!active) when.setText("Ends " + when_end);
                else active = false;
            }

            text_info.setText(events.getEvent().getEvent_description());
            title_text_info.setText(events.getEvent().getEvent_title());
            if (events.getLikes_count() != 0)
                count_likes.setText("" + events.getLikes_count());
            if (events.getEvent().getPlace() != null) {
                where.setText(events.getEvent().getPlace().getPlace_address());
            } else {
                where.setVisibility(View.GONE);
                place.setVisibility(View.GONE);
            }
        }
        icon_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, String> params = new LinkedHashMap<String, String>();

                params.put("access_token", getActivity().getSharedPreferences("token_info", getActivity().MODE_PRIVATE).getString("token", ""));
                params.put("event_id", "" + events.getEvent().getEvent_id());

                if (events.getLiked_by_you() != 1) {


                    App.getApi().eventLike(params, new Callback<JSONArray>() {
                        @Override
                        public void success(JSONArray jsonArray, Response response) {
                            String res = "";
                            try {
                                res = jsonArray.getJSONObject(0).getString("response_message");
                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }
                            if (res.equals("OK")) {
                                if (!count_likes.getText().equals("")) {
                                    int s = Integer.parseInt(count_likes.getText().toString()) + 1;
                                    count_likes.setText("" + s);
                                } else {
                                    count_likes.setText("" + 1);
                                }
                            }
                            events.setLiked_by_you(1);
                            icon_like.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_feed_icon_like_big_active));
                        }

                        @Override
                        public void failure(RetrofitError error) {

                        }
                    });


                } else {
                    App.getApi().eventUnLike(params, new Callback<JSONArray>() {
                        @Override
                        public void success(JSONArray jsonArray, Response response) {
                            String res = "";
                            try {
                                res = jsonArray.getJSONObject(0).getString("response_message");
                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }
                            if (res.equals("OK")) {

                                int s = Integer.parseInt(count_likes.getText().toString()) - 1;
                                if (Integer.parseInt(count_likes.getText().toString()) == 1) {
                                    count_likes.setText("");
                                } else {
                                    count_likes.setText("" + s);
                                }

                            }
                            events.setLiked_by_you(0);
                            icon_like.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_feed_icon_like_big_default));
                        }

                        @Override
                        public void failure(RetrofitError error) {

                        }
                    });


                }

            }
        });

        icon_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CommentActivity.class);
                intent.putExtra("id", events.getEvent().getEvent_id());
                startActivity(intent);
            }
        });


        action_button = (Button) v.findViewById(R.id.action);
        action_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show_dialog();
            }
        });


        return v;
    }


    public void show_dialog() {
        final Dialog dia = new Dialog(getActivity(), android.R.style.Theme_Translucent);
        dia.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dia.setCancelable(true);
        dia.setContentView(R.layout.dialog_event);

        Button create_meetup = (Button) dia.findViewById(R.id.create_meetup);


        create_meetup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MeetUpAddActivity.class);
                intent.putExtra("event", events.getEvent());
                startActivity(intent);
            }
        });

        Button send_event = (Button) dia.findViewById(R.id.send_to_friend);

        send_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SharedActivity.class);
                intent.putExtra("event", events.getEvent());
                startActivity(intent);
            }
        });


        Button attach_chat = (Button) dia.findViewById(R.id.attach_chat);


        Button cancel = (Button) dia.findViewById(R.id.cancel_button);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dia.dismiss();
            }
        });

        dia.show();

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
