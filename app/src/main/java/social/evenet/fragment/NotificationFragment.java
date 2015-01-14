package social.evenet.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.parse.ParseAnalytics;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import social.evenet.R;
import social.evenet.activity.App;
import social.evenet.adapter.NotificationAdapter;
import social.evenet.db.Event;
import social.evenet.db.MainAttachment;
import social.evenet.db.Meetups;
import social.evenet.db.Notification;
import social.evenet.db.Place;
import social.evenet.db.UserInfo;
import social.evenet.helper.Connectivity;
import social.evenet.widget.PullToRefreshListView;


public class NotificationFragment extends RefreshableFragment {

    private SharedPreferences pref;
    private  String[] param;
    private PullToRefreshListView listView;
    private NotificationAdapter adapter;
    private ArrayList<Notification> item;

    DisplayImageOptions  options = new DisplayImageOptions.Builder()
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .considerExifParams(true).build();



    // TODO: Rename and change types and number of parameters
    public static NotificationFragment newInstance(String param1, String param2) {
        NotificationFragment fragment = new NotificationFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }
    public NotificationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_notification, container, false);
        Map<String, String> dimensions = new HashMap<String, String>();
        dimensions.put("page_name ", "notification");
        ParseAnalytics.trackEvent("Page View", dimensions);
        param = new String[4];
        param[0] = getActivity().getSharedPreferences("token_info", getActivity().MODE_PRIVATE).getString("token", "");
        param[1] = getActivity().getSharedPreferences("token_info", getActivity().MODE_PRIVATE).getString("user_id", "");
        if (Connectivity.isConnectedFast(getActivity())) {
            param[2] = "" + 50;
        } else {
            param[2] = "" + 20;
        }
        param[3] = "" + 0;

        Map<String ,String>  options=new LinkedHashMap<>();
        options.put("access_token",param[0]);
        options.put("user_id",param[1]);
        options.put("count",param[2]);
        options.put("offset",param[3]);

        App.getApi().userNotification(options,new Callback<JSONArray>() {
            @Override
            public void success(JSONArray jsonArray, Response response) {
                ArrayList<Notification> notifications=new ArrayList<Notification>();
                JSONArray json = jsonArray.optJSONObject(0).optJSONArray("notifications");
                if (json.length() != 0) {
                    for (int i = 0; i < json.length(); i++) {
                        Notification not = new Notification();
                        UserInfo userInfo = new UserInfo();
                        Meetups meetups = new Meetups();
                        Event e = new Event();
                        Place place = new Place();
                        MainAttachment attachment = new MainAttachment();
                        not.setMeetup_id((json.optJSONObject(i).optInt("notification_id")));
                        if (!(json.optJSONObject(i).isNull("chat_name"))) {
                            not.setChat_name((json.optJSONObject(i).optString("chat_name")));
                        }
                        if (!json.optJSONObject(i).isNull("date")) {
                            not.setTime((json.optJSONObject(i).optInt("date")));
                        }
                        userInfo.setUser_id(json.optJSONObject(i).optJSONObject("user").optInt("user_id"));
                        userInfo.setName(json.optJSONObject(i).optJSONObject("user").optString("name"));
                        userInfo.setSurname(json.optJSONObject(i).optJSONObject("user").optString("surname"));
                        userInfo.setPhoto_small(json.optJSONObject(i).optJSONObject("user").optString("photo_small"));
                        not.setNotification_type(json.optJSONObject(i).optString("notification_type"));
                        if (!json.optJSONObject(i).isNull("meetup")) {
                            meetups.setMeetup_id(json.optJSONObject(i).optJSONObject("meetup").optInt("meetup_id"));
                            meetups.setMeetup_name(json.optJSONObject(i).optJSONObject("meetup").optString("meetup_name"));
                        }
                        if (!json.optJSONObject(i).isNull("place")) {
                            place.setPlace_id(json.optJSONObject(i).optJSONObject("place").optInt("place_id"));
                            place.setPlace_title(json.optJSONObject(i).optJSONObject("place").optString("place_title"));
                            place.setPlace_description(json.optJSONObject(i).optJSONObject("place").optString("text"));
                        }
                        if (!json.optJSONObject(i).isNull("event")) {
                            e.setEvent_id(json.optJSONObject(i).optJSONObject("event").optInt("event_id"));
                            e.setEvent_title(json.optJSONObject(i).optJSONObject("event").optString("event_title"));
                        }
                        if (!json.optJSONObject(i).isNull("main_attachment")) {
                            attachment.setSmall_photo(json.optJSONObject(i).optJSONObject("main_attachment").optString("photo_small"));
                            e.setMainAttachment(attachment);
                        }
                        not.setEvent(e);
                        not.setPlace(place);
                        not.setMeetups(meetups);
                        not.setUserInfo(userInfo);
                        notifications.add(not);
                    }
                }

                item = notifications;
                adapter = new NotificationAdapter(getActivity(), item);
                listView.setAdapter(adapter);
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });

        listView=(PullToRefreshListView) v.findViewById(R.id.list_notification);
        return v;
    }




}
