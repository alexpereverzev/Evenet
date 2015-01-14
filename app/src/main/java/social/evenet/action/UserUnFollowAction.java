package social.evenet.action;

import android.content.Context;
import android.os.Bundle;

import com.parse.ParseObject;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import  social.evenet.helper.SupportInfo;
import  social.evenet.helper.Util;

/**
 * Created by Alexander on 09.09.2014.
 */
public class UserUnFollowAction  {

    JSONArray jsonArray;
    String[] error = new String[3];

    public UserUnFollowAction(CallbackResult callback, Context ctx, Bundle data, int requestId) {

    }
}
  /*  @Override
    public void execute() {

        Bundle b = new Bundle();
        JSONObject obj = null;
        boolean loginResult = false;

        HttpClient httpclient = new DefaultHttpClient();
     //   String url= SupportInfo.url + SupportInfo.getinfo + params[0] + "+&user_id" + params[1];
      //  HttpGet httpget = new HttpGet(SupportInfo.url + SupportInfo.un_user_follow + params[0] + "+&user_id=" + params[1]);

        httpget.setHeader("Content-type", "application/json");

        InputStream inputStream = null;
        String result = null;
        String[] response_info = new String[3];

        try {
            HttpResponse response = httpclient.execute(httpget);
            HttpEntity entity = response.getEntity();

            inputStream = entity.getContent();
            // json is UTF-8 by default
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
            StringBuilder sb = new StringBuilder();

            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            result = sb.toString();
            jsonArray = new JSONArray(result);
            for (int i = 0; i < jsonArray.length(); i++) {
                    response_info[0]=jsonArray.getJSONObject(i).getString("response_code");
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                error[0] = jsonArray.getJSONObject(0).getString("error_code");
                error[1] = jsonArray.getJSONObject(0).getString("error_message");
                error[2] = jsonArray.getJSONObject(0).getString("more_info");
                ParseObject errorObject = new ParseObject("APIError");
                errorObject.put("error_code",error[0]);
                errorObject.put("error_message",error[1]);
                errorObject.put("more_info",error[2]);
                errorObject.saveEventually();

            } catch (JSONException e1) {
                e1.printStackTrace();
            }
            b.putStringArray("failReason", response_info);
            _callback.onCallback(_requestId, Util.RESULT_ERROR, "UnFollowerAction", b);
            return;
        }


        if (response_info[0] != null) {

            b.putString("response_info", "ok");

            _callback.onCallback(_requestId, Util.RESULT_OK, "UserUnFollowerAction", b);
        } else {

           // b.putString("failReason", obj.optJSONObject("unfollower").optString("resultText"));
            _callback.onCallback(_requestId, Util.RESULT_ERROR, "UnFollowerAction", b);
        }

    }*/

