package social.evenet.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import social.evenet.R;


public class BaseTwitterActivity extends ActionBarActivity {

    private Context context;
    private static final String TAG = "BaseActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_twitter);
        context = BaseTwitterActivity.this;
    }
}

       /* findViewById(R.id.postImageButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (LoginTwitterActivity.isActive(context)) {
                    // try {

                    mAlertBuilder = new AlertDialog.Builder(context).create();
                    mAlertBuilder.setCancelable(false);
                    mAlertBuilder.setTitle(R.string.please_wait_title);
                    View view = getLayoutInflater().inflate(R.layout.view_loading, null);
                    ((TextView) view.findViewById(R.id.messageTextViewFromLoading)).setText(getString(R.string.posting_image_message));
                    mAlertBuilder.setView(view);
                    mAlertBuilder.show();


                        //       InputStream inputStream  = v.getContext().getAssets().open("1.png");
                        //       Bitmap bmp = BitmapFactory.decodeStream(inputStream);
                        //        String filename = Environment.getExternalStorageDirectory().toString() + File.separator + "1.png";
                        ///       Log.d("BITMAP", filename);
                        //       FileOutputStream out = new FileOutputStream(filename);
                        //        bmp.compress(Bitmap.CompressFormat.PNG, 90, out);

                        //   HelperMethods.postToTwitterWithImage(context, ((Activity)context), filename, getString(R.string.tweet_with_image_text), new HelperMethods.TwitterCallback() {

                        //             @Override
                        //             public void onFinsihed(Boolean response) {
                        //                  mAlertBuilder.dismiss();
                        //                Log.d(TAG, "----------------response----------------" + response);
                        //                 Toast.makeText(context, getString(R.string.image_posted_on_twitter), Toast.LENGTH_SHORT).show();
                        //             }
                        //           });

                        //        } catch (Exception ex) {
                        //            Toast.makeText(context, "ERROR", Toast.LENGTH_SHORT).show();
                        //         }
                        //      }else{
                        //          startActivity(new Intent(context, LoginActivity.class));
                        //      }
                        //    }
                        //   });

     /*   findViewById(R.id.postTweetButton).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                if (LoginTwitterActivity.isActive(context)) {
                    try {

                        mAlertBuilder = new AlertDialog.Builder(context).create();
                        mAlertBuilder.setCancelable(false);
                        mAlertBuilder.setTitle(R.string.please_wait_title);
                        View view = getLayoutInflater().inflate(R.layout.view_loading, null);
                        ((TextView) view.findViewById(R.id.messageTextViewFromLoading)).setText(getString(R.string.posting_tweet_message));
                        mAlertBuilder.setView(view);
                        mAlertBuilder.show();

                      //  HelperMethods.postToTwitter(context, ((Activity) context), "tweet text", new HelperMethods.TwitterCallback() {
                     //       @Override
                      //      public void onFinsihed(Boolean response) {
                     //           Log.d(TAG, "----------------response----------------" + response);
                    //            mAlertBuilder.dismiss();
                    //            Toast.makeText(context, getString(R.string.tweet_posted_on_twitter), Toast.LENGTH_SHORT).show();

                    //        }
                   //     });

                    } catch (Exception ex) {
                        ex.printStackTrace();
                        Toast.makeText(context, "ERROR", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    startActivity(new Intent(context, LoginActivity.class));
                }
            }
        });
  //  }


 /*   @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.base_twitter, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }*/
//}

