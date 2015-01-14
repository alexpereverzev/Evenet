package social.evenet.activity;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import social.evenet.R;
import social.evenet.fragment.SignPhoneFragment;

public class SignDetail extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_detail);
        getSupportFragmentManager().beginTransaction().replace(R.id.content_fragment_sign, new SignPhoneFragment()).commit();
    }



}
