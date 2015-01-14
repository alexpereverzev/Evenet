package social.evenet.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.pkmmte.view.CircularImageView;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import social.evenet.R;
import social.evenet.activity.App;
import social.evenet.db.Meetups;
import social.evenet.fragment.MeetingFragment;
import social.evenet.helper.SupportInfo;
import social.evenet.helper.Util;

//import org.joda.time.DateTime;
//import org.joda.time.format.DateTimeFormat;
//import org.joda.time.format.DateTimeFormatter;

/**
 * Created by Александр on 13.09.2014.
 */
public class MeetAdapter extends BaseAdapter {

    private Context ctx;
    private LayoutInflater lInflater;
    private ArrayList<Meetups> objects;

    DisplayImageOptions options = new DisplayImageOptions.Builder()
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .considerExifParams(true).build();

    public MeetAdapter(Context context, ArrayList<Meetups> users) {
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

        final ViewHolder holder;
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.meetup_adapter, viewGroup, false);
            holder = new ViewHolder();

            holder.name = (TextView) view.findViewById(R.id.name_meetup);
            holder.date = (TextView) view.findViewById(R.id.date_meetup);
            holder.message = (TextView) view.findViewById(R.id.message);
            holder.start = (TextView) view.findViewById(R.id.start_meetup);
            holder.end = (TextView) view.findViewById(R.id.end_meetup);
            holder.message_content = (LinearLayout) view.findViewById(R.id.message_content);
            holder.place_content = (LinearLayout) view.findViewById(R.id.linear_place_content);
            holder.place_title = (TextView) view.findViewById(R.id.place_title);
            holder.count_user = (TextView) view.findViewById(R.id.count_user);
            holder.photo_meeting = (CircularImageView) view.findViewById(R.id.photo_meeting);
            holder.parent = (RelativeLayout) view.findViewById(R.id.parent);
            holder.delete = (Button) view.findViewById(R.id.delete_button);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();

        }

        final Meetups list_de = getDefaultList(position);

        holder.name.setText(list_de.getMeetup_name());
        holder.count_user.setText("" + list_de.getUsers_count());

        // holder.icon.setImageDrawable(ctx.getResources().getDrawable(R.drawable.ic_launcher));
        //   ImageLoader.getInstance().displayImage(list_de.get, holder.icon, options);

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //   MeetingFragment.mListView.closeAnimate(position - 1);

