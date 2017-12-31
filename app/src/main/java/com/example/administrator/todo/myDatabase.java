package com.example.administrator.todo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Administrator on 2017/12/31.
 */

public class myDatabase extends SQLiteOpenHelper{
    private static final String DB_NAME = "Tasks.db";
    private static final String TABLE_NAME = "Tasks";
    private static final int DB_VERSION = 1;
    private static final String SQL_CREATE_TABLE = "create table "+ TABLE_NAME
            + "(_id integer primary key autoincrement,"
            + " title text,"
            + "details text,"
            + "deadline integer,"
            + "remind_time integer,"
            + "completed integer);";
    public myDatabase(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // no action
    }
    public void insert(String title, String details, Date deadline, Date remind_time, boolean isCompleted) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("details", details);
        values.put("deadline", deadline.getTime());
        if (remind_time != null) {
            values.put("remind_time", remind_time.getTime());
        } else {
            values.put("remind_time", -1);
        }
        values.put("completed", isCompleted ? 1 : 0);
        db.insert(TABLE_NAME, null, values);
        db.close();
    }
    public void update(int id, String title, String details, Date deadline, Date remind_time, boolean isCompleted) {
        SQLiteDatabase db = getWritableDatabase();
        String whereClause = "_id = ?";
        String[] whereArgs = {String.valueOf(id)};
        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("details", details);
        values.put("deadline", deadline.getTime());
        if (remind_time != null) {
            values.put("remind_time", remind_time.getTime());
        } else {
            values.put("remind_time", -1);
        }
        values.put("completed", isCompleted ? 1 : 0);
        db.update(TABLE_NAME, values, whereClause, whereArgs);
        db.close();
    }
    public void delete(int id) {
        SQLiteDatabase db = getWritableDatabase();
        String whereClause = "_id = ?";
        String[] whereArgs = {String.valueOf(id)};
        db.delete(TABLE_NAME, whereClause, whereArgs);
        db.close();
    }
    public TaskInstance queryByDate(Date date) {
        SQLiteDatabase db = getReadableDatabase();
        String[] columns = {"_id", "title", "details", "deadline", "remind_time", "completed"};
        String selection = "deadline = ?";
        String[] selectionArgs = {String.valueOf(date.getTime())};
        Cursor cursor = db.query(TABLE_NAME, columns, selection, selectionArgs, null, null, null);
        if (!cursor.moveToFirst()) {
            db.close();
            cursor.close();
            return null;
        } else {
            Date ddl, remind;
            Long time1 = cursor.getLong(3);
            ddl = new Date();
            ddl.setTime(time1);
            Long time2 = cursor.getLong(4);
            if (time2 == -1) {
                remind = null;
            } else {
                remind = new Date();
                remind.setTime(time2);
            }
            TaskInstance task = new TaskInstance(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    ddl,
                    remind,
                    cursor.getInt(5) == 1
                    );
            db.close();
            cursor.close();
            return task;
        }
    }
    public ArrayList<TaskInstance> getAllTasks() {
        ArrayList<TaskInstance> ret = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String[] columns = {"_id", "title", "details", "deadline", "remind_time", "completed"};
        Cursor cursor = db.query(TABLE_NAME, columns, null, null, null, null, null);
        if (!cursor.moveToFirst()) {
            return null;
        } else {
            do {
                Date ddl, remind;
                Long time1 = cursor.getLong(3);
                ddl = new Date();
                ddl.setTime(time1);
                Long time2 = cursor.getLong(4);
                if (time2 == -1) {
                    remind = null;
                } else {
                    remind = new Date();
                    remind.setTime(time2);
                }
                TaskInstance task = new TaskInstance(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        ddl,
                        remind,
                        cursor.getInt(5) == 1
                );
                ret.add(task);
            } while (cursor.moveToNext());
        }
        db.close();
        cursor.close();
        return ret;
    }
}
