package social.evenet.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import social.evenet.R;
import social.evenet.db.UserInfo;

public class LaucherActivity extends HashActivity {

    private Intent i;
    private SharedPreferences pref;
    private Map<String, String> param;
    private Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laucher);
        getSupportActionBar().hide();
        pref = getSharedPreferences("token_info", MODE_PRIVATE);
        String token = pref.getString("token", "");
        mContext = this;


        if (!token.isEmpty()) {


            //   GetInfoCheck info = new GetInfoCheck();
            param = new LinkedHashMap<>();
            param.put("access_token", token);
            param.put("user_id", pref.getString("user_id", ""));
            App.getApi().userInfo(param, new Callback<JSONArray>() {
                @Override
                public void success(JSONArray jsonArray, Response response) {
                    ArrayList<UserInfo> mas = new ArrayList<UserInfo>();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        UserInfo userInfo = new UserInfo();
                        JSONObject jsonobject = jsonArray.optJSONObject(i);
                        userInfo.setName(jsonobject.optString("name"));
                        userInfo.setSurname(jsonobject.optString("surname"));
                        userInfo.setNikname(jsonobject.optString("screen_name"));
                        userInfo.setFollowers_count(jsonobject.optInt("followers_count"));
                        userInfo.setFollowing_count(jsonobject.optInt("following_count"));
                        userInfo.setFriends_count(jsonobject.optInt("friends_count"));
                        userInfo.setFeedback_notification_count(jsonobject.optInt("feedback_notifications_count"));
                        userInfo.setCommon_meetups(jsonobject.optInt("common_meetups"));
                        userInfo.setPlaces_in_lists(jsonobject.optInt("places_in_lists"));
                        userInfo.setReviews_count(jsonobject.optInt("reviews_count"));
                        userInfo.setPhoto_small(jsonobject.optString("photo_small"));
                        userInfo.setReputation(jsonobject.optInt("reputation"));
                        userInfo.setGender(jsonobject.optString("gender"));
                        if (!jsonobject.isNull("phone_number"))
                            userInfo.setPhone_number(jsonobject.optInt("phone_number"));
                        else {
                            userInfo.setPhone_number(0);
                        }
                        userInfo.setLast_seen(jsonobject.optString("last_seen"));
                        userInfo.setNative_lang(jsonobject.optString("native_lang"));
                        userInfo.setHome_city(jsonobject.optString("home_city"));
                        userInfo.setEvent_created(jsonobject.optInt("events_created"));
                        userInfo.setPhoto_big(jsonobject.optString("photo_big"));
                        userInfo.setRelation(jsonobject.optInt("relation"));
                        mas.add(userInfo);
                    }


                    SharedPreferences.Editor prefEditor = pref.edit();
                    prefEditor.putString("username", mas.get(0).getName());
                    prefEditor.putString("surname", mas.get(0).getSurname());
                    prefEditor.putString("foto", mas.get(0).getPhoto_small());

                    prefEditor.commit();

                    i = new Intent(mContext, MenuNewActivity.class);
                    i.putExtra("username", mas.get(0).getName());
                    i.putExtra("surname", mas.get(0).getSurname());

                    if (mas.size() == 0) {
                        i = new Intent(mContext, Login.class);
                    }

                }

                @Override
                public void failure(RetrofitError error) {

                }
            });


        } else i = new Intent(this, Login.class);


        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            public void run() {
                if (i != null)
                    startActivity(i);
                finish();

            }


        }, 3000);
    }

}
