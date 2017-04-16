package a50.cs.twincitiesrise;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by ruiaguiar1 on 4/16/17.
 * This is the singleton for SQL methods and persistent data saving across multiple activities
 */


//Singleton for saving data between activites
//We should also store all of our SQL and firebase querying in this class.

class PersistentSaver {
    private static final PersistentSaver ourInstance = new PersistentSaver();
    private SQLiteDatabase db;
    private final String DB_NAME = "Tasksdb.db";
    private final String REMINDER_TABLE = "Tasks";


    static PersistentSaver getInstance() {
        return ourInstance;
    }

    private PersistentSaver() {
    }
}
