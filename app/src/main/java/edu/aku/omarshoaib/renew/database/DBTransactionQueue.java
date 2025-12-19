package edu.aku.omarshoaib.renew.database;

import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.LinkedList;
import java.util.Queue;

/* For queueing db transactions on inserting multiple tables.
 * Currently this is used for Synced Recs */

public class DBTransactionQueue {
    private final SupportSQLiteDatabase db;
    private final Queue<Runnable> transactionQueue;
    private final ITransactionQueueCallback callback;

    public DBTransactionQueue(SupportSQLiteDatabase db, ITransactionQueueCallback callback) {
        this.db = db;
        this.callback = callback;
        this.transactionQueue = new LinkedList<>();
    }

    public void addTransaction(String query, int index, int length) {
        // index = syncTablesList index
        // length = downloaded records count
        transactionQueue.add(() -> {
            // Execute your database operation
            db.execSQL(query);
            // Update sync list item
            if (callback != null)
                callback.onTransactionEnd(index, length);
        });
    }

    public void executeTransactions() {
        db.beginTransaction();
        try {
            while (!transactionQueue.isEmpty()) {
                Runnable transaction = transactionQueue.poll();
                if (transaction != null) {
                    transaction.run();
                }
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    // Get teh status of the queue to process further
    public boolean isDBTransactionQueueEmpty() {
        return transactionQueue.isEmpty();
    }

    // For updating sync list items after a particular transaction ends
    public interface ITransactionQueueCallback {
        // index = syncTablesList index
        // length = downloaded records count
        void onTransactionEnd(int index, int length);
    }
}
