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

import org.json.JSONArray;
import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import social.evenet.R;
import social.evenet.activity.App;
import social.evenet.db.Comment;
import social.evenet.helper.SupportInfo;
import social.evenet.helper.Util;

/**
 * Created by Alexander on 05.09.2014.
 */
public class CommentAdapter extends BaseAdapter {

    private Context ctx;
    private LayoutInflater lInflater;
    private ArrayList<Comment> objects;
    private Comment comment;

    DisplayImageOptions options = new DisplayImageOptions.Builder()
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .considerExifParams(true).build();

    public CommentAdapter(Context context, ArrayList<Comment> users) {
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
            view = lInflater.inflate(R.layout.list_comment, viewGroup, false);
            holder = new ViewHolder();
            holder.icon_user = (CircularImageView) view.findViewById(R.id.icon_user_comment);
            holder.name = (TextView) view.findViewById(R.id.name_user);
            holder.text_comment = (TextView) view.findViewById(R.id.text_comment);
            holder.time_comment = (TextView) view.findViewById(R.id.time_comment);
            holder.likes = (ImageView) view.findViewById(R.id.likes_button);
            holder.likes_count = (TextView) view.findViewById(R.id.likes_count);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();

        }
        comment = getUser(position);
        holder.name.setText(comment.getUser().getName() + " " + comment.getUser().getSurname());
        holder.text_comment.setText(comment.getComment());
        String t = "" + comment.getTime();
        Date d = new Date(Long.parseLong(t) * 1000);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (comment.getLiked_by_you() == 1) {
            holder.likes.setImageDrawable(ctx.getResources().getDrawable(R.drawable.ic_feed_icon_like_big_active));
        } else {
            holder.likes.setImageDrawable(ctx.getResources().getDrawable(R.drawable.ic_feed_icon_like_big_default));
        }
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

        holder.time_comment.setText("" + end);
        if (comment.getLikes_count() != 0)
            holder.likes_count.setText("" + comment.getLikes_count());
        holder.likes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  int position = (Integer) v.getTag();
                String[] param = new String[4];
                param[0] = SupportInfo.comment_like;
                param[1] = ctx.getSharedPreferences("token_info", ctx.MODE_PRIVATE).getString("token", "");
                param[2] = "comment_id";
                param[3] = "" + getUser(position).getComment_id();
                comment = getUser(position);
                Map<String, String> params = new LinkedHashMap<String, String>();
                params.put("access_token", ctx.getSharedPreferences("token_info", ctx.MODE_PRIVATE).getString("token", ""));
                params.put("comment_id", "" + comment.getComment_id());
                if (comment.getLiked_by_you() != 1) {
                    App.getApi().commentLike(params, new Callback<JSONArray>() {
                        @Override
                        public void success(JSONArray jsonArray, Response response) {
                            String res = "";
                            try {
                                res = jsonArray.getJSONObject(0).getString("response_message");
                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }
                            if (res.equals("OK")) {

                                if (!holder.likes_count.getText().equals("")) {
                                    int s = Integer.parseInt(holder.likes_count.getText().toString()) + 1;
                                    holder.likes_count.setText("" + s);
                                } else {
                                    holder.likes_count.setText("" + 1);
                                }
                            }
                            comment.setLiked_by_you(1);
                            holder.likes.setImageDrawable(ctx.getResources().getDrawable(R.drawable.ic_feed_icon_like_big_active));
                        }

                        @Override
                        public void failure(RetrofitError error) {

                        }
                    });


                } else {
                    App.getApi().eventUnLike(params, new Callback<JSONArray>() {
                        @Override
                        public void success(JSONArray jsonArray, Response response) {
                            String res = "";
                            try {
                                res = jsonArray.getJSONObject(0).getString("response_message");
                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }
                            if (res.equals("OK")) {

                                int s = Integer.parseInt(holder.likes_count.getText().toString()) - 1;
                                if (Integer.parseInt(holder.likes_count.getText().toString()) == 1) {
                                    holder.likes_count.setText("");
                                } else {
                                    holder.likes_count.setText("" + s);
                                }

                            }
                            comment.setLiked_by_you(0);
                            holder.likes.setImageDrawable(ctx.getResources().getDrawable(R.drawable.ic_feed_icon_like_big_default));
                        }

                        @Override
                        public void failure(RetrofitError error) {

                        }
                    });

                }
            }
        });
        holder.likes.setTag(position);
        if (comment.getUser().getPhoto_small() != null) {
            ImageLoader.getInstance().displayImage(comment.getUser().getPhoto_small(), holder.icon_user, options);
        }
        return view;
    }

    private Comment getUser(int position) {
        return ((Comment) getItem(position));
    }

    private static class ViewHolder {
        public TextView name;
        public CircularImageView icon_user;
        public TextView text_comment;
        public TextView time_comment;
        public ImageView likes;
        public TextView likes_count;
    }

}
