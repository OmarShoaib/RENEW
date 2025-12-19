package edu.aku.omarshoaib.renew.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.aku.omarshoaib.renew.global.Callbacks;

public abstract class GenericFilterAdapter<T> extends RecyclerView.Adapter<GenericFilterAdapter<T>.ViewHolder> {
    private final Activity activity;
    private final RecyclerView recyclerView;
    private final Callbacks.IRVOnItemClickListener onItemClickListener;
    private final boolean isMultiSelect;
    private List<T> mainList;
    private List<T> filteredList;

    public GenericFilterAdapter(Activity activity, List<T> mainList, RecyclerView recyclerView,
                                Callbacks.IRVOnItemClickListener onItemClickListener, boolean isMultiSelect) {
        this.activity = activity;
        this.mainList = mainList;
        filteredList = new ArrayList<>(mainList);
        this.recyclerView = recyclerView;
        this.onItemClickListener = onItemClickListener;
        this.isMultiSelect = isMultiSelect;
    }

    protected abstract View createView(Activity activity, ViewGroup viewGroup, int viewType);

    protected abstract void bindView(T item, ViewHolder viewHolder, int position, boolean isMultiSelect);

    protected abstract void filter(String searchText, List<T> mainList, List<T> filteredList);

    protected class ViewHolder extends RecyclerView.ViewHolder {
        private final Map<Integer, View> views;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            views = new HashMap<>();
            views.put(0, itemView);

            itemView.setOnClickListener(view -> {
                try {
                    if (view != null && onItemClickListener != null) {
                        // Sometimes crashing here - Reason still unknown
                        int index = (int) view.getTag();
                        onItemClickListener.onItemClick(recyclerView, filteredList.get(index), index);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }

        public void initViewById(int id) {
            View view = (getView() != null ? getView().findViewById(id) : null);

            if (view != null)
                views.put(id, view);
        }

        public View getView() {
            return getView(0);
        }

        public View getView(int id) {
            if (views.containsKey(id))
                return views.get(id);
            else
                initViewById(id);

            return views.get(id);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        return new ViewHolder(createView(activity, viewGroup, viewType));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        bindView(getItem(position), holder, position, isMultiSelect);
    }

    public Activity getContext() {
        return activity;
    }

    public void _filter(String searchText, List<T> mainList, List<T> filteredList) {
        searchText = searchText.toLowerCase();
        filteredList.clear();
        if (searchText.length() == 0) {
            filteredList.addAll(mainList);
        } else {
            filter(searchText, mainList, filteredList);
           /* for (Followup item : mainList) {
                if (filterType.equals("1")) {
                    // Filter by Name
                    if (item.getPregWomanName().toLowerCase().contains(charText))
                        filteredList.add(item);
                } else {
                    // Filter by Id
                    if (item.getMotherChildId().toLowerCase().contains(charText))
                        filteredList.add(item);
                }
            }*/
        }
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    public T getItem(int index) {
        return ((filteredList != null && index < filteredList.size()) ? filteredList.get(index) : null);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void addAll(List<T> list) {
        filteredList.addAll(list);
        notifyDataSetChanged();
    }

    public List<T> getList() {
        return filteredList;
    }

    public void setList(List<T> mainList) {
        this.mainList = mainList;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void reset() {
        filteredList.clear();
        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateList(List<T> list) {
        this.filteredList.addAll(list);
        notifyDataSetChanged();
    }

    public void updateRangeInserted(List<T> mainList, int positionStart, int itemCount) {
        this.filteredList = mainList;
        notifyItemRangeInserted(positionStart, itemCount);
    }

}