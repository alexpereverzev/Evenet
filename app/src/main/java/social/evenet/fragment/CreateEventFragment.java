package social.evenet.fragment;



import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import social.evenet.R;

/**
 * A simple {@link android.app.Fragment} subclass.
 *
 */
public class CreateEventFragment extends RefreshableFragment {

    private EditText name;
    private Button place;
    private EditText data;
    private EditText time;
    private Button create;

    public CreateEventFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_event, container, false);
    }


}
