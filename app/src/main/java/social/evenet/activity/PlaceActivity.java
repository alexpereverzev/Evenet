package social.evenet.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import java.util.HashMap;
import java.util.Map;

import social.evenet.R;
import social.evenet.fragment.PlaceFragmentFeed;

public class PlaceActivity extends HashActivity {

    private Context mContext;

    public  FragmentManager fr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place);
        fr=getSupportFragmentManager();
        Map<String, String> dimensions = new HashMap<String, String>();
        dimensions.put("page_name ", "place info");

        mContext=this;
            fr.beginTransaction().replace(R.id.content,new PlaceFragmentFeed()).commit();

    }






}
