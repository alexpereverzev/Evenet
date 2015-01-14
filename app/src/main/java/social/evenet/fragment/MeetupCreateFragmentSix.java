package social.evenet.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import social.evenet.R;
import social.evenet.db.Place;


/**
 * A simple {@link android.app.Fragment} subclass.
 */
public class MeetupCreateFragmentSix extends RefreshableFragment {

    private ImageView imageView;
    private TextView name;
    private TextView time;
    private TextView date;
    private TextView address;
    private ImageView disable_address;
    private LinearLayout invite;
    final int STATIC_INTEGER_FRIENDS = 101;

    public static Fragment getMeetupCreateFragmetSix(Bundle b) {
        MeetupCreateFragmentSix fragment = new MeetupCreateFragmentSix();
        fragment.setArguments(b);
        return fragment;
    }

    public MeetupCreateFragmentSix() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_create_meetup_six, container, false);
        imageView = (ImageView) v.findViewById(R.id.image_meetup);
        name = (TextView) v.findViewById(R.id.name_meetup);


        name.setText(getArguments().getString("name"));

        setImageView(getArguments().getInt("image"), imageView);

        date = (TextView) v.findViewById(R.id.data_meetup);

        time=(TextView) v.findViewById(R.id.time_meetup);

        date.setText(getArguments().getString("data"));

        time.setText(getArguments().getString("time"));

        address=(TextView) v.findViewById(R.id.address_meetup);
        if(getArguments().getStringArray("address")!=null){
            String [] a=getArguments().getStringArray("address");
            String ad="";
            if(a[0]!=null){
                ad=ad+" "+a[0];
            }
            if(a[1]!=null){
                ad=ad+" "+a[1];
            }
            if(a[2]!=null){
                ad=ad+" "+a[2];
            }

            address.setText(ad);

        }
        else {
        Place place= getArguments().getParcelable("place");
            address.setText(place.getPlace_title());
        }

        disable_address=(ImageView) v.findViewById(R.id.disable_address);

        disable_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
           //     startActivity(new Intent(getActivity(), MapsActivity.class));
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_meetup, MeetupCreateFragmentFive.getMeetupCreateFragmetFive(getArguments())).commit();
            }
        });
        invite=(LinearLayout) v.findViewById(R.id.invite_friends_layout);
        invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InviteFriendsFragment inviteFriendsFragment=new InviteFriendsFragment();
                inviteFriendsFragment.setArguments(getArguments());
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_meetup, inviteFriendsFragment).commit();
            }
        });

        return v;
    }



}
