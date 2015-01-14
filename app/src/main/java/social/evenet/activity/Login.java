package social.evenet.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import social.evenet.R;


public class Login extends HashActivity {

    private Button sign_up;
    private long timestamp;
    private SharedPreferences pref;
    private EditText input_login;
    private EditText input_pass;
    private Button sign_in;
    private Context mContext;
    private String[] parameter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sign_up = (Button) findViewById(R.id.enter_button);
        mContext = this;
        input_login = (EditText) findViewById(R.id.input_login);
        input_pass = (EditText) findViewById(R.id.input_password);
        sign_in = (Button) findViewById(R.id.sign_in_button);
        timestamp = System.currentTimeMillis() / 1000L;
        input_login.setText("t001");
        input_pass.setText("evenet");
        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (input_login.getText().toString().isEmpty()) {
                    show_dialog(getResources().getString(R.string.error_login));
                } else if (input_pass.getText().toString().isEmpty()) {
                    show_dialog(getResources().getString(R.string.error_pass));
                } else {

                    //"t001"+"evenet"
                    String hash = MD5(input_login.getText().toString() + input_pass.getText().toString());
                    String res = MD5(hash + timestamp);
                    parameter = new String[4];
                    parameter[0] = input_login.getText().toString();
                    parameter[1] = res;
                    parameter[2] = "" + timestamp;
                    Map<String, String> opt = new LinkedHashMap<String, String>();
                    opt.put("login", input_login.getText().toString());
                    opt.put("hash", res);
                    opt.put("timestamp", "" + timestamp);
                    App.getApi().login(opt, new Callback<JSONArray>() {
                        @Override
                        public void success(JSONArray jsonArray, Response response) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonobject = jsonArray.optJSONObject(i);
                                pref = getSharedPreferences("token_info", MODE_PRIVATE);
                                SharedPreferences.Editor prefEditor = pref.edit();
                                prefEditor.putString("token", jsonobject.optString("access_token"));
                                prefEditor.putString("user_id", jsonobject.optString("user_id"));
                                prefEditor.commit();
                                parameter[0] = jsonobject.optString("access_token");
                                parameter[1] = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                                parameter[2] = getDeviceName();
                                parameter[3] = Build.VERSION.RELEASE;
                                Map<String, String> options = new LinkedHashMap<String, String>();
                                options.put("access_token", jsonobject.optString("access_token"));
                                options.put("token", Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));
                                options.put("system_version", getDeviceName());
                                options.put("device_model", Build.VERSION.RELEASE);
                                App.getApi().registerDevice(options, call);
                                show_access_dialog();
                            }
                        }

                        @Override
                        public void failure(RetrofitError error) {

                        }
                    });

                }
            }
        });

        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext, LaucherSingUpActivity.class));

            }
        });


    }

    private Callback<JSONArray> call = new Callback<JSONArray>() {
        @Override
        public void success(JSONArray jsonArray, Response response) {
            startActivity(new Intent(mContext, MenuNewActivity.class));
            finish();
        }

        @Override
        public void failure(RetrofitError error) {

        }
    };


    public void show_access_dialog() {
        final Dialog dia = new Dialog(this, android.R.style.Theme_Translucent);
        dia.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dia.setCancelable(true);
        dia.setContentView(R.layout.register_device_dialog);

        Button cancel = (Button) dia.findViewById(R.id.btncancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dia.dismiss();
            }
        });


        Button ok = (Button) dia.findViewById(R.id.btnok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dia.dismiss();

            }
        });


        dia.show();

    }

    public String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            String[] mac = model.split("-");
            String result = mac[0].substring(0, mac[0].length() - 1);
            result = result.replace(" ", "_");
            return result;

        }
    }


    private String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }


}
