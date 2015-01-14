package social.evenet.fragment;


import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.parse.ParseAnalytics;
import com.pkmmte.view.CircularImageView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import social.evenet.R;
import social.evenet.activity.App;
import social.evenet.adapter.WallAdapter;
import social.evenet.db.Event;
import social.evenet.db.MainAttachment;
import social.evenet.db.Place;
import social.evenet.db.Posts;
import social.evenet.db.UserInfo;
import social.evenet.helper.Helper;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 */
public class ProfilesUsers extends RefreshableFragment {


    private ArrayList<UserInfo> massive;
    private ArrayList<Posts> item;
    private ListView listView;
    private WallAdapter adapter;
    private CircularImageView icon_user;
    private TextView name_surname;
    private TextView followers;
    private TextView following;
    private Button button_following;
    private Button button_follower;
    private Button button_friends;

    private TextView friends;
    private UserInfo userInfo;
    private ImageView setting_profile;
    private SharedPreferences pref;
    private Button follow;
    private  String[] param;
    private LinearLayout following_layout;
    private TextView suggested_events;
    private TextView place_list;
    private TextView common_meetup;
    private TextView reviews;
    private TextView nikname;
    private CircularImageView logo;

    DisplayImageOptions  options_foto = new DisplayImageOptions.Builder()
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .considerExifParams(true).build();



    public ProfilesUsers() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Map<String, String> dimensions = new HashMap<String, String>();
        dimensions.put("page_name ", "profile");
        ParseAnalytics.trackEvent("Page View", dimensions);

        param = new String[4];
        param[0] = getActivity().getSharedPreferences("token_info", getActivity().MODE_PRIVATE).getString("token", "");
        Bundle args = getArguments();
        if(args!=null){
        userInfo = getArguments().getParcelable("user");

            param[1] = ""+userInfo.getUser_id();
        }
        else {
            param[1] = getActivity().getSharedPreferences("token_info", getActivity().MODE_PRIVATE).getString("user_id", "");
        }


