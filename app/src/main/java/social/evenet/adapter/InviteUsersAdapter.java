package social.evenet.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import social.evenet.R;
import social.evenet.db.UserInfo;

/**
 * Created by Александр on 21.09.2014.
 */
public class InviteUsersAdapter extends BaseAdapter {


    private Context ctx;
    private LayoutInflater lInflater;
    private ArrayList<UserInfo> objects;

    DisplayImageOptions  options = new DisplayImageOptions.Builder()
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .considerExifParams(true).build();

    public InviteUsersAdapter(Context context, ArrayList<UserInfo> users) {
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


        final ViewHolder holder;
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.invite_list_users, viewGroup, false);
            holder = new ViewHolder();
            holder.icon = (ImageView) view.findViewById(R.id.icon_user);
            holder.name = (TextView) view.findViewById(R.id.name_user);
            holder.phone_user=(TextView) view.findViewById(R.id.phone_user);
            holder.checkBox=(CheckBox) view.findViewById(R.id.checkbox_user);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();

        }

        final UserInfo list_de = getDefaultList(position);

        holder.name.setText(list_de.getName()+" "+list_de.getSurname());

        holder.icon.setImageDrawable(ctx.getResources().getDrawable(R.drawable.ic_launcher));
        if (list_de.getPhoto_small() != null) {
            ImageLoader.getInstance().displayImage(list_de.getPhoto_small(), holder.icon, options);
        }
        if(list_de.getPhone()!=""){
            holder.phone_user.setText("Phone :"+list_de.getPhone());
        }
        else {
            holder.phone_user.setVisibility(View.GONE);
        }

        holder.checkBox.setTag(position);

       holder.checkBox.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
                if(holder.checkBox.isChecked()){
                    list_de.setCheck(true);
                }
               else {
                    list_de.setCheck(false);
                }
           }
       });





        return view;
    }

    private UserInfo getDefaultList(int position) {
        return ((UserInfo) getItem(position));
    }

    private static class ViewHolder {
        public TextView name;
        public ImageView icon;
        private TextView phone_user;
        private CheckBox checkBox;

    }

}