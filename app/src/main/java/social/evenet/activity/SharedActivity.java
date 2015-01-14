package social.evenet.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ToggleButton;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKSdk;
import com.vk.sdk.VKSdkListener;
import com.vk.sdk.VKUIHelper;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiPhoto;
import com.vk.sdk.api.model.VKAttachments;
import com.vk.sdk.api.model.VKPhotoArray;
import com.vk.sdk.api.model.VKWallPostResult;
import com.vk.sdk.api.photo.VKImageParameters;
import com.vk.sdk.api.photo.VKUploadImage;
import com.vk.sdk.dialogs.VKCaptchaDialog;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import social.evenet.fb.SimpleFacebook;
import social.evenet.fb.entities.Feed;
import social.evenet.R;
import social.evenet.db.Event;
import social.evenet.db.UserInfo;
import social.evenet.helper.SupportInfo;
import social.evenet.twitter.T4JTwitterFunctions;

public class SharedActivity extends HashActivity {

    final int STATIC_INTEGER_FRIENDS = 101;
    private static final int TWITTER_LOGIN_REQUEST_CODE = 1;
    DisplayImageOptions options = new DisplayImageOptions.Builder()
            .cacheInMemory(true)
            .resetViewBeforeLoading(true)
            .cacheOnDisk(true)
            .considerExifParams(true)
            .build();

    private ImageView evenet;
    private EditText message_shared;
    private ImageView icon_shared;
    private Button shared;
    private Context mContext;
    private String friends_id;
    private Event event;
    private String[] param;
    private ToggleButton facebook;
    private ToggleButton vk;
    private ToggleButton instagram;
    private ToggleButton twitter;
    private Bitmap photo;
    private ImageView icon_facebook;
    private ImageView icon_vk;
    private ImageView icon_twitter;


