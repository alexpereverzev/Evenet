package social.evenet.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import social.evenet.activity.App;
import social.evenet.activity.MeetUpAddActivity;
import social.evenet.adapter.InviteUsersAdapter;
import social.evenet.db.UserInfo;
import social.evenet.helper.Connectivity;
import social.evenet.widget.PullToRefreshListView;

/**
 * Created by Александр on 09.11.2014.
 */
public class InviteFriendsFragment extends RefreshableFragment {


    private ArrayList<UserInfo> item;
    private InviteUsersAdapter adapter;
    private PullToRefreshListView listView;
    private String[] param;
    private Button invite;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.user_info_fragment, container, false);
        setActionBar("Invite Friends");
        param = new String[4];

        param[0] = getActivity().getSharedPreferences("token_info", getActivity().MODE_PRIVATE).getString("token", "");
        param[1] = getActivity().getSharedPreferences("token_info", getActivity().MODE_PRIVATE).getString("user_id", "");

        invite=(Button) v.findViewById(R.id.invite);
        invite.setVisibility(View.INVISIBLE);

        listView= (PullToRefreshListView) v.findViewById(R.id.list_users);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent resultData = new Intent();
                resultData.putExtra("user", item.get(i));

            }
        });

        if (Connectivity.isConnectedFast(getActivity())) {
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

        App.getApi().userGetFriends(options, callbackFriend);

        return v;
    }


    public void setActionBar(String title) {


        MeetUpAddActivity activity=(MeetUpAddActivity)getActivity();
        View custom=activity.getCustomBar();
        ImageView disable_actionbar = (ImageView) custom.findViewById(R.id.disable_actionbar);

        TextView ti=(TextView) custom.findViewById(R.id.title);

        ti.setText(title);

        TextView done = (TextView) custom.findViewById(R.id.done_actionbar);

        done.setVisibility(View.VISIBLE);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<UserInfo> list=new ArrayList<UserInfo>();
                Iterator<UserInfo> iterator=item.iterator();
                while (iterator.hasNext()){
                    UserInfo userInfo=iterator.next();
                    if(userInfo.isCheck()){
                        list.add(userInfo);
                    }
                }
                Bundle b=getArguments();
                b.putParcelableArrayList("user_list",list);
                b.putString("count_user"," "+list.size()+" Friends Invited");
                MeetupCreateFragmentSeven seven=new MeetupCreateFragmentSeven();
                seven.setArguments(b);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_meetup, seven).commit();
            }
        });

            disable_actionbar.setVisibility(View.INVISIBLE);

    }

    private Callback<JSONArray> callbackFriend = new Callback<JSONArray>() {
        @Override
        public void success(JSONArray jsonArray, Response response) {
            item = new ArrayList<>();
            JSONArray json = jsonArray.optJSONObject(0).optJSONArray("friends");
            for (int i = 0; i < json.length(); i++) {
                UserInfo userInfo = new UserInfo();
                userInfo.setUser_id(json.optJSONObject(i).optInt("user_id"));
                userInfo.setName(json.optJSONObject(i).optString("name"));
                userInfo.setSurname(json.optJSONObject(i).optString("surname"));
                userInfo.setPhoto_small(json.optJSONObject(i).optString("photo_small"));
                userInfo.setRelation(json.optJSONObject(i).optInt("relation"));
                item.add(userInfo);

            }
            adapter = new InviteUsersAdapter(getActivity(), item);
            listView.setAdapter(adapter);


        }

        @Override
        public void failure(RetrofitError error) {

        }
    };


}
