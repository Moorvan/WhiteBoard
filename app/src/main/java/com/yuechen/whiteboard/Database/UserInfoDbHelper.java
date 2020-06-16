package com.yuechen.whiteboard.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import com.yuechen.whiteboard.DataSource.UserInfoDataSource;

import java.util.HashMap;
import java.util.Map;

public class UserInfoDbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "UserInfo.db";

    /* Inner class that defines the table contents */
    public static class UserInfoEntry implements BaseColumns {
        public static final String TABLE_NAME = "user_info";
        public static final String COLUMN_NAME_USERNAME = "username";
        public static final String COLUMN_NAME_PASSWORD = "password";
        public static final String COLUMN_NAME_YEAR = "year";
        public static final String COLUMN_NAME_SEMESTER_INDEX = "semester_index";
    }

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + UserInfoEntry.TABLE_NAME + " (" +
                    UserInfoEntry._ID + " INTEGER PRIMARY KEY, " +
                    UserInfoEntry.COLUMN_NAME_USERNAME + " TEXT, " +
                    UserInfoEntry.COLUMN_NAME_PASSWORD + " TEXT, " +
                    UserInfoEntry.COLUMN_NAME_YEAR + " INTEGER, " +
                    UserInfoEntry.COLUMN_NAME_SEMESTER_INDEX + " INTEGER) ";

    private static final String SQL_DELETE_CONTENTS =
            "DELETE FROM " + UserInfoEntry.TABLE_NAME;

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + UserInfoEntry.TABLE_NAME;

    public UserInfoDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    /**
     * Read semester dates from database.
     *
     * @return
     */
    public Map<String, String> readUserInfo() {
        SQLiteDatabase db = getWritableDatabase();

        String[] projection = {
                UserInfoEntry.COLUMN_NAME_USERNAME,
                UserInfoEntry.COLUMN_NAME_PASSWORD,
                UserInfoEntry.COLUMN_NAME_YEAR,
                UserInfoEntry.COLUMN_NAME_SEMESTER_INDEX
        };

        Cursor cursor = db.query(
                UserInfoEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        Map<String, String> userInfo = new HashMap<>();
        while (cursor.moveToNext()) {
            String username = cursor.getString(cursor.getColumnIndexOrThrow(UserInfoEntry.COLUMN_NAME_USERNAME));
            String password = cursor.getString(cursor.getColumnIndexOrThrow(UserInfoEntry.COLUMN_NAME_PASSWORD));
            String year = cursor.getString(cursor.getColumnIndexOrThrow(UserInfoEntry.COLUMN_NAME_YEAR));
            String semesterIndex = cursor.getString(cursor.getColumnIndexOrThrow(UserInfoEntry.COLUMN_NAME_SEMESTER_INDEX));
            userInfo.put("username", username);
            userInfo.put("password", password);
            userInfo.put("year", year);
            userInfo.put("semesterIndex", semesterIndex);
            break;
        }
        cursor.close();
        return userInfo;
    }

    public long updateUserInfo() {
        clearUserInfo();
        SQLiteDatabase db = getWritableDatabase();

        long count = 0;
        ContentValues values = new ContentValues();
        values.put(UserInfoEntry.COLUMN_NAME_USERNAME, UserInfoDataSource.getUsername());
        values.put(UserInfoEntry.COLUMN_NAME_PASSWORD, UserInfoDataSource.getPassword());
        values.put(UserInfoEntry.COLUMN_NAME_YEAR, UserInfoDataSource.getYear());
        values.put(UserInfoEntry.COLUMN_NAME_SEMESTER_INDEX, UserInfoDataSource.getSemesterIndex());
        long newRowId = db.insert(UserInfoEntry.TABLE_NAME, null, values);
        if (newRowId != -1) count++;

        return count;
    }

    public void clearUserInfo() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(SQL_DELETE_CONTENTS);
    }
}