//                url_meet_status = SupportInfo.url +  + getSharedPreferences("token_info", MODE_PRIVATE).getString("token", "") + "&meetup_id=" + meetup_id + "&status=";

                Map<String,String> params = new LinkedHashMap<String, String>();
                params.put("access_token", ctx.getSharedPreferences("token_info", ctx.MODE_PRIVATE).getString("token", ""));
                params.put("meetup_id", ""+list_de.getMeetup_id());
                params.put("status", "2");
                String url_post = SupportInfo.url + "meetup.setStatus.php";
                App.getApi().meetupChangeStatus(params,new Callback<JSONArray>() {
                    @Override
                    public void success(JSONArray jsonArray, Response response) {
                        JSONObject jsonObject = jsonArray.optJSONObject(0);
                        if (jsonObject.isNull("error_code")) {
                            objects.remove(position);

                            MeetingFragment.mergeAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                });


            }
        });

        if (list_de.getEvent() != null) {
            if (list_de.getEvent().getCreator() != null) {
                holder.place_content.setVisibility(View.GONE);
                holder.message_content.setVisibility(View.VISIBLE);
                if (list_de.getEvent().getCreator() != null) {
                    ImageLoader.getInstance().displayImage(list_de.getEvent().getCreator().getPhoto_small(), holder.photo_meeting, options);
                }
                holder.message.setText(list_de.getEvent().getMessage().getMessage());

            } else {

                if (list_de.getEvent().getMainAttachment().getUrl() != null) {
                    holder.place_content.setVisibility(View.VISIBLE);
                    holder.message_content.setVisibility(View.GONE);
                    ImageLoader.getInstance().displayImage(list_de.getEvent().getMainAttachment().getUrl(), holder.photo_meeting, options);
                }
            }

        } else {
            holder.place_content.setVisibility(View.VISIBLE);
            holder.message_content.setVisibility(View.GONE);
            if (list_de.getPlace() != null) {
                holder.place_title.setText(list_de.getPlace().getPlace_title());
            }
        }

        if (list_de.getEvent() != null) {
            if (list_de.getEvent().getMainAttachment() != null) {
                ImageLoader.getInstance().displayImage(list_de.getEvent().getMainAttachment().getSmall_photo(), holder.photo_meeting, options);

            }
        }

        holder.date.setText("" + list_de.getTime());
        String time = "" + list_de.getEnd();

        Date d = new Date(Long.parseLong(time) * 1000);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        // end = timeConvert(end, sdf, d);

        String t = "" + list_de.getStart();

        d = new Date(Long.parseLong(time) * 1000);
        holder.start.setText(timeConvert(t, sdf, d));


        String end = Util.convertToWeek(sdf.format(d));
        String begin = Util.convertToWeek(sdf.format(new Date(Long.parseLong("" + list_de.getStart()) * 1000)));
         String begin1 = Util.convertToWeek(sdf.format(new Date(Long.parseLong("" + list_de.getStart()) * 1000)));
        String end1 = Util.convertToWeek(sdf.format(new Date(Long.parseLong("" + list_de.getEnd()) * 1000)));
        String when_start = " ";
        String when_end = " ";
        DateTimeFormatter formatter1 = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        if (list_de.getEnd() != 0) {
            DateTime endDate = DateTime.parse(sdf.format(new Date(Long.parseLong("" + list_de.getEnd()) * 1000)), formatter1);
            String[] end_date = end1.split(",");
            for (int i = 0; i < end_date.length; i++) {
                String s = end_date[i];
                if (s.contains("0")) {
                    continue;
                } else if (s.contains("-")) {
                    s = s.replace("-", "");
                    when_end = when_end + s + " ago";
                    break;
                } else {
                    when_end = when_end + s;
                    break;
                }
            }

        }
        DateTime startDate = DateTime.parse(sdf.format(new Date(Long.parseLong("" + list_de.getStart()) * 1000)), formatter1);
        String[] begin_date = begin1.split(",");

        for (int i = 0; i < begin_date.length; i++) {
            String s = begin_date[i];
            if (s.contains("0")) {
                continue;
            } else if (s.contains("-")) {
                s = s.replace("-", "");
                when_start = when_start + s + " ago";
                break;
            } else {
                when_start = when_start + s;
                // if((s.contains("months"))||(s.contains("week"))||(s.contains("weeks"))){
                break;
                //  }
            }
        }


        if (list_de.getEnd() != 0) {

            if (end.contains("-")) {
                end = end.replace("-", " ");
                end = end + "ago";
            } else if (end.contains("0")) {
                end = Util.convertToDate(sdf.format(d));
                if (end.contains("-")) {
                    end = end.replace("-", " ");
                    end = end + "ago";
                    //} else if (end.contains("0")) {
                } else if (end.contains("-")) {
                    end = end.replace("-", " ");
                    end = end + "ago";

                } else {
                    end = Util.convertToDateTime(sdf.format(d));
                    if (end.contains("-")) {
                        end = end.replace("-", " ");
                        end = end + "ago";

                    }

                }
            }
            holder.end.setText(when_end);
            //holder.end.setText(end);
        }


        Typeface tf = Typeface.createFromAsset(ctx.getAssets(),
                "fonts/HelveticaNeueMedium.ttf");
        holder.end.setTypeface(tf);
        holder.parent.setVisibility(View.VISIBLE);
        if (when_start.length() > 1) {
            if (when_start.contains("1 day")) {
              //  int i = startDate.getDayOfWeek();
                Calendar calendar = Calendar.getInstance();
                Calendar c = Calendar.getInstance();
                c.set(2011, 7, 1, 0, 0, 0);
             //   c.add(Calendar.DAY_OF_MONTH, i - 1);
                String s1 = String.format("%tA", c) + ", ";
           //     int starth = startDate.getHourOfDay();
            //    int startm = startDate.getMinuteOfHour();

           //     if (startm < 10)
          //          when_start = s1 + "" + starth + "h 0" + startm + "m";
          //      else when_start = s1 + "" + starth + "h " + startm + "m";

                // when_start= when_start.replace("1 day",s1);
            }
            if (when_start.contains(" hour")) {
                when_start = when_start.replace(" hour", "h");
            }

            holder.end.setText("Starts " + when_start);
        }

        return view;
    }

    public String timeConvert(String end, SimpleDateFormat sdf, Date d) {
        if (end.contains("-")) {
            end = end.replace("-", " ");
            end = end + "ago";
        } else if (end.contains("0")) {
            end = Util.convertToDate(sdf.format(d));
            if (end.contains("-")) {
                end = end.replace("-", " ");
                end = end + "ago";
            }
            //} else if (end.contains("0")) {
        } else if (end.contains("-")) {
            end = end.replace("-", " ");
            end = end + "ago";
        } else {
            end = Util.convertToDateTime(sdf.format(d));
            if (end.contains("-")) {
                end = end.replace("-", " ");
                end = end + "ago";

            }

        }
        return end;
    }


    private Meetups getDefaultList(int position) {
        return ((Meetups) getItem(position));
    }

    private static class ViewHolder {
        public TextView name;
        public CircularImageView icon;
        public TextView date;
        public TextView start;
        public TextView end;
        public LinearLayout message_content;
        public TextView count_user;
        public LinearLayout place_content;
        public TextView message;
        public TextView place_title;
        public CircularImageView photo_meeting;
        public Button delete;
        public RelativeLayout parent;
    }

}