    private SimpleFacebook mSimpleFacebook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shared);
        mContext=this;

        VKUIHelper.onCreate(this);
        VKSdk.initialize(sdkListener, getResources().getString(R.string.vk_app_id));

        event=getIntent().getParcelableExtra("event");
        message_shared=(EditText) findViewById(R.id.text_shared);
        evenet=(ImageView) findViewById(R.id.icon_social_evenet);
        icon_shared=(ImageView) findViewById(R.id.icon_shared);
        shared=(Button) findViewById(R.id.shared);
         message_shared.setText(event.getEvent_title());
        vk=(ToggleButton) findViewById(R.id.indicator_social_vk);
        instagram=(ToggleButton) findViewById(R.id.indicator_social_instagram);

        twitter=(ToggleButton) findViewById(R.id.indicator_social_twitter);

        ImageLoader.getInstance().displayImage(event.getMainAttachment().getUrl(),icon_shared,options);

        shared.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(facebook.getText().toString().equals("ON")){
                Feed feed = new Feed.Builder()
                        .setMessage("Clone it out...")
                        .setName(event.getEvent_title())
                        .setCaption("Code less, do the same.")
                        .setDescription(event.getEvent_description())
                        .setPicture(event.getMainAttachment().getUrl())
                        .setLink("https://github.com/sromku/android-simple-facebook")

                        .build();

                mSimpleFacebook.publish(feed,onPublishListener);
                }

                if(twitter.getText().toString().equals("ON")){
                    if(photo==null){
                        photo=getBitmapPhoto();
                    }
                    String fileName = "image.jpg";
                    Bitmap bitmap=photo;
                    try
                    {
                        FileOutputStream ostream = openFileOutput( fileName, Context.MODE_WORLD_READABLE);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, ostream);
                        ostream.close();
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    Uri.fromFile( new File( getFileStreamPath(fileName).getAbsolutePath()));

                    T4JTwitterFunctions.postToTwitterImage(mContext, SharedActivity.this, new File( getFileStreamPath(fileName).getAbsolutePath()), "YS0IWHeZACBLzccfMNdg2ovSo", "eTFiCQF5Gnp5MHVRhhHLk7Z7D4ZFAmOYFvwxY2y0nuBBsJE6gp", message_shared.getText().toString(), new T4JTwitterFunctions.TwitterPostResponse() {
                        @Override
                        public void OnResult(Boolean success) {
                            // TODO Auto-generated method stub
                            if (success) {
                                //   Toast.makeText(getActivity().getApplicationContext(), "Tweet posted successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                //  Toast.makeText(getActivity().getApplicationContext(), "Tweet did not post!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

                if(vk.getText().toString().equals("ON")){

                    if(photo==null){
                        photo=getBitmapPhoto();
                    }
                    VKRequest request = VKApi.uploadWallPhotoRequest(new VKUploadImage(photo, VKImageParameters.jpgImage(0.9f)), 0, 60479154);
                    request.executeWithListener(new VKRequest.VKRequestListener() {
                        @Override
                        public void onComplete(VKResponse response) {
                            photo.recycle();
                            VKApiPhoto photoModel = ((VKPhotoArray) response.parsedModel).get(0);
                            VKAttachments vkApiAttachments=new VKAttachments(photoModel);

                            makePost(new VKAttachments(photoModel),message_shared.getText().toString());
                        }

                        @Override
                        public void onError(VKError error) {
                            System.out.print("");
                        }
                    });
                }

                if(instagram.getText().toString().equals("ON")){
                    if(photo==null){
                        photo=getBitmapPhoto();
                    }
                    String fileName = "image.jpg";
                   Bitmap bitmap=photo;
                    try
                    {
                        FileOutputStream ostream = openFileOutput( fileName, Context.MODE_WORLD_READABLE);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, ostream);
                        ostream.close();
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                    Intent share = new Intent(Intent.ACTION_SEND);
                    share.setType("image/*");
                    share.putExtra(Intent.EXTRA_SUBJECT, message_shared.getText());
                    share.putExtra(Intent.EXTRA_TEXT, event.getEvent_description());
                    share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile( new File( getFileStreamPath(fileName).getAbsolutePath())));
                    share.setPackage("com.instagram.android");
                    startActivity(Intent.createChooser(share,"Share via"));


                }

              param = new String[6];
                param[0] =getSharedPreferences("token_info", MODE_PRIVATE).getString("token", "");
                param[1]=""+event.getEvent_id();
                if(friends_id!=null){
                param[2]=friends_id;
                param[3]=message_shared.getText().toString();
                    Map<String,String> options=new LinkedHashMap<String, String>();
                    options.put("access_token",param[0]);
                    options.put("event_id",param[1]);
                    options.put("recipients",param[2]);
                    options.put("message",param[3]);
                    App.getApi().sendEventFriend(options,new Callback<JSONArray>() {
                        @Override
                        public void success(JSONArray jsonArray, Response response) {

                            finish();
                        }

                        @Override
                        public void failure(RetrofitError error) {

                        }
                    });

                }
            }
        });

        evenet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(mContext, InviteFriendsActivity.class);
                startActivityForResult(i, STATIC_INTEGER_FRIENDS);
            }
        });

        facebook=(ToggleButton) findViewById(R.id.indicator_social_facebook);

        twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(twitter.isChecked()){
                    if (!T4JTwitterLoginActivity.isConnected(mContext)){
                        Intent twitterLoginIntent = new Intent(mContext, T4JTwitterLoginActivity.class);
                        twitterLoginIntent.putExtra(T4JTwitterLoginActivity.TWITTER_CONSUMER_KEY, "YS0IWHeZACBLzccfMNdg2ovSo");
                        twitterLoginIntent.putExtra(T4JTwitterLoginActivity.TWITTER_CONSUMER_SECRET, "eTFiCQF5Gnp5MHVRhhHLk7Z7D4ZFAmOYFvwxY2y0nuBBsJE6gp");
                        startActivityForResult(twitterLoginIntent, TWITTER_LOGIN_REQUEST_CODE);
                        twitter.setChecked(true);
                    }

                }
                else {
                    twitter.setChecked(false);
                }
            }
        });

       mSimpleFacebook= SimpleFacebook.getInstance(this);

        if(mSimpleFacebook.isLogin()) facebook.setChecked(true);

        if(VKSdk.isLoggedIn()) vk.setChecked(true);

        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(facebook.isChecked()){
                    if(mSimpleFacebook.isLogin()){
                    facebook.setChecked(true);}
                    else {
                        mSimpleFacebook.login(mOnLoginListener);
                    }

                }
                else {
                    facebook.setChecked(false);
                }
            }
        });

        instagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(instagram.isChecked()){
                    boolean installed  =   appInstalledOrNot("com.instagram.android");
                    if(!installed){
                    String url="http://play.google.com/store/apps/details?id=com.instagram.android";
                        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(i);
                    }
                    else {
                        instagram.setChecked(true);
                    }
                }
                else {
                    instagram.setChecked(false);
                }

            }
        });

        vk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(vk.isChecked()){
                    if(VKSdk.isLoggedIn()){
                        vk.setChecked(true);}
                    else {
                        vk.setChecked(false);
                        VKSdk.authorize(SupportInfo.sMyScope,false,true);

                        if (VKSdk.wakeUpSession()) {


                        }
                    //    VKSdk.authorize(SupportInfo.sMyScope, true, true);
                    }

                }
                else {
                    vk.setChecked(false);
                }
            }
        });

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {

            case (STATIC_INTEGER_FRIENDS): {
                if (resultCode == Activity.RESULT_OK) {
                    String invite_id="";
                    String invite_name=" ";
                    //   String newText = data.getStringExtra(PUBLIC_STATIC_STRING_IDENTIFIER);
                    ArrayList<UserInfo> e = data.getParcelableArrayListExtra("user");
                    Iterator<UserInfo> iterator=e.iterator();
                    while (iterator.hasNext()){
                        UserInfo userInfo= iterator.next();
                        invite_name=invite_name+userInfo.getName()+" "+userInfo.getSurname()+" ";
                        if(userInfo.getSurname()!=null)
                            invite_id=invite_id+userInfo.getUser_id()+",";
                    }


                    invite_id = invite_id.substring(0, invite_id.length()-1);

                    friends_id=invite_id;
                    // TODO Update your TextView.
                }
                break;
            }
            default:  VKUIHelper.onActivityResult(this, requestCode, resultCode, data);
        }
    }




    public SimpleFacebook.OnLoginListener mOnLoginListener = new SimpleFacebook.OnLoginListener() {

        @Override
        public void onFail(String reason) {
            //   mTextStatus.setText(reason);

        }

        @Override
        public void onException(Throwable throwable) {
            //  mTextStatus.setText("Exception: " + throwable.getMessage());

        }

        @Override
        public void onThinking() {

        }

        @Override
        public void onLogin() {




        }

        @Override
        public void onNotAcceptingPermissions() {

        }
    };

    public SimpleFacebook.OnPublishListener onPublishListener = new SimpleFacebook.OnPublishListener() {

        @Override
        public void onFail(String reason) {

            // insure that you are logged in before publishing
            System.out.print(reason);
        }

        @Override
        public void onException(Throwable throwable) {


        }

        @Override
        public void onThinking() {
            // show progress bar or something to the user while publishing

        }

        @Override
        public void onComplete(String postId) {
            System.out.print(postId);
        }
    };

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
            vk.setChecked(true);

            getInfo();


        }

        @Override
        public void onAcceptUserToken(VKAccessToken token) {
            //startTestActivity();
            vk.setChecked(true);
            getInfo();
        }
    };

    private void makePost(VKAttachments attachments, String message) {

        VKRequest post = VKApi.wall().post(VKParameters.from(VKApiConst.OWNER_ID, id_user, VKApiConst.ATTACHMENTS, attachments, VKApiConst.MESSAGE, message));
        post.setModelClass(VKWallPostResult.class);
        post.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                VKWallPostResult result = (VKWallPostResult)response.parsedModel;
             //   Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(String.format("https://vk.com/wall-60479154_%s", result.post_id)) );
              //  startActivity(i);
            }

            @Override
            public void onError(VKError error) {
               // showError(error.apiError != null ? error.apiError : error);
            }
        });
    }

    public void getInfo(){
        VKRequest request = VKApi.users().get(VKParameters.from(VKApiConst.FIELDS,
                "id"));

        request.secure = false;
        request.useSystemLanguage = false;
        request.executeWithListener(mRequestListener);


    }
String id_user;
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
                    id_user=jsonArray.getJSONObject(i).getString("id");
                    userInfo.setSurname(jsonArray.getJSONObject(i).getString("last_name"));

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


            //  setResponseText(response.json.toString());
        }

        @Override
        public void onError(VKError error)
        {
            System.out.print("");

        }

        @Override
        public void onProgress(VKRequest.VKProgressType progressType, long bytesLoaded,
                               long bytesTotal)
        {

        }

        @Override
        public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts)
        {

        }
    };

    public Bitmap getBitmapPhoto(){
        icon_shared.setDrawingCacheEnabled(true);
        icon_shared.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        icon_shared.layout(0, 0,
                icon_shared.getMeasuredWidth(), icon_shared.getMeasuredHeight());
        icon_shared.buildDrawingCache(true);
         Bitmap photo = Bitmap.createBitmap(icon_shared.getDrawingCache());
        icon_shared.setDrawingCacheEnabled(false);
        return photo;
    }

    private boolean appInstalledOrNot(String uri) {
        PackageManager pm = getPackageManager();
        boolean app_installed = false;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        }
        catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed ;
    }

}
