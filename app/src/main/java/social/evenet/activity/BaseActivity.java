package social.evenet.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import social.evenet.R;
import social.evenet.fragment.LisFragment;
import social.evenet.fragment.MeetingFragment;
import social.evenet.fragment.NotificationFragment;
import social.evenet.fragment.ProfileFragment;


/**
 * @author uncle_doc
 * @date 20.02.2012
 */
public abstract class BaseActivity extends ActionBarActivity {

    private static Menu mMenu;
    public static final int MNU_Feed = 0;
    public static final int MNU_List = 1;
    public static final int MNU_Notification = 2;
    public static final int MNU_Meeting = 3;
    public static final int MNU_Profile = 4;

    public static FragmentManager fr;

    private boolean flag=true;

    private String TAG="TEST";

    Context mCoxte;


    private Handler mHandler = new Handler();

    private TextView empty;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fr=getSupportFragmentManager();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        mMenu=MenuNewActivity.menu1;
        mCoxte=this;
      //  clearMenuIcon();
            Intent intent = null;
        int a =item.getItemId();
            switch (item.getItemId()) {
                case R.id.admeet_up:
                    //intent = new Intent(this, FeedActivity.class);
                   // mMenu.getItem(1).setVisible(true);
                 //   mMenu.getItem(0).setVisible(false);

                 //   mMenu.getItem(1).setIcon(R.drawable.ic_tab_bar_intro_pressed);
                   intent=new Intent(this, MeetUpAddActivity.class);
                    startActivity(intent);
               //     showFragment(new FeedsFragment());

                    break;
                case R.id.list_feed:

                    intent=new Intent(this, ListPagerActivity.class);
                    startActivity(intent);


                    break;

                case MNU_List:
                   // intent = new Intent(this, ListActivity.class);
                    mMenu.getItem(1).setIcon(R.drawable.ic_tab_bar_library_pressed);
                    showFragment(new LisFragment());
                    break;
                case MNU_Meeting:
                 /*   intent = new Intent(this, MeetingActivity.class);
                 */
                        mMenu.getItem(2).setIcon(R.drawable.ic_tab_bar_quiz_pressed);
                    showFragment(new MeetingFragment());
                 /*   getSupportFragmentManager().beginTransaction()
                            .replace(R.id.recipeList, new TestFragment(),TAG).commit();
                 */   break;
                case MNU_Notification:
                   // intent = new Intent(this, NotificationActivity.class);
                    mMenu.getItem(3).setIcon(R.drawable.ic_tab_bar_info_pressed);
                    showFragment(new NotificationFragment());
                    break;
                case MNU_Profile:
                    mMenu.getItem(4).setIcon(R.drawable.ic_menu_more_active);
                   showFragment(new ProfileFragment());
                  //  intent = new Intent(this, ProfileActivity.class);
                   // intent.putExtra("test", "test");
                    break;

            }
      //  startActivity(intent);

        return super.onOptionsItemSelected(item);
    }

    public boolean isLarge() {
        return (getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }



    /*public void setUpgradeButton(int title) {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        View customBar = View.inflate(getApplicationContext(), R.layout.custom_actionbar, null);
        actionBar.setCustomView(customBar);
        TextView textTitle = (TextView) findViewById(R.id.txtTitle);
        textTitle.setText(title);


    }*/




    public void showFragment(Fragment s) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.recipeList, s).commit();

    }



    public void clearMenuIcon() {
       mMenu.getItem(0).setIcon(R.drawable.ic_launcher);

        mMenu.getItem(1).setIcon(R.drawable.ic_launcher);

        mMenu.getItem(2).setIcon(R.drawable.ic_launcher);

        mMenu.getItem(3).setIcon(R.drawable.ic_launcher);


        mMenu.getItem(4).setIcon(R.drawable.ic_launcher);


    }





    @Override
    protected void onResumeFragments() {

        super.onResumeFragments();
    }



}
