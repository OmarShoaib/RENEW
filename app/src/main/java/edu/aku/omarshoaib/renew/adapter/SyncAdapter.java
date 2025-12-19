package edu.aku.omarshoaib.renew.adapter;

import static edu.aku.omarshoaib.renew.global.AppConstants._EMPTY_;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.aku.omarshoaib.renew.R;
import edu.aku.omarshoaib.renew.databinding.ItemSyncBinding;
import edu.aku.omarshoaib.renew.global.AppConstants;
import edu.aku.omarshoaib.renew.global.Callbacks;
import edu.aku.omarshoaib.renew.model.AppInfo;
import edu.aku.omarshoaib.renew.model.SyncModel;
import edu.aku.omarshoaib.renew.webcall.web_client.WebAPI;
import edu.aku.omarshoaib.renew.webcall.web_client.WebClient;

public class SyncAdapter extends RecyclerView.Adapter<SyncAdapter.ViewHolder> {
    private final Activity activity;
    private List<SyncModel> mainList;
    private final Callbacks.IRVOnItemClickListener onItemClickListener;

    public SyncAdapter(Activity activity, List<SyncModel> mainList, Callbacks.IRVOnItemClickListener onItemClickListener) {
        this.activity = activity;
        this.mainList = mainList;
        this.onItemClickListener = onItemClickListener;
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {
        ItemSyncBinding binding;

        public ViewHolder(ItemSyncBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSyncBinding itemView = ItemSyncBinding.inflate(LayoutInflater.from(activity), parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ItemSyncBinding bi = holder.binding;
        SyncModel syncData = mainList.get(position);
        int statusId = syncData.getStatusId();
        String tableName = syncData.getTable().toUpperCase();
        String sectionName = syncData.getTableSections();

        bi.loading.setVisibility(View.VISIBLE);
        bi.tableNameTV.setText(tableName);
        bi.statusTV.setText(syncData.getStatus());
        bi.messageTV.setText(syncData.getMessage());
        bi.sectionTV.setText(AppConstants.isEmpty(sectionName) ? _EMPTY_ : sectionName);
        setStatus(statusId, bi.statusColorView, bi.messageTV, syncData.getMessage(), syncData.isSyncedPerfect(), bi.loading);

        if (tableName.equals(AppInfo.NAME.toUpperCase())) {
            if (!AppInfo.isAppUpdated()) {
                if (!AppConstants.isEmpty(syncData.getMessage()) &&
                        syncData.getMessage().contains("New Version Available")) {
                    // New Version Available
                    String message = syncData.getMessage().split("=")[0].trim();
                    String outputFile = syncData.getMessage().split("=")[1].trim();
                    bi.messageTV.setText(message);
                    bi.newVersionDLBtn.setVisibility(View.VISIBLE);
                    bi.newVersionDLBtn.setOnClickListener(view -> {
                        String link = WebClient.getBaseUrl() + AppConstants.API_NAME +
                                WebAPI.VERSION_OUTPUT_JSON_FILE_PATH.replace("..", "") + "/" + outputFile;
                        AppConstants.openWebLink(activity, link);
                    });
                } else {
                    if (!AppConstants.isEmpty(syncData.getMessage()))
                        // File Not Found
                        bi.messageTV.setText(String.format(activity.getString(R.string.sync_error),
                                activity.getString(R.string.file_not_found)));
                    else bi.messageTV.setText(_EMPTY_);
                    bi.newVersionDLBtn.setVisibility(View.GONE);
                    bi.newVersionDLBtn.setOnClickListener(null);
                    bi.progressTV.setVisibility(View.INVISIBLE);
                }
            } else {
                // Latest Version installed
                bi.messageTV.setText(syncData.getMessage());
                bi.newVersionDLBtn.setVisibility(View.GONE);
                bi.newVersionDLBtn.setOnClickListener(null);
            }
        } else {
            bi.newVersionDLBtn.setVisibility(View.GONE);
            bi.newVersionDLBtn.setOnClickListener(null);
            bi.progressTV.setVisibility(View.INVISIBLE);
        }

        if (tableName.equals("STRINGS") || tableName.equals("RANGES"))
            bi.syncItem.setVisibility(View.GONE);
        else bi.syncItem.setVisibility(View.VISIBLE);
    }

    private void setStatus(int statusId, View statusColorView, TextView messageTV,
                           String message, boolean isSyncedPerfect, ProgressBar loading) {
        if (statusId == 1) {
            // Success
            statusColorView.setBackgroundColor(Color.GREEN);
            messageTV.setText(message);
            loading.setVisibility(View.GONE);
        } else if (statusId == 2) {
            // Error
            statusColorView.setBackgroundColor(Color.RED);
            messageTV.setText(message);
            loading.setVisibility(View.GONE);
        } else {
            // Not Processed
            statusColorView.setBackgroundColor(Color.GRAY);
        }
    }

    @Override
    public int getItemCount() {
        return mainList.size();
    }

}
