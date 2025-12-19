package edu.aku.omarshoaib.renew.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Filter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.aku.omarshoaib.renew.global.Callbacks;

public abstract class GenericLVFilterAdapter<T> extends ArrayAdapter<T> {

    private final Activity activity;
    //    private final ListView listView;
    private final AutoCompleteTextView autoCompleteTextView;
    private final Callbacks.ILVOnItemClickListener onItemClickListener;
    // This Id is just for reference of item clicked
    private final int clickableViewId;
    private final Map<Integer, View> views = new HashMap<>();
    private List<T> mainList, copiedMainList, mainListFiltered;

    public GenericLVFilterAdapter(Activity activity, List<T> mainList, /*ListView listView,*/
                                  AutoCompleteTextView autoCompleteTextView,
                                  Callbacks.ILVOnItemClickListener onItemClickListener,
                                  int clickableViewId) {
        super(activity, 0, mainList);
        this.activity = activity;
        this.mainList = mainList;
        this.mainListFiltered = mainList;
        /*this.listView = listView;*/
        this.autoCompleteTextView = autoCompleteTextView;
        this.onItemClickListener = onItemClickListener;
        this.clickableViewId = clickableViewId;
    }

    protected class ViewHolder {

        public ViewHolder() {
        }

        public void initViewById(int id) {
            View view = (getView() != null ? getView().findViewById(id) : null);

            if (view != null)
                views.put(id, view);
        }

        public View getView() {
            return getView(0);
        }

        public void setView(View itemView) {
            views.put(0, itemView);
        }

        public View getView(int id) {
            if (views.containsKey(id))
                return views.get(id);
            else
                initViewById(id);

            return views.get(id);
        }
    }

    protected abstract View createView(Activity activity, View convertView, ViewGroup viewGroup, ViewHolder viewHolder, T item, int position);

    protected abstract boolean checkFilterCondition(T item, String charString);

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = createView(activity, convertView, parent, new ViewHolder(), mainList.get(position), position);

        view.setOnClickListener(view1 -> {
            int index = (Integer) view1.findViewById(clickableViewId).getTag();
            T item;
            if (mainListFiltered != null && mainListFiltered.size() > 0)
                item = mainListFiltered.get(index);
            else
                item = mainList.get(index);
//            if (listView != null)
//            onItemClickListener.onItemClick(listView, item, index);
            onItemClickListener.onItemClick(autoCompleteTextView, item, index);
        });

        return view;
    }

    @Override
    public int getCount() {
        return mainList.size();
    }

    @Nullable
    @Override
    public T getItem(int position) {
        return mainList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return filter;
    }

    private final Filter filter = new Filter() {

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            mainList = (List<T>) results.values; // has the filtered values
            notifyDataSetChanged();  // notifies the data with new filtered values
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
            mainListFiltered = new ArrayList<>();

            if (copiedMainList == null) {
                copiedMainList = new ArrayList<>(mainList); // saves the original data in copiedMainList
            }

            /*
             *  If constraint(CharSequence that is received) is null returns the copiedMainList(Original) values
             *  else does the Filtering and returns filteredArrList(filtered)
             */
            if (constraint == null || constraint.length() == 0) {

                // set the Original result to return
                results.count = copiedMainList.size();
                results.values = copiedMainList;
                mainListFiltered = null;
            } else {
                constraint = constraint.toString().toLowerCase();
                for (int i = 0; i < copiedMainList.size(); i++) {
                    T item = copiedMainList.get(i);

                    // Filter Condition
                    if (checkFilterCondition(item, constraint.toString())) {
                        mainListFiltered.add(item);
                    }
                }

                // set the filtered result to return
                results.count = mainListFiltered.size();
                results.values = mainListFiltered;
            }
            return results;
        }

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            /*if (resultValue instanceof Food)
                // Food
                return ((Food) resultValue).getFoodName();
            else
                // Ingredient
                return ((Ingredient) resultValue).getIngredientName();*/
            return "";
        }
    };

}
