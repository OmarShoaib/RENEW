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
import edu.aku.omarshoaib.renew.activity.sections.woman.Section2.followup.SectionF02a;
import edu.aku.omarshoaib.renew.database.AppDatabase;
import edu.aku.omarshoaib.renew.databinding.ItemFollowupBinding;
import edu.aku.omarshoaib.renew.global.AppConstants;
import edu.aku.omarshoaib.renew.global.MainApp;
import edu.aku.omarshoaib.renew.model.Form2a;
import edu.aku.omarshoaib.renew.model.VPHQ9;

public class Followup2aAdapter extends RecyclerView.Adapter<Followup2aAdapter.ViewHolder> implements Filterable {

    private final Activity activity;
    private final List<VPHQ9> mainList;
    private List<VPHQ9> filteredList;
    private int searchType = 1;

    public Followup2aAdapter(Activity activity, List<VPHQ9> mainList) {
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
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemFollowupBinding itemView = ItemFollowupBinding.inflate(LayoutInflater.from(activity), parent, false);
        itemView.itemLayout.setOnClickListener(view -> {
            int pos = (int) view.getTag();
            MainApp.vPHQ9 = filteredList.get(pos);
//            if (MainApp.selectedMWRA.getStatus() != 3) {
            Form2a form2a = AppDatabase.getDBInstance().form2aDao().getDataByParticipantId(MainApp.vPHQ9.getParticipantId());
            if (form2a != null) MainApp.form2a = form2a;
            else Form2a.initMeta();
            MainApp.isSynced = MainApp.form2a.getSynced().equals("1");
            AppConstants.gotoActivity(activity, SectionF02a.class, true);
//            } else
//                AlertPopup.alert(activity, activity.getString(R.string.form_synced),
//                        activity.getString(R.string.form_synced_desc), AppConstants.TYPE_SUCCESS);
        });
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ItemFollowupBinding bi = holder.binding;
        VPHQ9 vphq9 = filteredList.get(position);
        bi.itemLayout.setTag(position);
        bi.memberNameTV.setText(vphq9.getParticipantName());
        bi.pIdTV.setText(vphq9.getParticipantId());
        bi.clusterNoTV.setText(vphq9.getScreeningDate());
        bi.hhIdTV.setText(
                AppConstants.getRichText(String.format(Locale.getDefault(),
                        activity.getString(R.string.age_c), vphq9.getAge())
                )
        );
        bi.contactNoTV.setText(vphq9.getContactNumber());

        Form2a form2a = AppDatabase.getDBInstance().form2aDao()
                .getDataByParticipantId(vphq9.getParticipantId());
            bi.statusIV.setVisibility(
                    form2a == null ? View.GONE : View.VISIBLE);

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
                filteredList = (List<VPHQ9>) results.values;
                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<VPHQ9> filteredResults;
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

    protected List<VPHQ9> getFilteredResults(String constraint) {
        List<VPHQ9> results = new ArrayList<>();

        for (VPHQ9 item : mainList) {
            if (searchType == 1) {
                // Search by Name
                if (item.getParticipantName().toLowerCase().contains(constraint)) results.add(item);
            } else if (searchType == 2) {
                // Search by Participant Id
                if (item.getParticipantId().toLowerCase().contains(constraint)) results.add(item);
            } else if (searchType == 3) {
                // Search by Participant Id
                if (item.getContactNumber().contains(constraint)) results.add(item);
            }
        }
        return results;
    }
}