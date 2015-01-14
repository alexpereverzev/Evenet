package social.evenet.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import social.evenet.R;
import social.evenet.activity.App;
import social.evenet.activity.CommentActivity;
import social.evenet.activity.PlaceActivity;
import social.evenet.db.Events;
import social.evenet.helper.SupportInfo;
import social.evenet.helper.Util;
import social.evenet.widget.CustomImageView;

/**
 * Created by Alexander on 05.09.2014.
 */
public class FeedAdapterNew extends BaseAdapter {
    private Context ctx;
    private LayoutInflater lInflater;
    private List<Events> objects;
    private Events events;
    private boolean active=false;

    DisplayImageOptions options = new DisplayImageOptions.Builder()
           .cacheInMemory(true)
            .resetViewBeforeLoading(true)
            .imageScaleType(ImageScaleType.EXACTLY)
            .cacheOnDisk(true)
           // .delayBeforeLoading(300)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .build();

    public FeedAdapterNew(Context context, List<Events> users) {
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
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
    final    ViewHolder holder;
        View view = convertView;
        // if (view == null) {

        // holder = new ViewHolder();
        if (view == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.custom_feed_full, null);
            holder.icon_feed = (CustomImageView) view.findViewById(R.id.icon_feed);
            holder.parent= (LinearLayout) view.findViewById(R.id.parent_feed);
            holder.child_lienar = (LinearLayout) view.findViewById(R.id.child_linear);
            holder.icon_title = (ImageView) view.findViewById(R.id.icon_title);
            holder.label_category = (TextView) view.findViewById(R.id.label_category);
            holder.icon_like = (ImageView) view.findViewById(R.id.icon_like);
            holder.comment = (ImageView) view.findViewById(R.id.label_checkout);
            holder.title_text_info = (TextView) view.findViewById(R.id.title_text_info);
            holder.text_info = (TextView) view.findViewById(R.id.text_info);
            holder.category=(TextView) view.findViewById(R.id.place_category);
            holder.place_title=(TextView) view.findViewById(R.id.place_name);
            holder.count_comments=(TextView) view.findViewById(R.id.count_comment);
            holder.count_likes = (TextView) view.findViewById(R.id.count_likes);
            holder.label_distance = (TextView) view.findViewById(R.id.label_distance);
            holder.count_checkin=(TextView) view.findViewById(R.id.checkin) ;
            TextView textView=(TextView) view.findViewById(R.id.label_time);
            holder.label_time = textView;
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();

        }
        events = getEvent(position);

        Typeface typeface = Typeface.createFromAsset(ctx.getAssets(),
                "fonts/HelveticaNeue.ttf");

        Typeface typeface_light = Typeface.createFromAsset(ctx.getAssets(),
                "fonts/HelveticaNeueLight.ttf");

        holder.label_distance.setTypeface(typeface_light);

        holder.text_info.setTypeface(typeface);
        holder.count_likes.setTypeface(typeface);
        holder.count_comments.setTypeface(typeface);
        holder.count_checkin.setTypeface(typeface);
        if (!events.getEvent().getEvent_description().isEmpty()) {
            if (events.getEvent().getEvent_description().length() > 100) {
                String data = events.getEvent().getEvent_description().substring(0, 100);
                holder.text_info.setText(data + "...");
            } else {
                holder.text_info.setText(events.getEvent().getEvent_description());
            }
        }
        else holder.text_info.setText("");


