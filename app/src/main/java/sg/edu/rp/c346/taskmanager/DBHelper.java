package sg.edu.rp.c346.taskmanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "tasks.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NOTE = "task";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TASK_NAME = "taskName";
    private static final String COLUMN_DESCRIPTION = "description";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //TODO CREATE TABLE Note
        String createTableSql = "CREATE TABLE " + TABLE_NOTE
                + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_TASK_NAME + " TEXT,"
                + COLUMN_DESCRIPTION + " TEXT )";
        db.execSQL(createTableSql);
        Log.i("info", "Created Table");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTE);
        onCreate(db);
    }

    public long insertTask(String taskName,String description){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TASK_NAME, taskName);
        values.put(COLUMN_DESCRIPTION, description);
        long result = db.insert(TABLE_NOTE, null, values);
        if (result == -1) {
            Log.d("DBHelper", "Insert failed");
        }
        db.close();
        Log.d("SQL Insert", "ID:" + result);
        return result;
    }

    public ArrayList<Task> getAllTasks() {
        //TODO return records in Java objects
        ArrayList<Task> tasks = new ArrayList<Task>();
        String selectQuery = "SELECT " + COLUMN_ID + ","
                + COLUMN_TASK_NAME + ", "
                + COLUMN_DESCRIPTION
                + " FROM " + TABLE_NOTE;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String taskName = cursor.getString(1);
                String description = cursor.getString(2);
                Task task = new Task(id,taskName, description);
                tasks.add(task);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return tasks;
    }
}
