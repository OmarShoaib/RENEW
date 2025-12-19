package edu.aku.omarshoaib.renew.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.aku.omarshoaib.renew.global.Callbacks;

public abstract class GenericAdapter<T> extends RecyclerView.Adapter<GenericAdapter<T>.ViewHolder> {
    private final Activity activity;
    private List<T> mainList;
    private final RecyclerView recyclerView;
    private final Callbacks.IRVOnItemClickListener onItemClickListener;
    private final boolean isMultiSelect;

    protected abstract View createView(Activity activity, ViewGroup viewGroup, int viewType);

    protected abstract void bindView(T item, ViewHolder viewHolder, int position, boolean isMultiSelect);

    public GenericAdapter(Activity activity, List<T> mainList, RecyclerView recyclerView,
                          Callbacks.IRVOnItemClickListener onItemClickListener, boolean isMultiSelect) {
        this.activity = activity;
        this.mainList = mainList;
        this.recyclerView = recyclerView;
        this.onItemClickListener = onItemClickListener;
        this.isMultiSelect = isMultiSelect;
    }

    @Override
    public int getItemCount() {
        return mainList.size();
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

    public T getItem(int index) {
        return ((mainList != null && index < mainList.size()) ? mainList.get(index) : null);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void addAll(List<T> list) {
        mainList.addAll(list);
        notifyDataSetChanged();
    }

    public Activity getContext() {
        return activity;
    }

    public void setList(List<T> mainList) {
        this.mainList = mainList;
    }

    public List<T> getList() {
        return mainList;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void reset() {
        mainList.clear();
        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateList(List<T> list) {
        this.mainList.addAll(list);
        notifyDataSetChanged();
    }

    public void updateRangeInserted(List<T> mainList, int positionStart, int itemCount) {
        this.mainList = mainList;
        notifyItemRangeInserted(positionStart, itemCount);
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {
        private final Map<Integer, View> views;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            views = new HashMap<>();
            views.put(0, itemView);

            itemView.setOnClickListener(view -> {
                int index = (int) view.getTag();
                if (onItemClickListener != null)
                    onItemClickListener.onItemClick(recyclerView, mainList.get(index), index);
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

    // Enable all layouts to be clicked in view mode
    // i.e. when form1 will be viewed after sync, the global enableDisableViews()
    // function on BaseActivity disable all views so we explicitly enable
    // views to be clickable even after sync
    /*public void enableItemClick(RecyclerView recyclerView) {
        for (int i = 0; i < getItemCount(); i++) {
            ViewHolder viewHolder = (ViewHolder) recyclerView.findViewHolderForAdapterPosition(i);
            if (viewHolder != null)
                viewHolder.itemView.setEnabled(true);
        }
    }*/
}