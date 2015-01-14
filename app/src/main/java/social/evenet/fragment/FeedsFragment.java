package social.evenet.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.viewpagerindicator.CirclePageIndicator;

import social.evenet.R;


public class FeedsFragment extends Fragment {

    private CirclePageIndicator viewpager;

    private ViewPager mPager;
    private CirclePageIndicator mIndicator;
    private ProductPagerAdapter adapter;
    private FeedFragmentNew feedFragmentNew;
    private FeedNewFragment feedFragmentNear;
    private ListFeedEventFragment listFeedEventFragment;

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
        View view = inflater.inflate(R.layout.fragment_feeds, container, false);

        if (savedInstanceState != null) {

            System.out.print("");

        }

        mPager = (ViewPager) view.findViewById(R.id.pager);
        if (mPager.getAdapter() != null) {
            mPager.setAdapter(null);
            adapter = null;
        }

        adapter = new ProductPagerAdapter(getActivity().getSupportFragmentManager());
        mPager.setAdapter(adapter);



        mIndicator = (CirclePageIndicator) view.findViewById(R.id.indicator);
        mIndicator.setViewPager(mPager);

        adapter.getRegisteredFragment(mPager.getCurrentItem());

        return view;
    }


    private class ProductPagerAdapter extends FragmentStatePagerAdapter {

        SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();

        public SparseArray<Fragment> getRegisteredFragments() {
            return registeredFragments;
        }

        private FragmentManager fm1;


        public ProductPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
            fm1 = fragmentManager;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Fragment fragment = (Fragment) super.instantiateItem(container, position);
            if(position==1){

            }
            registeredFragments.put(position, fragment);
            return fragment;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            registeredFragments.remove(position);
            super.destroyItem(container, position, object);
        }

        public Fragment getRegisteredFragment(int position) {
            return registeredFragments.get(position);
        }


        @Override
        public Fragment getItem(int position) {

            switch (position) {

                case 0:
                    return  new FeedNewFragment();

                case 1:
                    return  FeedFragmentNew.getFeedFragment(1);

                case 2:
                    return new ListFeedEventFragment();

            }
            return new FeedNewFragment();
        }


        @Override
        public CharSequence getPageTitle(int position) {
            return "";
        }

        @Override
        public int getCount() {

            return 3;
        }


    }

    @Override
    public void onDestroy() {
        adapter = null;
        super.onDestroy();
    }
}
