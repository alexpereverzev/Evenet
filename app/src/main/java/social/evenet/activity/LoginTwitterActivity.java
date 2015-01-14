package social.evenet.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.LoginActivity;

import social.evenet.helper.AppConstant;




import social.evenet.R;


public class LoginTwitterActivity extends ActionBarActivity {

    public static final int TWITTER_LOGIN_RESULT_CODE_SUCCESS = 1111;
    public static final int TWITTER_LOGIN_RESULT_CODE_FAILURE = 2222;

    private static final String TAG = "LoginActivity";

    private WebView twitterLoginWebView;
    private AlertDialog mAlertBuilder;
    private static String twitterConsumerKey;
    private static String twitterConsumerSecret;

  //  private static twitter4j.Twitter twitter;
   // private static RequestToken requestToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter_login);
        twitterConsumerKey = "twitter_consumer_key";
        twitterConsumerSecret = "twitter_consumer_secret";

        if(twitterConsumerKey == null || twitterConsumerSecret == null){
            Log.e(TAG, "ERROR: Consumer Key and Consumer Secret required!");
            LoginTwitterActivity.this.setResult(TWITTER_LOGIN_RESULT_CODE_FAILURE);
            LoginTwitterActivity.this.finish();
        }


        mAlertBuilder = new AlertDialog.Builder(this).create();
        mAlertBuilder.setCancelable(false);
        mAlertBuilder.setTitle("Please wait");
        View view = getLayoutInflater().inflate(R.layout.view_loading, null);
        ((TextView) view.findViewById(R.id.messageTextViewFromLoading)).setText(getString(R.string.authenticating_your_app_message));
        mAlertBuilder.setView(view);
        mAlertBuilder.show();


        twitterLoginWebView = (WebView)findViewById(R.id.twitterLoginWebView);
        twitterLoginWebView.setBackgroundColor(Color.TRANSPARENT);
        twitterLoginWebView.setWebViewClient( new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url){

                if( url.contains(AppConstant.TWITTER_CALLBACK_URL)){
                    Uri uri = Uri.parse(url);
                    LoginTwitterActivity.this.saveAccessTokenAndFinish(uri);
                    return true;
                }
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                if(mAlertBuilder != null){
                    mAlertBuilder.cancel();
                }
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);

                if(mAlertBuilder != null){
                    mAlertBuilder.show();
                }
            }
        });

        Log.d(TAG, "Authorize....");
        askOAuth();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(mAlertBuilder != null) {
            mAlertBuilder.dismiss();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void saveAccessTokenAndFinish(final Uri uri){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String verifier = uri.getQueryParameter(AppConstant.IEXTRA_OAUTH_VERIFIER);
                try {
                    SharedPreferences sharedPrefs = getSharedPreferences(AppConstant.SHARED_PREF_NAME, Context.MODE_PRIVATE);
              //      AccessToken accessToken = twitter.getOAuthAccessToken(requestToken, verifier);
                    SharedPreferences.Editor e = sharedPrefs.edit();
               //     e.putString(AppConstant.SHARED_PREF_KEY_TOKEN, accessToken.getToken());
              //      e.putString(AppConstant.SHARED_PREF_KEY_SECRET, accessToken.getTokenSecret());
                    e.commit();


                    LoginTwitterActivity.this.setResult(TWITTER_LOGIN_RESULT_CODE_SUCCESS);
                } catch (Exception e) {
                    e.printStackTrace();
                    if(e.getMessage() != null){
                        Log.e(TAG, e.getMessage());

                    }else{
                        Log.e(TAG, "ERROR: Twitter callback failed");
                    }
                    LoginTwitterActivity.this.setResult(TWITTER_LOGIN_RESULT_CODE_FAILURE);
                }
                LoginTwitterActivity.this.finish();
            }
        }).start();
    }


    public static boolean isActive(Context ctx) {
        SharedPreferences sharedPrefs = ctx.getSharedPreferences(AppConstant.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPrefs.getString(AppConstant.SHARED_PREF_KEY_TOKEN, null) != null;
    }

    public static void logOutOfTwitter(Context ctx){
        SharedPreferences sharedPrefs = ctx.getSharedPreferences(AppConstant.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor e = sharedPrefs.edit();
        e.putString(AppConstant.SHARED_PREF_KEY_TOKEN, null);
        e.putString(AppConstant.SHARED_PREF_KEY_SECRET, null);
        e.commit();
    }

    public static String getAccessToken(Context ctx){
        SharedPreferences sharedPrefs = ctx.getSharedPreferences(AppConstant.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPrefs.getString(AppConstant.SHARED_PREF_KEY_TOKEN, null);
    }

    public static String getAccessTokenSecret(Context ctx){
        SharedPreferences sharedPrefs = ctx.getSharedPreferences(AppConstant.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPrefs.getString(AppConstant.SHARED_PREF_KEY_SECRET, null);
    }

    private void askOAuth() {
       /* ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
        configurationBuilder.setOAuthConsumerKey(twitterConsumerKey);
        configurationBuilder.setOAuthConsumerSecret(twitterConsumerSecret);
        Configuration configuration = configurationBuilder.build();
        twitter = new TwitterFactory(configuration).getInstance();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    requestToken = twitter.getOAuthRequestToken(AppConstant.TWITTER_CALLBACK_URL);
                } catch (Exception e) {
                    final String errorString = e.toString();
                    LoginTwitterActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mAlertBuilder.cancel();
                            Toast.makeText(LoginTwitterActivity.this, errorString.toString(), Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                    return;
                }

                LoginTwitterActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        twitterLoginWebView.loadUrl(requestToken.getAuthenticationURL());
                    }
                });
            }
        }).start();*/
    }

}
