package social.evenet.fragment;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import org.json.JSONArray;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import social.evenet.R;
import social.evenet.activity.App;
import social.evenet.adapter.UsersAdapter;
import social.evenet.db.UserInfo;
import social.evenet.helper.Connectivity;
import social.evenet.widget.PullToRefreshListView;


public class UserInfoFragment extends RefreshableFragment {
    private Toolbar actionbar;
    private ArrayList<UserInfo> item;
    private UsersAdapter adapter;
    private PullToRefreshListView listView;
    private String[] param;


    public UserInfoFragment() {
    }

    public static UserInfoFragment getFragment(int a, String user_id) {
        Bundle bundle = new Bundle(1);
        bundle.putInt("info", a);
        bundle.putString("user_id", user_id);
        UserInfoFragment f = new UserInfoFragment();
        f.setArguments(bundle);
        return f;

    }


    public static UserInfoFragment getFragment(int a, UserInfo userInfo) {
        Bundle bundle = new Bundle(1);

        bundle.putInt("info", a);
        bundle.putParcelable("user", userInfo);

        UserInfoFragment f = new UserInfoFragment();
        f.setArguments(bundle);
        return f;

    }

    public static UserInfoFragment getFragment(int a, ArrayList<UserInfo> listInfo) {
        Bundle bundle = new Bundle(1);
        bundle.putInt("info", a);
        bundle.putParcelableArrayList("listuser", listInfo);
        UserInfoFragment f = new UserInfoFragment();
        f.setArguments(bundle);
        return f;

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        param = new String[4];
        int a = getArguments().getInt("info");
        param[0] = getActivity().getSharedPreferences("token_info", getActivity().MODE_PRIVATE).getString("token", "");
        //param[1] = getActivity().getSharedPreferences("token_info", getActivity().MODE_PRIVATE).getString("user_id", "");
        param[1] = getArguments().getString("user_id");


        View v = inflater.inflate(R.layout.user_info_fragment, container, false);

        actionbar = (Toolbar) v.findViewById(R.id.actionbar);


        listView = (PullToRefreshListView) v.findViewById(R.id.list_users);

        if (Connectivity.isConnectedFast(getActivity())) {
            param[2] = "" + 50;

        } else {
            param[2] = "" + 20;

        }

        param[3] = "" + 0;
        Bundle b = new Bundle();

        Map<String, String> options = new LinkedHashMap<>();
        options.put("access_token", param[0]);
        options.put("user_id", param[1]);
        options.put("count", param[2]);
        options.put("offset", param[3]);

        b.putStringArray("param", param);


        switch (a) {
            case 0:
                setCustomTitle("Follower Info");
                App.getApi().userFollower(options, callbackFollower);
                break;
            case 1:
                setCustomTitle("Following Info");
                App.getApi().userFollowing(options, callbackFollowing);
                break;
            case 2:
                setCustomTitle("Friend Info");
                App.getApi().userGetFriends(options, callbackFriend);
                break;
            case 3:
                setCustomTitle("Users likes Comment");

                break;
            case 4:

                ProfilesUsers profileFragment = new ProfilesUsers();
                b = new Bundle();

                b.putParcelable("user", getArguments().getParcelable("user"));
                profileFragment.setArguments(b);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_users, profileFragment).commit();
                setActionBar("Users likes Comment");

                break;
            case 5:
                item = getArguments().getParcelableArrayList("listuser");
                adapter = new UsersAdapter(getActivity(), item);
                listView.setAdapter(adapter);
                break;
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ProfilesUsers profileFragment = new ProfilesUsers();
                Bundle b = new Bundle();
                b.putParcelable("user", item.get(i - 1));
                profileFragment.setArguments(b);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_users, profileFragment).commit();
            }
        });

        return v;
    }


    public void setCustomTitle(String title) {
        LayoutInflater mInflater = LayoutInflater.from(getActivity());
        View customBar = mInflater.inflate(R.layout.actionbar_custom, actionbar);
        TextView textTitle = (TextView) customBar.findViewById(R.id.title);
        textTitle.setText(title);

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
            adapter = new UsersAdapter(getActivity(), item);
            listView.setAdapter(adapter);


        }

        @Override
        public void failure(RetrofitError error) {

        }
    };

    private Callback<JSONArray> callbackFollower = new Callback<JSONArray>() {
        @Override
        public void success(JSONArray jsonArray, Response response) {
            item = new ArrayList<>();
            JSONArray json = jsonArray.optJSONObject(0).optJSONArray("followers");
            for (int i = 0; i < json.length(); i++) {
                UserInfo userInfo = new UserInfo();
                userInfo.setUser_id(json.optJSONObject(i).optInt("user_id"));
                userInfo.setName(json.optJSONObject(i).optString("name"));
                userInfo.setSurname(json.optJSONObject(i).optString("surname"));
                userInfo.setPhoto_small(json.optJSONObject(i).optString("photo_small"));
                userInfo.setRelation(json.optJSONObject(i).optInt("relation"));
                item.add(userInfo);
            }
            adapter = new UsersAdapter(getActivity(), item);
            listView.setAdapter(adapter);


        }

        @Override
        public void failure(RetrofitError error) {

        }
    };


    private Callback<JSONArray> callbackFollowing = new Callback<JSONArray>() {
        @Override
        public void success(JSONArray jsonArray, Response response) {
            item = new ArrayList<>();
            JSONArray json = jsonArray.optJSONObject(0).optJSONArray("following");
            for (int i = 0; i < json.length(); i++) {
                UserInfo userInfo = new UserInfo();

                userInfo.setUser_id(json.optJSONObject(i).optInt("user_id"));
                userInfo.setName(json.optJSONObject(i).optString("name"));
                userInfo.setSurname(json.optJSONObject(i).optString("surname"));
                userInfo.setPhoto_small(json.optJSONObject(i).optString("photo_small"));
                userInfo.setRelation(json.optJSONObject(i).optInt("relation"));
                item.add(userInfo);

            }
            adapter = new UsersAdapter(getActivity(), item);
            listView.setAdapter(adapter);


        }

        @Override
        public void failure(RetrofitError error) {

        }
    };


}
