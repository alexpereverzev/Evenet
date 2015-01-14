package social.evenet.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.parse.ParseAnalytics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import social.evenet.R;
import social.evenet.adapter.ListAdapter;
import social.evenet.db.DefaultList;

public class AddListActivity extends HashActivity {


    private ListView listView;
    private ListAdapter adapter;
    private ArrayList<DefaultList> item = new ArrayList<DefaultList>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Map<String, String> dimensions = new HashMap<String, String>();
        dimensions.put("page_name ", "add_lists");
        ParseAnalytics.trackEvent("Page View", dimensions);

        setContentView(R.layout.activity_add_list);
        listView = (ListView) findViewById(R.id.list_lis);
        String[] array = getResources().getStringArray(R.array.list_default);
        for (int i = 0; i < array.length; i++) {
            DefaultList d = new DefaultList();
            d.setName(array[i]);
            item.add(d);
        }


        DefaultList custom = new DefaultList();
        custom.setName("Custom");
        item.add(custom);
        adapter = new ListAdapter(this, item);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i1, long l) {

                Map<String, String> options = new LinkedHashMap<String, String>();
                options.put("access_token", getSharedPreferences("token_info", MODE_PRIVATE).getString("token", ""));
                options.put("only_in_my_areas", "0");
                options.put("name", item.get(i1).getName());
                App.getApi().listAdd(options);


            }
        });

    }


}
