package social.evenet.fragment;


import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import social.evenet.BL.FeedBL;
import social.evenet.R;
import social.evenet.activity.App;
import social.evenet.activity.MenuNewActivity;
import social.evenet.adapter.FeedAdapterNew;
import social.evenet.db.Company;
import social.evenet.db.Event;
import social.evenet.db.Events;
import social.evenet.db.MainAttachment;
import social.evenet.db.Place;
import social.evenet.db.UserInfo;
import social.evenet.helper.Connectivity;
import social.evenet.widget.PullToRefreshListView;


public class ListFeedEventFragment extends RefreshableFragment implements LocationListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private List<Events> list;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
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
    private Handler handler;
    private List<Events> item;
    private String[] param;
    private FeedBL feedBL;

    public static FeedAdapterNew adapter;
    public static PullToRefreshListView listView;


    public static ListFeedEventFragment newInstance(String param1, String param2) {
        ListFeedEventFragment fragment = new ListFeedEventFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public ListFeedEventFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_feed, container, false);
        initParam();
        App.getApi().listEvent(putOption(param),callbackEvent);
        feedBL = new FeedBL(getActivity());
        Map<String, String> dimensions = new HashMap<String, String>();
        dimensions.put("page_name ", "Events List");
        listView = (PullToRefreshListView) v.findViewById(R.id.list_feed);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle b = new Bundle();
                if(item.size()>0) {
                    Events events = item.get(position - 1);
                    b.putParcelable("events", events);
                    b.putString("count", "" + position);
                    //  getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, FeedDetailFragment.getFeedFragment(b), "feed_detail").commit();
                    MenuNewActivity aa = (MenuNewActivity) getActivity();
                    aa.showFragment(1, FeedDetailFragment.getFeedFragment(b));
                }
            }
        });

        listView.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
               initParam();
               App.getApi().listEvent(putOption(param),callbackEvent);

            }
        });

      listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, final int totalItemCount) {
                if ((totalItemCount > 0) && (!flag)) {
                    int lastInScreen = firstVisibleItem + visibleItemCount;
                    if (lastInScreen == totalItemCount) {
                        flag = true;
                        count = count + 50;
                        param[4]=""+count;
                        App.getApi().listEvent(putOption(param),callback);


                    }
                }
            }
        });


        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                List<Events> mas=  (List<Events>) msg.obj;
                if (mas.size() > 0) {
                    item=  (List<Events>) msg.obj;
                    adapter = new FeedAdapterNew(getActivity(), item);
                    listView.setAdapter(adapter);
                    listView.setSelection(1);
                    flag = false;
                }
            }
        };


        return v;
    }

    public Map<String,String> putOption(String [] param){
        Map<String ,String>  options=new LinkedHashMap<>();
        options.put("access_token",param[0]);
        options.put("latitude",param[1]);
        options.put("longitude",param[2]);
        options.put("count",param[3]);
        options.put("offset",param[4]);
        return options;
    }

    public void initParam(){
        location=getLocation();
        param = new String[5];
        param[0] = getActivity().getSharedPreferences("token_info", getActivity().MODE_PRIVATE).getString("token", "");
        if (location != null) {

            param[1] = "" + location.getLatitude();
            param[2] = "" + location.getLongitude();
        } else {
            param[1] = "" + 55.72;
            param[2] = "" + 37.61;

        }
        if (Connectivity.isConnectedFast(getActivity())) {
            param[3] = "50";

        } else {
            param[3] = "20";
        }
        param[4] = "0";
    }

    private boolean flag = false;

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
                    Log.d("Network", "Network Enabled");
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

    private static int count = 0;





    public List<Events> parseFeed(JSONArray jsonArray){
        ArrayList<Events> massive = new ArrayList<Events>();
        try {
            jsonArray = jsonArray.optJSONObject(0).optJSONArray("events");
            if(jsonArray.length()!=1){
                for (int i = 0; i < jsonArray.length(); i++) {
                    Events e = new Events();
                    e.setCurrent_coordinat_latitude(Double.parseDouble(param[1]));
                    e.setCurrent_coordinat_longutide(Double.parseDouble(param[2]));
                    Event event=new Event();
                    MainAttachment mainAttachment=new MainAttachment();
                    Company company=new Company();
                    UserInfo creator=new UserInfo();
                    Place place=new Place();
                    JSONObject jsonobject = jsonArray.getJSONObject(i);
                    e.setLikes_count(jsonobject.getInt("likes_count"));
                    e.setComments_count(jsonobject.getInt("comments_count"));
                    e.setLiked_by_you(jsonobject.getInt("liked_by_you"));
                    e.setCheckins_count(jsonobject.getInt("checkins_count"));
                    JSONObject jsonevent = jsonobject.getJSONObject("event");
                    event.setEvent_id(jsonevent.getInt("event_id"));
                    event.setEvent_title(jsonevent.getString("event_title"));
                    event.setEvent_description(jsonevent.getString("event_description"));
                    event.setLang_id(jsonevent.getInt("lang_id"));
                    event.setBegins(jsonevent.getString("begins"));
                    event.setEnds(jsonevent.getString("ends"));
                    if(!jsonevent.isNull("main_attachment")){
                        JSONObject jsonmain_attachment = jsonevent.getJSONObject("main_attachment");
                        mainAttachment.setId(jsonmain_attachment.getInt("id"));
                        mainAttachment.setType(jsonmain_attachment.getString("type"));
                        mainAttachment.setUrl(jsonmain_attachment.getJSONObject("photo_big").getString("url"));
                        mainAttachment.setSmall_photo(jsonmain_attachment.getJSONObject("photo_small").getString("url"));

                        if(!jsonevent.isNull("company")){
                            JSONObject jsonCompany = jsonevent.getJSONObject("company");
                            company.setCompany_name(jsonCompany.getString("company_name"));
                            if(jsonCompany.optInt("company_id")!=0){
                                company.setCompany_id(jsonCompany.getInt("company_id"));
                                company.setLang_id(jsonCompany.getInt("lang_id"));
                            }
                        }
                        if(!jsonevent.isNull("creator")){
                            creator.setUser_id(jsonevent.getJSONObject("creator").getInt("user_id"));
                            creator.setName(jsonevent.getJSONObject("creator").getString("name"));
                            creator.setSurname(jsonevent.getJSONObject("creator").getString("surname"));
                            if(!jsonevent.getJSONObject("creator").isNull("photo_small"))
                                creator.setPhoto_small(jsonevent.getJSONObject("creator").getString("photo_small"));
                        }
                    }
                    JSONObject jsonPlace = jsonevent.getJSONObject("place");
                    place.setPlace_id(jsonPlace.getInt("place_id"));
                    place.setPlace_title(jsonPlace.getString("place_title"));
                    place.setPlace_description(jsonPlace.getString("place_description"));
                    place.setLang_id(jsonPlace.getInt("lang_id"));
                    place.setLatitude(jsonPlace.getDouble("latitude"));
                    place.setLongitude(jsonPlace.getDouble("longitude"));
                    place.setPlace_address(jsonPlace.getString("place_address"));
                    place.setPlace_category(jsonPlace.getString("category_name"));
                    place.setCategory_id(jsonPlace.getInt("category_id"));
                    event.setMainAttachment(mainAttachment);
                    event.setCompany(company);
                    event.setPlace(place);
                    e.setEvent(event);
                    massive.add(e);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return massive;
    }


    private Callback<JSONArray> callback=new Callback<JSONArray>() {
        @Override
        public void success(JSONArray jsonArray, Response response) {

            if (jsonArray.optJSONObject(0).optJSONArray("events").length()!=0) {
                Message msg = new Message();
                msg.obj = parseFeed(jsonArray);
                handler.sendMessage(msg);
            } else {
                   count = 0;
                    param[4]=""+count;
                    flag=false;

            }
        }

        @Override
        public void failure(RetrofitError error) {

        }
    };

    private Callback<JSONArray> callbackEvent=new Callback<JSONArray>() {
        @Override
        public void success(JSONArray jsonArray, Response response) {
            flag = false;

            item=parseFeed(jsonArray);
            adapter = new FeedAdapterNew(getActivity(), item);
            listView.setAdapter(adapter);
            listView.onRefreshComplete();
        }

        @Override
        public void failure(RetrofitError error) {

        }
    };
}
