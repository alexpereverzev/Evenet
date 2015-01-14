package social.evenet.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import social.evenet.R;
import social.evenet.activity.CommentActivity;
import social.evenet.activity.MenuNewActivity;
import social.evenet.db.Event;
import social.evenet.db.Events;
import social.evenet.fragment.CompanyFragment;
import social.evenet.fragment.FeedDetailFragment;
import social.evenet.helper.Util;

/**
 * Created by Alexander on 05.09.2014.
 */
public class EventAdapter extends BaseAdapter {

    private Context ctx;
    private LayoutInflater lInflater;
    private List<Event> objects;


    DisplayImageOptions options = new DisplayImageOptions.Builder()
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .considerExifParams(true).build();

    public EventAdapter(Context context, List<Event> users) {
        ctx = context;
        objects = users;
        lInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getItemViewType(int position) {
        if (position % 2 == 0) return 0;
        else return 1;
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

    static boolean flag = false;

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {


        ViewHolder holder;
        View view = convertView;
        if (view == null) {

            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.event_list, null);
            holder.icon_event = (ImageView) view.findViewById(R.id.event_icon);
            holder.time = (TextView) view.findViewById(R.id.time_event);
            holder.title = (TextView) view.findViewById(R.id.title_event);
            holder.icon_event = (ImageView) view.findViewById(R.id.icon_user);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        Event event = getUser(position);

        holder.title.setText(event.getEvent_title());
        if (event.getMainAttachment().getUrl() != null) {
            ImageLoader.getInstance().displayImage(event.getMainAttachment().getUrl(), holder.icon_event, options);
        }

        Date d = new Date(Long.parseLong(event.getEnds()) * 1000);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        // String end = "";//
        String end = Util.convertToWeek(sdf.format(d));

        if (end.contains("-")) {
            end = end.replace("-", " ");
            end = end + "ago";
        } else if (end.contains("0")) {
            end = Util.convertToDate(sdf.format(d));
            if (end.contains("-")) {
                end = end.replace("-", " ");
                end = end + "ago";
                //} else if (end.contains("0")) {
            } else {
                end = Util.convertToDateTime(sdf.format(d));

            }
        }
            holder.time.setText("Ends in " + end);
            return view;
        }


    private Event getUser(int position) {
        return ((Event) getItem(position));
    }


    private static class ViewHolder {
        public TextView title;
        public ImageView icon_event;
        public TextView time;
        public ImageView user;


    }

}
