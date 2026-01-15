package edu.aku.omarshoaib.renew.database.dao;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

import edu.aku.omarshoaib.renew.model.HCF;
import edu.aku.omarshoaib.renew.model.VPHQ9;

@Dao
public abstract class VPHQ9Dao implements BaseDao<VPHQ9> {

    @Query("SELECT * FROM VPHQ9")
    public abstract List<VPHQ9> getAllData();

    @Query("DELETE FROM VPHQ9")
    public abstract void deleteAll();

//    @Query("Select * from VPHQ9 Where hfCode = :hfCode")
//    public abstract VPHQ9 getVPHQ9byCode(String hfCode);

    @Transaction
    public void reinsert(VPHQ9[] list) {
        deleteAll();
        addAll(list);
    }
}
