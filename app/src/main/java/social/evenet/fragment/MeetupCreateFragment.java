package social.evenet.fragment;



import android.location.Location;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import social.evenet.R;

/**
 * A simple {@link android.app.Fragment} subclass.
 *
 */
public class MeetupCreateFragment extends RefreshableFragment implements View.OnClickListener {

    private ImageView first;
    private ImageView second;
    private ImageView thrid;
    private ImageView four;
    private ImageView five;
    private ImageView six;
    private ImageView seven;
    private ImageView eight;


    public MeetupCreateFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_meetup_create, container, false);
        first=(ImageView) v.findViewById(R.id.present);
        second=(ImageView) v.findViewById(R.id.message);
        thrid=(ImageView) v.findViewById(R.id.circle);
        four=(ImageView) v.findViewById(R.id.shopping);
        five=(ImageView) v.findViewById(R.id.music);
        six=(ImageView) v.findViewById(R.id.book);
        seven=(ImageView) v.findViewById(R.id.love);
        eight=(ImageView) v.findViewById(R.id.gym);

        first.setOnClickListener(this);
        second.setOnClickListener(this);
        thrid.setOnClickListener(this);
        four.setOnClickListener(this);
        five.setOnClickListener(this);
        six.setOnClickListener(this);
        seven.setOnClickListener(this);
        eight.setOnClickListener(this);


        return v;
    }


    @Override
    public void onClick(View v) {

        Bundle b=new Bundle();
        switch (v.getId()){
            case R.id.present: b.putInt("image",1); break;
            case R.id.message:  b.putInt("image",2); break;
            case R.id.circle:  b.putInt("image",3); break;
            case R.id.shopping:  b.putInt("image",4); break;
            case R.id.music:  b.putInt("image",5); break;
            case R.id.book:  b.putInt("image",6); break;
            case R.id.love:  b.putInt("image",7); break;
            case R.id.gym:  b.putInt("image",8); break;

        }
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_meetup, MeetupCreateFragmentSecond.getMeetupCreateFragmetSecond(b)).commit();
    }


}
