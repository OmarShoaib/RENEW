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
import edu.aku.omarshoaib.renew.activity.sections.woman.Section2.followup.SectionF02b;
import edu.aku.omarshoaib.renew.database.AppDatabase;
import edu.aku.omarshoaib.renew.databinding.ItemFollowupBinding;
import edu.aku.omarshoaib.renew.global.AppConstants;
import edu.aku.omarshoaib.renew.global.MainApp;
import edu.aku.omarshoaib.renew.model.Form2b;
import edu.aku.omarshoaib.renew.model.VForm2b;

public class Followup2bAdapter extends RecyclerView.Adapter<Followup2bAdapter.ViewHolder> implements Filterable {

    private final Activity activity;
    private final List<VForm2b> mainList;
    private List<VForm2b> filteredList;
    private int searchType = 1;

    public Followup2bAdapter(Activity activity, List<VForm2b> mainList) {
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
    public Followup2bAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemFollowupBinding itemView = ItemFollowupBinding.inflate(LayoutInflater.from(activity), parent, false);
        itemView.itemLayout.setOnClickListener(view -> {
            int pos = (int) view.getTag();
            MainApp.vForm2b = filteredList.get(pos);
//            if (MainApp.selectedMWRA.getStatus() != 3) {
            Form2b form2b = AppDatabase.getDBInstance().form2bDao().getDataByParticipantId(MainApp.vForm2b.getParticipantId());
            if (form2b != null) MainApp.form2b = form2b;
            else Form2b.initMeta();
            AppConstants.gotoActivity(activity, SectionF02b.class, true);
//            } else
//                AlertPopup.alert(activity, activity.getString(R.string.form_synced),
//                        activity.getString(R.string.form_synced_desc), AppConstants.TYPE_SUCCESS);
        });
        return new Followup2bAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull Followup2bAdapter.ViewHolder holder, int position) {
        ItemFollowupBinding bi = holder.binding;
        VForm2b vForm2b = filteredList.get(position);
        bi.itemLayout.setTag(position);
        bi.memberNameTV.setText(vForm2b.getParticipantName());
        bi.pIdTV.setText(vForm2b.getParticipantId());
        bi.clusterNoTV.setText(vForm2b.getScreeningDate());

        bi.hhIdTV.setText(
                AppConstants.getRichText(String.format(Locale.getDefault(),
                        activity.getString(R.string.age_c), vForm2b.getAge())
                )
        );
        bi.contactNoTV.setText(vForm2b.getContactNumber());

        Form2b form2b = AppDatabase.getDBInstance().form2bDao()
                .getDataByParticipantId(vForm2b.getParticipantId());
        bi.statusIV.setVisibility(
                form2b == null ? View.GONE : View.VISIBLE);
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
                filteredList = (List<VForm2b>) results.values;
                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<VForm2b> filteredResults;
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

    protected List<VForm2b> getFilteredResults(String constraint) {
        List<VForm2b> results = new ArrayList<>();

        for (VForm2b item : mainList) {
            if (searchType == 1) {
                // Search by Name
                if (item.getParticipantName().toLowerCase().contains(constraint))
                    results.add(item);
            } else if (searchType == 2) {
                // Search by Participant Id
                if (item.getParticipantId().toLowerCase().contains(constraint))
                    results.add(item);
            } else if (searchType == 3) {
                // Search by contact no
                if (item.getContactNumber().contains(constraint))
                    results.add(item);
            }
        }
        return results;
    }
}