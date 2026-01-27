package edu.aku.omarshoaib.renew.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import edu.aku.omarshoaib.renew.R;
import edu.aku.omarshoaib.renew.activity.sections.woman.Section3.followup.SectionF03a;
import edu.aku.omarshoaib.renew.database.AppDatabase;
import edu.aku.omarshoaib.renew.databinding.ItemFollowupBinding;
import edu.aku.omarshoaib.renew.global.AppConstants;
import edu.aku.omarshoaib.renew.global.MainApp;
import edu.aku.omarshoaib.renew.model.Form3a;
import edu.aku.omarshoaib.renew.model.VForm3a;

public class Followup3aAdapter extends RecyclerView.Adapter<Followup3aAdapter.ViewHolder> implements Filterable {

    private final Activity activity;
    private final List<VForm3a> mainList;
    private List<VForm3a> filteredList;
    private int searchType = 1;

    public Followup3aAdapter(Activity activity, List<VForm3a> mainList) {
        this.activity = activity;
        this.mainList = mainList;
        this.filteredList = new ArrayList<>(mainList);
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {
        ItemFollowupBinding binding;

        public ViewHolder(ItemFollowupBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }

    @NonNull
    @Override
    public Followup3aAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemFollowupBinding itemView = ItemFollowupBinding.inflate(LayoutInflater.from(activity), parent, false);
        itemView.itemLayout.setOnClickListener(view -> {
            int pos = (int) view.getTag();
            MainApp.vForm3a = filteredList.get(pos);
            Form3a form3a = AppDatabase.getDBInstance().form3aDao().getDataByParticipantId(MainApp.vForm3a.getParticipantId());
            if (form3a != null) MainApp.form3a = form3a;
            else Form3a.initMeta();
            AppConstants.gotoActivity(activity, SectionF03a.class, true);
        });
        return new Followup3aAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull Followup3aAdapter.ViewHolder holder, int position) {
        ItemFollowupBinding bi = holder.binding;
        VForm3a vForm3a = filteredList.get(position);
        Form3a form3a = AppDatabase.getDBInstance().form3aDao()
                .getDataByParticipantId(vForm3a.getParticipantId());
        bi.statusIV.setVisibility(form3a != null ? View.VISIBLE : View.GONE);
        bi.itemLayout.setTag(position);
        bi.memberNameTV.setText(vForm3a.getParticipantName());
        bi.pIdTV.setText(vForm3a.getParticipantId());
        bi.clusterNoTV.setText(vForm3a.getScreeningDate());

        bi.hhIdTV.setText(
                AppConstants.getRichText(String.format(Locale.getDefault(),
                        activity.getString(R.string.age_c), vForm3a.getAge())
                )
        );
        bi.contactNoTV.setText(vForm3a.getContactNumber());
    }

    @Override
    public int getItemCount() {
        return filteredList != null ? filteredList.size() : 0;
    }

    public void setSearchType(int searchType) {
        this.searchType = searchType;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @SuppressLint("NotifyDataSetChanged")
            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredList = (List<VForm3a>) results.values;
                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<VForm3a> filteredResults;
                if (constraint.length() == 0) {
                    filteredResults = mainList;
                } else {
                    filteredResults = getFilteredResults(constraint.toString().toLowerCase());
                }

                FilterResults results = new FilterResults();
                results.values = filteredResults;

                return results;
            }
        };
    }

    protected List<VForm3a> getFilteredResults(String constraint) {
        List<VForm3a> results = new ArrayList<>();

        for (VForm3a item : mainList) {
            if (searchType == 1) {
                // Search by Name
                if (item.getParticipantName().toLowerCase().contains(constraint))
                    results.add(item);
            } else if (searchType == 2) {
                // Search by Participant Id
                if (item.getParticipantId().toLowerCase().contains(constraint))
                    results.add(item);
            } else if (searchType == 3) {
                // Search by Contact No
                if (item.getContactNumber().contains(constraint))
                    results.add(item);
            }
        }
        return results;
    }
}