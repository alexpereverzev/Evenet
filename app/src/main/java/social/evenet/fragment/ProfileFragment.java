package social.evenet.fragment;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.parse.ParseAnalytics;
import com.pkmmte.view.CircularImageView;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

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
import social.evenet.activity.MenuNewActivity;
import social.evenet.activity.UsersInfo;
import social.evenet.adapter.WallAdapter;
import social.evenet.db.Event;
import social.evenet.db.MainAttachment;
import social.evenet.db.Place;
import social.evenet.db.Posts;
import social.evenet.db.UserInfo;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 */
public class ProfileFragment extends RefreshableFragment {

    private Handler mhandler = new Handler();
    private ArrayList<UserInfo> massive;
    private ArrayList<Posts> item;
    private ListView listView;
    private WallAdapter adapter;
    private CircularImageView icon_user;
    private TextView name_surname;
    private TextView followers;
    private TextView following;

    private Button button_friends;
    private Button follow;
    private TextView friends;
    private TextView nikname;
    private UserInfo userInfo;
    private ImageView setting_profile;
    private SharedPreferences pref;
    private String[] param;
    private LinearLayout following_layout;
    private TextView suggested_events;
    private TextView place_list;
    private TextView common_meetup;
    private TextView reviews;
    private CircularImageView logo;
    private ScrollView scrollView;
    public static ViewGroup decorViewGroup;
    private SlidingUpPanelLayout sliding_layout;

    int a, b;


    public static View logoView;

    DisplayImageOptions options_display = new DisplayImageOptions.Builder()
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .considerExifParams(true).build();


    public ProfileFragment() {
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
        if (args != null) {
            userInfo = getArguments().getParcelable("user");
            param[1] = "" + userInfo.getUser_id();
        } else {
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
                userInfo = massive.get(0);
                if (followers != null) {
                    followers.setText(" " + userInfo.getFollowers_count() + " followers");
                    name_surname.setText(massive.get(0).getName() + " " + massive.get(0).getSurname());
                    following.setText(" " + massive.get(0).getFollowing_count());
                    friends.setText(" " + massive.get(0).getFriends_count() + " friends");
                    place_list.setText("" + userInfo.getPlaces_in_lists());
                    common_meetup.setText("" + userInfo.getCommon_meetups());
                    reviews.setText("" + userInfo.getReviews_count());
                    nikname.setText("@" + userInfo.getNikname());
                    ImageLoader.getInstance().displayImage(massive.get(0).getPhoto_small(), logo, options_display);
                    ImageLoader.getInstance().displayImage(massive.get(0).getPhoto_small(), icon_user, options_display);
                }


                    Bundle b = new Bundle();
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

        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        sliding_layout = (SlidingUpPanelLayout) view.findViewById(R.id.sliding_layout);
        sliding_layout.setPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {

            }

            @Override
            public void onPanelCollapsed(View panel) {
                scrollView.scrollTo(0, 0);
            }

            @Override
            public void onPanelExpanded(View panel) {

            }

            @Override
            public void onPanelAnchored(View panel) {

            }

            @Override
            public void onPanelHidden(View panel) {
                scrollView.scrollTo(0, 0);
            }
        });

