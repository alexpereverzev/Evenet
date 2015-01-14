package social.evenet.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;


import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;

import social.evenet.R;
import social.evenet.db.UserInfo;


import social.evenet.fragment.UserInfoFragment;
import social.evenet.fragment.UserWhoGoingFragment;

public class WhosGoingMeetupActivity extends ActionBarActivity {

    private ViewPager mPager;
    private TabPageIndicator mIndicator;
    private UserPagerAdapter adapter;
    private ArrayList<UserInfo> going;
    private ArrayList<UserInfo> going_not;
    private ArrayList<UserInfo> going_not_know;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whos_going_meetup);
        going=getIntent().getParcelableArrayListExtra("item_going");
        going_not=getIntent().getParcelableArrayListExtra("item_not");
        going_not_know=getIntent().getParcelableArrayListExtra("item_not_know");


        getSupportFragmentManager().beginTransaction().replace(R.id.content_users, UserWhoGoingFragment.getFragment(going,going_not,going_not_know)).commit();
       /* mPager = (ViewPager) findViewById(R.id.pager);
        if (mPager.getAdapter() != null) {
            mPager.setAdapter(null);
            adapter = null;
        }

        adapter = new UserPagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(adapter);


        mPager.setCurrentItem(1);
        mIndicator = (TabPageIndicator) findViewById(R.id.indicator);


        mIndicator.setViewPager(mPager);*/

    }

    private class UserPagerAdapter extends FragmentStatePagerAdapter {


        private FragmentManager fm1;


        public UserPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
            fm1 = fragmentManager;
        }


        @Override
        public Fragment getItem(int position) {

            switch (position) {

                case 0:
                    return UserInfoFragment.getFragment(5,going);

                case 1:
                    return UserInfoFragment.getFragment(5,going_not_know);

                case 2:
                    return UserInfoFragment.getFragment(5,going_not);

                //   default: return FeedFragment.getFeedFragment(1);

                // case 3:  return new ProductFragment(list_item.get(position), booth_value[position], context_activity);

            }
            return UserInfoFragment.getFragment(5,going);
        }

        @Override
        public CharSequence getPageTitle(int position) {

            switch (position) {
                case 0:
                    return "Going";

                case 1:
                    return "Not know";
                case 2:
                    return "Not Going";
            }

            return "";
        }

        @Override
        public int getCount() {
            // return list_item.size();
            return 3;
        }


    }

    @Override
    public void onDestroy() {
        adapter = null;
        super.onDestroy();
    }


}
