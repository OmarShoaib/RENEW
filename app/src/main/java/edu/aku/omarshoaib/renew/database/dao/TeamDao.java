package edu.aku.omarshoaib.renew.database.dao;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

import edu.aku.omarshoaib.renew.model.Teams;

@Dao
public abstract class TeamDao implements BaseDao<Teams> {

    @Query("SELECT * FROM Teams")
    public abstract List<Teams> getAllData();

    @Query("DELETE FROM Teams")
    public abstract void deleteAll();

//    @Query("Select * from Teams Where hfCode = :hfCode")
//    public abstract Teams getTeamsbyCode(String hfCode);

    @Transaction
    public void reinsert(Teams[] list) {
        deleteAll();
        addAll(list);
    }
}