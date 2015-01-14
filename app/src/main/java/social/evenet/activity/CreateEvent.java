package social.evenet.activity;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import social.evenet.R;
import social.evenet.fragment.CreateEventFragment;
import social.evenet.fragment.CreateEventSuggest;
import social.evenet.pager.JazzyViewPager;
import social.evenet.pager.OutlineContainer;

public class CreateEvent extends ActionBarActivity {

    private TextView suggest_place;
    private LinearLayout parent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=this;
        setContentView(R.layout.activity_create_event);

        suggest_place=(TextView) findViewById(R.id.suggest_place);
        parent=(LinearLayout) findViewById(R.id.parent_view);
        suggest_place.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            parent.setVisibility(View.GONE);
            getSupportFragmentManager().beginTransaction().replace(R.id.content,new CreateEventSuggest()).commit();
            }
        });

        setupJazziness(JazzyViewPager.TransitionEffect.ZoomOut);
    }



    private class MainAdapter extends PagerAdapter {
        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            LayoutInflater li = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v=   li.inflate(R.layout.create_item, container, false);

            container.addView(v, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            pager.setObjectForPosition(v, position);
            return v;
        }

        @Override public float getPageWidth(int position) { return(0.35f); }


        @Override
        public void destroyItem(ViewGroup container, int position, Object obj) {
            container.removeView(pager.findViewFromObject(position));
        }
        @Override
        public int getCount() {
            return 200;
        }
        @Override
        public boolean isViewFromObject(View view, Object obj) {
            if (view instanceof OutlineContainer) {
                return ((OutlineContainer) view).getChildAt(0) == obj;
            } else {
                return view == obj;
            }
        }
    }





    private Context mContext;
    private JazzyViewPager pager;


    private void setupJazziness(JazzyViewPager.TransitionEffect effect) {
        pager = (JazzyViewPager) findViewById(R.id.jazzy_pager);
        pager.setTransitionEffect(effect);
        pager.setAdapter(new MainAdapter());
        pager.setPageMargin(30);

       /* pager.setPageMargin(-50);
        pager.setHorizontalFadingEdgeEnabled(true);
        pager.setFadingEdgeLength(30);*/
    }

}




