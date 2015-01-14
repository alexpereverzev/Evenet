package social.evenet.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;

import social.evenet.R;
import social.evenet.fragment.LisFragment;

public class ListPagerActivity extends ActionBarActivity {


    public static boolean flag=false;
    public static FragmentManager fr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_pager);

        fr=getSupportFragmentManager();
        fr.beginTransaction().replace(R.id.content,new LisFragment()).commit();



    }



}
