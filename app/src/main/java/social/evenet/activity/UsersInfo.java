package social.evenet.activity;

import android.content.Intent;
import android.os.Bundle;

import social.evenet.R;
import social.evenet.db.UserInfo;
import social.evenet.fragment.UserInfoFragment;

public class UsersInfo extends HashActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_following);

        int a = getIntent().getIntExtra("number", 100);
        if(getIntent().getParcelableExtra("user")!=null){
            UserInfo userInfo=getIntent().getParcelableExtra("user");
            getSupportFragmentManager().beginTransaction().replace(R.id.content_users, UserInfoFragment.getFragment(a,userInfo)).commit();
        }
        else if(a!=100) {
            String user_id=getIntent().getStringExtra("user_id");
            getSupportFragmentManager().beginTransaction().replace(R.id.content_users, UserInfoFragment.getFragment(a,user_id)).commit();

        }


    }


}