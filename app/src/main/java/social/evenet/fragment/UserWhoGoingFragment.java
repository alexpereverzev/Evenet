package social.evenet.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;

import social.evenet.R;
import social.evenet.db.UserInfo;


public class UserWhoGoingFragment extends Fragment {


    private ViewPager mPager;
    private TabPageIndicator mIndicator;
    private UserPagerAdapter adapter;
    private ArrayList<UserInfo> going;
    private ArrayList<UserInfo> going_not;
    private ArrayList<UserInfo> going_not_know;

    public UserWhoGoingFragment(){

    }

    public static UserWhoGoingFragment getFragment(ArrayList<UserInfo> listInfo,ArrayList<UserInfo> listInfo1,ArrayList<UserInfo> listInfo2) {
        Bundle bundle = new Bundle(3);


            bundle.putParcelableArrayList("item_going", listInfo);

              bundle.putParcelableArrayList("item_not", listInfo1);
            
            bundle.putParcelableArrayList("item_not_know", listInfo2);





        UserWhoGoingFragment f = new UserWhoGoingFragment();
        f.setArguments(bundle);
        return f;

    }

    private static final String[] titles = new String[] {
            "Not Going", "Going", "Not know"};



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_whos_going, container, false);

        going=getArguments().getParcelableArrayList("item_going");
        going_not=getArguments().getParcelableArrayList("item_not");
        going_not_know=getArguments().getParcelableArrayList("item_not_know");

        mPager = (ViewPager) view.findViewById(R.id.pager);
        if (mPager.getAdapter() != null){
            mPager.setAdapter(null);
            adapter=null;
        }

        adapter = new UserPagerAdapter(getActivity().getSupportFragmentManager());
        mPager.setAdapter(adapter);


        mPager.setCurrentItem(1);
        mIndicator = (TabPageIndicator) view.findViewById(R.id.indicator);


        mIndicator.setViewPager(mPager);


        //setActionBar("Feed Info");


        return view;
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
