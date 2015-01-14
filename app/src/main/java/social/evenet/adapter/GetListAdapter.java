package social.evenet.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.pkmmte.view.CircularImageView;

import java.util.ArrayList;

import social.evenet.R;
import social.evenet.db.DefaultList;
import social.evenet.db.Lists;

/**
 * Created by Alexander on 05.09.2014.
 */
public class GetListAdapter extends BaseAdapter {

    private Context ctx;
    private LayoutInflater lInflater;
    private ArrayList<Lists> objects;
     private  String [] names_list;

    public GetListAdapter(Context context, ArrayList<Lists> users) {
        ctx = context;
        objects = users;
        lInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        names_list=ctx.getResources().getStringArray(R.array.list_default);
    }


    DisplayImageOptions  options = new DisplayImageOptions.Builder()
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .considerExifParams(true).build();

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

        ViewHolder holder;
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.custom_list_choose, viewGroup, false);
            holder = new ViewHolder();
            holder.icon = (CircularImageView) view.findViewById(R.id.icon_list);
            holder.name = (TextView) view.findViewById(R.id.name_list);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();

        }

        Lists list_de = getDefaultList(position);
        if(position!=15)
        holder.name.setText(names_list[position]);
        else holder.name.setText("Not now");
     //    holder.icon.setImageDrawable(ctx.getResources().getDrawable(R.drawable.ic_launcher));
       // ImageLoader.getInstance().displayImage(list_de.getUrl(), holder.icon, options);


        return view;
    }

    private Lists getDefaultList(int position) {
        return ((Lists) getItem(position));
    }

    private static class ViewHolder {
        public TextView name;
        public CircularImageView icon;

    }
}
