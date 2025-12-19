package edu.aku.omarshoaib.renew.database.dao;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

import edu.aku.omarshoaib.renew.global.DateUtils;
import edu.aku.omarshoaib.renew.model.Form2;
import edu.aku.omarshoaib.renew.model.SyncModel;

@Dao
public abstract class Form2Dao implements BaseDao<Form2> {

    @Query("SELECT * FROM Form2")
    public abstract List<Form2> getAllData();

    // This query is used to check if the data in the table exists
    @Query("SELECT * FROM Form2 Order by id DESC Limit 1")
    public abstract Form2 isDataExists();

    @Query("SELECT * FROM Form2 WHERE uid IN (:uIds)")
    public abstract List<Form2> getAllUnSyncedDataByUIds(List<String> uIds);

    // Use it in child table - Get data from child table (updated Query)
    /*@Query("SELECT child.* FROM ChildForm child LEFT JOIN ParentForm parent ON child.uuid = parent.uid WHERE (child.uuid IN (:uuid)) OR (child.synced = '' AND parent.synced != '') OR child.isError IS 1")
    public abstract List<ChildForm> getAllUnSyncedDataByUuIds(List<String> uuid);*/

    @Query("UPDATE Form2 SET iStatus = :iStatus, iStatus96x = :iStatus96x, isFormCompleteOnce = :isFormCompleteOnce, endingDate = :endingDate WHERE id = :id")
    public abstract void updateIStatus(long id, String iStatus, String iStatus96x, boolean isFormCompleteOnce, String endingDate);

    @Query("SELECT * FROM Form2 WHERE districtCode = :districtCode AND scrId = :checkId AND synced = '1'")
    public abstract boolean isFormSynced(String districtCode, String checkId);

    // This query is only used for updating sync list
    // id = rowId
    @Query("SELECT * FROM Form2 WHERE id = :id")
    public abstract Form2 getDataById(long id);

    @Query("SELECT * FROM Form2 WHERE uid = :uid")
    public abstract Form2 getDataByUid(String uid);

    @Query("SELECT * FROM Form2 WHERE districtCode = :districtCode AND scrId = :scrId")
    public abstract Form2 getDataByScrId(String districtCode, String scrId);

    @Query("SELECT * FROM Form2 WHERE sysDate LIKE :date || '%'")
    public abstract List<Form2> getAllByDate(String date);

    // Get total count by username i.e. user specific records count
    @Query("SELECT COUNT(*) FROM Form2 WHERE username = :username")
    public abstract int getTotalCountByUsername(String username);

    // Get total count
    @Query("SELECT COUNT(*) FROM Form2")
    public abstract int getTotalCount();

    @Query("DELETE FROM Form2")
    public abstract void deleteAll();

    // Update sync status as success
    public void updateSyncSuccess(List<SyncModel.WebResponse> responses) {
        if (responses != null && !responses.isEmpty() && responses.get(0).getError() == 0) {
            String syncedDate = DateUtils.getCurrentDateTime();
            String synced = "1";
            for (int i = 0; i < responses.size(); i++) {
                Form2 forms = getDataById(responses.get(i).getId());
                forms.setSyncDate(syncedDate);
                forms.setSynced(synced);
                forms.setError(false);
                update(forms);
            }
        }
    }

    // Update error status while uploading
    public void updateSyncError(List<Form2> list) {
        for (int i = 0; i < list.size(); i++) {
            Form2 obj = list.get(i);
            obj.setError(true);
            update(obj);
        }
    }

}
