package social.evenet.activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import social.evenet.R;
import social.evenet.adapter.InviteUsersAdapter;
import social.evenet.db.UserInfo;
import social.evenet.exeption.ContextNullReference;
import social.evenet.helper.Connectivity;
import social.evenet.helper.Util;
import social.evenet.widget.PullToRefreshListView;

public class InviteFriendsActivity extends HashActivity {

    private Handler mhandler = new Handler();
    private ArrayList<UserInfo> item;
    private InviteUsersAdapter adapter;
    private PullToRefreshListView listView;
    private String[] param;
    private Context mContext;
    private Button invite;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_info_fragment);
        mContext = this;
        param = new String[4];
        setActionBar("Invite Friends");
        param[0] = getSharedPreferences("token_info", MODE_PRIVATE).getString("token", "");
        param[1] = getSharedPreferences("token_info", MODE_PRIVATE).getString("user_id", "");

        invite = (Button) findViewById(R.id.invite);
        invite.setVisibility(View.VISIBLE);
        invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultData = new Intent();
                ArrayList<UserInfo> list = new ArrayList<UserInfo>();
                Iterator<UserInfo> iterator = item.iterator();
                while (iterator.hasNext()) {
                    UserInfo userInfo = iterator.next();
                    if (userInfo.isCheck()) {
                        list.add(userInfo);
                    }
                }
                // if(item.get(0).isCheck()){
                resultData.putExtra("user", list);
                setResult(Activity.RESULT_OK, resultData);
                finish();

            }
        });

        listView = (PullToRefreshListView) findViewById(R.id.list_users);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent resultData = new Intent();
                resultData.putExtra("user", item.get(i));
                setResult(Activity.RESULT_OK, resultData);
                finish();
            }
        });

        if (Connectivity.isConnectedFast(this)) {
            param[2] = "" + 50;

        } else {
            param[2] = "" + 20;

        }

        param[3] = "" + 0;


        Map<String,String> options=new LinkedHashMap<>();
        options.put("access_token",param[0]);
        options.put("user_id",param[1]);
        options.put("count",param[2]);
        options.put("offset",param[3]);
        App.getApi().userGetFriends(options,new Callback<JSONArray>() {
            @Override
            public void success(JSONArray jsonArray, Response response) {
                item=new ArrayList<UserInfo>();
                jsonArray = jsonArray.optJSONObject(0).optJSONArray("friends");
                for (int i = 0; i < jsonArray.length(); i++) {
                    UserInfo userInfo=new UserInfo();

                    userInfo.setUser_id(jsonArray.optJSONObject(i).optInt("user_id"));
                    userInfo.setName(jsonArray.optJSONObject(i).optString("name"));
                    userInfo.setSurname(jsonArray.optJSONObject(i).optString("surname"));
                    userInfo.setPhoto_small(jsonArray.optJSONObject(i).optString("photo_small"));
                    userInfo.setRelation(jsonArray.optJSONObject(i).optInt("relation"));
                    item.add(userInfo);

                }

                getAddressBookUsers();
                adapter = new InviteUsersAdapter(mContext, item);
                listView.setAdapter(adapter);
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });




    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.invite_friends, menu);
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



    private View customBar;

    public void setActionBar(String title) {

        Toolbar actionbar = (Toolbar) findViewById(R.id.actionbar);
        LayoutInflater mInflater = LayoutInflater.from(this);
        customBar = mInflater.inflate(R.layout.actionbar_custom, actionbar);

        ImageView disable_actionbar = (ImageView) customBar.findViewById(R.id.disable_actionbar);

        TextView done = (TextView) customBar.findViewById(R.id.done_actionbar);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        disable_actionbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
        if (title.equals("Invite Friends")) {
            disable_actionbar.setVisibility(View.INVISIBLE);
        }

    }



    public void getAddressBookUsers(){
        ContentResolver cr =getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {
                UserInfo userInfo=new UserInfo();
                String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                userInfo.setName(name);
                if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                    Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?",
                            new String[]{id}, null);
                    while (pCur.moveToNext()) {

                        int phoneType = pCur.getInt(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
                        String phoneNumber = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                        userInfo.setPhone(phoneNumber);
                        item.add(userInfo);
                        switch (phoneType) {
                            case ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE:

                                Log.e(name + "(mobile number)", phoneNumber);
                                break;
                            case ContactsContract.CommonDataKinds.Phone.TYPE_HOME:
                                Log.e(name + "(home number)", phoneNumber);
                                break;
                            case ContactsContract.CommonDataKinds.Phone.TYPE_WORK:
                                Log.e(name + "(work number)", phoneNumber);
                                break;
                            case ContactsContract.CommonDataKinds.Phone.TYPE_OTHER:
                              // Log.e(name + "(other number)", phoneNumber);
                                break;
                            default:
                                break;
                        }
                    }
                    pCur.close();
                }
            }
        }
    }

}
