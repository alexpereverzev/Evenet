package social.evenet.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import social.evenet.R;

/**
 * A simple {@link android.app.Fragment} subclass.
 */
public class MeetupCreateFragmentThrid extends RefreshableFragment {

    private ImageView imageView;
    private TextView name;
    private CaldroidFragment caldroidFragment;


    public static Fragment getMeetupCreateFragmetSecond(Bundle b) {
        MeetupCreateFragmentThrid fragment = new MeetupCreateFragmentThrid();
        fragment.setArguments(b);
        return fragment;
    }

    public MeetupCreateFragmentThrid() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_meetup_create_third, container, false);
        imageView = (ImageView) v.findViewById(R.id.image_meetup);
        name = (TextView) v.findViewById(R.id.name_meetup);


        name.setText(getArguments().getString("name"));

        setImageView(getArguments().getInt("image"), imageView);

        final SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM, yyyy");
        caldroidFragment = new CaldroidFragment();

        Bundle args = new Bundle();
        Calendar cal = Calendar.getInstance();
        args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
        args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
        args.putBoolean(CaldroidFragment.ENABLE_SWIPE, true);
        args.putBoolean(CaldroidFragment.SIX_WEEKS_IN_CALENDAR, true);
        caldroidFragment.setArguments(args);
        caldroidFragment.setCaldroidListener(new CaldroidListener() {
            @Override
            public void onSelectDate(Date date, View view) {
                String s = formatter.format(date);
                Bundle b=getArguments();
                b.putString("data",s);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_meetup, MeetupCreateFragmentFour.getMeetupCreateFragmetSecond(b)).commit();
            }
        });

        FragmentTransaction t = getActivity().getSupportFragmentManager().beginTransaction();
        t.replace(R.id.calendar1, caldroidFragment);
        t.commit();

        return v;
    }


}
