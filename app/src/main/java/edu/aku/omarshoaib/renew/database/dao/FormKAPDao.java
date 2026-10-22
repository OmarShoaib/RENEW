package edu.aku.omarshoaib.renew.database.dao;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

import edu.aku.omarshoaib.renew.global.DateUtils;
import edu.aku.omarshoaib.renew.model.FormKAP;
import edu.aku.omarshoaib.renew.model.SyncModel;

@Dao
public abstract class FormKAPDao implements BaseDao<FormKAP> {

    @Query("SELECT * FROM FormKAP")
    public abstract List<FormKAP> getAllData();

    // This query is used to check if the data in the table exists
    @Query("SELECT * FROM FormKAP Order by id DESC Limit 1")
    public abstract FormKAP isDataExists();

    @Query("SELECT * FROM FormKAP WHERE uid IN (:uIds)")
    public abstract List<FormKAP> getAllUnSyncedDataByUIds(List<String> uIds);

    // Use it in child table - Get data from child table (updated Query)
//    @Query("SELECT child.* FROM PARTICIPANT child LEFT JOIN FormKAP parent ON child.uuid = parent.uid WHERE (child.uuid IN (:uuid)) OR (child.synced = '' AND parent.synced != '') OR child.isError IS 1")
//    public abstract List<Participant> getAllUnSyncedDataByUuIds(List<String> uuid);

    @Query("UPDATE FormKAP SET iStatus = :iStatus, iStatus96x = :iStatus96x, isFormCompleteOnce = :isFormCompleteOnce, endingDate = :endingDate WHERE id = :id")
    public abstract void updateIStatus(long id, String iStatus, String iStatus96x, boolean isFormCompleteOnce, String endingDate);

    @Query("SELECT EXISTS(SELECT 1 FROM FormKAP WHERE districtCode = :districtCode AND scrId = :checkId AND synced = '1')")
    public abstract boolean isFormSynced(String districtCode, String checkId);

    // This query is only used for updating sync list
    // id = rowId
    @Query("SELECT * FROM FormKAP WHERE id = :id")
    public abstract FormKAP getDataById(long id);

    @Query("SELECT * FROM FormKAP WHERE uid = :uid")
    public abstract FormKAP getDataByUid(String uid);

    @Query("SELECT * FROM FormKAP WHERE districtCode = :districtCode AND scrId = :scrId")
    public abstract FormKAP getDataByScrId(String districtCode, String scrId);

    @Query("SELECT * FROM FormKAP WHERE sysDate LIKE :date || '%'")
    public abstract List<FormKAP> getAllByDate(String date);

    // Get total count by username i.e. user specific records count
    @Query("SELECT COUNT(*) FROM FormKAP WHERE username = :username")
    public abstract int getTotalCountByUsername(String username);

    // Get total count
    @Query("SELECT COUNT(*) FROM FormKAP")
    public abstract int getTotalCount();

    @Query("DELETE FROM FormKAP")
    public abstract void deleteAll();

    // Update sync status as success
    public void updateSyncSuccess(List<SyncModel.WebResponse> responses) {
        if (responses != null && !responses.isEmpty() && responses.get(0).getError() == 0) {
            String syncedDate = DateUtils.getCurrentDateTime();
            String synced = "1";
            for (int i = 0; i < responses.size(); i++) {
                FormKAP forms = getDataById(responses.get(i).getId());
                forms.setSyncDate(syncedDate);
                forms.setSynced(synced);
                forms.setError(false);
                update(forms);
            }
        }
    }

    // Update error status while uploading
    public void updateSyncError(List<FormKAP> list) {
        for (int i = 0; i < list.size(); i++) {
            FormKAP obj = list.get(i);
            obj.setError(true);
            update(obj);
        }
    }

}