        listView = (ListView) view.findViewById(R.id.wall);
        icon_user = (CircularImageView) view.findViewById(R.id.icon_profile);
        name_surname = (TextView) view.findViewById(R.id.name_surname_user);
        followers = (TextView) view.findViewById(R.id.profile_follower);
        following = (TextView) view.findViewById(R.id.profile_following);
        friends = (TextView) view.findViewById(R.id.profile_friends);
        following_layout = (LinearLayout) view.findViewById(R.id.following);
        following_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), UsersInfo.class);
                intent.putExtra("number", 1);
                intent.putExtra("user_id", param[1]);
                startActivity(intent);
            }
        });

        setting_profile = (ImageView) view.findViewById(R.id.setting_profile);
        setting_profile.setVisibility(View.VISIBLE);
        setting_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MenuNewActivity) getActivity()).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, SettingFragment.getFragment(userInfo)).commit();
            }
        });
        nikname = (TextView) view.findViewById(R.id.nikname);
        followers.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), UsersInfo.class);
                intent.putExtra("number", 0);
                intent.putExtra("user_id", param[1]);
                startActivity(intent);
            }
        });
        friends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), UsersInfo.class);
                intent.putExtra("number", 2);
                intent.putExtra("user_id", param[1]);
                startActivity(intent);
            }
        });
        follow = (Button) view.findViewById(R.id.follow);
        follow.setVisibility(View.GONE);
        suggested_events = (TextView) view.findViewById(R.id.number_suggested);

        place_list = (TextView) view.findViewById(R.id.number_place);

        common_meetup = (TextView) view.findViewById(R.id.commons_meetups);

        reviews = (TextView) view.findViewById(R.id.reviews);


        if (userInfo != null) {
            place_list.setText("" + userInfo.getPlaces_in_lists());

            common_meetup.setText("" + userInfo.getCommon_meetups());

            reviews.setText("" + userInfo.getReviews_count());
            nikname.setText("@" + userInfo.getNikname());
        }

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

        TextView suggested_label = (TextView) view.findViewById(R.id.suggest_events);
        TextView placeinlists = (TextView) view.findViewById(R.id.places_in_lists);
        TextView following_label = (TextView) view.findViewById(R.id.following_label);
        TextView commons_meetup = (TextView) view.findViewById(R.id.commons_meetup);
        TextView reviews_label = (TextView) view.findViewById(R.id.reviews_label);
        suggested_label.setTypeface(typeface_light);
        placeinlists.setTypeface(typeface_light);
        following_label.setTypeface(typeface_light);
        commons_meetup.setTypeface(typeface_light);
        reviews_label.setTypeface(typeface_light);

        LayoutInflater infla_logo = LayoutInflater.from(getActivity());
        logoView = infla_logo.inflate(R.layout.logo_view, null, false);
        logo = (CircularImageView) logoView.findViewById(R.id.icon_profile);
        logo.setVisibility(View.VISIBLE);
        decorViewGroup = (ViewGroup) getActivity().getWindow().getDecorView();

        scrollView = (ScrollView) view.findViewById(R.id.scroll);

        a = nikname.getTop();
        b = nikname.getBottom();

        scrollView.scrollTo(0, 0);

        scrollView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                MenuNewActivity activity = (MenuNewActivity) getActivity();


                switch (event.getAction()) {
                    case MotionEvent.ACTION_SCROLL:


                    case MotionEvent.ACTION_MOVE:

                        if (!flag) {
                            Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.zoom_min);
                            animation.setFillAfter(true);
                            animation.setAnimationListener(new Animation.AnimationListener() {
                                @Override
                                public void onAnimationStart(Animation animation) {

                                }

                                @Override
                                public void onAnimationEnd(Animation animation) {

                                }

                                @Override
                                public void onAnimationRepeat(Animation animation) {

                                }
                            });
                            logo.startAnimation(animation);

                            flag = true;
                        }


                        if (scrollView.getScrollY() > 25) {
                            if (flag) {

                                Animation animation1 = AnimationUtils.loadAnimation(getActivity(), R.anim.zoom_min_second);
                                animation1.setFillAfter(true);
                                logo.startAnimation(animation1);
                                animation1.setAnimationListener(new Animation.AnimationListener() {
                                    @Override
                                    public void onAnimationStart(Animation animation) {

                                    }

                                    @Override
                                    public void onAnimationEnd(Animation animation) {


                                        decorViewGroup.removeView(logoView);
                                        delete = true;
                                        icon_user.setVisibility(View.VISIBLE);
                                    }

                                    @Override
                                    public void onAnimationRepeat(Animation animation) {

                                    }
                                });
                            }
                        }


                        if (scrollView.getScrollY() > 140) {
                            activity.getSupportActionBar().setTitle(userInfo.getName());

                        }

                        //  else


                        break;
                    case MotionEvent.ACTION_DOWN:


                        break;
                    case MotionEvent.ACTION_CANCEL:

                    case MotionEvent.ACTION_UP:
                        if (scrollView.getScrollY() < 50) {

                            if (delete && flag) {
                                activity.getSupportActionBar().setTitle("Profile");
                                icon_user.setVisibility(View.INVISIBLE);
                                //    decorViewGroup.addView(logoView);
                                Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.zoom_max);
                                animation.setFillAfter(true);
                                animation.setAnimationListener(new Animation.AnimationListener() {
                                    @Override
                                    public void onAnimationStart(Animation animation) {

                                    }

                                    @Override
                                    public void onAnimationEnd(Animation animation) {
                                        flag = false;
                                    }

                                    @Override
                                    public void onAnimationRepeat(Animation animation) {

                                    }
                                });
                                logo.startAnimation(animation);
                                delete = false;


                            }
                        }
                        break;
                }
                return false;
            }
        });

        return view;
    }

    static boolean flag = false;
    public static boolean delete = false;

    @Override
    public void onDestroy() {
       // decorViewGroup.removeView(logoView);
        super.onDestroy();
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
          scrollView.scrollTo(0, 0);

      }

      @Override
      public void failure(RetrofitError error) {

      }
  };

}
