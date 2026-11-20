package edu.aku.omarshoaib.renew.database.dao;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

import edu.aku.omarshoaib.renew.database.AppDatabase;
import edu.aku.omarshoaib.renew.model.BLRandomized;

@Dao
public abstract class BLRandomizedDao implements BaseDao<BLRandomized> {

    @Query("SELECT * FROM bl_randomised")
    public abstract List<BLRandomized> getAllData();

    @Query("SELECT * FROM bl_randomised WHERE districtId = :districtId")
    public abstract List<BLRandomized> getAllByDist(String districtId);

    // This query is used to check if the data in the table exists
    @Query("SELECT * FROM bl_randomised Order by id DESC Limit 1")
    public abstract BLRandomized isDataExists();

    @Query("SELECT * FROM bl_randomised WHERE clusterNo = :clusterNo AND tabNo = :tabNo AND hhId = :hhId")
    public abstract BLRandomized getDataByHHId(String clusterNo, String tabNo, String hhId);

    @Query("DELETE FROM bl_randomised")
    public abstract void deleteAll();

    @Transaction
    public void reinsert(BLRandomized[] list) {
        AppDatabase.getDBInstance().runInTransaction(() -> {
            deleteAll();
            addAll(list);
        });
    }

}
