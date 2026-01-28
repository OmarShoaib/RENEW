package edu.aku.omarshoaib.renew.database.dao;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

import edu.aku.omarshoaib.renew.global.DateUtils;
import edu.aku.omarshoaib.renew.model.Participant;
import edu.aku.omarshoaib.renew.model.SyncModel;

@Dao
public abstract class ParticipantDao implements BaseDao<Participant> {

    @Query("SELECT * FROM Participant")
    public abstract List<Participant> getAllData();

    @Query("SELECT * FROM Participant WHERE uuid = :uuid AND lineNo = :lineNo")
    public abstract Participant getDataByLineNo(String uuid, int lineNo);

    @Query("SELECT child.* FROM Participant child LEFT JOIN Form1 parent ON child.uuid = parent.uid WHERE (child.uuid IN (:uuid)) OR (child.synced = '' AND parent.synced != '') OR child.isError IS 1")
    public abstract List<Participant> getDataByUuids(List<String> uuid);

    @Query("SELECT * FROM Participant WHERE ((synced IS '' OR synced IS null) AND (syncDate IS '' OR syncDate IS null) AND (iStatus != '' OR iStatus != null)) OR isError IS 1")
    public abstract List<Participant> getAllUnSyncedData();

    @Query("SELECT * FROM Participant WHERE (uuid IN (:uIds) AND (synced IS '' OR synced IS null) AND (syncDate IS '' OR syncDate IS null)) OR isError IS 1")
    public abstract List<Participant> getAllUnSyncedDataByUIds(List<String> uIds);

    @Query("SELECT child.* FROM PARTICIPANT child LEFT JOIN Form1 parent ON child.uuid = parent.uid WHERE (child.uuid IN (:uuid)) OR (child.synced = '' AND parent.synced != '') OR child.isError IS 1")
    public abstract List<Participant> getAllUnSyncedDataByUuIds(List<String> uuid);

    @Query("UPDATE PARTICIPANT SET iStatus = :iStatus, iStatus96x = :iStatus96x, isFormCompleteOnce = :isFormCompleteOnce, endingDate = :endingDate WHERE id = :id")
    public abstract void updateIStatus(long id, String iStatus, String iStatus96x, boolean isFormCompleteOnce, String endingDate);

    // This query is only used for updating sync list
    // id = rowId
    @Query("SELECT * FROM Participant WHERE id = :id")
    public abstract Participant getDataById(long id);

    @Query("SELECT * FROM Participant WHERE uid = :uid")
    public abstract Participant getDataByUid(String uid);

    @Query("SELECT * FROM Participant WHERE uuid = :uuid")
    public abstract List<Participant> getAllDataByUuid(String uuid);

    @Query("SELECT * FROM Participant WHERE sysDate LIKE :date || '%'")
    public abstract List<Participant> getAllByDate(String date);

    // Get total count
    @Query("SELECT COUNT(*) FROM Participant")
    public abstract int getTotalCount();

    @Query("DELETE FROM Participant")
    public abstract void deleteAll();

    // Update sync status as success
    public void updateSyncSuccess(List<SyncModel.WebResponse> responses) {
        if (responses != null && responses.size() > 0 && responses.get(0).getError() == 0) {
            String syncedDate = DateUtils.getCurrentDateTime();
            String synced = "1";
            for (int i = 0; i < responses.size(); i++) {
                Participant obj = getDataById(responses.get(i).getId());
                obj.setSyncDate(syncedDate);
                obj.setSynced(synced);
                obj.setError(false);
                update(obj);
            }
        }
    }

    // Update error status while uploading
    public void updateSyncError(List<Participant> list) {
        for (int i = 0; i < list.size(); i++) {
            Participant obj = list.get(i);
            obj.setError(true);
            update(obj);
        }
    }
}
