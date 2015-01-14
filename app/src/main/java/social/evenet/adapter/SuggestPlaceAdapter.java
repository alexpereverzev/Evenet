package social.evenet.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import social.evenet.R;
import social.evenet.db.SuggestPlace;

/**
 * Created by Alexander on 01.12.2014.
 */
public class SuggestPlaceAdapter extends BaseAdapter {


    private Context ctx;
    private LayoutInflater lInflater;
    private ArrayList<SuggestPlace> objects;

    public SuggestPlaceAdapter(Context context, ArrayList<SuggestPlace> users) {
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
            view = lInflater.inflate(R.layout.suggest_place, viewGroup, false);
            holder = new ViewHolder();
            holder.icon_place = (ImageView) view.findViewById(R.id.icon_place);


            holder.name_place = (TextView) view.findViewById(R.id.name_place);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();

        }

        SuggestPlace list_de = getDefaultList(position);

        holder.icon_place.setImageDrawable(ctx.getResources().getDrawable(list_de.getIcon()));
        holder.name_place.setText(list_de.getTitle());

        return view;
    }

    private SuggestPlace getDefaultList(int position) {
        return ((SuggestPlace) getItem(position));
    }

    private static class ViewHolder {


        public ImageView icon_place;
        public TextView name_place;


    }

}
