package social.evenet.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import social.evenet.R;
import social.evenet.db.Posts;

/**
 * Created by Александр on 21.09.2014.
 */
public class WallAdapter extends BaseAdapter implements View.OnClickListener {

    private static int id_events=0;
    private Context ctx;
    private LayoutInflater lInflater;
    private List<Posts> objects;
    private static int count = 0;
    private Posts posts;
    private String name;
    private String icon;


    //ImageLoader.getInstance().displayImage(profile.getBaseUrl() + profile.getUserPicThumb(), holder.userPhoto, options);

    DisplayImageOptions  options = new DisplayImageOptions.Builder()
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .considerExifParams(true).build();

    public WallAdapter(Context context, List<Posts> users, String name_user, String url) {
        ctx = context;
        objects = users;
        name=name_user;
        icon=url;
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


    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {

        int rowType = getItemViewType(position);

        ViewHolder holder;
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.list_wall, viewGroup, false);
            holder = new ViewHolder();
            holder.user_name = (TextView) view.findViewById(R.id.user_name);
            holder.user_icon = (ImageView) view.findViewById(R.id.user_icon);
            holder.post_text = (TextView) view.findViewById(R.id.post_text);
            holder.title_event = (TextView) view.findViewById(R.id.title_event);
            holder.icon_event = (ImageView) view.findViewById(R.id.icon_event);
            holder.count_likes = (TextView) view.findViewById(R.id.count_likes_wall);
            holder.likes = (ImageView) view.findViewById(R.id.likes);
            holder.event_wall=(LinearLayout) view.findViewById(R.id.event_wall);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();

        }
        posts = getUser(position);
        holder.event_wall.setVisibility(View.INVISIBLE);
        if(position==0){
            id_events=posts.getEvent().getEvent_id();
        }
        else {
            if(id_events!=posts.getEvent().getEvent_id()){
                holder.event_wall.setVisibility(View.VISIBLE);
                Posts p=getUser(position-1);
                holder.title_event.setText(p.getEvent().getEvent_title());
                holder.count_likes.setText(""+p.getLikes_count()+" likes");
                id_events=posts.getEvent().getEvent_id();


            }
        }

        holder.user_name.setText(name);
        if (icon != null) {
            ImageLoader.getInstance().displayImage(icon, holder.user_icon, options);
        }
        holder.post_text.setText(posts.getMessage());
        /* if (posts.getEvent().getMainAttachment().getUrl() != null) {
            ImageLoader.getInstance().displayImage(posts.getEvent().getMainAttachment().getUrl(), holder.icon_event, options);
        }*/
        if(getCount()==position+1){
            holder.event_wall.setVisibility(View.VISIBLE);
            holder.title_event.setText(posts.getEvent().getEvent_title());
            holder.count_likes.setText(""+posts.getLikes_count()+" likes");
        }

        return view;
    }

    private Posts getUser(int position) {
        return ((Posts) getItem(position));
    }

    @Override
    public void onClick(View v) {

        String s = "f";
        switch (v.getId()) {
           /* case R.id.label_like:
                s = "as";
                break;*/


        }
    }


    private static class ViewHolder {
        public TextView user_name;
        public ImageView user_icon;
        public TextView post_text;
        public TextView title_event;
        public TextView label_time;
        public ImageView icon_event;
        public ImageView likes;
        public ImageView comment;
        public TextView count_likes;
        public LinearLayout event_wall;


    }

}