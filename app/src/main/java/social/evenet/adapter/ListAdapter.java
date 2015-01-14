package social.evenet.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pkmmte.view.CircularImageView;

import java.util.ArrayList;

import social.evenet.R;
import social.evenet.db.DefaultList;
import social.evenet.db.Events;

/**
 * Created by Alexander on 05.09.2014.
 */
public class ListAdapter extends BaseAdapter {

    private Context ctx;
    private LayoutInflater lInflater;
    private ArrayList<DefaultList> objects;


    public ListAdapter(Context context, ArrayList<DefaultList> users) {
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

        ViewHolder holder;
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.custom_list_choose, viewGroup, false);
            holder = new ViewHolder();
            holder.icon=(CircularImageView) view.findViewById(R.id.icon_list);
            holder.name = (TextView) view.findViewById(R.id.name_list);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();

        }

       DefaultList list_de = getDefaultList(position);

        holder.name.setText(list_de.getName());
        holder.icon.setImageDrawable(ctx.getResources().getDrawable(R.drawable.ic_launcher));



        return view;
    }

    private DefaultList getDefaultList(int position) {
        return ((DefaultList) getItem(position));
    }

    private static class ViewHolder {
        public TextView name;
        public CircularImageView icon;

    }
}
