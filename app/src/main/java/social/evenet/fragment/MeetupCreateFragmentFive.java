package social.evenet.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import social.evenet.R;
import social.evenet.activity.SearchPlaceActivity;


/**
 * A simple {@link android.app.Fragment} subclass.
 */
public class MeetupCreateFragmentFive extends RefreshableFragment {

    private ImageView imageView;
    private TextView name;
    private TextView time;
    private TextView date;
    private ImageView location;
    private LinearLayout invite;
    private ImageView venue;
    final int STATIC_INTEGER_FRIENDS = 101;

    public static Fragment getMeetupCreateFragmetFive(Bundle b) {
        MeetupCreateFragmentFive fragment = new MeetupCreateFragmentFive();
        fragment.setArguments(b);
        return fragment;
    }

    public MeetupCreateFragmentFive() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_create_meetup_five, container, false);
        imageView = (ImageView) v.findViewById(R.id.image_meetup);
        name = (TextView) v.findViewById(R.id.name_meetup);


        name.setText(getArguments().getString("name"));

        setImageView(getArguments().getInt("image"),imageView);

        date = (TextView) v.findViewById(R.id.data_meetup);

        time = (TextView) v.findViewById(R.id.time_meetup);

        date.setText(getArguments().getString("data"));

        time.setText(getArguments().getString("time"));

        location = (ImageView) v.findViewById(R.id.location);

        venue=(ImageView) v.findViewById(R.id.venue);

        venue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), SearchPlaceActivity.class);
                intent.putExtra("bundle",getArguments());
                startActivityForResult(intent,103);
            }
        });

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //     startActivity(new Intent(getActivity(), MapsActivity.class));
                MapsFragment mapsFragment = new MapsFragment();
                mapsFragment.setArguments(getArguments());
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_meetup, mapsFragment).commit();
            }
        });

        invite = (LinearLayout) v.findViewById(R.id.invite_friends_layout);

        return v;
    }



}
