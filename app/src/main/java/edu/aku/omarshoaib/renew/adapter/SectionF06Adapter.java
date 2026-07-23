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
import edu.aku.omarshoaib.renew.activity.sections.child.SectionF06;
import edu.aku.omarshoaib.renew.database.AppDatabase;
import edu.aku.omarshoaib.renew.databinding.ItemFollowupBinding;
import edu.aku.omarshoaib.renew.global.AppConstants;
import edu.aku.omarshoaib.renew.global.DateUtils;
import edu.aku.omarshoaib.renew.global.MainApp;
import edu.aku.omarshoaib.renew.model.Form6;
import edu.aku.omarshoaib.renew.model.VFormF06;

public class SectionF06Adapter extends RecyclerView.Adapter<SectionF06Adapter.ViewHolder> implements Filterable {

    private final Activity activity;
    private final List<VFormF06> mainList;
    private List<VFormF06> filteredList;
    private int searchType = 1;

    public SectionF06Adapter(Activity activity, List<VFormF06> mainList) {
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
            MainApp.vFormF06 = filteredList.get(pos);
            if (AppConstants.isEmpty(MainApp.vFormF06.getType())) {
                AppConstants.showSimpleSnackBar(activity, "Please Sync Again ", AppConstants.TYPE_ERROR);
                return;
            }
//            if (MainApp.selectedMWRA.getStatus() != 3) {
            Form6 form6 = AppDatabase.getDBInstance().form6Dao().getDataByParticipantId(MainApp.user.getDistId(),
                    MainApp.vFormF06.getParticipantId(), MainApp.vFormF06.getVisitNumber());
            if (form6 != null) MainApp.form6 = form6;
            else Form6.initMeta();
            MainApp.isSynced = MainApp.form6.getSynced().equals("1");
            AppConstants.gotoActivity(activity, SectionF06.class, true);
//            } else
//                AlertPopup.alert(activity, activity.getString(R.string.form_synced),
//                        activity.getString(R.string.form_synced_desc), AppConstants.TYPE_SUCCESS);
        });
        return new SectionF06Adapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ItemFollowupBinding bi = holder.binding;
        VFormF06 vForm06 = filteredList.get(position);
        bi.itemLayout.setTag(position);
        bi.memberNameTV.setText(vForm06.getChildName());
        bi.pIdTV.setText(vForm06.getParticipantId());
//        bi.clusterNoTV.setText(vForm06.getScreeningDate());
        bi.memberIV.setImageDrawable(AppCompatResources.getDrawable(activity, R.drawable.ic_baby_boy));

        String[] dob = vForm06.getDob().split("-");
        List<String> age = DateUtils.calculateAge(vForm06.getEnrollmentDate(),
                dob[0], dob[1], dob[2]);
        int ageInMonths = DateUtils.getAgeInMonths(age.get(0), age.get(1));
//        bi.clusterNoTV.setText(ageInMonths)
        bi.visitContainer.setVisibility(View.VISIBLE);
        bi.visitNoTV.setText(AppConstants.getRichText(String.format(Locale.getDefault(),
                activity.getString(R.string.visitNo_c), AppConstants.parseInt(vForm06.getVisitNumber()))));
        bi.dueDateTV.setText(AppConstants.getRichText(String.format(Locale.getDefault(),
                activity.getString(R.string.dueDate_c), vForm06.getVisitDate())));
        bi.clusterNoTV.setText(vForm06.getFatherName());
        bi.hhIdTV.setText(AppConstants.getRichText(String.format(Locale.getDefault(),
                        activity.getString(R.string.age_c_months), ageInMonths)));
        bi.contactNoTV.setText(vForm06.getContactNo());

        Form6 form6 = AppDatabase.getDBInstance().form6Dao()
                .getDataByParticipantId(MainApp.user.getDistId(),
                        vForm06.getParticipantId(), vForm06.getVisitNumber());
        bi.statusIV.setVisibility(form6 == null ? View.GONE : View.VISIBLE);
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
                filteredList = (List<VFormF06>) results.values;
                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<VFormF06> filteredResults;
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

    protected List<VFormF06> getFilteredResults(String constraint) {
        List<VFormF06> results = new ArrayList<>();

        for (VFormF06 item : mainList) {
            if (searchType == 1) {
                // Search by Name
                if (item.getChildName().toLowerCase().contains(constraint))
                    results.add(item);
            } else if (searchType == 2) {
                // Search by Participant Id
                if (item.getParticipantId().toLowerCase().contains(constraint))
                    results.add(item);
            } else if (searchType == 3) {
                // Search by contact no
                if (item.getContactNo().contains(constraint))
                    results.add(item);
            }
        }
        return results;
    }
}