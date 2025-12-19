package edu.aku.omarshoaib.renew.database.dao;

import androidx.room.Dao;
import androidx.room.RawQuery;
import androidx.sqlite.db.SupportSQLiteQuery;

import java.util.List;

@Dao
public interface GeneralDao {

    // To hit room db checkpoint manually
    @RawQuery
    int checkpoint(SupportSQLiteQuery supportSQLiteQuery);

    @RawQuery
    List<String> getUnsyncedDataUIds(SupportSQLiteQuery query);

    @RawQuery
    int rawQuery(SupportSQLiteQuery query);

    @RawQuery
    List<String> getAllUIds(SupportSQLiteQuery query);


}
