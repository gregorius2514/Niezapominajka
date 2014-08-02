package database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import task.entity.Task;

public class SqliteController extends SQLiteOpenHelper{

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "tasksDatabase";

    // Contacts table name
    private static final String TABLE_TASKS = "tasks";

    // Contacts Table Columns names
    private static final String KEY_ID = "taskID";
    private static final String KEY_NAME = "taskName";
    private static final String KEY_DESC = "taskDesc";
    private static final String KEY_DAY = "day";
    private static final String KEY_MONTH = "month";
    private static final String KEY_YEAR = "year";

    public SqliteController(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_TASKS_TABLE = "CREATE TABLE " + TABLE_TASKS + " ("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_NAME + " TEXT," + KEY_DESC + " TEXT,"
                + KEY_DAY + " TEXT," + KEY_MONTH + " TEXT," + KEY_YEAR + " TEXT" + " )";
        db.execSQL(CREATE_TASKS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
// Drop older table if existed
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
        dropTable(db);
        // Create tables again
        onCreate(db);
    }

    // FOR TESTING METHOD IS PUBLIC !!
    public void dropTable(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
    }

    // Adding new task
    public boolean addTask(Task task) {
        long returnValue = 0;
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
//        values.put(KEY_ID, task.getId());
        values.put(KEY_NAME, task.getTaskName());
        values.put(KEY_DESC, task.getTaskDesc());
        values.put(KEY_DAY, Integer.toString(task.getDay()));
        values.put(KEY_MONTH, Integer.toString(task.getMonth()));
        values.put(KEY_YEAR, Integer.toString(task.getYear()));

        // Inserting Row
        returnValue = db.insert(TABLE_TASKS, null, values);
        db.close(); // Closing database connection

        // -1 error on insert new row to db or
        // ID of inserted row
        if( returnValue == -1 )
            return false;
        else
            return true;
    }

    // Getting Single Task
    public Task getTask(String taskName) {
        SQLiteDatabase db = this.getReadableDatabase();


        // !! FOR TESTS
        assert db != null;
        // TODO Remove line above after tests

        Cursor cursor = db.query(TABLE_TASKS, new String[] { KEY_ID, KEY_NAME,
                        KEY_DESC, KEY_DAY, KEY_MONTH, KEY_YEAR }, KEY_NAME + "=?",
                new String[] { taskName }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Task task = new Task(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2), Integer.parseInt(cursor.getString(3)),
                Integer.parseInt(cursor.getString(4)), Integer.parseInt(cursor.getString(5)));

        // return contact
        return task;
    }

    // Getting All Tasks
    public List<Task> getAllTasks() {

        List<Task> contactList = new ArrayList<Task>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_TASKS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Task task = new Task();
                task.setId(Integer.parseInt(cursor.getString(0)));
                task.setTaskName(cursor.getString(1));
                task.setTaskDesc(cursor.getString(2));
                task.setDay(Integer.parseInt(cursor.getString(3)));
                task.setMonth(Integer.parseInt(cursor.getString(4)));
                task.setYear(Integer.parseInt(cursor.getString(5)));

                contactList.add(task);
            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }

    // Getting Tasks Count
    public int getTasksCount() {

        String countQuery = "SELECT  * FROM " + TABLE_TASKS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }
    // Updating single Task
    public int updateTask(Task task) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, task.getTaskName());
        values.put(KEY_DESC, task.getTaskDesc());
        values.put(KEY_DAY, task.getDay());
        values.put(KEY_MONTH, task.getMonth());
        values.put(KEY_YEAR, task.getYear());

        // updating row
        return db.update(TABLE_TASKS, values, KEY_ID + "=" + task.getId(), null);
    }

    // Deleting single Task
    public int deleteTask(Task task) {

        SQLiteDatabase db = this.getWritableDatabase();
        int value = db.delete(TABLE_TASKS, KEY_NAME + " = ?",
                new String[] { String.valueOf(task.getTaskName()) });
        db.close();

        return value;
    }

    public List<String> getTasksTitles() {
        List<Task> tasks = getAllTasks();
        List<String> taskTitles = new ArrayList<String>();

        for (Task task : tasks) {
            taskTitles.add(task.getTaskName());
        }

        return taskTitles;
    }

}
