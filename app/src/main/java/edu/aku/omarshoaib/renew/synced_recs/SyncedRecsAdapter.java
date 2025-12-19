package edu.aku.omarshoaib.renew.synced_recs;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import edu.aku.omarshoaib.renew.databinding.ItemSyncedRecsBinding;
import edu.aku.omarshoaib.renew.global.Callbacks;
import edu.aku.omarshoaib.renew.global.MainApp;

public class SyncedRecsAdapter extends RecyclerView.Adapter<SyncedRecsAdapter.ViewHolder> {

    private final Activity activity;
    private List<JSONObject> mainList;
    private final String mainTable;
    private final Callbacks.IRVOnItemClickListener onItemClickListener;

    private Gson gsonCustom;

    public SyncedRecsAdapter(Activity activity, List<JSONObject> mainList, String mainTable,
                             Callbacks.IRVOnItemClickListener onItemClickListener) {
        this.activity = activity;
        this.mainList = mainList;
        this.mainTable = mainTable;
        this.onItemClickListener = onItemClickListener;
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {
        ItemSyncedRecsBinding binding;

        public ViewHolder(ItemSyncedRecsBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSyncedRecsBinding itemView = ItemSyncedRecsBinding.inflate(LayoutInflater.from(activity),
                parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            ItemSyncedRecsBinding bi = holder.binding;
            JSONObject jObject = mainList.get(position);
            String[] fieldsToDisplay = DownloadSyncedRecsData.getSyncedRecsDisplayFields(mainTable);
            assert fieldsToDisplay != null;

            bi.itemLayout.setTag(position);
            bi.siteAC.setText(MainApp.selectedCluster.getClusterNo());
            // replaceAll is to remove all spaces from field names if any
            bi.memberNameTV.setText(jObject.getString(fieldsToDisplay[0].replaceAll("\\s", "")));
            bi.usernameTV.setText(MainApp.user.getUsername());
            bi.checkedIdTV.setText(jObject.getString(fieldsToDisplay[1].replaceAll("\\s", "")));
            bi.sysDateTV.setText(jObject.getString(fieldsToDisplay[2].replaceAll("\\s", "")));

            bi.itemLayout.setOnClickListener(view -> {
                int pos = (int) view.getTag();
                String jObjectStr = mainList.get(pos).toString();
                Class<?> clazz = DownloadSyncedRecsData.getObjectClazz(mainTable);
                assert clazz != null;
                gsonCustom = new GsonBuilder()
                        .registerTypeAdapter(clazz, new GSONTypedAdapter())
                        .create();
                Object obj = gsonCustom.fromJson(jObjectStr, clazz);
                onItemClickListener.onItemClick(null, obj, pos);
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return mainList.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void addAll(List<JSONObject> list) {
        mainList.addAll(list);
        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void reset() {
        mainList.clear();
        notifyDataSetChanged();
    }

    public void updateRangeInserted(List<JSONObject> mainList, int positionStart, int itemCount) {
        this.mainList = mainList;
        notifyItemRangeInserted(positionStart, itemCount);
    }
}
