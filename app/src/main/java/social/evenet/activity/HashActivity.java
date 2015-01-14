package social.evenet.activity;

import android.app.Dialog;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import social.evenet.R;

/**
 * Created by Александр on 06.09.2014.
 */
public abstract class HashActivity extends ActionBarActivity {



    RelativeLayout progressBar = null;

    private Handler mHandler = new Handler();


    public String MD5(String md5) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(md5.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
        }
        return null;
    }

    public void show_dialog(String name_error){
        final Dialog dia = new Dialog(this, android.R.style.Theme_Translucent);
        dia.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dia.setCancelable(true);
        dia.setContentView(R.layout.custom_dialog);

        TextView desc=(TextView) dia.findViewById(R.id.disc_error);
        desc.setText(name_error);


        Button cancel=(Button) dia.findViewById(R.id.btncancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dia.dismiss();
            }
        });

        dia.show();

    }




    protected void hideProgressBar(final String message){
        if (progressBar != null) {
            mHandler.post(new Runnable() {
                public void run() {
                    progressBar.setVisibility(View.GONE);

                }
            });
        }
    }




}
