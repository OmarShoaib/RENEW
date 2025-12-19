package edu.aku.omarshoaib.renew.database.dao;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

import edu.aku.omarshoaib.renew.database.AppDatabase;
import edu.aku.omarshoaib.renew.model.Cluster;

@Dao
public abstract class ClusterDao implements BaseDao<Cluster>  {

    @Query("SELECT * FROM Cluster")
    public abstract List<Cluster> getAllData();

    // This query is used to check if the data in the table exists
    @Query("SELECT * FROM cluster Order by id DESC Limit 1")
    public abstract Cluster isDataExists();

    @Query("SELECT * FROM Cluster WHERE clusterNo = :clusterNo")
    public abstract Cluster getDataByClusterNo(String clusterNo);

    @Query("DELETE FROM Cluster")
    public abstract void deleteAll();

    @Transaction
    public void reinsert(Cluster[] list) {
        AppDatabase.getDBInstance().runInTransaction(() -> {
            deleteAll();
            addAll(list);
        });
    }
}
