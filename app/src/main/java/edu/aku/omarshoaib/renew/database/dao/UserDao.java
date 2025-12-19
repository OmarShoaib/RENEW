package edu.aku.omarshoaib.renew.database.dao;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

import edu.aku.omarshoaib.renew.database.AppDatabase;
import edu.aku.omarshoaib.renew.global.MainApp;
import edu.aku.omarshoaib.renew.global.UserAuth;
import edu.aku.omarshoaib.renew.model.User;

@Dao
public abstract class UserDao implements BaseDao<User> {

    @Query("SELECT * FROM User")
    public abstract List<User> getAllData();

    // This query is used to check if the data in the table exists
    @Query("SELECT * FROM user Order by userId DESC Limit 1")
    public abstract User isDataExists();

    @Query("SELECT * FROM User WHERE username = :username")
    public abstract User getUserByUsername(String username);

    @Query("DELETE FROM User")
    public abstract void deleteAll();

    @Transaction
    public void reinsert(User[] list) {
        deleteAll();
        addAll(list);
    }

    // User login
    public boolean doLogin(String username, String password) {
        User user = AppDatabase.getDBInstance().userDao().getUserByUsername(username);
        if (user != null) {
            if (UserAuth.checkPassword(password, user.getPasswordEnc())) {
                MainApp.user = user;
                return true;
            }
        }
        return false;
    }
}
