package social.evenet.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import social.evenet.R;
import social.evenet.adapter.CommentAdapter;
import social.evenet.db.Comment;
import social.evenet.db.UserInfo;
import social.evenet.helper.Connectivity;

public class CommentActivity extends HashActivity implements View.OnClickListener {
    private Comment comment;
    private ListView list_comment;
    private CommentAdapter adapter;
    private ArrayList<Comment> item = new ArrayList<Comment>();
    private String[] param;
    private Context mContext;
    private EditText editcomment;
    private Button send;
    private Intent intent;
    private InputMethodManager mgr;
    private int par = 0;
    private Map<String, String> params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        intent = new Intent(this, UsersInfo.class);
        setContentView(R.layout.activity_comment);
        list_comment = (ListView) findViewById(R.id.list_comment);
        adapter = new CommentAdapter(this, item);
        list_comment.setAdapter(adapter);
        editcomment = (EditText) findViewById(R.id.my_comment);
        send = (Button) findViewById(R.id.send_comment);
        editcomment.requestFocus();
        param = new String[4];
        params = new LinkedHashMap<>();
        params.put("access_token", getSharedPreferences("token_info", MODE_PRIVATE).getString("token", ""));

        param[0] = getSharedPreferences("token_info", MODE_PRIVATE).getString("token", "");
        param[1] = "" + getIntent().getIntExtra("id", 0);
        if (Connectivity.isConnectedFast(this)) {
            param[2] = "" + 50;
            params.put("count", "50");
        } else {
            params.put("count", "20");
            param[2] = "" + 20;
        }
        params.put("offset", "0");
        param[3] = "" + 0;
        Bundle b = new Bundle();


        if (getIntent().getIntExtra("place_id", 0) != 0) {
            params.put("place_id", "" + getIntent().getIntExtra("place_id", 0));
            par = getIntent().getIntExtra("number", 0);
            param[1] = "" + getIntent().getIntExtra("place_id", 0);
            App.getApi().placeComment(params, callback);

        } else {
            params.put("event_id", "" + getIntent().getIntExtra("id", 0));
            App.getApi().eventComment(params, callback);

        }
        list_comment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                intent.putExtra("user_id", item.get(position).getComment_id());
                intent.putExtra("number", 3);
                startActivity(intent);
            }
        });
        send.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        String[] params = new String[4];

        UserInfo userInfo = new UserInfo();
        comment = new Comment();
        Map<String, String> publish_params = new LinkedHashMap<>();
        switch (par) {
            case 0:
                params[0] = param[0];
                params[1] = param[1];
                if (editcomment.getText() != null) {
                    String toAppName = editcomment.getText().toString().replace("0", " "); //use 0 instead of space in app name
                    params[2] = toAppName.trim();
                }

                comment.setComment(editcomment.getText().toString());
                String time = Calendar.getInstance().getTime().toString();

                userInfo.setUser_id(Integer.parseInt(getSharedPreferences("token_info", MODE_PRIVATE).getString("user_id", "")));
                userInfo.setName(getSharedPreferences("token_info", MODE_PRIVATE).getString("username", ""));
                userInfo.setSurname(getSharedPreferences("token_info", MODE_PRIVATE).getString("surname", ""));
                userInfo.setPhoto_small(getSharedPreferences("token_info", MODE_PRIVATE).getString("foto", ""));
                comment.setUser(userInfo);
                comment.setTime((int) System.currentTimeMillis());

                publish_params.put("access_token", param[0]);
                publish_params.put("place_id", param[1]);
                publish_params.put("text", editcomment.getText().toString());
                publish_params.put("publish", "1");
                App.getApi().userAddComment(publish_params, callback_comment);


                break;
            case 1:
                params[0] = param[0];
                params[1] = param[1];
                params[2] = editcomment.getText().toString();
                if (editcomment.getText() != null) {
                    String toAppName = editcomment.getText().toString().replace("0", " "); //use 0 instead of space in app name
                    params[2] = toAppName.trim();
                }


                comment.setComment(editcomment.getText().toString());
                userInfo = new UserInfo();
                comment.setUser(userInfo);
                comment.setTime((int) System.currentTimeMillis());
                item.add(comment);
                adapter.notifyDataSetChanged();
                editcomment.setText("");
                publish_params.put("access_token", param[0]);
                publish_params.put("event_id", param[1]);
                publish_params.put("text", editcomment.getText().toString());
                publish_params.put("publish", "1");

                App.getApi().userAddComment(publish_params, callback_comment);


                break;
            case 2:
                break;
        }
    }

    private Callback<JSONArray> callback = new Callback<JSONArray>() {
        @Override
        public void success(JSONArray jsonArray, Response response) {
            jsonArray = jsonArray.optJSONObject(0).optJSONArray("comments");
            ArrayList<Comment> comments = new ArrayList<Comment>();
            if (jsonArray.length() != 0) {
                for (int i = 0; i < jsonArray.length(); i++) {

                    UserInfo userInfo = new UserInfo();
                    Comment comment = new Comment();
                    comment.setComment_id(jsonArray.optJSONObject(i).optInt("comment_id"));
                    comment.setComment(jsonArray.optJSONObject(i).optString("text"));
                    comment.setTime(jsonArray.optJSONObject(i).optInt("time"));
                    comment.setLikes_count(jsonArray.optJSONObject(i).optInt("likes_count"));
                    comment.setLiked_by_you(jsonArray.optJSONObject(i).optInt("liked_by_you"));
                    userInfo.setUser_id(jsonArray.optJSONObject(i).optJSONObject("user").optInt("user_id"));
                    userInfo.setName(jsonArray.optJSONObject(i).optJSONObject("user").optString("name"));
                    userInfo.setSurname(jsonArray.optJSONObject(i).optJSONObject("user").optString("surname"));
                    userInfo.setPhoto_small(jsonArray.optJSONObject(i).optJSONObject("user").optString("photo_small"));
                    userInfo.setRelation(jsonArray.optJSONObject(i).optJSONObject("user").optInt("relation"));
                    comment.setUser(userInfo);
                    comments.add(comment);

                }
            }
            item = comments;
            adapter = new CommentAdapter(mContext, item);
            list_comment.setAdapter(adapter);

        }

        @Override
        public void failure(RetrofitError error) {

        }
    };


    private Callback<JSONArray> callback_comment = new Callback<JSONArray>() {
        @Override
        public void success(JSONArray jsonArray, Response response) {
            String res = jsonArray.optJSONObject(0).optString("comment_id");

            if (res != null) {
                item.add(comment);
                adapter.notifyDataSetChanged();
                editcomment.setText("");
                mgr.hideSoftInputFromInputMethod(editcomment.getWindowToken(), 0);
            }
        }

        @Override
        public void failure(RetrofitError error) {

        }
    };
}
