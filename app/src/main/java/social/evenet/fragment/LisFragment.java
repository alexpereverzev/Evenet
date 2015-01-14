package social.evenet.fragment;

import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.parse.ParseAnalytics;

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
import social.evenet.activity.ListPagerActivity;
import social.evenet.adapter.GetListAdapter;
import social.evenet.db.Lists;

public class LisFragment extends RefreshableFragment {
    private Handler mhandler = new Handler();
    private ListView current_list;
    private GetListAdapter adapter;
    private String[] param;
    private ArrayList<Lists> item;
    public static Bundle b=new Bundle();


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         Map<String, String> dimensions = new HashMap<String, String>();
         dimensions.put("page_name ", "lists");
         ParseAnalytics.trackEvent("Page View", dimensions);
         View v = inflater.inflate(R.layout.fragment_list, container, false);
         current_list = (ListView) v.findViewById(R.id.current_list);
         param = new String[4];
         param[0] = getActivity().getSharedPreferences("token_info", getActivity().MODE_PRIVATE).getString("token", "");
         param[1] = "" + 15;
         param[2] = "" + 0;
        Map<String ,String>  options=new LinkedHashMap<>();
        options.put("access_token",param[0]);
        options.put("count",param[1]);
        options.put("offset",param[2]);
        App.getApi().userList(options,new Callback<JSONArray>() {
            @Override
            public void success(JSONArray jsonArray, Response response) {
                ArrayList<Lists> massive=new ArrayList<Lists>();
               JSONArray json = jsonArray;
                for (int i = 0; i < json.length(); i++) {
                    JSONObject jsonobject = jsonArray.optJSONObject(i);
                    Lists lists_info=new Lists();
                    lists_info.setList_id(jsonobject.optInt("list_id"));
                    lists_info.setName(jsonobject.optString("name"));
                    lists_info.setUrl(jsonobject.optString("image"));
                    massive.add(lists_info);
                }
                item = massive;
                Lists l=new Lists();
                l.setList_id(115);
                l.setName("115");
                item.add(l);
                adapter = new GetListAdapter(getActivity(), item);
                current_list.setAdapter(adapter);
            }

            @Override
            public void failure(RetrofitError error) {
            }
        });


        current_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                param[1]=""+50;
                param[3]=""+item.get(i).getList_id();
                b.putStringArray("param",param);
              String [] names=  getResources().getStringArray(R.array.list_default);
                b.putString("name",names[i]);
                ListPagerActivity activity=(ListPagerActivity)getActivity();
                  activity.getSupportFragmentManager().beginTransaction().replace(R.id.content,ListPlaceFragment.newInstance(b)).commit();
            }
        });

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
