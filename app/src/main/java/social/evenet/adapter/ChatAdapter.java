package social.evenet.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.pkmmte.view.CircularImageView;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import social.evenet.R;
import social.evenet.db.Event;
import social.evenet.helper.Util;
import social.evenet.model.ChatModel;

/**
 * Created by Александр on 27.09.2014.
 */
public class ChatAdapter extends BaseAdapter {

    private Context ctx;
    private LayoutInflater lInflater;
    private List<ChatModel> objects;

    DisplayImageOptions options = new DisplayImageOptions.Builder()
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .considerExifParams(true).build();

    public ChatAdapter(Context context, List<ChatModel> users) {
        ctx = context;
        objects = users;
        lInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    private ChatModel getChat(int position) {
        return ((ChatModel) getItem(position));
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
    public View getView(int position, View view, ViewGroup viewGroup) {

        ViewHolder holder;
        View v = view;
        ChatModel chatModel = getChat(getCount()-position-1);
        /*if (v == null) {
            if (chatModel.getSend()) {
               v = lInflater.inflate(R.layout.adapter_chat_send, viewGroup, false);
            }
            else {
                v = lInflater.inflate(R.layout.adapter_chat_recive, viewGroup, false);

            }
            holder = new ViewHolder();
            holder.icon_user = (ImageView) v.findViewById(R.id.user_icon);
            holder.when_message = (TextView) v.findViewById(R.id.when_message);
            holder.text_message = (TextView) v.findViewById(R.id.text_message);
            holder.upload_1 = (ImageView) v.findViewById(R.id.upload_1);
            holder.upload_2 = (ImageView) v.findViewById(R.id.upload_2);
            holder.upload_3 = (ImageView) v.findViewById(R.id.upload_3);
            holder.upload_4 = (ImageView) v.findViewById(R.id.upload_4);
            holder.upload_5 = (ImageView) v.findViewById(R.id.upload_5);

            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }*/

        if (chatModel.getSend()) {
            v = lInflater.inflate(R.layout.adapter_chat_send, viewGroup, false);

        }
        else {
            v = lInflater.inflate(R.layout.adapter_chat_recive, viewGroup, false);

        }

        icon_user = (CircularImageView) v.findViewById(R.id.user_icon);
        when_message = (TextView) v.findViewById(R.id.when_message);
        text_message = (TextView) v.findViewById(R.id.text_message);
        upload_1 = (ImageView) v.findViewById(R.id.upload_1);
        upload_2 = (ImageView) v.findViewById(R.id.upload_2);
        upload_3 = (ImageView) v.findViewById(R.id.upload_3);
        upload_4 = (ImageView) v.findViewById(R.id.upload_4);
        upload_5 = (ImageView) v.findViewById(R.id.upload_5);

        icon_event = (ImageView) v.findViewById(R.id.event_icon_chat);
        time = (TextView) v.findViewById(R.id.time_event);
        title = (TextView) v.findViewById(R.id.title_event);
        icon_event = (ImageView) v.findViewById(R.id.icon_user);
        event_layout=(LinearLayout) v.findViewById(R.id.event_linear);
        if(chatModel.getMessage()!=null){
        text_message.setText(chatModel.getMessage());
            text_message.setVisibility(View.VISIBLE);
        }
        else {
            text_message.setVisibility(View.GONE);
        }

        if(chatModel.getMessage().isEmpty()) text_message.setVisibility(View.GONE);

            if (chatModel.getIcon() != null) {
               ImageLoader.getInstance().displayImage(chatModel.getIcon(), icon_user, options);
             }
        if(chatModel.getImage()!=null){

            BitmapFactory.Options options=new BitmapFactory.Options();// Create object of bitmapfactory's option method for further option use
            options.inPurgeable = true;
            upload_1.setImageBitmap(
                    decodeSampledBitmapFromResource(chatModel.getImage(), 100, 100));
            upload_1.setVisibility(View.VISIBLE);

        }
        else {
            upload_1.setVisibility(View.GONE);
        }
        if(chatModel.getImage_second()!=null){

            BitmapFactory.Options options=new BitmapFactory.Options();// Create object of bitmapfactory's option method for further option use
            options.inPurgeable = true;

            upload_2.setImageBitmap(
                    decodeSampledBitmapFromResource(chatModel.getImage_second(), 100, 100));
            upload_2.setVisibility(View.VISIBLE);

        }
        else {
            upload_2.setVisibility(View.GONE);
        }
        if(chatModel.getImage_third()!=null){
            BitmapFactory.Options options=new BitmapFactory.Options();// Create object of bitmapfactory's option method for further option use
            options.inPurgeable = true;
              upload_3.setImageBitmap(
                    decodeSampledBitmapFromResource(chatModel.getImage_third(), 100, 100));

        }
        else {
            upload_3.setVisibility(View.GONE);
        }
        if(chatModel.getImage_four()!=null){

            upload_4.setImageBitmap(
                    decodeSampledBitmapFromResource(chatModel.getImage_four(), 100, 100));

        }
        else {
            upload_4.setVisibility(View.GONE);
        }
        if(chatModel.getImage_five()!=null){

            upload_5.setImageBitmap(
                    decodeSampledBitmapFromResource(chatModel.getImage_five(), 100, 100));

        }
        else {
            upload_5.setVisibility(View.GONE);
        }
        if (chatModel.getEvent()!=null) {
            event_layout.setVisibility(View.VISIBLE);
          //  FeedBL bl=new FeedBL(ctx);
            Event event= chatModel.getEvent();
            title.setText(event.getEvent_title());
            if (event.getMainAttachment()!= null) {
                ImageLoader.getInstance().displayImage(event.getMainAttachment().getUrl(), icon_event, options);
            }
            else   icon_event.setVisibility(View.GONE);
            if(event.getEnds()!=null) {
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
                    } else {
                        end = Util.convertToDateTime(sdf.format(d));
                    }
                }
                time.setText("Ends " + end);
            }
            else time.setVisibility(View.GONE);
        }
        else if(chatModel.getPlace()!=null){
            event_layout.setVisibility(View.VISIBLE);
            title.setText(chatModel.getPlace().getPlace_title());
            time.setText(chatModel.getPlace().getPlace_address());
        }
        else {
            event_layout.setVisibility(View.GONE);
        }
        if(chatModel.getAttachment()!=null){
            ImageLoader.getInstance().displayImage(chatModel.getAttachment().getUrl(),upload_1,options);
            upload_1.setVisibility(View.VISIBLE);
        }
        else upload_1.setVisibility(View.GONE);
        return v;
    }

