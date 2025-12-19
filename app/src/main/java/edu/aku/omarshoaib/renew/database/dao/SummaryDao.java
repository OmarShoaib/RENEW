package edu.aku.omarshoaib.renew.database.dao;

import androidx.room.Dao;
import androidx.room.RawQuery;
import androidx.sqlite.db.SupportSQLiteQuery;

@Dao
public interface SummaryDao {

    /*FOR SUMMARY*/

    // Get total count by date
    @RawQuery
    int getTotalCount(SupportSQLiteQuery query);

    // Get synced count
    @RawQuery
    int getSyncedCount(SupportSQLiteQuery query);

    // Get unsynced count
    @RawQuery
    int getUnsyncedCount(SupportSQLiteQuery query);

    // Get completed count
    @RawQuery
    int getCompletedCount(SupportSQLiteQuery query);

    // Get completed other count
    @RawQuery
    int getCompletedOtherCount(SupportSQLiteQuery query);

    // Get completed count
    @RawQuery
    int getInCompleteCount(SupportSQLiteQuery query);

}