        holder.title_text_info.setText(events.getEvent().getEvent_title());
        final Intent intent = new Intent(ctx, CommentActivity.class);
        intent.putExtra("id", events.getEvent().getEvent_id());
        holder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ctx.startActivity(intent);
            }
        });
        if (events.getLikes_count() != 0) {
            holder.count_likes.setText("" + events.getLikes_count());
        }

        if (events.getCheckins_count() != 0) {
            holder.count_checkin.setText("" + events.getCheckins_count());
            holder.count_checkin.setVisibility(View.VISIBLE);
        }
        else {
            holder.count_checkin.setText(" ");

        }


        holder.child_lienar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ctx, PlaceActivity.class);
                intent.putExtra("place_id",events.getEvent().getPlace().getPlace_id());
                ctx.startActivity(intent);
            }
        });


        holder.icon_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                events = getEvent(position);
                Map<String,String> params = new LinkedHashMap<String, String>();

                params.put("access_token", ctx.getSharedPreferences("token_info", ctx.MODE_PRIVATE).getString("token", ""));
                params.put("event_id", ""+events.getEvent().getEvent_id());
                if (events.getLiked_by_you() != 1) {



                    App.getApi().eventLike(params,new Callback<JSONArray>() {
                        @Override
                        public void success(JSONArray jsonArray, Response response) {
                            String res = "";
                            try {
                                res = jsonArray.getJSONObject(0).getString("response_message");
                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }
                            if (res.equals("OK")) {
                                if(!holder.count_likes.getText().equals("")) {
                                    int s = Integer.parseInt(holder.count_likes.getText().toString()) + 1;
                                    holder.count_likes.setText("" + s);
                                }
                                else {
                                    holder.count_likes.setText("" + 1);
                                }
                            }
                            events.setLiked_by_you(1);
                            holder.icon_like.setImageDrawable(ctx.getResources().getDrawable(R.drawable.ic_feed_icon_like_big_active));
                        }

                        @Override
                        public void failure(RetrofitError error) {

                        }
                    });
                }
                else {
                   App.getApi().eventUnLike(params,new Callback<JSONArray>() {
                       @Override
                       public void success(JSONArray jsonArray, Response response) {
                           String res = "";
                           try {
                               res = jsonArray.getJSONObject(0).getString("response_message");
                           } catch (JSONException e1) {
                               e1.printStackTrace();
                           }
                           if (res.equals("OK")) {

                               int s = Integer.parseInt(holder.count_likes.getText().toString()) - 1;
                               if(Integer.parseInt(holder.count_likes.getText().toString())==1){
                                   holder.count_likes.setText("");
                               }
                               else {
                                   holder.count_likes.setText("" + s);
                               }

                           }
                           events.setLiked_by_you(0);
                           holder.icon_like.setImageDrawable(ctx.getResources().getDrawable(R.drawable.ic_feed_icon_like_big_default));
                       }

                       @Override
                       public void failure(RetrofitError error) {

                       }
                   });
                }
            }
        });


        if (events.getEvent().getMainAttachment().getUrl() != null) {
             holder.icon_feed.post(new Runnable() {
                @Override
                public void run() {
                    ImageAware imageAware = new ImageViewAware(holder.icon_feed, false);
                    ImageLoader.getInstance().displayImage(events.getEvent().getMainAttachment().getUrl(),imageAware, options);
                }
            });
        }

        double fin_cor = events.getEvent().getPlace().getLatitude();
        double fin_long = events.getEvent().getPlace().getLongitude();
        String distance = "" + Util.CalculationByDistance(getEvent(position).getCurrent_coordinat_latitude(), getEvent(position).getCurrent_coordinat_longutide(), fin_cor, fin_long);
        double di = Util.CalculationByDistance(getEvent(position).getCurrent_coordinat_latitude(), getEvent(position).getCurrent_coordinat_longutide(), fin_cor, fin_long);
        if (di < 1) {
            double dist=di*1000;
            holder.label_distance.setText(" " + (int)dist + "  m");
        } else {
            holder.label_distance.setText(" " + distance + " km");
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String begin = Util.convertToWeek(sdf.format(new Date(Long.parseLong(events.getEvent().getBegins()) * 1000)));
        String end = Util.convertToWeek(sdf.format(new Date(Long.parseLong(events.getEvent().getEnds()) * 1000)));
        String when_start=" ";
        String when_end=" ";
        DateTimeFormatter formatter1 = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        DateTime endDate = DateTime.parse(sdf.format(new Date(Long.parseLong(events.getEvent().getEnds()) * 1000)), formatter1);
        DateTime startDate = DateTime.parse(sdf.format(new Date(Long.parseLong(events.getEvent().getBegins()) * 1000)), formatter1);
        String [] begin_date=begin.split(",");
        String [] end_date=end.split(",");
        for(int i=0; i<begin_date.length; i++){
            String s=begin_date[i];
            if((s.contains("-"))||(s.contains("0"))||(s.length()==2)){
                continue;
            }
            else {
                when_start=when_start+s;

                    break;

            }
        }

        for(int i=0; i<end_date.length; i++){
            String s=end_date[i];
            if(s.contains("0")){
                continue;
            }
            else if(s.contains("-")){
                s=s.replace("-","");
                when_end=when_end+s+" ago";
                break;
            }
            else {
                when_end=when_end+s;
                break;
            }
        }
        holder.label_time.setVisibility(View.VISIBLE);
        Typeface tf = Typeface.createFromAsset(ctx.getAssets(),
                "fonts/HelveticaNeueMedium.ttf");
        holder.label_time.setTypeface(tf);
        holder.parent.setVisibility(View.VISIBLE);
        if(when_start.length()>1){
            if(when_start.contains("1 day")){
                int i = startDate.getDayOfWeek();
                Calendar c = Calendar.getInstance();
                c.set(2011, 7, 1, 0, 0, 0);
                c.add(Calendar.DAY_OF_MONTH, i - 1);
                String s1 = String.format("%tA", c)+", ";
              int starth=startDate.getHourOfDay();
                int startm=startDate.getMinuteOfHour();

                if(startm<10)
                   when_start=s1+""+starth+"h 0"+startm+"m";
                else  when_start=s1+""+starth+"h "+startm+"m";

            }
            if(when_start.contains(" hour")){
               when_start= when_start.replace(" hour","h");
            }

            holder.label_time.setText("Starts " + when_start);
        }
       else if(when_end.length()>1){
            if(when_end.contains("1 day")){
               int i = endDate.getDayOfWeek();
                Calendar c = Calendar.getInstance();
                c.set(2011, 7, 1, 0, 0, 0);
                c.add(Calendar.DAY_OF_MONTH, i - 1);
                String s1 = String.format("%tA", c)+", ";
              int endh=endDate.getHourOfDay();
                int endm=endDate.getMinuteOfHour();


              if(endm<10)
                when_end=s1+""+endh+"h 0"+endm+"m";
                else  when_end=s1+""+endh+"h "+endm+"m";
            }
            else if (when_end.contains("year")){

                holder.label_time.setText("active");
                 active=true;
            }
            if(when_end.contains(" hour")){
                when_end= when_end.replace(" hour","h");
            }
            if(!active) holder.label_time.setText("Ends " + when_end);
            else active=false;
        }


        if (events.getComments_count() != 0) {
            holder.count_comments.setVisibility(View.VISIBLE);
            holder.count_comments.setText("" + events.getComments_count());
        } else {
            holder.count_comments.setVisibility(View.GONE);
        }
        if (events.getLiked_by_you() == 1) {
            holder.icon_like.setImageDrawable(ctx.getResources().getDrawable(R.drawable.ic_feed_icon_like_big_active));

        } else {
            holder.icon_like.setImageDrawable(ctx.getResources().getDrawable(R.drawable.ic_feed_icon_like_big_default));

        }
        holder.place_title.setTypeface(typeface);
        holder.category.setTypeface(typeface);
        holder.place_title.setText(events.getEvent().getPlace().getPlace_title());

        holder.category.setText(events.getEvent().getPlace().getPlace_category());

        if(events.getComments_count()!=0){
        holder.count_comments.setVisibility(View.VISIBLE);
        holder.count_comments.setText(""+events.getComments_count());}
        else {
            holder.count_comments.setVisibility(View.GONE);
        }


        return view;
    }

    private Events getEvent(int position) {
        return ((Events) getItem(position));
    }


    private static class ViewHolder {
        public ImageView icon_title;
        public TextView label_category;
        public CustomImageView icon_feed;
        public TextView title_text_info;
        public TextView text_info;
        public ImageView comment;
        public ImageView icon_like;
        public TextView label_distance;
        public TextView label_time;
        public TextView category;
        public TextView place_title;
        public TextView count_likes;
        public TextView label_checkout;
        public LinearLayout child_lienar;
        public TextView count_comments;
        public ImageView icon_place;
        public TextView count_checkin;
        public LinearLayout parent;

    }
}
