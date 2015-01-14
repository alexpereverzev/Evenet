package social.evenet.fragment;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.parse.ParseAnalytics;

import org.json.JSONArray;
import org.json.JSONException;
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
import social.evenet.adapter.PlaceListAdapter;
import social.evenet.adapter.ScheldureListAdapter;
import social.evenet.db.Company;
import social.evenet.db.Event;
import social.evenet.db.Events;
import social.evenet.db.MainAttachment;
import social.evenet.db.Place;
import social.evenet.widget.PullToRefreshList;


public class PlaceFragment extends RefreshableFragment {


    private Intent intent;

    private static int count = 0;

    private Handler mhandler = new Handler();


    private List<Events> item;
    private String[] param;
    private FeedBL feedBL;
    public static PlaceListAdapter adapter;
    public static PullToRefreshList listView;
    private MenuItem searchItem;

    private FeedBL bl;



    public static Location location;


    public PlaceFragment() {
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
        // Inflate the layout for this fragment


        Map<String, String> dimensions = new HashMap<String, String>();
        dimensions.put("page_name ", "place info");
        ParseAnalytics.trackEvent("Page View", dimensions);


        View v = inflater.inflate(R.layout.fragment_place, container, false);
        listView = (PullToRefreshList) v.findViewById(R.id.exListView);


        param = getArguments().getStringArray("param");
        if (param != null) {

            Map<String, String> params = new LinkedHashMap<>();
            params.put("access_token", param[0]);
            params.put("place_id", param[1]);
            params.put("count", param[2]);
            params.put("offset", param[3]);
            App.getApi().placeEvent(params, new Callback<JSONArray>() {
                @Override
                public void success(JSONArray jsonArray, Response response) {
                    item = new ArrayList<Events>();
                    try {
                        jsonArray = jsonArray.optJSONObject(0).optJSONArray("events");
                        if (jsonArray.length() != 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                Events e = new Events();
                                Event event = new Event();
                                MainAttachment mainAttachment = new MainAttachment();
                                Company company = new Company();
                                JSONObject jsonobject = jsonArray.getJSONObject(i);
                                e.setLikes_count(jsonobject.getInt("likes_count"));
                                e.setComments_count(jsonobject.getInt("comments_count"));
                                e.setLiked_by_you(jsonobject.getInt("liked_by_you"));
                                JSONObject jsonevent = jsonobject.getJSONObject("event");
                                event.setEvent_id(jsonevent.getInt("event_id"));
                                event.setEvent_title(jsonevent.getString("event_title"));
                                event.setEvent_description(jsonevent.getString("event_description"));
                                event.setLang_id(jsonevent.getInt("lang_id"));
                                event.setBegins(jsonevent.getString("begins"));
                                event.setEnds(jsonevent.getString("ends"));
                                if (!jsonevent.isNull("main_attachment")) {
                                    JSONObject jsonmain_attachment = jsonevent.getJSONObject("main_attachment");
                                    mainAttachment.setId(jsonmain_attachment.getInt("id"));
                                    mainAttachment.setType(jsonmain_attachment.getString("type"));
                                    mainAttachment.setUrl(jsonmain_attachment.getString("url"));
                                }
                                if (!jsonevent.isNull("company")) {
                                    JSONObject jsonCompany = jsonevent.getJSONObject("company");
                                    company.setCompany_name(jsonCompany.getString("company_name"));
                                    if (jsonCompany.optInt("company_id") != 0) {
                                        company.setCompany_id(jsonCompany.getInt("company_id"));
                                        company.setLang_id(jsonCompany.getInt("lang_id"));
                                    }
                                }
                                event.setMainAttachment(mainAttachment);
                                event.setCompany(company);
                                e.setEvent(event);
                                item.add(e);
                            }

                        }

                        listView.onRefreshComplete();

                        ArrayList<ArrayList<Events>> groups = new ArrayList<ArrayList<Events>>();

                        for (int i = 0; i < item.size(); i++) {
                            ArrayList<Events> list1 = new ArrayList<Events>();
                            list1.add(item.get(i));
                            groups.add(list1);
                        }


                        listView.setAdapter(adapter);

                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                }

                @Override
                public void failure(RetrofitError error) {

                }
            });


        } else if (getArguments().getStringArray("scheldure") != null) {
            Map<String, String> opt = new LinkedHashMap<>();
            opt.put("access_token", param[0]);
            opt.put("offset", param[1]);
            opt.put("count", param[2]);
            opt.put("event_id", param[3]);
            opt.put("latitude", param[4]);
            opt.put("longitude", param[5]);
            App.getApi().eventSheldure(opt, new Callback<JSONArray>() {
                @Override
                public void success(JSONArray jsonArray, Response response) {
                    ArrayList<Events> massive = new ArrayList<Events>();
                    jsonArray = jsonArray.optJSONObject(0).optJSONArray("rules");
                    if (jsonArray.length() != 0) {

                        for (int i = 0; i < jsonArray.length(); i++) {
                            Events e = new Events();
                            Event event = new Event();
                            Place place = new Place();
                            JSONObject jsonobject = jsonArray.optJSONObject(i);
                            event.setEvent_id(jsonobject.optInt("rule_id"));
                            event.setLang_id(jsonobject.optInt("lang_id"));
                            event.setBegins(jsonobject.optString("begins"));
                            event.setEnds(jsonobject.optString("ends"));
                            JSONObject jsonPlace = jsonobject.optJSONObject("place");
                            place.setPlace_id(jsonPlace.optInt("place_id"));
                            place.setPlace_title(jsonPlace.optString("place_title"));
                            place.setPlace_description(jsonPlace.optString("place_description"));
                            place.setLang_id(jsonPlace.optInt("lang_id"));
                            place.setLatitude(jsonPlace.optDouble("latitude"));
                            place.setLongitude(jsonPlace.optDouble("longitude"));
                            place.setPlace_address(jsonPlace.optString("place_address"));
                            event.setPlace(place);
                            e.setEvent(event);
                            massive.add(e);
                        }

                    }
                    item = massive;
                    listView.onRefreshComplete();

                    ArrayList<ArrayList<Events>> groups = new ArrayList<ArrayList<Events>>();

                    for (int i = 0; i < item.size(); i++) {
                        ArrayList<Events> list1 = new ArrayList<Events>();
                        list1.add(item.get(i));
                        groups.add(list1);
                    }
                    ScheldureListAdapter adapter = new ScheldureListAdapter(getActivity(), groups);

                    listView.setAdapter(adapter);
                }

                @Override
                public void failure(RetrofitError error) {

                }
            });

        }


        listView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {


                intent.putExtra("place_id", item.get(groupPosition).getEvent().getPlace().getPlace_id());
                startActivity(intent);
                return false;
            }
        });
 /*       //list_feed = (ListView) v.findViewById(R.id.list_feed);
        listView.setOnRefreshListener(new PullToRefreshList.OnRefreshListener() {
            @Override
            public void onRefresh() {
                location = getLocation();

                count=count+20;
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
                    param[3] = "20";
                    param[4] = ""+count;
                } else {
                    param[3] = "20";
                    param[4] = ""+count;
                }
                try {
                    Bundle b = new Bundle();
                    b.putStringArray("param", param);
                    startCommand(ActionList.EXP_ACTION, b, true);
                } catch (ContextNullReference contextNullReference) {
                    contextNullReference.printStackTrace();
                }

            }
        });*/


        return v;


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
