package edu.aku.omarshoaib.renew.database.dao;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

import edu.aku.omarshoaib.renew.global.DateUtils;
import edu.aku.omarshoaib.renew.model.Form1;
import edu.aku.omarshoaib.renew.model.Participant;
import edu.aku.omarshoaib.renew.model.SyncModel;

@Dao
public abstract class Form1Dao implements BaseDao<Form1> {

    @Query("SELECT * FROM Form1")
    public abstract List<Form1> getAllData();

    // This query is used to check if the data in the table exists
    @Query("SELECT * FROM Form1 Order by id DESC Limit 1")
    public abstract Form1 isDataExists();

    @Query("SELECT * FROM Form1 WHERE uid IN (:uIds)")
    public abstract List<Form1> getAllUnSyncedDataByUIds(List<String> uIds);

    // Use it in child table - Get data from child table (updated Query)
//    @Query("SELECT child.* FROM PARTICIPANT child LEFT JOIN Form1 parent ON child.uuid = parent.uid WHERE (child.uuid IN (:uuid)) OR (child.synced = '' AND parent.synced != '') OR child.isError IS 1")
//    public abstract List<Participant> getAllUnSyncedDataByUuIds(List<String> uuid);

    @Query("UPDATE Form1 SET iStatus = :iStatus, iStatus96x = :iStatus96x, isFormCompleteOnce = :isFormCompleteOnce, endingDate = :endingDate WHERE id = :id")
    public abstract void updateIStatus(long id, String iStatus, String iStatus96x, boolean isFormCompleteOnce, String endingDate);

    @Query("SELECT * FROM Form1 WHERE districtCode = :districtCode AND scrId = :checkId AND synced = '1'")
    public abstract boolean isFormSynced(String districtCode, String checkId);

    // This query is only used for updating sync list
    // id = rowId
    @Query("SELECT * FROM Form1 WHERE id = :id")
    public abstract Form1 getDataById(long id);

    @Query("SELECT * FROM Form1 WHERE uid = :uid")
    public abstract Form1 getDataByUid(String uid);

    @Query("SELECT * FROM Form1 WHERE districtCode = :districtCode AND scrId = :scrId")
    public abstract Form1 getDataByScrId(String districtCode, String scrId);

    @Query("SELECT * FROM Form1 WHERE sysDate LIKE :date || '%'")
    public abstract List<Form1> getAllByDate(String date);

    // Get total count by username i.e. user specific records count
    @Query("SELECT COUNT(*) FROM Form1 WHERE username = :username")
    public abstract int getTotalCountByUsername(String username);

    // Get total count
    @Query("SELECT COUNT(*) FROM Form1")
    public abstract int getTotalCount();

    @Query("DELETE FROM Form1")
    public abstract void deleteAll();

    // Update sync status as success
    public void updateSyncSuccess(List<SyncModel.WebResponse> responses) {
        if (responses != null && !responses.isEmpty() && responses.get(0).getError() == 0) {
            String syncedDate = DateUtils.getCurrentDateTime();
            String synced = "1";
            for (int i = 0; i < responses.size(); i++) {
                Form1 forms = getDataById(responses.get(i).getId());
                forms.setSyncDate(syncedDate);
                forms.setSynced(synced);
                forms.setError(false);
                update(forms);
            }
        }
    }

    // Update error status while uploading
    public void updateSyncError(List<Form1> list) {
        for (int i = 0; i < list.size(); i++) {
            Form1 obj = list.get(i);
            obj.setError(true);
            update(obj);
        }
    }

}
