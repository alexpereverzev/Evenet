package social.evenet.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
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
import social.evenet.db.UserInfo;

public class Registration extends HashActivity {


    private Context mContext;
    private boolean flag = true;
    private Button sign_up;
    private EditText name_surname;
    private EditText email_phone;
    private EditText login;
    private EditText password;
    private EditText confirm_password;
    private String[] param;

    private UserInfo userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);



        sign_up = (Button) findViewById(R.id.sign_up);
        name_surname = (EditText) findViewById(R.id.input_name_surname);
        email_phone = (EditText) findViewById(R.id.input_email_phone);
        login = (EditText) findViewById(R.id.input_login);
        password = (EditText) findViewById(R.id.input_password);
        confirm_password = (EditText) findViewById(R.id.input_confirm_password);
        param = new String[5];

        mContext = this;

        if(getIntent().getParcelableExtra("user")!=null){
            userInfo=getIntent().getParcelableExtra("user");
            name_surname.setText(userInfo.getName()+" "+userInfo.getSurname());
            email_phone.setText(userInfo.getEmail());
        }



        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = true;
                if (!name_surname.getText().toString().isEmpty()) {
                    String[] res = name_surname.getText().toString().split(" ");
                    param[0] = res[0];
                    if (res.length > 1) param[1] = res[1];

                    // param=name_surname.getText().toString().split(" ");
                } else {
                    show_dialog("incorrect name");
                    flag = false;
                }

                if (flag) {

                    if (!isValidEmail(email_phone.getText().toString())) {
                        param[4] = email_phone.getText().toString();
                    } else {
                        show_dialog("incorrect email");
                        flag = false;
                    }


                }

                if (flag) {
                    if (!login.getText().toString().isEmpty()) {
                        param[2] = login.getText().toString();

                    } else {
                        flag = false;
                        show_dialog("incorrect login");

                    }


                    if (flag) {

                        if ((!password.getText().toString().isEmpty()) && (password.getText().toString().equals(confirm_password.getText().toString()))) {
                            param[3] = MD5(login.getText().toString() + password.getText().toString());

                        } else {

                            flag = false;
                            show_dialog("incorrect password");

                        }
                    }


                }


                if (flag) {

                    Map<String,String> options=new LinkedHashMap<String, String>();
                    options.put("name",param[0]);
                    options.put("surname",param[1]);
                    options.put("login",param[2]);
                    options.put("password_hash",param[3]);
                    options.put("email",param[4]);
                    App.getApi().registerUser(options,new Callback<JSONArray>() {
                        @Override
                        public void success(JSONArray jsonArray, Response response) {

                            String res="";

                                JSONObject jsonobject = jsonArray.optJSONObject(0);

                                res = jsonobject.optString("response_code");
                                //res = jsonobject.optString("response_message");
                            if(res.equals("200"))
                            startActivity(new Intent(mContext, Login.class));
                            else  show_dialog("Error Registration");
                        }

                        @Override
                        public void failure(RetrofitError error) {

                        }
                    });


                }
            }
        });


    }





    public final static boolean isValidEmail(CharSequence target) {
        if (TextUtils.isEmpty(target)) {
            return true;
        } else {
            return !android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

}
