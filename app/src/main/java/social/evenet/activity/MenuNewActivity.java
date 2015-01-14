package social.evenet.activity;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import android.widget.FrameLayout;
import android.widget.ListView;

import com.facebook.Session;

import java.util.ArrayList;
import java.util.HashMap;
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
import social.evenet.fragment.ProfileFragment;
import social.evenet.helper.ObjectDrawerItem;


/**
 * Created by appus_dd6n on 7/14/14.
 */
public class MenuNewActivity extends BaseActivity  implements
        SearchView.OnQueryTextListener {

    private SearchView searchView;
    private MenuItem searchItem;
    public static FragmentManager fr;

    private String[] mNavigationDrawerItemTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private HashMap<Integer,Fragment> array=new HashMap<Integer, Fragment>();

    private  ViewGroup decorViewGroup;
    private View logoView;

    static boolean profile_choose=false;

    private View feed;

    private FrameLayout def;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.actv_menu);
      //  ParseAnalytics.trackAppOpened(getIntent());
        mTitle = mDrawerTitle = getTitle();
        mNavigationDrawerItemTitles = getResources().getStringArray(R.array.navigation_drawer_items_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);



        ObjectDrawerItem[] drawerItem = new ObjectDrawerItem[5];
        String[] ar=getResources().getStringArray(R.array.navigation_drawer_items_array);

        drawerItem[0] = new ObjectDrawerItem(R.drawable.info, ar[0]);
        drawerItem[1] = new ObjectDrawerItem(R.drawable.ic_icon_meetups_default, ar[1]);
        drawerItem[2] = new ObjectDrawerItem(R.drawable.quiz, ar[2]);
        drawerItem[3] = new ObjectDrawerItem(R.drawable.ic_icon_profile_default, ar[3]);
        drawerItem[4] = new ObjectDrawerItem(R.drawable.cases, ar[4]);

        DrawerItemCustomAdapter adapter = new DrawerItemCustomAdapter(this, R.layout.listview_row_item, drawerItem);

        LayoutInflater inflaterHeader = getLayoutInflater();
        ViewGroup header = (ViewGroup) inflaterHeader.inflate(R.layout.header_drawer, null);

        mDrawerList.addHeaderView(header);
        header.setOnClickListener(null);

        mDrawerList.setAdapter(adapter);

        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        LayoutInflater infla_logo = LayoutInflater.from(this);
        logoView = infla_logo.inflate(R.layout.logo_view, null, false);

        decorViewGroup = (ViewGroup) getWindow().getDecorView();


        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                R.drawable.ic_navigation_drawer,
                R.string.drawer_open,
                R.string.drawer_close
        ) {


            public void onDrawerClosed(View view) {
                if(profile_choose){
                    if(fl)
                     ProfileFragment.decorViewGroup.addView(ProfileFragment.logoView);
                        fl=false;
                }
                super.onDrawerClosed(view);

                getSupportActionBar().setTitle(mTitle);

            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                if(profile_choose){
                    if(!ProfileFragment.delete){
                      ProfileFragment.decorViewGroup.removeView(ProfileFragment.logoView);
                    fl=true;}
                }
                super.onDrawerOpened(drawerView);


                getSupportActionBar().setTitle(mDrawerTitle);
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tag="feed";
        FragmentManager fm = getSupportFragmentManager();

        feed=(View) findViewById(R.id.feeds);

        def=(FrameLayout) findViewById(R.id.content_frame);

    }


   private boolean fl=false;

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
            SharedPreferences.Editor prefEditor = pref.edit();
            prefEditor.putBoolean("event_near", false);
            prefEditor.commit();
           hide();
            showFragment(0,null);
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

        FeedFragmentNew.adapter=new FeedAdapterNew(this, li);
        FeedFragmentNew.adapter.notifyDataSetChanged();

        return false;
    }

    private String tag="";

    private class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }

    }

    private void selectItem(int position) {
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentByTag(tag);


        switch (position-1) {
            case 0:

                //fragment = new FeedsFragment();
                menu1.getItem(0).setVisible(true); profile_choose=false;
                tag="feed";
                showFragment(0,fragment);
                break;
            case 1:
               fragment = new MeetingFragment();
                menu1.getItem(0).setVisible(false); menu1.getItem(1).setVisible(true); profile_choose=false;
                showFragment(1,fragment);
                tag="meet";
                break;
            case 2:

                fragment = new NotificationFragment();
                showFragment(2,fragment);
                menu1.getItem(0).setVisible(false);profile_choose=false;
                tag="noti";
                break;
            case 3:
                 fragment = new ProfileFragment();
                showFragment(3,fragment);

                menu1.getItem(0).setVisible(false); profile_choose=true;
                tag="prof";
                break;
            case 4:
                Intent i=new Intent(this, CreateEvent.class);
                startActivity(i);
                /*fragment = new CreateEventFragment();
                showFragment(4,fragment);

                menu1.getItem(0).setVisible(false); profile_choose=false;
                tag="creat";*/
                break;

            default:
                fragment=new ProfileFragment();
                break;
        }


            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            setTitle(mNavigationDrawerItemTitles[position-1]);
            mDrawerLayout.closeDrawer(mDrawerList);

        }

    public void showFragment(int i, Fragment fragment){
        hide();
        switch (i){
            case 0:  feed.setVisibility(View.VISIBLE); break;
            case 1: def.setVisibility(View.VISIBLE); getSupportFragmentManager().beginTransaction().replace(R.id.content_frame,fragment).commit(); break;
            case 2: def.setVisibility(View.VISIBLE); getSupportFragmentManager().beginTransaction().replace(R.id.content_frame,fragment).commit(); break;
            case 3: def.setVisibility(View.VISIBLE); getSupportFragmentManager().beginTransaction().replace(R.id.content_frame,fragment).commit(); break;
            case 4: def.setVisibility(View.VISIBLE); getSupportFragmentManager().beginTransaction().replace(R.id.content_frame,fragment).commit(); break;
        }

    }

    public void hide(){
        feed.setVisibility(View.GONE);
    //    profile.setVisibility(View.GONE);
    //    meetup.setVisibility(View.GONE);
   //     notification.setVisibility(View.GONE);
        def.setVisibility(View.GONE);
    }

    private void showFragment(int fragmentIndex, boolean addToBackStack) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        for (int i = 0; i < array.size(); i++) {
            if (i == fragmentIndex) {
                transaction.show(array.get(i));
                if (Session.getActiveSession().isClosed()) {
                    mDrawerLayout
                            .setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                }
            } else {
                transaction.hide(array.get(i));
            }
        }
        if (addToBackStack) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }

    }




