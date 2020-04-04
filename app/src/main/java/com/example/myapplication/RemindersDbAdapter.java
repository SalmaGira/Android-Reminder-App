//package com.mine.trial1;
package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;


public class RemindersDbAdapter {

    //these are the column names
    public static final String COL_ID = "_id";
    public static final String COL_CONTENT = "content";
    public static final String COL_IMPORTANT = "important";
    //these are the corresponding indices
    public static final int INDEX_ID = 0;
    public static final int INDEX_CONTENT = INDEX_ID + 1;
    public static final int INDEX_IMPORTANT = INDEX_ID + 2;
    //used for logging
    private static final String TAG = "RemindersDbAdapter";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;
    private static final String DATABASE_NAME = "dba_remdrs";
    private static final String TABLE_NAME = "tbl_remdrs";
    private static final int DATABASE_VERSION = 1;
    private final Context mCtx;
    //SQL statement used to create the database
    private static final String DATABASE_CREATE =
            "CREATE TABLE if not exists " + TABLE_NAME + " ( " +
                    COL_ID + " INTEGER PRIMARY KEY autoincrement, " +
                    COL_CONTENT + " TEXT, " +
                    COL_IMPORTANT + " INTEGER );";


    public RemindersDbAdapter(Context ctx) {
        this.mCtx = ctx;
    }
    //open
    public void open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
    }
    //close
    public void close() {
        if (mDbHelper != null) {
            mDbHelper.close();
        }
    }


    //TODO implement the function createReminder() which take the name as the content of the reminder and boolean important...note that the id will be created for you automatically
    public void createReminder(String name, boolean important) {
      String query;
      try {
          open();
          query = "insert into " + TABLE_NAME + "(" + COL_CONTENT + " " + COL_IMPORTANT + " )" +
                  " values(" + name + ", " + Boolean.toString(important) + ");";
          mDb.execSQL(query);
          close();
      }
      catch (Exception e){
//          Toast.makeText(MainActivity.this, "ERROR "+e.toString(), Toast.LENGTH_SHORT).show();
      }
//        Reminder reminder = new Reminder(1,name,1);

        // execSQL
    }
    //TODO overloaded to take a reminder
    public long createReminder(Reminder reminder) {
        String query;
        long isInserted = -1;
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_ID, reminder.getId());
        contentValues.put(COL_CONTENT, reminder.getContent());
        contentValues.put(COL_IMPORTANT, reminder.getImportant());
        try {
            isInserted = mDb.insert(TABLE_NAME, null, contentValues);
        }
        catch (Exception e) {
            //
        }

        return isInserted;

        // execSQL or insert
    }

    //TODO implement the function fetchReminderById() to get a certain reminder given its id
    public void fetchReminderById(int id) {
       String query;
        Cursor res;
//       Reminder reminder;
//        String from[] = { TABLE_NAME, COL_CONTENT };
//        String where = WordsDB.ID + "=?";
//        String[] whereArgs = new String[]{index+""};
//        Cursor cursor = db.query(WordsDB.TermTable, from, where, whereArgs, null, null, null, null);
//        return cursor;
        try {
            open();
//            query = "select from " + TABLE_NAME + " where `" + COL_ID + "`= " + id;
//            res = mDb.rawQuery(query, null);
            res = mDb.rawQuery("select * from " + TABLE_NAME + " where " + COL_ID + " = ?", new String[] {Integer.toString(id)});
            close();
        }
        catch (Exception e){
            //
        }

       return;

       // rawQuery with where id
    }


    //TODO implement the function fetchAllReminders() which get all reminders
    public Cursor fetchAllReminders() {
        String query;
        Cursor res;

        open();
        query = "select " + COL_CONTENT +" from " + TABLE_NAME;
        res = mDb.rawQuery(query, null);
        close();

        return res;
        // rawQuery get all
    }

    //TODO implement the function updateReminder() to update a certain reminder
    public void updateReminder(com.example.myapplication.Reminder reminder) {
        open();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_ID, reminder.getId());
        contentValues.put(COL_CONTENT, reminder.getContent());
        contentValues.put(COL_IMPORTANT, reminder.getImportant());
        mDb.update(TABLE_NAME, contentValues, COL_ID + " = ?", new String[] {Integer.toString(reminder.getId())});
        close();
        // execSQL
    }
    //TODO implement the function deleteReminderById() to delete a certain reminder given its id
    public void deleteReminderById(int nId) {
        try {
            open();
            mDb.delete(TABLE_NAME, COL_ID + " = ?", new String[] {Integer.toString(nId)});
            close();
        }
        catch (Exception e){
            //
        }
        // execSQL
    }

    //TODO implement the function deleteAllReminders() to delete all reminders
    public void deleteAllReminders() {
        try {
            open();
//            mDb.delete(TABLE_NAME, COL_ID + " = ?", new String[] {Integer.toString(nId)});
            mDb.execSQL("delete from "+ TABLE_NAME);
            close();
        }
        catch (Exception e){
            //
        }

        // execSQL
    }


    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.w(TAG, DATABASE_CREATE);
            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }


}
