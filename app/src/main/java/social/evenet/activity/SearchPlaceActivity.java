package social.evenet.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

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
import social.evenet.adapter.PlaceListAdapter;
import social.evenet.db.Place;

public class SearchPlaceActivity extends ActionBarActivity {

    private LinearLayout empty;
    private ListView listView;
    private PlaceListAdapter adapter;
    private List<Place> item;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search_place);
        empty = (LinearLayout) findViewById(R.id.empty_search);
        listView = (ListView) findViewById(R.id.list_place);
        context = this;
        Map<String,String> params = new LinkedHashMap<>();
        params.put("access_token", getSharedPreferences("token_info", MODE_PRIVATE).getString("token", ""));
        params.put("count", "200");
        params.put("offset", "0");

        App.getApi().placeRecomended(params,new Callback<JSONArray>() {
            @Override
            public void success(JSONArray jsonArray, Response response) {
                item = new ArrayList<Place>();
                try {
                    jsonArray = jsonArray.getJSONObject(0).getJSONArray("places");
                    if (jsonArray.length() != 0) {

                        for (int i = 0; i < jsonArray.length(); i++) {
                        Place place = new Place();
                        JSONObject jsonPlace = jsonArray.getJSONObject(i);
                        place.setPlace_id(jsonPlace.getInt("place_id"));
                        place.setPlace_title(jsonPlace.getString("place_title"));
                        if (!jsonPlace.isNull("place_description"))
                            place.setPlace_description(jsonPlace.getString("place_description"));
                            if (!jsonPlace.isNull("land_id"))
                                place.setLang_id(jsonPlace.getInt("lang_id"));
                            place.setLatitude(jsonPlace.getDouble("latitude"));
                            place.setLongitude(jsonPlace.getDouble("longitude"));
                            place.setPlace_address(jsonPlace.getString("place_address"));
                            place.setCategory_id(jsonPlace.getInt("category_id"));
                            place.setPlace_category(jsonPlace.getString("category_name"));
                            if (!jsonPlace.isNull("count_place"))
                                place.setPlace_count(jsonPlace.getInt("count_place"));
                            if (!jsonPlace.isNull("friend_count"))
                                place.setFriends_count(jsonPlace.getInt("friend_count"));
                            item.add(place);
                        }
                    }

                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
                adapter = new PlaceListAdapter(context, item);
                listView.setAdapter(adapter);
                if (item.size() > 0) {
                    empty.setVisibility(View.GONE);
                    listView.setVisibility(View.VISIBLE);
                } else {
                    listView.setVisibility(View.GONE);
                    empty.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                listView.setVisibility(View.GONE);
                empty.setVisibility(View.VISIBLE);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(getIntent().getStringExtra("detail")!=null){
                    Intent intent = new Intent(context, MeetupDetailActivity.class);
                    intent.putExtra("data",item.get(position));
                    setResult(Activity.RESULT_OK, intent);
                }
                else {
                    Bundle b = getIntent().getBundleExtra("bundle");
                    b.putParcelable("place", item.get(position));
                    Intent intent = new Intent(context, MeetUpAddActivity.class);
                    intent.putExtra("data", b);
                    setResult(Activity.RESULT_OK, intent);
                }
                finish();
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search_place, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
