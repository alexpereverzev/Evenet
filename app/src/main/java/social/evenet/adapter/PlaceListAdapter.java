package social.evenet.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.util.List;

import social.evenet.R;
import social.evenet.db.Place;

/**
 * Created by Alexander on 23.09.2014.
 */
public class PlaceListAdapter extends BaseAdapter {


    private Context ctx;
    private LayoutInflater lInflater;
    private List<Place> objects;


    DisplayImageOptions options = new DisplayImageOptions.Builder()
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .considerExifParams(true).build();

    public PlaceListAdapter(Context context, List<Place> users) {
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



    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {

        ViewHolder holder;
        View view = convertView;
        if (view == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_place_adapter, null);
            holder.address = (TextView) view.findViewById(R.id.address);
            holder.title = (TextView) view.findViewById(R.id.title);
            holder.category=(TextView) view.findViewById(R.id.category);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        Place place = getUser(position);
        holder.title.setText(place.getPlace_title());
        if(place.getPlace_address().isEmpty()) holder.address.setVisibility(View.GONE);
        else holder.address.setText(place.getPlace_address());
        holder.category.setText(place.getPlace_category());
        return view;
    }


    private Place getUser(int position) {
        return ((Place) getItem(position));
    }


    private static class ViewHolder {
        public TextView title;
        public TextView category;
        public TextView address;
        public ImageView user;


    }


}

