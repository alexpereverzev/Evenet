package social.evenet.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseAnalytics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import social.evenet.R;
import social.evenet.adapter.CompanyAdapter;
import social.evenet.db.Company;
import social.evenet.exeption.ContextNullReference;
import social.evenet.helper.Util;

/**
 * Created by Александр on 21.09.2014.
 */
public class CompanyFragment extends RefreshableFragment {
    private Handler mhandler = new Handler();
    private  String[] param;
    private Company company;
    private CompanyAdapter adapter;
    private ArrayList<Company> item;
    private TextView title_company;
    private TextView website;
    private ImageView logo_company;

    public CompanyFragment() {
        // Required empty public constructor
    }

    public static CompanyFragment getCompanyFragment(Bundle b) {

        CompanyFragment fragment=new CompanyFragment();
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

         if(getArguments()!=null){
             company=getArguments().getParcelable("company");
         }

        Map<String, String> dimensions = new HashMap<String, String>();
        dimensions.put("page", "Company Info");
        dimensions.put("position", "0");
        ParseAnalytics.trackEvent("Page View", dimensions);

        View v = inflater.inflate(R.layout.adapter_company, container, false);
        website=(TextView) v.findViewById(R.id.website_company);
        title_company=(TextView) v.findViewById(R.id.title_company);
        param = new String[4];
        param[0] = getActivity().getSharedPreferences("token_info", getActivity().MODE_PRIVATE).getString("token", "");
        param[1]=""+company.getCompany_id();




        return v;
    }




}
