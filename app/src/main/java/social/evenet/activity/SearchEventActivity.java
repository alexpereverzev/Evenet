package social.evenet.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.parse.ParseAnalytics;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import social.evenet.BL.FeedBL;
import social.evenet.R;
import social.evenet.adapter.EventAdapter;
import social.evenet.db.Event;

public class SearchEventActivity extends HashActivity  implements
        SearchView.OnQueryTextListener{

    private Handler mhandler = new Handler();
    private List<Event> list;
      private FeedBL feedBL;
    private EventAdapter adapter;
    private ListView listView;
    private SearchView searchView;
    private MenuItem searchItem;
    private FeedBL bl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_event);
        bl=new FeedBL(this);

        Map<String, String> dimensions = new HashMap<String, String>();
        dimensions.put("page_name ", "Search Events");
        ParseAnalytics.trackEvent("Page View", dimensions);


        feedBL = new FeedBL(this);
        list = feedBL.getEvent();
        adapter=new EventAdapter(this,list);
        listView = (ListView) findViewById(R.id.event_list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent resultData = new Intent();
                resultData.putExtra("item", list.get(i));
                setResult(Activity.RESULT_OK, resultData);
                finish();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search_event, menu);


        searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onQueryTextSubmit(String s) {

        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        List<Event> li=  bl.searchItem(s);
        MenuItemCompat.collapseActionView(searchItem);
        adapter=new EventAdapter(this, li);
        listView.setAdapter(adapter);
        return false;
    }



}
