package social.evenet.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import social.evenet.R;
import social.evenet.helper.CustomTabHost;


/**
 * Created by appus_dd6n on 7/14/14.
 */
public class MenuFragment extends Fragment {

    private CustomTabHost mTabHost;

    public static final String INTRO_ID = "feed";
    public static final String CASES_ID = "list";
    public static final String QUIZ_ID = "meeting";
    public static final String ABOUT_ID = "notification";
    public static final String MORE_ID = "profile";




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.main_menu_fragment, container, false);


        // View view = inflater.inflate(R.layout.main_tab_host, container, false);
       /* CustomTabHost tabHost = (CustomTabHost) view.findViewById(android.R.id.tabhost);
        tabHost.setSaveEnabled(true);
        tabHost.setSaveFromParentEnabled(true);
        tabHost.setup(getActivity(), getChildFragmentManager(), R.id.realtabcontent);
        tabHost.getTabWidget().setStripEnabled(false);

       //
        createTabsView(tabHost);

        mTabHost = tabHost;*/

        return view;
    }

    private void createTabsView(CustomTabHost tabHost) {
        View tab = createTabView(R.string.mnu_feed, R.drawable.intro);
        tabHost.addTab(tabHost.newTabSpec(INTRO_ID).setIndicator(tab), FeedsFragment.class, null);
        tab = createTabView(R.string.mnu_list, R.drawable.cases);
        tabHost.addTab(tabHost.newTabSpec(CASES_ID).setIndicator(tab), LisFragment.class, null);
        tab = createTabView(R.string.mnu_notification, R.drawable.info);
        tabHost.addTab(tabHost.newTabSpec(QUIZ_ID).setIndicator(tab), MeetingFragment.class, null);
        tab = createTabView(R.string.mnu_meeting, R.drawable.quiz);
        tabHost.addTab(tabHost.newTabSpec(ABOUT_ID).setIndicator(tab), NotificationFragment.class, null);
        tab = createTabView(R.string.mnu_profile, R.drawable.more);
        tabHost.addTab(tabHost.newTabSpec(MORE_ID).setIndicator(tab), ProfileFragment.class, null);

        tabHost.setCurrentTab(0);
    }

    private View createTabView(int title, int drawable) {
        View tab = View.inflate(getActivity(), R.layout.tab, null);
        ((TextView) tab.findViewById(R.id.tab_text)).setText(title);
        ((ImageView) tab.findViewById(R.id.tab_icon)).setImageResource(drawable);
        return tab;
    }

    public String getCurrentTabTag(){

        return mTabHost.getCurrentTabTag();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mTabHost = null;
    }


    protected void changeDefaultActionbar(int titleRes, int imageRes, String booth) {

        /*
        View v = getLayoutInflater().inflate(R.layout.actionbar_custom, null);
        title = (TextView) v.findViewById(R.id.title);
        back=(Button) v.findViewById(R.id.action_bar_back);






        title.setText(getResources().getString(titleRes));
        ActionBar actionBar = (ActionBar)getActivity().getActionBar();
        actionBar.setDisplayShowCustomEnabled(true);


        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);*/


    }



}
