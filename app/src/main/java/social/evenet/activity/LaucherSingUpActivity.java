package social.evenet.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import social.evenet.R;

public class LaucherSingUpActivity extends ActionBarActivity {

    private LinearLayout facebook;
    private LinearLayout vk;
    private LinearLayout email;


    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laucher_sing_up);
        facebook=(LinearLayout) findViewById(R.id.sign_up_with_facebook);
        vk=(LinearLayout) findViewById(R.id.sign_up_with_vk);
        email=(LinearLayout) findViewById(R.id.sign_up_with_email);


        mContext=this;

        vk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext, VKActivity.class));
            }
        });

        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext, FacebookActivity.class));
            }
        });

        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext, Registration.class));

            }
        });


    }


    private static final int TWITTER_LOGIN_REQUEST_CODE = 1;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("TAG", "ON ACTIVITY RESULT!");
        if(requestCode == TWITTER_LOGIN_REQUEST_CODE){
            Log.d("TAG", "TWITTER LOGIN REQUEST CODE");
            if(resultCode == T4JTwitterLoginActivity.TWITTER_LOGIN_RESULT_CODE_SUCCESS){
                Log.d("TAG", "TWITTER LOGIN SUCCESS");

            }else if(resultCode == T4JTwitterLoginActivity.TWITTER_LOGIN_RESULT_CODE_FAILURE){
                Log.d("TAG", "TWITTER LOGIN FAIL");
            }else{
                //
            }
        }
    }

}
