package social.evenet.adapter;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import java.util.ArrayList;

import  social.evenet.db.AutoCompleteSearch;
import  social.evenet.db.Place;

/**
 * Created by Александр on 28.09.2014.
 */
public class AutoCompleteAdapter <T> extends ArrayAdapter<Place> implements Filterable {

    public AutoCompleteAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    private ArrayList<Place> ite;

    public void setItem(ArrayList<Place> item){
        ite=item;
    }



    @Override
    public Filter getFilter() {
        return filter;
    }

    private Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();
            if (constraint != null) {
                filterResults.count = getCount();
            }

  //          filterResults.values=
            // do some other stuff

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence contraint, FilterResults results) {
            if (results != null && results.count > 0) {
                notifyDataSetChanged();
            }
        }

    };
}