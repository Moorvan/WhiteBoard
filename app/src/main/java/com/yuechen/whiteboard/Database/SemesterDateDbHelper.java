package com.yuechen.whiteboard.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.util.HashMap;
import java.util.Map;

public class SemesterDateDbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "SemesterDate.db";

    /* Inner class that defines the table contents */
    public static class SemesterDateEntry implements BaseColumns {
        public static final String TABLE_NAME = "semester_date";
        public static final String COLUMN_NAME_YEAR = "year";
        public static final String COLUMN_NAME_SEMESTER_INDEX = "semester_index";
        public static final String COLUMN_NAME_BEGIN_DATE = "begin_date";
    }

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + SemesterDateEntry.TABLE_NAME + " (" +
                    SemesterDateEntry.COLUMN_NAME_YEAR + " INTEGER, " +
                    SemesterDateEntry.COLUMN_NAME_SEMESTER_INDEX + " INTEGER, " +
                    SemesterDateEntry.COLUMN_NAME_BEGIN_DATE + " DATE, " +
                    "PRIMARY KEY(" + SemesterDateEntry.COLUMN_NAME_YEAR + ", " +
                    SemesterDateEntry.COLUMN_NAME_SEMESTER_INDEX + "))";

    private static final String SQL_DELETE_CONTENTS =
            "DELETE FROM " + SemesterDateEntry.TABLE_NAME;

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + SemesterDateEntry.TABLE_NAME;

    public SemesterDateDbHelper(Context context) {
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
    public Map<String, Map<String, String>> readSemesterDates() {
        SQLiteDatabase db = getWritableDatabase();

        String[] projection = {
                SemesterDateEntry.COLUMN_NAME_YEAR,
                SemesterDateEntry.COLUMN_NAME_SEMESTER_INDEX,
                SemesterDateEntry.COLUMN_NAME_BEGIN_DATE
        };

        Cursor cursor = db.query(
                SemesterDateEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        Map<String, Map<String, String>> semestereDates = new HashMap<>();
        while (cursor.moveToNext()) {
            String year = cursor.getString(cursor.getColumnIndexOrThrow(SemesterDateEntry.COLUMN_NAME_YEAR));
            String index = cursor.getString(cursor.getColumnIndexOrThrow(SemesterDateEntry.COLUMN_NAME_SEMESTER_INDEX));
            String date = cursor.getString(cursor.getColumnIndexOrThrow(SemesterDateEntry.COLUMN_NAME_BEGIN_DATE));
            if (!semestereDates.containsKey(year)) {
                semestereDates.put(year, new HashMap<>());
            }
            semestereDates.get(year).put(index, date);
        }
        cursor.close();
        return semestereDates;
    }

    public long insertSemesterDates(Map<String, Map<String, String>> semestereDates) {
        SQLiteDatabase db = getWritableDatabase();

        long count = 0;
        for (Map.Entry<String, Map<String, String>> year : semestereDates.entrySet()) {
            for (Map.Entry<String, String> semester : year.getValue().entrySet()) {
                ContentValues values = new ContentValues();
                values.put(SemesterDateEntry.COLUMN_NAME_YEAR, year.getKey());
                values.put(SemesterDateEntry.COLUMN_NAME_SEMESTER_INDEX, semester.getKey());
                values.put(SemesterDateEntry.COLUMN_NAME_BEGIN_DATE, semester.getValue());
                long newRowId = db.insert(SemesterDateEntry.TABLE_NAME, null, values);
                if (newRowId != -1) count++;
            }
        }
        return count;
    }

    public void clearSemesterDates() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(SQL_DELETE_CONTENTS);
    }
}
