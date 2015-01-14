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

import java.util.ArrayList;

import social.evenet.R;
import social.evenet.db.Company;
import social.evenet.db.UserInfo;

/**
 * Created by Александр on 21.09.2014.
 */
public class CompanyAdapter extends BaseAdapter {


    private Context ctx;
    private LayoutInflater lInflater;
    private ArrayList<Company> objects;

    DisplayImageOptions  options = new DisplayImageOptions.Builder()
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .considerExifParams(true).build();

    public CompanyAdapter(Context context, ArrayList<Company> users) {
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
            view = lInflater.inflate(R.layout.adapter_company, viewGroup, false);
            holder = new ViewHolder();
            holder.icon = (ImageView) view.findViewById(R.id.logo_company);
            holder.name = (TextView) view.findViewById(R.id.title_company);
            holder.website = (TextView) view.findViewById(R.id.website);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();

        }

        Company list_de = getDefaultList(position);

        holder.name.setText(list_de.getCompany_title());
        holder.website.setText(list_de.getWebsite());
       // holder.icon.setImageDrawable(ctx.getResources().getDrawable(R.drawable.ic_launcher));
        /*if (list_de.getPhoto_small() != null) {
            ImageLoader.getInstance().displayImage(list_de.getPhoto_small(), holder.icon, options);
        }*/


        return view;
    }

    private Company getDefaultList(int position) {
        return ((Company) getItem(position));
    }

    private static class ViewHolder {
        public TextView name;
        public TextView website;
        public ImageView icon;

    }

}