    public TextView when_message;
    public CircularImageView icon_user;
    public TextView text_message;
    public ImageView upload_1;
    public ImageView upload_2;
    public ImageView upload_3;
    public ImageView upload_4;
    public ImageView upload_5;

    public TextView title;
    public ImageView icon_event;
    public TextView time;
    public ImageView user;
    public LinearLayout event_layout;

    private static class ViewHolder {
        public TextView when_message;
        public ImageView icon_user;
        public TextView text_message;
        public ImageView upload_1;
        public ImageView upload_2;
        public ImageView upload_3;
        public ImageView upload_4;
        public ImageView upload_5;
    }

    public static Bitmap decodeSampledBitmapFromResource(byte[] res,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(res, 0, res.length, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(res, 0, res.length, options);
    }

    class BitmapWorkerTask extends AsyncTask<byte[], Void, Bitmap> {
        private final WeakReference<ImageView> imageViewReference;
        private byte[] data;

        public BitmapWorkerTask(ImageView imageView) {
            // Use a WeakReference to ensure the ImageView can be garbage collected
            imageViewReference = new WeakReference<ImageView>(imageView);
        }


        @Override
        protected Bitmap doInBackground(byte[]... bytes) {
            data = bytes[0];
            return decodeSampledBitmapFromResource(data, 100, 100);

        }

        // Once complete, see if ImageView is still around and set bitmap.
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (imageViewReference != null && bitmap != null) {
                final ImageView imageView = imageViewReference.get();
                if (imageView != null) {
                    imageView.setImageBitmap(bitmap);
                }
            }
        }
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

}
