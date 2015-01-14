package social.evenet.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import social.evenet.R;
import social.evenet.activity.App;
import social.evenet.activity.PlaceMenuActivity;
import social.evenet.adapter.PlaceEventAdapter;
import social.evenet.db.Company;
import social.evenet.db.Event;
import social.evenet.db.Events;
import social.evenet.db.MainAttachment;
import social.evenet.db.Place;
import social.evenet.helper.Connectivity;
import social.evenet.helper.Helper;

/**
 * Created by Alexander on 21.10.2014.
 */
public class PlaceFragmentFeed extends RefreshableFragment {

    private Context mContext;
    private String[] param;
    private TextView place_title;
    private TextView place_address;
    private Button place_event;
    private Intent intent;
    private ArrayList<Events> item;
    private ListView event_place;
    private PlaceEventAdapter adapter;
    private static int count = 0;
    private int place_id;
    private Map<String, String> para;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.place_fragment_place, null);

        place_title = (TextView) v.findViewById(R.id.place_title);
        place_address = (TextView) v.findViewById(R.id.place_address);
        mContext = getActivity();
        place_event = (Button) v.findViewById(R.id.add_lists);
        para = new LinkedHashMap<>();
        param = new String[4];
        param[0] = getActivity().getSharedPreferences("token_info", getActivity().MODE_PRIVATE).getString("token", "");
        para.put("access_token", param[0]);
        place_id = getActivity().getIntent().getIntExtra("place_id", 0);
        param[1] = "" + place_id;
        para.put("place_id", param[1]);
        if (Connectivity.isConnectedFast(getActivity())) {
            param[2] = "50";
        } else {
            param[2] = "20";
        }
        param[3] = "" + count;
        para.put("count", param[2]);
        para.put("offset", param[3]);
        intent = new Intent(getActivity(), PlaceMenuActivity.class);
        event_place = (ListView) v.findViewById(R.id.event_place);
        place_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Map<String, String> params = new LinkedHashMap<>();
                params.put("access_token", param[0]);
                params.put("place_id", param[1]);
                App.getApi().listPlace(params, new Callback<JSONArray>() {
                    @Override
                    public void success(JSONArray jsonArray, Response response) {
                        String res = jsonArray.optJSONObject(0).optString("response_message");
                        if (res.equals("OK")) {
                            Toast.makeText(getActivity(), "Add List", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                });


            }
        });
        App.getApi().placeInfo(para, new Callback<JSONArray>() {
            @Override
            public void success(JSONArray jsonArray, Response response) {
                ArrayList<Event> m = new ArrayList<Event>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    Event event = new Event();
                    Place place = new Place();
                    JSONObject jsonobject = jsonArray.optJSONObject(i);
                    place.setPlace_id(jsonobject.optInt("place_id"));
                    place.setPlace_title(jsonobject.optString("place_title"));
                    place.setPlace_address(jsonobject.optString("place_address"));
                    place.setLatitude(jsonobject.optDouble("latitude"));
                    place.setLongitude(jsonobject.optDouble("longitude"));
                    place.setLang_id(jsonobject.optInt("lang_id"));
                    if (!jsonobject.isNull("category_name")) {
                        place.setPlace_category(jsonobject.optString("category_name"));
                        place.setCategory_id(jsonobject.optInt("category_id"));

                    }
                    event.setPlace(place);
                    place_address.setText(event.getPlace().getPlace_address());
                    place_title.setText(event.getPlace().getPlace_title());
                }

            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
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
                                mainAttachment.setUrl(jsonmain_attachment.getJSONObject("photo_original").getString("url"));
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

                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
                List<Events> list = item;
                adapter = new PlaceEventAdapter(getActivity(), list);
                event_place.setAdapter(adapter);
                Helper.getListViewSize(event_place);
                event_place.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Bundle b = new Bundle();
                        b.putParcelable("events", item.get(position));
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content, FeedDetailFragment.getFeedFragment(b)).commit();
                    }
                });
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });


        return v;
    }


}