        Map<String, String> options = new LinkedHashMap<>();
        options.put("access_token", param[0]);
        options.put("user_id", param[1]);
        App.getApi().userInfo(options, new Callback<JSONArray>() {
            @Override
            public void success(JSONArray jsonArray, Response response) {
                massive = new ArrayList<UserInfo>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    UserInfo userInfo = new UserInfo();
                    JSONObject jsonobject = jsonArray.optJSONObject(i);
                    userInfo.setName(jsonobject.optString("name"));
                    userInfo.setSurname(jsonobject.optString("surname"));
                    userInfo.setNikname(jsonobject.optString("screen_name"));
                    userInfo.setFollowers_count(jsonobject.optInt("followers_count"));
                    userInfo.setFollowing_count(jsonobject.optInt("following_count"));
                    userInfo.setFriends_count(jsonobject.optInt("friends_count"));
                    userInfo.setFeedback_notification_count(jsonobject.optInt("feedback_notifications_count"));
                    userInfo.setCommon_meetups(jsonobject.optInt("common_meetups"));
                    userInfo.setPlaces_in_lists(jsonobject.optInt("places_in_lists"));
                    userInfo.setReviews_count(jsonobject.optInt("reviews_count"));
                    userInfo.setPhoto_small(jsonobject.optString("photo_small"));
                    userInfo.setReputation(jsonobject.optInt("reputation"));
                    userInfo.setGender(jsonobject.optString("gender"));
                    if (!jsonobject.isNull("phone_number"))
                        userInfo.setPhone_number(jsonobject.optInt("phone_number"));
                    else {
                        userInfo.setPhone_number(0);
                    }
                    userInfo.setLast_seen(jsonobject.optString("last_seen"));
                    userInfo.setNative_lang(jsonobject.optString("native_lang"));
                    userInfo.setHome_city(jsonobject.optString("home_city"));
                    userInfo.setEvent_created(jsonobject.optInt("events_created"));
                    userInfo.setPhoto_big(jsonobject.optString("photo_big"));
                    userInfo.setRelation(jsonobject.optInt("relation"));
                    massive.add(userInfo);
                }
                userInfo=massive.get(0);
                if(followers!=null){
                    followers.setText(" " + userInfo.getFollowers_count()+" followers");
                    name_surname.setText(massive.get(0).getName()+" "+massive.get(0).getSurname());
                    following.setText(" "+massive.get(0).getFollowing_count());
                    friends.setText(" "+massive.get(0).getFriends_count());

                    following.setText(" " + massive.get(0).getFollowing_count());
                    friends.setText(" " + massive.get(0).getFriends_count()+" friends");
                    place_list.setText("" + userInfo.getPlaces_in_lists());

                    common_meetup.setText("" + userInfo.getCommon_meetups());

                    reviews.setText("" + userInfo.getReviews_count());
                    nikname.setText("@" + userInfo.getNikname());

                    ImageLoader.getInstance().displayImage(massive.get(0).getPhoto_small(), logo, options_foto);

                }

                    param[2] = "20";
                    param[3] = "0";
                    Map<String, String> options = new LinkedHashMap<>();
                    options.put("access_token", param[0]);
                    options.put("user_id", param[1]);
                    options.put("count","20");
                    options.put("offset","0");
                    App.getApi().userWall(options,callBack);



            }

            @Override
            public void failure(RetrofitError error) {
            }
        });


        View view=inflater.inflate(R.layout.fragment_profile, container, false);

        follow=(Button) view.findViewById(R.id.follow);
        listView=(ListView) view.findViewById(R.id.wall);
        icon_user=(CircularImageView) view.findViewById(R.id.icon_profile);
        name_surname=(TextView)view.findViewById(R.id.name_surname_user);
        followers=(TextView)view.findViewById(R.id.profile_follower);
        following=(TextView)view.findViewById(R.id.profile_following);
        friends=(TextView)view.findViewById(R.id.profile_friends);
        setting_profile=(ImageView) view.findViewById(R.id.setting_profile);
        setting_profile.setVisibility(View.INVISIBLE);
        follow.setVisibility(View.VISIBLE);
        follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Map<String,String> options=new LinkedHashMap<String, String>();
                    options.put("access_token",param[0]);
                    options.put("user_id",""+userInfo.getUser_id());
                    App.getApi().userFollow(options,new Callback<JSONArray>() {
                        @Override
                        public void success(JSONArray jsonArray, Response response) {

                        }

                        @Override
                        public void failure(RetrofitError error) {

                        }
                    });

            }
        });

        setting_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_users, SettingFragment.getFragment(userInfo)).commit();
            }
        });




        button_follower=(Button) view.findViewById(R.id.button_follower);
        button_friends=(Button) view.findViewById(R.id.button_friends);



        button_follower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_users, UserInfoFragment.getFragment(0, param[1])).commit();

            }
        });


        button_friends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_users, UserInfoFragment.getFragment(2, param[1])).commit();
            }
        });

        nikname = (TextView) view.findViewById(R.id.nikname);
        following_layout = (LinearLayout) view.findViewById(R.id.following);
        suggested_events=(TextView) view.findViewById(R.id.number_suggested);

        place_list=(TextView) view.findViewById(R.id.number_place);

        common_meetup=(TextView) view.findViewById(R.id.commons_meetups);

        reviews=(TextView) view.findViewById(R.id.reviews);

        following_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_users, UserInfoFragment.getFragment(1, param[1])).commit();
            }
        });

        Typeface typeface_light = Typeface.createFromAsset(getActivity().getAssets(),
                "fonts/HelveticaNeueLight.ttf");
        Typeface typeface_medium = Typeface.createFromAsset(getActivity().getAssets(),
                "fonts/HelveticaNeueMedium.ttf");
        Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(),
                "fonts/HelveticaNeue.ttf");


        nikname.setTypeface(typeface_medium);
        name_surname.setTypeface(typeface_medium);
        followers.setTypeface(typeface_medium);
        friends.setTypeface(typeface_medium);

        place_list.setTypeface(typeface_light);
        common_meetup.setTypeface(typeface_light);
        reviews.setTypeface(typeface_light);
        following.setTypeface(typeface_light);

        TextView suggested_label=(TextView) view.findViewById(R.id.suggest_events);
        TextView placeinlists=(TextView) view.findViewById(R.id.places_in_lists);
        TextView following_label=(TextView) view.findViewById(R.id.following_label);
        TextView commons_meetup=(TextView) view.findViewById(R.id.commons_meetup);
        TextView reviews_label=(TextView) view.findViewById(R.id.reviews_label);
        suggested_label.setTypeface(typeface_light);
        placeinlists.setTypeface(typeface_light);
        following_label.setTypeface(typeface_light);
        commons_meetup.setTypeface(typeface_light);
        reviews_label.setTypeface(typeface_light);

        LayoutInflater infla_logo = LayoutInflater.from(getActivity());
        View logoView = infla_logo.inflate(R.layout.logo_view, null, false);
        logo=(CircularImageView) logoView.findViewById(R.id.icon_profile);
        ViewGroup decorViewGroup = (ViewGroup) getActivity().getWindow().getDecorView();
        decorViewGroup.addView(logoView);

        return view;
    }




    private Callback<JSONArray> callBack=new Callback<JSONArray>() {

        @Override
        public void success(JSONArray jsonArray, Response response) {
            ArrayList<Posts> massive = new ArrayList<Posts>();
            JSONArray json = jsonArray.optJSONObject(0).optJSONArray("posts");
            if (json.length() != 0) {


                for (int i = 0; i < json.length(); i++) {
                    Posts p = new Posts();

                    Event event = new Event();

                    MainAttachment mainAttachment = new MainAttachment();
                    Place place = new Place();

                    JSONObject jsonobject = json.optJSONObject(i);
                    p.setDate(jsonobject.optInt("date"));
                    if (!jsonobject.isNull("text")) p.setMessage(jsonobject.optString("text"));
                    JSONObject jsonevent = jsonobject.optJSONObject("event");

                    event.setEvent_id(jsonevent.optInt("event_id"));
                    event.setEvent_title(jsonevent.optString("event_title"));
                    if (!jsonevent.isNull("main_attachment")) {
                        JSONObject jsonmain_attachment = jsonevent.optJSONObject("main_attachment");
                        mainAttachment.setId(jsonmain_attachment.optInt("id"));
                        mainAttachment.setType(jsonmain_attachment.optString("type"));
                        mainAttachment.setUrl(jsonmain_attachment.optJSONObject("photo_big").optString("url"));
                        mainAttachment.setSmall_photo(jsonmain_attachment.optJSONObject("photo_small").optString("url"));
                    }

                    p.setEvent(event);
                    massive.add(p);


                }

            }


            item = massive;
            adapter = new WallAdapter(getActivity(), item, userInfo.getName(), userInfo.getPhoto_small());
            listView.setAdapter(adapter);
            Helper.getListViewSize(listView);

        }

        @Override
        public void failure(RetrofitError error) {

        }
    };

}
