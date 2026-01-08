package edu.aku.omarshoaib.renew.database.dao;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

import edu.aku.omarshoaib.renew.database.AppDatabase;
import edu.aku.omarshoaib.renew.global.MainApp;
import edu.aku.omarshoaib.renew.global.UserAuth;
import edu.aku.omarshoaib.renew.model.HCF;
import edu.aku.omarshoaib.renew.model.User;

@Dao
public abstract class HCFDao implements BaseDao<HCF>{

    @Query("SELECT * FROM HCF")
    public abstract List<User> getAllData();

    @Query("DELETE FROM HCF")
    public abstract void deleteAll();

    @Transaction
    public void reinsert(HCF[] list) {
        deleteAll();
        addAll(list);
    }
}