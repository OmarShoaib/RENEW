package edu.aku.omarshoaib.renew.database.dao;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

import edu.aku.omarshoaib.renew.model.VForm3a;
import edu.aku.omarshoaib.renew.model.VFormF06;

@Dao
public abstract class VForm06Dao implements BaseDao<VFormF06> {

    @Query("SELECT * FROM vw_Form6")
    public abstract List<VFormF06> getAllData();

    @Query("DELETE FROM vw_Form6")
    public abstract void deleteAll();

    @Transaction
    public void reinsert(VFormF06[] list) {
        deleteAll();
        addAll(list);
    }
}