package edu.aku.omarshoaib.renew.database.dao;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

import edu.aku.omarshoaib.renew.model.VForm2b;
import edu.aku.omarshoaib.renew.model.VForm3a;

@Dao
public abstract class VForm3aDao implements BaseDao<VForm3a> {

    @Query("SELECT * FROM VForm3a")
    public abstract List<VForm3a> getAllData();

    @Query("DELETE FROM VForm3a")
    public abstract void deleteAll();

    @Transaction
    public void reinsert(VForm3a[] list) {
        deleteAll();
        addAll(list);
    }
}
