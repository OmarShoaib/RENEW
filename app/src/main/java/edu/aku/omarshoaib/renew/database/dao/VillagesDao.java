package edu.aku.omarshoaib.renew.database.dao;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

import edu.aku.omarshoaib.renew.database.AppDatabase;
import edu.aku.omarshoaib.renew.model.Villages;

@Dao
public abstract class VillagesDao implements BaseDao<Villages> {

    @Query("SELECT * FROM Villages")
    public abstract List<Villages> getAllData();

    // This query is used to check if the data in the table exists
    @Query("SELECT * FROM villages Order by id DESC Limit 1")
    public abstract Villages isDataExists();

    // We are already filtering records based on user assigned district while downloading data
    // so all the villages are associated with the logged in user
    @Query("SELECT * FROM Villages")
    public abstract List<Villages> getAllVillages();

    @Query("SELECT DISTINCT tehsilName FROM Villages WHERE districtName = :districtName")
    public abstract List<String> getAllBy(String districtName);

    @Query("SELECT DISTINCT ucName FROM Villages WHERE districtName = :districtName AND tehsilName = :tehsilName")
    public abstract List<String> getAllBy(String districtName, String tehsilName);

    @Query("SELECT DISTINCT villageName FROM Villages WHERE districtName = :districtName AND tehsilName = :tehsilName AND ucName = :ucName")
    public abstract List<String> getAllBy(String districtName, String tehsilName, String ucName);

    @Query("DELETE FROM Villages")
    public abstract void deleteAll();

    @Transaction
    public void reinsert(Villages[] list) {
        AppDatabase.getDBInstance().runInTransaction(() -> {
            deleteAll();
            addAll(list);
        });
    }

}
