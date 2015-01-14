package social.evenet.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.AsyncHttpGet;
import com.koushikdutta.async.http.AsyncHttpResponse;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.pkmmte.view.CircularImageView;

import org.json.JSONArray;

import java.util.ArrayList;

import social.evenet.R;
import social.evenet.activity.MeetupDetailActivity;
import social.evenet.activity.MenuNewActivity;
import social.evenet.activity.PlaceActivity;
import social.evenet.activity.UsersInfo;
import social.evenet.db.Events;
import social.evenet.db.Notification;
import social.evenet.fragment.FeedDetailFragment;
import social.evenet.helper.SupportInfo;

/**
 * Created by Александр on 21.09.2014.
 */
public class NotificationAdapter extends BaseAdapter implements View.OnClickListener {

    private Handler handler = new Handler();
    private Context ctx;
    private LayoutInflater lInflater;
    private ArrayList<Notification> objects;

    DisplayImageOptions options = new DisplayImageOptions.Builder()
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .resetViewBeforeLoading(true)
            .considerExifParams(true).build();

    public NotificationAdapter(Context context, ArrayList<Notification> users) {
        ctx = context;
        objects = users;
        lInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {

        return objects.size();
    }

    @Override
    public Object getItem(int i) {
        return objects.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {

        final ViewHolder holder;
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.list_notification, viewGroup, false);
            holder = new ViewHolder();
            holder.icon = (CircularImageView) view.findViewById(R.id.icon_user);
            holder.name = (TextView) view.findViewById(R.id.name_user);

            holder.ok = (ImageView) view.findViewById(R.id.access);
            holder.go = (Button) view.findViewById(R.id.going);
            holder.no = (Button) view.findViewById(R.id.not_going);
            holder.user_notification = (LinearLayout) view.findViewById(R.id.linear_user_notification);
            holder.meetup_notification = (LinearLayout) view.findViewById(R.id.linear_meet_notification);
            holder.content_linear=(LinearLayout) view.findViewById(R.id.linear_content);
            holder.content = (TextView) view.findViewById(R.id.content);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();

        }

        Notification list_de = getDefaultList(position);
        holder.content.setText(list_de.getNotification_type());
        holder.name.setText(list_de.getUserInfo().getName() + " " + list_de.getUserInfo().getSurname());
      //  holder.icon.setImageDrawable(ctx.getResources().getDrawable(R.drawable.ic_launcher));
        if (list_de.getUserInfo().getPhoto_small() != null) {
           ImageLoader.getInstance().displayImage(list_de.getUserInfo().getPhoto_small(), holder.icon, options);
        }

        holder.icon.setTag(position);
        holder.icon.setOnClickListener(this);
        holder.go.setTag(position);
        holder.no.setTag(position);
        holder.go.setOnClickListener(this);
        holder.no.setOnClickListener(this);
        holder.ok.setTag(position);
        holder.ok.setOnClickListener(this);

        holder.content_linear.setTag(position);
        holder.content_linear.setOnClickListener(this);

     if(list_de.getNotification_type().equals("0")){
            holder.content.setText("Follower request");
            holder.meetup_notification.setVisibility(View.GONE);
            holder.user_notification.setVisibility(View.VISIBLE);
            holder.ok.setImageDrawable(ctx.getResources().getDrawable(R.drawable.ic_follower_you));

        }
        else if(list_de.getNotification_type().equals("1")){
            holder.content.setText("Follower request accepted");
            holder.meetup_notification.setVisibility(View.GONE);
            holder.user_notification.setVisibility(View.VISIBLE);
            holder.ok.setImageDrawable(ctx.getResources().getDrawable(R.drawable.ic_friend_follow));

        }
        else if(list_de.getNotification_type().equals("2")){
            holder.content.setText("Meetup invitation is accepted "+list_de.getMeetups().getMeetup_name());
            holder.meetup_notification.setVisibility(View.GONE);
            holder.user_notification.setVisibility(View.GONE);

        }
        else if(list_de.getNotification_type().equals("3")){
            holder.content.setText("Meetup invitation is declined "+list_de.getMeetups().getMeetup_name());
            holder.meetup_notification.setVisibility(View.GONE);
            holder.user_notification.setVisibility(View.GONE);
        }
        else if (list_de.getNotification_type().equals("4")) {
            holder.content.setText("Meetup invitation "+list_de.getMeetups().getMeetup_name());
            holder.user_notification.setVisibility(View.GONE);
            holder.meetup_notification.setVisibility(View.VISIBLE);
        }
     else if (list_de.getNotification_type().equals("5")) {
         if(list_de.getEvent()!=null)
         holder.content.setText("Recommended Event "+list_de.getEvent().getEvent_title());
         holder.user_notification.setVisibility(View.GONE);
         holder.meetup_notification.setVisibility(View.GONE);
     }
      else if(list_de.getNotification_type().equals("6")){
            holder.content.setText("Recommended Place"+list_de.getPlace().getPlace_title());
            holder.meetup_notification.setVisibility(View.GONE);
            holder.user_notification.setVisibility(View.GONE);

        }
     else if(list_de.getNotification_type().equals("7")){
         holder.content.setText("Advertisement "+list_de.getPlace().getPlace_title());
         holder.meetup_notification.setVisibility(View.GONE);
         holder.user_notification.setVisibility(View.GONE);
     }
     else if(list_de.getNotification_type().equals("8")){
         holder.content.setText("MeetupTimeChanged "+list_de.getMeetups().getMeetup_name());
         holder.meetup_notification.setVisibility(View.GONE);
         holder.user_notification.setVisibility(View.GONE);
     }
     else if(list_de.getNotification_type().equals("9")){
         holder.content.setText("MeetupPlaceChanged "+list_de.getMeetups().getMeetup_name());
         holder.meetup_notification.setVisibility(View.GONE);
         holder.user_notification.setVisibility(View.GONE);
     }
     else if(list_de.getNotification_type().equals("10")){
         holder.content.setText("MeetupEventChanged "+list_de.getMeetups().getMeetup_name());
         holder.meetup_notification.setVisibility(View.GONE);
         holder.user_notification.setVisibility(View.GONE);
     }
     else if(list_de.getNotification_type().equals("11")){
         holder.content.setText("MeetupPeopleLeft "+list_de.getMeetups().getMeetup_name());
         holder.meetup_notification.setVisibility(View.GONE);
         holder.user_notification.setVisibility(View.GONE);
     }
     else if(list_de.getNotification_type().equals("12")){
         holder.content.setText("MeetupPeopleJoined "+list_de.getMeetups().getMeetup_name());
         holder.meetup_notification.setVisibility(View.GONE);
         holder.user_notification.setVisibility(View.GONE);
     }
     else if(list_de.getNotification_type().equals("13")){
         holder.content.setText("FoursquareCheckin ");
         holder.meetup_notification.setVisibility(View.GONE);
         holder.user_notification.setVisibility(View.GONE);
     }
     else if(list_de.getNotification_type().equals("14")){
         holder.content.setText("VKPossibleFriend ");
         holder.meetup_notification.setVisibility(View.GONE);
         holder.user_notification.setVisibility(View.GONE);
     }
     else if(list_de.getNotification_type().equals("15")){
         holder.content.setText("FacebookPossibleFriend "+list_de.getMeetups().getMeetup_name());
         holder.meetup_notification.setVisibility(View.GONE);
         holder.user_notification.setVisibility(View.GONE);
     }
     else if(list_de.getNotification_type().equals("16")){
         holder.content.setText("RepostedEventLiked "+list_de.getEvent().getEvent_title());
         holder.meetup_notification.setVisibility(View.GONE);
         holder.user_notification.setVisibility(View.GONE);
     }
     else if(list_de.getNotification_type().equals("17")){
         holder.content.setText("CommentMention "+list_de.getMeetups().getMeetup_name());
         holder.meetup_notification.setVisibility(View.GONE);
         holder.user_notification.setVisibility(View.GONE);
     }
     else if(list_de.getNotification_type().equals("18")){
         holder.content.setText("EventMention "+list_de.getEvent().getEvent_title());
         holder.meetup_notification.setVisibility(View.GONE);
         holder.user_notification.setVisibility(View.GONE);
     }
     else if(list_de.getNotification_type().equals("19")){
         holder.content.setText("Reminder "+list_de.getMeetups().getMeetup_name());
         holder.meetup_notification.setVisibility(View.GONE);
         holder.user_notification.setVisibility(View.GONE);
     }
     else if(list_de.getNotification_type().equals("20")){
         holder.content.setText("EvenetRecommendationPlace  "+list_de.getPlace().getPlace_title());
         holder.meetup_notification.setVisibility(View.GONE);
         holder.user_notification.setVisibility(View.GONE);
     }
     else if(list_de.getNotification_type().equals("21")){
         holder.content.setText("EvenetRecommendationEvent "+list_de.getEvent());
         holder.meetup_notification.setVisibility(View.GONE);
         holder.user_notification.setVisibility(View.GONE);
     }
     else if(list_de.getNotification_type().equals("22")){
         holder.content.setText("EvenetRecommendationUser "+list_de.getEvent());
         holder.meetup_notification.setVisibility(View.GONE);
         holder.user_notification.setVisibility(View.GONE);
     }
     else if(list_de.getNotification_type().equals("23")){
         holder.content.setText("MyEventLiked "+list_de.getEvent());
         holder.meetup_notification.setVisibility(View.GONE);
         holder.user_notification.setVisibility(View.GONE);
     }
     else if(list_de.getNotification_type().equals("24")){
         holder.content.setText("MyEventCommented "+list_de.getEvent().getEvent_title());
         holder.meetup_notification.setVisibility(View.GONE);
         holder.user_notification.setVisibility(View.GONE);
     }
     else if(list_de.getNotification_type().equals("25")){
         holder.content.setText("MyEventChekedIn "+list_de.getEvent().getEvent_title());
         holder.meetup_notification.setVisibility(View.GONE);
         holder.user_notification.setVisibility(View.GONE);
     }
     else if(list_de.getNotification_type().equals("26")){
         holder.content.setText("MyEventPublished "+list_de.getEvent().getEvent_title());
         holder.meetup_notification.setVisibility(View.GONE);
         holder.user_notification.setVisibility(View.GONE);
     }
     else if(list_de.getNotification_type().equals("27")){
         holder.content.setText("TrustedAccountAwarded "+list_de.getUserInfo().getName()+" "+list_de.getUserInfo().getSurname());
         holder.meetup_notification.setVisibility(View.GONE);
         holder.user_notification.setVisibility(View.GONE);
     }
     else if(list_de.getNotification_type().equals("28")){
         holder.content.setText("SuggestedEvent "+list_de.getEvent().getEvent_title());
         holder.meetup_notification.setVisibility(View.GONE);
         holder.user_notification.setVisibility(View.GONE);
     }
     else if(list_de.getNotification_type().equals("29")){
         holder.content.setText("CheckinMentioned "+list_de.getUserInfo().getName()+" "+list_de.getUserInfo().getSurname());
         holder.meetup_notification.setVisibility(View.GONE);
         holder.user_notification.setVisibility(View.GONE);
     }




        return view;
    }


