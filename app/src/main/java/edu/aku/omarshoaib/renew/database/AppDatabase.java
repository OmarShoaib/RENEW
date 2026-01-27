package edu.aku.omarshoaib.renew.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SupportFactory;

import java.lang.reflect.Type;

import edu.aku.omarshoaib.renew.database.dao.EntryLogDao;
import edu.aku.omarshoaib.renew.database.dao.Form1Dao;
import edu.aku.omarshoaib.renew.database.dao.Form2Dao;
import edu.aku.omarshoaib.renew.database.dao.Form2aDao;
import edu.aku.omarshoaib.renew.database.dao.Form2bDao;
import edu.aku.omarshoaib.renew.database.dao.Form3Dao;
import edu.aku.omarshoaib.renew.database.dao.Form3aDao;
import edu.aku.omarshoaib.renew.database.dao.Form4Dao;
import edu.aku.omarshoaib.renew.database.dao.Form5Dao;
import edu.aku.omarshoaib.renew.database.dao.Form6Dao;
import edu.aku.omarshoaib.renew.database.dao.GeneralDao;
import edu.aku.omarshoaib.renew.database.dao.HCFDao;
import edu.aku.omarshoaib.renew.database.dao.ParticipantDao;
import edu.aku.omarshoaib.renew.database.dao.SummaryDao;
import edu.aku.omarshoaib.renew.database.dao.TeamDao;
import edu.aku.omarshoaib.renew.database.dao.UserDao;
import edu.aku.omarshoaib.renew.database.dao.VForm06Dao;
import edu.aku.omarshoaib.renew.database.dao.VForm2bDao;
import edu.aku.omarshoaib.renew.database.dao.VForm3aDao;
import edu.aku.omarshoaib.renew.database.dao.VPHQ9Dao;
import edu.aku.omarshoaib.renew.database.dao.VillagesDao;
import edu.aku.omarshoaib.renew.global.AppConstants;
import edu.aku.omarshoaib.renew.model.EntryLog;
import edu.aku.omarshoaib.renew.model.Form1;
import edu.aku.omarshoaib.renew.model.Form2;
import edu.aku.omarshoaib.renew.model.Form2a;
import edu.aku.omarshoaib.renew.model.Form2b;
import edu.aku.omarshoaib.renew.model.Form3;
import edu.aku.omarshoaib.renew.model.Form3a;
import edu.aku.omarshoaib.renew.model.Form4;
import edu.aku.omarshoaib.renew.model.Form5;
import edu.aku.omarshoaib.renew.model.Form6;
import edu.aku.omarshoaib.renew.model.HCF;
import edu.aku.omarshoaib.renew.model.Participant;
import edu.aku.omarshoaib.renew.model.SyncModel;
import edu.aku.omarshoaib.renew.model.Teams;
import edu.aku.omarshoaib.renew.model.User;
import edu.aku.omarshoaib.renew.model.VForm2b;
import edu.aku.omarshoaib.renew.model.VForm3a;
import edu.aku.omarshoaib.renew.model.VFormF06;
import edu.aku.omarshoaib.renew.model.VPHQ9;
import edu.aku.omarshoaib.renew.model.Villages;

@Database(entities = {User.class, Villages.class, EntryLog.class,
        Form1.class, Form2.class, Form2a.class, Form3.class, Form4.class, Form5.class,
        Form6.class, Participant.class, HCF.class, VPHQ9.class, Form2b.class, Form3a.class,
        VForm2b.class, VForm3a.class, Teams.class, VFormF06.class},
        version = 1, exportSchema = false)
@TypeConverters({SyncModel.ResponseDate.DataConverter.class,
        Form1.SF1.DataConverter.class, Form2.SF2.DataConverter.class, Form3.SF3.DataConverter.class,
        Form4.SF4.DataConverter.class, Form5.SF5.DataConverter.class, Form6.SF6.DataConverter.class,
        Participant.SF1.DataConverter.class, Form2a.SF2a.DataConverter.class,
        Form2b.SF2b.DataConverter.class, Form3a.SF3a.DataConverter.class})
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase appDatabase;

    private static volatile AppDatabase INSTANCE;

    // Get static instance of db
    public static AppDatabase getDBInstance() {
        return appDatabase;
    }


    // Database Initialization
    public static void initAndSecureDB(Context context) {
        SupportFactory factory = new SupportFactory(SQLiteDatabase.getBytes(AppConstants.IBAHC.toCharArray()));
        if (appDatabase == null) {
            // Room db Initialization
            Builder<AppDatabase> builder = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class, AppConstants.DATABASE_NAME);
            builder.allowMainThreadQueries();
//                builder.fallbackToDestructiveMigration();
            if (!AppConstants.IS_ADMIN)
                builder.openHelperFactory(factory);
            appDatabase = builder.build();
        }
    }

    /* DAOs */

    // This dao is used to implement the logic of multiple forms summaries
    // through single dao for the fake of generalize logic and code management
    public abstract SummaryDao summaryDao();

    // This dao is used for generalized queries in the app
    public abstract GeneralDao generalDao();

    public abstract EntryLogDao entryLogDao();

    public abstract UserDao userDao();

    public abstract VillagesDao villagesDao();

    public abstract Form1Dao form1Dao();

    public abstract ParticipantDao participantDao();

    public abstract Form2Dao form2Dao();

    public abstract Form2aDao form2aDao();

    public abstract Form2bDao form2bDao();

    public abstract Form3Dao form3Dao();

    public abstract Form3aDao form3aDao();

    public abstract Form4Dao form4Dao();

    public abstract Form5Dao form5Dao();

    public abstract Form6Dao form6Dao();

    public abstract VPHQ9Dao vphq9Dao();

    public abstract HCFDao hcfDao();

    public abstract VForm2bDao vForm2bDao();

    public abstract VForm3aDao vForm3aDao();

    public abstract VForm06Dao vFormF06Dao();

    public abstract TeamDao teamsDao();

    // Type converter used to save JsonObject in a single column
    public static class BaseConverter<T> {
        private final Gson gson;
        private final Type type;

        public BaseConverter(Type type) {
            this.type = type;
            this.gson = new GsonBuilder().serializeNulls().create();
        }

        @TypeConverter
        public String fromData(T data) {
            return gson.toJson(data, type);
        }

        @TypeConverter
        public T toData(String json) {
            return gson.fromJson(json, type);
        }
    }

    // Manual DB Migration - Sample for later Use
    /*public static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
//            database.execSQL("ALTER TABLE user "
//                    + " ADD COLUMN last_update INTEGER");
        }
    };*/

    // **Added this method to close the DB**
    public static void closeInstance() {
        if (INSTANCE != null) {
            INSTANCE.close();
            INSTANCE = null;
        }
    }
}