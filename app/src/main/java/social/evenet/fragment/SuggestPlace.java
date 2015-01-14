package social.evenet.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.ArrayList;

import social.evenet.R;
import social.evenet.adapter.SuggestPlaceAdapter;


public class SuggestPlace extends Fragment {

    private static final String ARG_PARAM1 = "param";
    private static int a=1;

    public static SuggestPlace newInstance(String param1) {
        SuggestPlace fragment = new SuggestPlace();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, Integer.valueOf(param1));

        fragment.setArguments(args);
        return fragment;
    }
    public SuggestPlace() {
        // Required empty public constructor
    }
    private GridView gridView;
    private SuggestPlaceAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_suggest_place, container, false);
        gridView=(GridView) v.findViewById(R.id.grid_place);
        ArrayList<social.evenet.db.SuggestPlace> item=new ArrayList<social.evenet.db.SuggestPlace>();

        if(getArguments()!=null){
           a=getArguments().getInt(ARG_PARAM1);
        }

        switch (a){
            case 1:
                social.evenet.db.SuggestPlace suggestPlace1=new social.evenet.db.SuggestPlace("Title", R.drawable.ic_spa);
                social.evenet.db.SuggestPlace suggestPlace2=new social.evenet.db.SuggestPlace("Title", R.drawable.ic_2);
                social.evenet.db.SuggestPlace suggestPlace3=new social.evenet.db.SuggestPlace("Title", R.drawable.ic_3);
                social.evenet.db.SuggestPlace suggestPlace4=new social.evenet.db.SuggestPlace("Title", R.drawable.ic_4);
                social.evenet.db.SuggestPlace suggestPlace5=new social.evenet.db.SuggestPlace("Title", R.drawable.ic_5);
                social.evenet.db.SuggestPlace suggestPlace6=new social.evenet.db.SuggestPlace("Title", R.drawable.ic_6);
                social.evenet.db.SuggestPlace suggestPlace7=new social.evenet.db.SuggestPlace("Title", R.drawable.ic_7);
                social.evenet.db.SuggestPlace suggestPlace8=new social.evenet.db.SuggestPlace("Title", R.drawable.ic_8);

                item.add(suggestPlace1);
                item.add(suggestPlace2);
                item.add(suggestPlace3);
                item.add(suggestPlace4);
                item.add(suggestPlace5);
                item.add(suggestPlace6);
                item.add(suggestPlace7);
                item.add(suggestPlace8);

                break;
            case 2:
                social.evenet.db.SuggestPlace suggestPlace9=new social.evenet.db.SuggestPlace("Title", R.drawable.ic_9);
                social.evenet.db.SuggestPlace suggestPlace10=new social.evenet.db.SuggestPlace("Title", R.drawable.ic_11);
                social.evenet.db.SuggestPlace suggestPlace11=new social.evenet.db.SuggestPlace("Title", R.drawable.ic_12);
                social.evenet.db.SuggestPlace suggestPlace12=new social.evenet.db.SuggestPlace("Title", R.drawable.ic_13);
                social.evenet.db.SuggestPlace suggestPlace13=new social.evenet.db.SuggestPlace("Title", R.drawable.ic_14);
                social.evenet.db.SuggestPlace suggestPlace14=new social.evenet.db.SuggestPlace("Title", R.drawable.ic_15);
                social.evenet.db.SuggestPlace suggestPlace15=new social.evenet.db.SuggestPlace("Title", R.drawable.ic_16);
                social.evenet.db.SuggestPlace suggestPlace16=new social.evenet.db.SuggestPlace("Title", R.drawable.ic_17);
                 item.add(suggestPlace9);
                item.add(suggestPlace10);
                item.add(suggestPlace11);
                item.add(suggestPlace12);
                item.add(suggestPlace13);
                item.add(suggestPlace14);
                item.add(suggestPlace15);
                item.add(suggestPlace16);
                break;
            case 3:

                social.evenet.db.SuggestPlace suggestPlace17=new social.evenet.db.SuggestPlace("Title", R.drawable.ic_18);
                social.evenet.db.SuggestPlace suggestPlace18=new social.evenet.db.SuggestPlace("Title", R.drawable.ic_19);
                social.evenet.db.SuggestPlace suggestPlace19=new social.evenet.db.SuggestPlace("Title", R.drawable.ic_20);
                social.evenet.db.SuggestPlace suggestPlace20=new social.evenet.db.SuggestPlace("Title", R.drawable.ic_21);
                social.evenet.db.SuggestPlace suggestPlace21=new social.evenet.db.SuggestPlace("Title", R.drawable.ic_22);
                social.evenet.db.SuggestPlace suggestPlace22=new social.evenet.db.SuggestPlace("Title", R.drawable.ic_23);
                social.evenet.db.SuggestPlace suggestPlace23=new social.evenet.db.SuggestPlace("Title", R.drawable.ic_24);
                social.evenet.db.SuggestPlace suggestPlace24=new social.evenet.db.SuggestPlace("Title", R.drawable.ic_25);
                item.add(suggestPlace17);
                item.add(suggestPlace18);
                item.add(suggestPlace19);
                item.add(suggestPlace20);
                item.add(suggestPlace21);
                item.add(suggestPlace22);
                item.add(suggestPlace23);
                item.add(suggestPlace24);

                break;

        }
        a++;
        if(a>3)a=1;
        adapter=new SuggestPlaceAdapter(getActivity(), item);
        gridView.setAdapter(adapter);


        return v;
    }


}
