package social.evenet.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
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
import social.evenet.activity.PlaceActivity;
import social.evenet.adapter.PlaceListAdapter;
import social.evenet.db.Place;


public class ListPlaceFragment extends RefreshableFragment {


    private String [] mParam;
    private String name_list;
    private ListView listView;
    private PlaceListAdapter adapter;
    private List<Place> item;

    public static ListPlaceFragment newInstance(Bundle bundle) {
        ListPlaceFragment fragment = new ListPlaceFragment();

        fragment.setArguments(bundle);
        return fragment;
    }
    public ListPlaceFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam = getArguments().getStringArray("param");
            name_list = getArguments().getString("name");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_list_place, container, false);
        listView=(ListView) v.findViewById(R.id.list_place);
        if (getArguments() != null) {
            mParam = getArguments().getStringArray("param");
            name_list = getArguments().getString("name");
        }

       if(name_list.equals("All Lists")){

            Map<String ,String> options=new LinkedHashMap<>();
            options.put("access_token",mParam[0]);
            options.put("count","200");
            options.put("offset",mParam[2]);
           App.getApi().placeRecomended(options,callback);


        }
       else if(name_list!=null) {



           Map<String, String> options = new LinkedHashMap<>();
           options.put("access_token", mParam[0]);
           options.put("count", "200");
           options.put("place_id", mParam[3]);
           options.put("offset", mParam[2]);
           App.getApi().placeRecomended(options, callback);

       }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(getActivity(), PlaceActivity.class);
                intent.putExtra("place_id",item.get(position).getPlace_id());
                startActivity(intent);
            }
        });
        return v;
    }

   private Callback<JSONArray> callback=new Callback<JSONArray>() {
       @Override
       public void success(JSONArray jsonArray, Response response) {
           item=new ArrayList<Place>();
           JSONArray json=jsonArray.optJSONObject(0).optJSONArray("places");
           if(json.length()!=0){
               for (int i = 0; i < json.length(); i++) {
                   Place place=new Place();
                   JSONObject jsonPlace = json.optJSONObject(i);
                   place.setPlace_id(jsonPlace.optInt("place_id"));
                   place.setPlace_title(jsonPlace.optString("place_title"));
                   if(!jsonPlace.isNull("place_description"))
                   place.setPlace_description(jsonPlace.optString("place_description"));
                   if(!jsonPlace.isNull("land_id"))
                   place.setLang_id(jsonPlace.optInt("lang_id"));
                   place.setLatitude(jsonPlace.optDouble("latitude"));
                   place.setLongitude(jsonPlace.optDouble("longitude"));
                   place.setPlace_address(jsonPlace.optString("place_address"));
                   place.setCategory_id(jsonPlace.optInt("category_id"));
                   place.setPlace_category(jsonPlace.optString("category_name"));
                   if(!jsonPlace.isNull("count_place"))
                       place.setPlace_count(jsonPlace.optInt("count_place"));
                   if(!jsonPlace.isNull("friend_count"))
                       place.setFriends_count(jsonPlace.optInt("friend_count"));
                   item.add(place);
               }
           }
           adapter = new PlaceListAdapter(getActivity(), item);
           listView.setAdapter(adapter);


       }

       @Override
       public void failure(RetrofitError error) {

       }
   };



}
