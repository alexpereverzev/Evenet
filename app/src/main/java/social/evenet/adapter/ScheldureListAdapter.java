package social.evenet.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import social.evenet.R;
import social.evenet.activity.CommentActivity;
import social.evenet.db.Events;
import social.evenet.helper.Util;

/**
 * Created by Alexander on 23.09.2014.
 */
public class ScheldureListAdapter extends BaseExpandableListAdapter {


    DisplayImageOptions options = new DisplayImageOptions.Builder()
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .considerExifParams(true).build();

    private ArrayList<ArrayList<Events>> mGroups;
    private Context mContext;

    public ScheldureListAdapter(Context context, ArrayList<ArrayList<Events>> groups) {
        mContext = context;
        mGroups = groups;

    }

    @Override
    public int getGroupCount() {
        return mGroups.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mGroups.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mGroups.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mGroups.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView,
                             ViewGroup parent) {

        ViewHolder2 holder;
        View view = convertView;
        if (view == null) {
            holder = new ViewHolder2();


            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.header_feed, null);
            holder.label_time = (TextView) view.findViewById(R.id.label_time);
            holder.label_title = (TextView) view.findViewById(R.id.label_title);
            holder.header = (LinearLayout) view.findViewById(R.id.header);
            holder.icon_title = (ImageView) view.findViewById(R.id.icon_title);
            view.setTag(holder);

        } else {
            holder = (ViewHolder2) view.getTag();
        }

        if (isExpanded) {
            //Изменяем что-нибудь, если текущая Group раскрыта
        } else {
            //Изменяем что-нибудь, если текущая Group скрыта
        }

        //double fin_cor = mGroups.get(groupPosition).get(0).getEvent().getPlace().getLatitude();
      //  double fin_long = mGroups.get(groupPosition).get(0).getEvent().getPlace().getLongitude();
      //  holder.label_distance.setText(" " + Util.CalculationByDistance(mGroups.get(groupPosition).get(0).getCurrent_coordinat_latitude(), mGroups.get(groupPosition).get(0).getCurrent_coordinat_longutide(), fin_cor, fin_long) + " km");
      if(mGroups.get(groupPosition).get(0).getEvent().getCompany()!=null){
        holder.label_title.setText(mGroups.get(groupPosition).get(0).getEvent().getCompany().getCompany_name());}
        else {
          holder.label_title.setVisibility(View.GONE);
      }
        Date d = new Date(Long.parseLong(mGroups.get(groupPosition).get(0).getEvent().getEnds()) * 1000);
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
      //  final Intent intent = new Intent(mContext, PlaceActivity.class);
      //  intent.putExtra("place_id", mGroups.get(groupPosition).get(0).getEvent().getPlace().getPlace_id());
        holder.icon_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

             //   mContext.startActivity(intent);
            }
        });

        holder.label_time.setText("Ends in " + end);
        ExpandableListView eLV = (ExpandableListView) parent;
        eLV.expandGroup(groupPosition);
        return view;

    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                             View convertView, ViewGroup parent) {
        final ViewHolder holder;
        View view = convertView;
        // if (view == null) {

        // holder = new ViewHolder();
        if (view == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                view = inflater.inflate(R.layout.custom_feed_scheldure, null);


            holder.title_text_info = (TextView) view.findViewById(R.id.title_text_info);
            holder.text_info = (TextView) view.findViewById(R.id.text_info);
            holder.text_address = (TextView) view.findViewById(R.id.text_address);


            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();

        }

        holder.text_info.setText(mGroups.get(groupPosition).get(childPosition).getEvent().getPlace().getPlace_description());
        holder.title_text_info.setText(mGroups.get(groupPosition).get(childPosition).getEvent().getPlace().getPlace_title());
        holder.text_address.setText(mGroups.get(groupPosition).get(childPosition).getEvent().getPlace().getPlace_address());
        final Intent intent = new Intent(mContext, CommentActivity.class);
        intent.putExtra("id", mGroups.get(groupPosition).get(childPosition).getEvent().getEvent_id());




        final Events events = mGroups.get(groupPosition).get(0);


        final int event_id = mGroups.get(groupPosition).get(childPosition).getEvent().getEvent_id();


        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


    private static class ViewHolder {
        public ImageView icon_title;
        public TextView text_address;

        public TextView title_text_info;
        public TextView text_info;




    }

    private static class ViewHolder2 {
        public TextView label_title;
        public ImageView icon_title;

        public TextView label_time;
        public Button comment;
        public TextView label_like;
        public LinearLayout header;
    }

}