    private Notification getDefaultList(int position) {
        return ((Notification) getItem(position));
    }

    private boolean flag = true;

    private boolean flag_friends = true;

    private View view;

    @Override
    public void onClick(View v) {
        int position = (Integer) v.getTag();
        view = v;
        String url;
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                ImageView  image =(ImageView)view;
                if (!flag) {
                    image.setImageDrawable(ctx.getResources().getDrawable(R.drawable.ic_follower_you));
                } else {
                    image.setImageDrawable(ctx.getResources().getDrawable(R.drawable.ic_friend_follow));

                }
            }
        };
        switch (v.getId()) {
            case R.id.access:
                if (flag) {
                    if (getDefaultList(position).getNotification_type().equals("0")) {
                        url = SupportInfo.url + SupportInfo.user_follow + ctx.getSharedPreferences("token_info", ctx.MODE_PRIVATE).getString("token", "") + "&user_id=" + getDefaultList(position).getUserInfo().getUser_id();
                        AsyncHttpClient.getDefaultInstance().executeJSONArray(new AsyncHttpGet(url), new AsyncHttpClient.JSONArrayCallback() {
                            @Override
                            public void onCompleted(Exception e, AsyncHttpResponse source, JSONArray result) {
                                if (e == null) {
                                    flag = false;
                                    Message msg = new Message();
                                    msg.obj = flag;
                                    handler.sendMessage(msg);
                                }
                            }
                        });
                        //   v.setBackgroundColor(ctx.getResources().getColor(R.color.blue));

                    } else if (getDefaultList(position).getNotification_type().equals("")) {

                    }
                } else {
                    if (getDefaultList(position).getNotification_type().equals("1")) {

                        url = SupportInfo.url + SupportInfo.un_user_follow + ctx.getSharedPreferences("token_info", ctx.MODE_PRIVATE).getString("token", "") + "&user_id=" + getDefaultList(position).getUserInfo().getUser_id();
                        AsyncHttpClient.getDefaultInstance().executeJSONArray(new AsyncHttpGet(url), new AsyncHttpClient.JSONArrayCallback() {
                            @Override
                            public void onCompleted(Exception e, AsyncHttpResponse source, JSONArray result) {
                                if (e == null) {
                                    flag = true;
                                    Message msg = new Message();
                                    msg.obj = flag;
                                    handler.sendMessage(msg);
                                }
                            }
                        });
                        ///   v.setBackgroundColor(ctx.getResources().getColor(R.color.red));
                    } else if (getDefaultList(position).getNotification_type().equals("")) {

                    }

                }

                break;
            case R.id.icon_user:
                Intent intent = new Intent(ctx, UsersInfo.class);
                Notification userInfo = getDefaultList(position);
                intent.putExtra("number", 4);
                intent.putExtra("user", userInfo.getUserInfo());
                intent.putExtra("user_id", "" + userInfo.getUserInfo().getUser_id());
                ctx.startActivity(intent);
                break;
            case R.id.going:
                url = SupportInfo.url+SupportInfo.meetup_set_status+ctx.getSharedPreferences("token_info", ctx.MODE_PRIVATE).getString("token", "")+"&meetup_id="+ getDefaultList(position).getMeetups().getMeetup_id()+"&status=0";
                AsyncHttpClient.getDefaultInstance().executeJSONArray(new AsyncHttpGet(url), new AsyncHttpClient.JSONArrayCallback() {
                    @Override
                    public void onCompleted(Exception e, AsyncHttpResponse source, JSONArray result) {
                        if (e == null) {
                        System.out.print("");
                        }
                    }
                });
               break;
            case R.id.not_going:
                url = SupportInfo.url+SupportInfo.meetup_set_status+ctx.getSharedPreferences("token_info", ctx.MODE_PRIVATE).getString("token", "")+"&meetup_id="+ getDefaultList(position).getMeetups().getMeetup_id()+"&status=2";
                AsyncHttpClient.getDefaultInstance().executeJSONArray(new AsyncHttpGet(url), new AsyncHttpClient.JSONArrayCallback() {
                    @Override
                    public void onCompleted(Exception e, AsyncHttpResponse source, JSONArray result) {
                        if (e == null) {
                            System.out.print("");
                        }
                    }
                });
                break;
            case R.id.linear_content:
                    if(getDefaultList(position).getNotification_type().equals("2")){
                        Intent intent1=new Intent(ctx, MeetupDetailActivity.class);
                        intent1.putExtra("meetup_id",getDefaultList(position).getMeetups().getMeetup_id());
                       ctx.startActivity(intent1);
                     }
                else if(getDefaultList(position).getNotification_type().equals("4")){
                    Intent intent1=new Intent(ctx, MeetupDetailActivity.class);
                        intent1.putExtra("meetup_id",getDefaultList(position).getMeetups().getMeetup_id());
                        ctx.startActivity(intent1);
                }
               else if(getDefaultList(position).getNotification_type().equals("8")){
                        Intent intent1=new Intent(ctx, MeetupDetailActivity.class);
                        intent1.putExtra("meetup_id",getDefaultList(position).getMeetups().getMeetup_id());
                           ctx.startActivity(intent1);
                    }
                    else if(getDefaultList(position).getNotification_type().equals("9")){
                        Intent intent1=new Intent(ctx, MeetupDetailActivity.class);
                        intent1.putExtra("meetup_id",getDefaultList(position).getMeetups().getMeetup_id());
                           ctx.startActivity(intent1);
                    }
                    else if(getDefaultList(position).getNotification_type().equals("10")){
                        Intent intent1=new Intent(ctx, MeetupDetailActivity.class);
                        intent1.putExtra("meetup_id",getDefaultList(position).getMeetups().getMeetup_id());
                           ctx.startActivity(intent1);
                    }
                    else if(getDefaultList(position).getNotification_type().equals("11")){
                        Intent intent1=new Intent(ctx, MeetupDetailActivity.class);
                        intent1.putExtra("meetup_id",getDefaultList(position).getMeetups().getMeetup_id());
                           ctx.startActivity(intent1);
                    }
                    else if(getDefaultList(position).getNotification_type().equals("12")){
                        Intent intent1=new Intent(ctx, MeetupDetailActivity.class);
                        intent1.putExtra("meetup_id",getDefaultList(position).getMeetups().getMeetup_id());
                           ctx.startActivity(intent1);
                    }
                    else if(getDefaultList(position).getNotification_type().equals("5")){

                        Bundle b = new Bundle();
                        Events events=new Events();
                        events.setEvent(getDefaultList(position).getEvent());
                        b.putParcelable("events", events);
                        b.putString("count", "" + 1);
                        MenuNewActivity aa = (MenuNewActivity)ctx;
                   /*     aa.hide();
                        aa.showFragment(4, FeedDetailFragment.getFeedFragment(b));
                    */
                        Intent intent1=new Intent(ctx, MeetupDetailActivity.class);
                        intent1.putExtra("item",getDefaultList(position).getEvent());

                    }
                    else if(getDefaultList(position).getNotification_type().equals("6")){


                        Intent intent1=new Intent(ctx, PlaceActivity.class);
                        intent1.putExtra("place_id",getDefaultList(position).getPlace().getPlace_id());
                           ctx.startActivity(intent1);
                    }
                    else if(getDefaultList(position).getNotification_type().equals("20")){
                        Intent intent1=new Intent(ctx, PlaceActivity.class);
                        intent1.putExtra("item",getDefaultList(position).getPlace().getPlace_id());
                           ctx.startActivity(intent1);
                    }


                break;

        }
    }

    private static class ViewHolder {
        public TextView name;
        public CircularImageView icon;
        public ImageView ok;
        public TextView content;
        public LinearLayout user_notification;
        public LinearLayout meetup_notification;
        public Button go;
        public Button no;
        public LinearLayout content_linear;


    }

}