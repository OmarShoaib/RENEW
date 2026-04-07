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
import edu.aku.omarshoaib.renew.activity.sections.child.SectionF05A;
import edu.aku.omarshoaib.renew.database.AppDatabase;
import edu.aku.omarshoaib.renew.databinding.ItemFollowupBinding;
import edu.aku.omarshoaib.renew.global.AppConstants;
import edu.aku.omarshoaib.renew.global.DateUtils;
import edu.aku.omarshoaib.renew.global.MainApp;
import edu.aku.omarshoaib.renew.model.Form5A;
import edu.aku.omarshoaib.renew.model.VFormF05A;

public class FollowUp5aAdapter extends RecyclerView.Adapter<FollowUp5aAdapter.ViewHolder> implements Filterable{
    private final Activity activity;
    private final List<VFormF05A> mainList;
    private List<VFormF05A> filteredList;
    private int searchType = 1;

    public FollowUp5aAdapter(Activity activity, List<VFormF05A> mainList) {
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
    public FollowUp5aAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemFollowupBinding itemView = ItemFollowupBinding.inflate(LayoutInflater.from(activity), parent, false);
        itemView.itemLayout.setOnClickListener(view -> {
            int pos = (int) view.getTag();
            MainApp.vFormF05a = filteredList.get(pos);
//            if (MainApp.selectedMWRA.getStatus() != 3) {
            Form5A form5a = AppDatabase.getDBInstance().form05aDao().getDataByParticipantId(MainApp.user.getDistId(),
                    MainApp.vFormF05a.getParticipantId());
            if (form5a != null) MainApp.form5a = form5a;
            else Form5A.initMeta();
            AppConstants.gotoActivity(activity, SectionF05A.class, true);
//            } else
//                AlertPopup.alert(activity, activity.getString(R.string.form_synced),
//                        activity.getString(R.string.form_synced_desc), AppConstants.TYPE_SUCCESS);
        });
        return new FollowUp5aAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FollowUp5aAdapter.ViewHolder holder, int position) {
        ItemFollowupBinding bi = holder.binding;
        VFormF05A vFormF05A = filteredList.get(position);
        bi.itemLayout.setTag(position);
        bi.memberNameTV.setText(vFormF05A.getChildName());
        bi.pIdTV.setText(vFormF05A.getParticipantId());
//        bi.clusterNoTV.setText(vFormF05A.getScreeningDate());
        bi.memberIV.setImageDrawable(AppCompatResources.getDrawable(activity, R.drawable.ic_baby_boy));

        String[] dob = vFormF05A.getDob().split("-");
        List<String> age = DateUtils.calculateAge(vFormF05A.getEnrollmentDate(),
                dob[0], dob[1], dob[2]);
        int ageInMonths = DateUtils.getAgeInMonths(age.get(0), age.get(1));
//        bi.clusterNoTV.setText(ageInMonths)
        bi.clusterNoTV.setText(vFormF05A.getFatherName());
        bi.hhIdTV.setText(
                AppConstants.getRichText(String.format(Locale.getDefault(),
                        activity.getString(R.string.age_c_months), ageInMonths)
                )
        );
        bi.contactNoTV.setText(vFormF05A.getContactNo());

        Form5A form5a = AppDatabase.getDBInstance().form05aDao()
                .getDataByParticipantId(MainApp.user.getDistId(),
                        vFormF05A.getParticipantId());
        bi.statusIV.setVisibility(
                form5a == null ? View.GONE : View.VISIBLE);
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
                filteredList = (List<VFormF05A>) results.values;
                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<VFormF05A> filteredResults;
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

    protected List<VFormF05A> getFilteredResults(String constraint) {
        List<VFormF05A> results = new ArrayList<>();

        for (VFormF05A item : mainList) {
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