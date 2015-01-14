package social.evenet.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.LoginActivity;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.VKSdkListener;
import com.vk.sdk.VKUIHelper;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKScopes;
import com.vk.sdk.api.model.VKUsersArray;
import com.vk.sdk.dialogs.VKCaptchaDialog;
import com.vk.sdk.util.VKUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import social.evenet.R;
import social.evenet.db.UserInfo;
import social.evenet.helper.SupportInfo;

public class VKActivity extends ActionBarActivity {

    private VKRequest myRequest;
    private Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vk);
        intent=new Intent(this, Registration.class);
        VKUIHelper.onCreate(this);
        VKSdk.initialize(sdkListener, getResources().getString(R.string.vk_app_id));
        VKSdk.authorize(SupportInfo.sMyScope, false, true);
        if (VKSdk.wakeUpSession()) {

            getInfo();
        }




        String[] fingerprint = VKUtil.getCertificateFingerprint(this, this.getPackageName());
        Log.d("Fingerprint", fingerprint[0]);

    }

    public void getInfo(){
        VKRequest request = VKApi.users().get(VKParameters.from(VKApiConst.FIELDS,
                "id,first_name,last_name,sex,bdate,city,country,photo_50,photo_100," +
                        "photo_200_orig,photo_200,photo_400_orig,photo_max,photo_max_orig,online," +
                        "online_mobile,lists,domain,has_mobile,contacts,connections,site,education," +
                        "universities,schools,can_post,can_see_all_posts,can_see_audio,can_write_private_message," +
                        "status,last_seen,common_count,relation,relatives,counters"));
//                    VKRequest request = VKApi.users().get(VKParameters.from(VKApiConst.USER_IDS, "1,2"));
        request.secure = false;
        request.useSystemLanguage = false;
      //  startApiCall(request);

        VKRequest get = new VKRequest("users.get", VKParameters.from(VKApiConst.USER_IDS, "1,2,44", VKApiConst.FIELDS, "photo_50,sex"), VKRequest.HttpMethod.GET, VKUsersArray.class);
        get.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                System.out.print(response);
            }

            @Override
            public void onError(VKError error) {
                super.onError(error);
                System.out.print(error);
            }
        });

        request.executeWithListener(mRequestListener);


    }

    private void startApiCall(VKRequest request) {
        Intent i = new Intent(this, ApiCallActivity.class);
        i.putExtra("request", request.registerObject());
//        startActivity(i);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.vk, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    private void showLogin() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, new LoginFragment())
                .commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        VKUIHelper.onResume(this);
        if (VKSdk.isLoggedIn()) {
         //   showLogout();
        } else {
       //     showLogin();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        VKUIHelper.onDestroy(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        VKUIHelper.onActivityResult(this, requestCode, resultCode, data);
    }


    private void showLogout() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, new LogoutFragment())
                .commit();
    }
    private final VKSdkListener sdkListener = new VKSdkListener() {
        @Override
        public void onCaptchaError(VKError captchaError) {
            new VKCaptchaDialog(captchaError).show();
        }

        @Override
        public void onTokenExpired(VKAccessToken expiredToken) {
            VKSdk.authorize(SupportInfo.sMyScope);
        }

        @Override
        public void onAccessDenied(final VKError authorizationError) {
            new AlertDialog.Builder(VKUIHelper.getTopActivity())
                    .setMessage(authorizationError.toString())
                    .show();
        }

        @Override
        public void onReceiveNewToken(VKAccessToken newToken) {
           // startTestActivity();
            getInfo();
        }

        @Override
        public void onAcceptUserToken(VKAccessToken token) {
            //startTestActivity();
            getInfo();
        }
    };
    private void startTestActivity() {
        startActivity(new Intent(this, TestActivity.class));
    }

    public static class LoginFragment extends android.support.v4.app.Fragment {
        public LoginFragment() {
            super();
        }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_login, container, false);
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            getView().findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    VKSdk.authorize(SupportInfo.sMyScope, true, false);
                }
            });

            getView().findViewById(R.id.force_oauth_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    VKSdk.authorize(SupportInfo.sMyScope, true, true);
                }
            });
        }
    }


    public static class LogoutFragment extends android.support.v4.app.Fragment {
        public LogoutFragment() {
            super();
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_logout, container, false);
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            getView().findViewById(R.id.continue_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((VKActivity) getActivity()).startTestActivity();
                }
            });

            getView().findViewById(R.id.logout).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    VKSdk.logout();
                    if (!VKSdk.isLoggedIn()) {
                        ((VKActivity) getActivity()).showLogin();
                    }
                }
            });
        }
    }

    VKRequest.VKRequestListener mRequestListener = new VKRequest.VKRequestListener()
    {
        @Override
        public void onComplete(VKResponse response)
        {
            try {
                JSONArray jsonArray=response.json.getJSONArray("response");
                UserInfo userInfo=new UserInfo();
                for(int i=0; i<jsonArray.length(); i++){
                    userInfo.setName(jsonArray.getJSONObject(i).getString("first_name"));
                    userInfo.setSurname(jsonArray.getJSONObject(i).getString("last_name"));
                    userInfo.setPhoto_small(jsonArray.getJSONObject(i).getString("photo_200"));
                    if(!jsonArray.getJSONObject(i).isNull("mobile_phone")){
                    userInfo.setPhone(jsonArray.getJSONObject(i).getString("mobile_phone"));
                    }

                }
                intent.putExtra("user",userInfo);
                startActivity(intent);
             //   userInfo.setName(jsonArray.getJSONObject("")getString(""));
            } catch (JSONException e) {
                e.printStackTrace();
            }


          //  setResponseText(response.json.toString());
        }

        @Override
        public void onError(VKError error)
        {
            System.out.print("");
         //   setResponseText(error.toString());
        }

        @Override
        public void onProgress(VKRequest.VKProgressType progressType, long bytesLoaded,
                               long bytesTotal)
        {
            // you can show progress of the request if you want
        }

        @Override
        public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts)
        {

        }
    };

   /* private void processRequestIfRequired(VKRequest request) {

        request.executeWithListener(mRequestListener);
    }*/
}