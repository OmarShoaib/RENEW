package edu.aku.omarshoaib.renew.database.dao;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;
import edu.aku.omarshoaib.renew.model.HCF;

@Dao
public abstract class HCFDao implements BaseDao<HCF>{

    @Query("SELECT * FROM HCF")
    public abstract List<HCF> getAllData();

    @Query("DELETE FROM HCF")
    public abstract void deleteAll();

    @Query("Select * from HCF Where hfCode like :hfCode")
    public abstract HCF getHcfbyCode(String hfCode);

    @Transaction
    public void reinsert(HCF[] list) {
        deleteAll();
        addAll(list);
    }
}