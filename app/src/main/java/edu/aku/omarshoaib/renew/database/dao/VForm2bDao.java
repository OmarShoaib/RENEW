package edu.aku.omarshoaib.renew.database.dao;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

import edu.aku.omarshoaib.renew.model.VForm2b;

@Dao
public abstract class VForm2bDao implements BaseDao<VForm2b> {

    @Query("SELECT * FROM VForm2b")
    public abstract List<VForm2b> getAllData();

    @Query("DELETE FROM VForm2b")
    public abstract void deleteAll();

    @Transaction
    public void reinsert(VForm2b[] list) {
        deleteAll();
        addAll(list);
    }
}