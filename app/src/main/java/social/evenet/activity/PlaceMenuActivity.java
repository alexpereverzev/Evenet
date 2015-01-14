package social.evenet.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.parse.ParseAnalytics;

import java.util.ArrayList;
import java.util.List;

import social.evenet.BL.FeedBL;
import social.evenet.R;
import social.evenet.adapter.DrawerItemCustomAdapter;
import social.evenet.adapter.FeedAdapterNew;
import social.evenet.db.Events;
import social.evenet.fragment.CreateEventFragment;
import social.evenet.fragment.FeedFragmentNew;
import social.evenet.fragment.MeetingFragment;
import social.evenet.fragment.NotificationFragment;
import social.evenet.fragment.PlaceFragment;
import social.evenet.fragment.ProfileFragment;
import social.evenet.helper.ObjectDrawerItem;

public class PlaceMenuActivity extends BaseActivity implements
    SearchView.OnQueryTextListener {

        private SearchView searchView;
        private MenuItem searchItem;
        public static FragmentManager fr;

        private String[] mNavigationDrawerItemTitles;
        private DrawerLayout mDrawerLayout;
        private ListView mDrawerList;
        private ActionBarDrawerToggle mDrawerToggle;
        private    String [] para;
        private CharSequence mDrawerTitle;
        private CharSequence mTitle;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            setContentView(R.layout.actv_menu);
            ParseAnalytics.trackAppOpened(getIntent());
            mTitle = mDrawerTitle = getTitle();
            mNavigationDrawerItemTitles = getResources().getStringArray(R.array.navigation_drawer_items_array);
            mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
            mDrawerList = (ListView) findViewById(R.id.left_drawer);


            ObjectDrawerItem[] drawerItem = new ObjectDrawerItem[5];
            String[] ar=getResources().getStringArray(R.array.navigation_drawer_items_array);

            drawerItem[0] = new ObjectDrawerItem(R.drawable.info, ar[0]);
            drawerItem[1] = new ObjectDrawerItem(R.drawable.intro, ar[1]);
            drawerItem[2] = new ObjectDrawerItem(R.drawable.quiz, ar[2]);
            drawerItem[3] = new ObjectDrawerItem(R.drawable.cases, ar[3]);
            drawerItem[4] = new ObjectDrawerItem(R.drawable.cases, ar[4]);

            DrawerItemCustomAdapter adapter = new DrawerItemCustomAdapter(this, R.layout.listview_row_item, drawerItem);

            LayoutInflater inflaterHeader = getLayoutInflater();
            ViewGroup header = (ViewGroup) inflaterHeader.inflate(R.layout.header_drawer, null);

            mDrawerList.addHeaderView(header);
            header.setOnClickListener(null);

            mDrawerList.setAdapter(adapter);

            mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

            mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
            mDrawerToggle = new ActionBarDrawerToggle(
                    this,
                    mDrawerLayout,
                    R.drawable.drawer,
                    R.string.drawer_open,
                    R.string.drawer_close
            ) {

                /** Called when a drawer has settled in a completely closed state. */
                public void onDrawerClosed(View view) {
                    super.onDrawerClosed(view);
                    getSupportActionBar().setTitle(mTitle);
                }

                /** Called when a drawer has settled in a completely open state. */
                public void onDrawerOpened(View drawerView) {
                    super.onDrawerOpened(drawerView);
                    getSupportActionBar().setTitle(mDrawerTitle);
                }
            };

            mDrawerLayout.setDrawerListener(mDrawerToggle);

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

           // fr = getSupportFragmentManager();


           para=getIntent().getStringArrayExtra("param");
            if(para!=null){
                PlaceFragment placeFragment=new PlaceFragment();
                Bundle b=new Bundle();
                b.putStringArray("param",para);
                placeFragment.setArguments(b);
                BaseActivity.fr.beginTransaction().replace(R.id.content_frame, placeFragment).commit();

            }
            else if(getIntent().getStringArrayExtra("scheldure")!=null) {
                PlaceFragment placeFragment=new PlaceFragment();
                Bundle b=new Bundle();
                para=getIntent().getStringArrayExtra("scheldure");
                b.putStringArray("scheldure",getIntent().getStringArrayExtra("scheldure"));
                placeFragment.setArguments(b);
                BaseActivity.fr.beginTransaction().replace(R.id.content_frame, placeFragment).commit();
            }
          //  fr.beginTransaction().replace(R.id.content_frame, FeedFragmentNew.getFeedFragment(para), "feeds").commit();

        }



        @Override
        protected void onPostCreate(Bundle savedInstanceState) {
            super.onPostCreate(savedInstanceState);
            mDrawerToggle.syncState();
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {

            if (mDrawerToggle.onOptionsItemSelected(item)) {
                return true;
            }

            return super.onOptionsItemSelected(item);
        }

        @Override
        public void setTitle(CharSequence title) {
            mTitle = title;
            getSupportActionBar().setTitle(mTitle);
        }

        @Override
        public void onBackPressed() {

            SharedPreferences pref = getSharedPreferences("token_info", MODE_PRIVATE);
            boolean flag = pref.getBoolean("event_near", false);


            if (flag) {
                PlaceFragment placeFragment=new PlaceFragment();
                Bundle b=new Bundle();
              // para=getIntent().getStringArrayExtra("scheldure");
                if(getIntent().getStringArrayExtra("param")==null)
                b.putStringArray("scheldure",para);
                else {
                    b.putStringArray("param",para);

                }
                placeFragment.setArguments(b);
                BaseActivity.fr.beginTransaction().replace(R.id.content_frame, placeFragment).commit();

            } else super.onBackPressed();


        }

        public static Menu menu1;

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            menu1=menu;
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.feed, menu);
            menu.getItem(0).setVisible(true);
            menu.getItem(1).setVisible(false);
            searchItem = menu.findItem(R.id.action_search);
            searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

            searchView.setOnQueryTextListener(this);

            return super.onCreateOptionsMenu(menu);
        }

        @Override
        public boolean onQueryTextSubmit(String s) {
            return false;
        }

        @Override
        public boolean onQueryTextChange(String s) {
            FeedBL bl=new FeedBL(this);
            List<Events> li=  bl.searchItemEvents(s);
            MenuItemCompat.collapseActionView(searchItem);
            ArrayList<ArrayList<Events>> groups = new ArrayList<ArrayList<Events>>();

            for(int i=0; i<li.size(); i++){
                ArrayList<Events> list1=new ArrayList<Events>();
                list1.add(li.get(i));
                groups.add(list1);
            }

            //  FeedFragmentNew.adapter=new ExpListAdapter(this, groups);
            FeedFragmentNew.adapter=new FeedAdapterNew(this, li);
           // FeedFragmentNew.listView.setAdapter(FeedFragmentNew.adapter);
            // listView.setAdapter(adapter);
            return false;
        }


        private class DrawerItemClickListener implements ListView.OnItemClickListener {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItem(position);
            }

        }

    private void selectItem(int position) {
        Intent intent=new Intent(this,MenuNewActivity.class);
        Fragment fragment = null;
        //  position=position-1;
        switch (position-1) {
            case 0:
               // fragment = new FeedsFragment();  menu1.getItem(0).setVisible(true);
                startActivity(intent);
                break;
            case 1:
                fragment = new MeetingFragment(); menu1.getItem(0).setVisible(false); menu1.getItem(1).setVisible(true);
                break;
            case 2:
                fragment = new NotificationFragment(); menu1.getItem(0).setVisible(false);
                break;
            case 3:
                fragment = new ProfileFragment(); menu1.getItem(0).setVisible(false);
                break;
            case 4:
                fragment = new CreateEventFragment(); menu1.getItem(0).setVisible(false);
                break;

            default:
                fragment=new ProfileFragment();
                break;
        }

        if (fragment != null) {

            BaseActivity.fr.beginTransaction().replace(R.id.content_frame, fragment).commit();

            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            setTitle(mNavigationDrawerItemTitles[position-1]);
            mDrawerLayout.closeDrawer(mDrawerList);

        }
    }



}
