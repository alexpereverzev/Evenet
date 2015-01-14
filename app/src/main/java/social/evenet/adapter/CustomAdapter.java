package social.evenet.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import social.evenet.R;
import social.evenet.db.Place;

public class CustomAdapter extends ArrayAdapter<Place> implements Filterable {
    private final String MY_DEBUG_TAG = "CustomerAdapter";
    private ArrayList<Place> items;
    private ArrayList<Place> itemsAll;
    private ArrayList<Place> suggestions;
    private int viewResourceId;

    public CustomAdapter(Context context, int viewResourceId, ArrayList<Place> items) {
        super(context, viewResourceId, items);
        this.items = items;


        this.viewResourceId = viewResourceId;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(viewResourceId, null);
        }
        Place customer = items.get(position);
        if (customer != null) {
            TextView customerNameLabel = (TextView) v.findViewById(R.id.suggest);
            if (customerNameLabel != null) {
//              Log.i(MY_DEBUG_TAG, "getView Customer Name:"+customer.getName());
                customerNameLabel.setText(customer.getPlace_address());
            }
        }
        return v;
    }

    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    Filter nameFilter = new Filter() {
        @Override
        public String convertResultToString(Object resultValue) {
            String str = ((Place)(resultValue)).getPlace_address();
            return str;
        }
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if(constraint != null) {


                FilterResults filterResults = new FilterResults();
                filterResults.values = items;
                filterResults.count = items.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            ArrayList<Place> filteredList = (ArrayList<Place>) results.values;
            if(results != null && results.count > 0) {
                clear();
                for (Place c : filteredList) {
                    add(c);
                }
                notifyDataSetChanged();
            }

        }
    };


}