package social.evenet.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import social.evenet.R;

/**
 * A simple {@link android.app.Fragment} subclass.
 */
public class MeetupCreateFragmentSecond extends RefreshableFragment {

    private ImageView imageView;
    private EditText name;


    public static Fragment getMeetupCreateFragmetSecond(Bundle b) {
        MeetupCreateFragmentSecond fragmentSecond = new MeetupCreateFragmentSecond();
        fragmentSecond.setArguments(b);
        return fragmentSecond;
    }

    public MeetupCreateFragmentSecond() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_meetup_create_second, container, false);
        imageView = (ImageView) v.findViewById(R.id.image_meetup);
        name = (EditText) v.findViewById(R.id.name_meetup);


        name.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT){
                    InputMethodManager in = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

                    // NOTE: In the author's example, he uses an identifier
                    // called searchBar. If setting this code on your EditText
                    // then use v.getWindowToken() as a reference to your
                    // EditText is passed into this callback as a TextView

                    in.hideSoftInputFromWindow(name
                                    .getApplicationWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                   Bundle bundle=new Bundle();
                    bundle.putString("name",name.getText().toString());
                    bundle.putInt("image",getArguments().getInt("image"));

                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_meetup,MeetupCreateFragmentThrid.getMeetupCreateFragmetSecond(bundle)).commit();
                }
                return false;
            }
        });

        setImageView(getArguments().getInt("image"));


        return v;
    }


    public void setImageView(int a) {

        switch (a) {
            case 1:
                imageView.setImageResource(R.drawable.ic_first_big);
                break;
            case 2:
                imageView.setImageResource(R.drawable.ic_second_big);
                break;
            case 3:
                imageView.setImageResource(R.drawable.ic_third_big);
                break;
            case 4:
                imageView.setImageResource(R.drawable.ic_four_big);
                break;
            case 5:
                imageView.setImageResource(R.drawable.ic_five_big);
                break;
            case 6:
                imageView.setImageResource(R.drawable.ic_six_big);
                break;
            case 7:
                imageView.setImageResource(R.drawable.ic_seven_big);
                break;
            case 8:
                imageView.setImageResource(R.drawable.ic_eight_big);
                break;

        }

    }

}
