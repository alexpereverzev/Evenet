package social.evenet.fragment;



import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;

import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import social.evenet.activity.App;
import social.evenet.activity.Login;
import social.evenet.R;
import social.evenet.activity.MenuNewActivity;
import social.evenet.db.UserInfo;

/**
 * A simple {@link android.app.Fragment} subclass.
 *
 */
public class SettingFragment extends RefreshableFragment {

    private Handler mhandler = new Handler();
    private EditText username;
    private EditText lastname;
    private EditText firstname;
    private EditText email;
    private EditText gender;
    private EditText birthday;
    private EditText change_password;
    private EditText website;
    private EditText phone;

    private TextView logout;
    private Map<String,String> options;
    private  String[] param;
    private SharedPreferences pref;
    private Button save;

    public SettingFragment() {
        // Required empty public constructor
    }

    public static SettingFragment getFragment(UserInfo user) {
        Bundle bundle = new Bundle(1);

                bundle.putParcelable("user", user);
        SettingFragment f=new SettingFragment();
        f.setArguments(bundle);
                return f;

        }
   private int mYear;
    private int mMonth;
    private int mDay;
    private  UserInfo user;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        user = getArguments().getParcelable("user");
        param = new String[5];
        param[0] = getActivity().getSharedPreferences("token_info", getActivity().MODE_PRIVATE).getString("token", "");
        options=new LinkedHashMap<>();
        View v=inflater.inflate(R.layout.fragment_setting, container, false);
        username=(EditText) v.findViewById(R.id.username);
        firstname=(EditText) v.findViewById(R.id.first_name);
        lastname=(EditText) v.findViewById(R.id.last_name);
        email=(EditText) v.findViewById(R.id.email);
        gender=(EditText) v.findViewById(R.id.gender);
        birthday=(EditText) v.findViewById(R.id.birthday);
        change_password=(EditText) v.findViewById(R.id.change_password);
        website=(EditText) v.findViewById(R.id.website);
        logout=(TextView) v.findViewById(R.id.logout);
        phone=(EditText) v.findViewById(R.id.phone);
        final Calendar c = Calendar.getInstance();

        if(user.getName()!=null) firstname.setText(user.getName());
        if(user.getSurname()!=null) lastname.setText(user.getSurname());
        if(user.getB_day()!=null) birthday.setText(user.getB_day());
        if(user.getPhone_number()!=0)
        {
            phone.setText(""+user.getPhone_number());
        }


        birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_YEAR);

                DatePickerDialog dpd = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // Display Selected date in textbox
                                view.toString();
                                birthday.setText(dayOfMonth + "-"
                                        + (monthOfYear + 1) + "-" + year);

                            }
                        }, mYear, mMonth, mDay);

                dpd.show();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pref = getActivity().getSharedPreferences("token_info", getActivity().MODE_PRIVATE);
                SharedPreferences.Editor prefEditor = pref.edit();
                prefEditor.putString("token", "");
                prefEditor.putString("user_id", "");
                prefEditor.commit();
                startActivity(new Intent(getActivity(), Login.class));
            }
        });

        save=(Button) v.findViewById(R.id.save);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    options.put("access_token",param[0]);
                    options.put("name",firstname.getText().toString());
                    options.put("surname",lastname.getText().toString());
                    options.put("phone_number",phone.getText().toString());
                    App.getApi().userChangeInfo(options,callback);
                    Bundle b = new Bundle();


            }
        });

        return v;
    }

    private Callback<JSONArray> callback=new Callback<JSONArray>() {
        @Override
        public void success(JSONArray jsonArray, Response response) {
            String s= getActivity().getClass().getSimpleName();

            if(s.equals("MenuNewActivity"))
                ((MenuNewActivity) getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new ProfileFragment()).commit();
            else {
                ProfilesUsers profileFragment=new ProfilesUsers();
                Bundle b=new Bundle();
                b.putParcelable("user",user);
                profileFragment.setArguments(b);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_users, profileFragment).commit();

            }

        }

        @Override
        public void failure(RetrofitError error) {

        }
    };


}
