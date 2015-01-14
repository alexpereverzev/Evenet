package social.evenet.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import social.evenet.R;
import social.evenet.activity.App;
import social.evenet.activity.MeetUpAddActivity;
import social.evenet.activity.MeetupDetailActivity;
import social.evenet.db.Place;
import social.evenet.db.UserInfo;
import social.evenet.helper.SupportInfo;


/**
 * A simple {@link android.app.Fragment} subclass.
 */
public class MeetupCreateFragmentSeven extends RefreshableFragment {

    private ImageView imageView;
    private TextView name;
    private TextView time;
    private TextView date;
    private TextView address;
    private ImageView disable_address;
    private TextView invite_friends;
    private ImageView create;
    private String[] param;
    private String addr="";
    private int meetup_id;
    private String friends_id;
    private Calendar calendar;
    private SimpleDateFormat formatter;
    private Handler mhandler = new Handler();

    public static Fragment getMeetupCreateFragmetSeven(Bundle b) {
        MeetupCreateFragmentSeven fragment = new MeetupCreateFragmentSeven();
        fragment.setArguments(b);
        return fragment;
    }

    public MeetupCreateFragmentSeven() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_create_meetup_seven, container, false);
        imageView = (ImageView) v.findViewById(R.id.image_meetup);
        name = (TextView) v.findViewById(R.id.name_meetup);
        setActionBar("Create Meetup");
        calendar=Calendar.getInstance();
        formatter = new SimpleDateFormat("dd MMMM, yyyy");
        try {
            Date start= formatter.parse(getArguments().getString("data"));
            calendar.setTime(start);

            int mHour=Integer.parseInt(getArguments().getString("hour"));
            int mMinute=Integer.parseInt(getArguments().getString("minute"));


            calendar.set(Calendar.HOUR, mHour);
            calendar.set(Calendar.MINUTE, mMinute);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        name.setText(getArguments().getString("name"));

        setImageView(getArguments().getInt("image"), imageView);

        date = (TextView) v.findViewById(R.id.data_meetup);

        time=(TextView) v.findViewById(R.id.time_meetup);

        date.setText(getArguments().getString("data"));

        time.setText(getArguments().getString("time"));

        address=(TextView) v.findViewById(R.id.address_meetup);

        if(getArguments().getStringArray("address")!=null){
            String [] a=getArguments().getStringArray("address");
            String ad="";
            if(a[0]!=null){
                ad=ad+" "+a[0];
            }
            if(a[1]!=null){
                ad=ad+" "+a[1];
            }
            if(a[2]!=null){
                ad=ad+" "+a[2];
            }

            address.setText(ad);

        }
        else {
            Place place= getArguments().getParcelable("place");
            address.setText(place.getPlace_title());
        }
        disable_address=(ImageView) v.findViewById(R.id.disable_address);

        disable_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
           //     startActivity(new Intent(getActivity(), MapsActivity.class));
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_meetup, MeetupCreateFragmentFive.getMeetupCreateFragmetFive(getArguments())).commit();
            }
        });

        invite_friends=(TextView) v.findViewById(R.id.count_friends);

        invite_friends.setText(getArguments().getString("count_user"));

        create=(ImageView) v.findViewById(R.id.create_meetup);

        ArrayList<UserInfo> e = getArguments().getParcelableArrayList("user_list");
        Iterator<UserInfo> iterator = e.iterator();
        String invite_id = "";
        while (iterator.hasNext()) {

            UserInfo userInfo = iterator.next();

            if (userInfo.getSurname() != null)
                invite_id = invite_id + userInfo.getUser_id() + ",";
        }
          invite_id = invite_id.substring(0, invite_id.length() - 1);

        friends_id = invite_id;

        param = new String[3];
        param[0] = getActivity().getSharedPreferences("token_info", getActivity().MODE_PRIVATE).getString("token", "");

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                Map<String,String> params=new LinkedHashMap<String, String>();
                params.put("access_token",param[0]);
                params.put("name",getArguments().getString("name"));
                long timestart = calendar.getTimeInMillis() / 1000L;
                params.put("startdate",""+timestart);
                if(getArguments().getString("lat")!=null) {
                    params.put("latitude", getArguments().getString("lat"));
                    params.put("longitude", getArguments().getString("lon"));
                    params.put("address",addr);

                }
                else {
                    Place place = getArguments().getParcelable("place");
                    params.put("place_id",""+place.getPlace_id());
                 //   params.put("latitude", place.getLatitude());
                //    params.put("longitude", place.getLongitude());
                //    params.put("address", place.getPlace_address());

                  }


                App.getApi().createMeetup(params,new Callback<JSONArray>() {
                    @Override
                    public void success(JSONArray jsonArray, Response response) {
                        try {
                            meetup_id = jsonArray.getJSONObject(0).getInt("meetup_id");
                            if(meetup_id!=0){

                                    param[2] = "" + meetup_id;
                                    param[1] = friends_id;
                                    Map<String,String> p=new LinkedHashMap<String, String>();
                                    p.put("access_token",param[0]);
                                    p.put("friends",""+friends_id);
                                    p.put("meetup_id","" + meetup_id);
                                    App.getApi().addMeetup(p,callback);
                                    Bundle b = new Bundle();
                                    b.putStringArray("param", param);

                            }

                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                });

            }
        });

        return v;
    }




    private Callback<JSONArray> callback=new Callback<JSONArray>() {
        @Override
        public void success(JSONArray jsonArray, Response response) {
            String [] res=new String[2];
            if(jsonArray.length()!=0){
                JSONObject jsonObject1 =jsonArray.optJSONObject(0);
                res[0]=jsonObject1.optString("response_code");
                res[1]=jsonObject1.optString("response_message");
            }
            if (res.length != 0) {
            Intent intent = new Intent(getActivity(), MeetupDetailActivity.class);
            intent.putExtra("meetup_id", meetup_id);
            startActivity(intent);
            getActivity().finish();}

        }

        @Override
        public void failure(RetrofitError error) {

        }
    };

    public void setActionBar(String title) {


        MeetUpAddActivity activity=(MeetUpAddActivity)getActivity();
        View custom=activity.getCustomBar();
        ImageView disable_actionbar = (ImageView) custom.findViewById(R.id.disable_actionbar);

        TextView ti=(TextView) custom.findViewById(R.id.title);

        ti.setText(title);

        TextView done = (TextView) custom.findViewById(R.id.done_actionbar);

        done.setVisibility(View.INVISIBLE);

        disable_actionbar.setVisibility(View.VISIBLE);


    }

}
