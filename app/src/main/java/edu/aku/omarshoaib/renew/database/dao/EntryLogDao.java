package edu.aku.omarshoaib.renew.database.dao;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

import edu.aku.omarshoaib.renew.global.DateUtils;
import edu.aku.omarshoaib.renew.model.EntryLog;
import edu.aku.omarshoaib.renew.model.SyncModel;

@Dao
public abstract class EntryLogDao implements BaseDao<EntryLog> {

    @Query("SELECT * FROM entrylog")
    public abstract List<EntryLog> getAllData();

    @Query("SELECT * FROM entrylog WHERE ((synced IS '' OR synced IS null) AND (syncDate IS '' OR syncDate IS null)) OR isError IS 1")
    public abstract List<EntryLog> getAllUnSyncedData();

    @Query("SELECT * FROM entrylog WHERE id = :id")
    public abstract EntryLog getData(int id);

    @Query("DELETE FROM entrylog")
    public abstract void deleteAll();

    // Update sync status as success
    public void updateSyncSuccess(List<SyncModel.WebResponse> responses) {
        if (responses != null && responses.size() > 0 && responses.get(0).getError() == 0) {
            String syncedDate = DateUtils.getCurrentDateTime();
            String synced = "1";
            for (int i = 0; i < responses.size(); i++) {
                EntryLog entryLog = getData(responses.get(i).getId());
                entryLog.setSyncDate(syncedDate);
                entryLog.setSynced(synced);
                entryLog.setError(false);
                update(entryLog);
            }
        }
    }

    // Update error status while uploading
    public void updateSyncError(List<EntryLog> list) {
        for (int i = 0; i < list.size(); i++) {
            EntryLog obj = list.get(i);
            obj.setError(true);
            update(obj);
        }
    }

}
