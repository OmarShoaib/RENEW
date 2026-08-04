package edu.aku.omarshoaib.renew.database.dao;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

import edu.aku.omarshoaib.renew.model.VFormF05A;

@Dao
public abstract class VFormF05ADao implements BaseDao<VFormF05A>{

    @Query("SELECT * FROM vw_form5a")
    public abstract List<VFormF05A> getAllData();

    @Query("DELETE FROM vw_form5a")
    public abstract void deleteAll();

    @Transaction
    public void reinsert(VFormF05A[] list) {
        deleteAll();
        addAll(list);
    }
}