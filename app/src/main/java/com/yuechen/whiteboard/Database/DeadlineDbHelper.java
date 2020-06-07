package com.yuechen.whiteboard.Database;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import com.yuechen.whiteboard.Model.Deadline;

import java.util.ArrayList;
import java.util.List;

public class DeadlineDbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Deadline.db";

    /* Inner class that defines the table contents */
    public static class DeadlineEntry implements BaseColumns {
        public static final String TABLE_NAME = "deadline";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_EVENT_TYPE = "event_type";
        public static final String COLUMN_NAME_CALENDAR_NAME = "calendar_name";
        public static final String COLUMN_NAME_URL = "url";
        public static final String COLUMN_NAME_CALENDAR_ID = "calendar_id";
        public static final String COLUMN_NAME_START_DATE_TIME = "start_date_time";
        public static final String COLUMN_NAME_END_DATE_TIME = "end_date_time";
    }

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + DeadlineEntry.TABLE_NAME + " (" +
                    DeadlineEntry.COLUMN_NAME_ID + " TEXT PRIMARY KEY," +
                    DeadlineEntry.COLUMN_NAME_TITLE + " TEXT," +
                    DeadlineEntry.COLUMN_NAME_EVENT_TYPE + " TEXT," +
                    DeadlineEntry.COLUMN_NAME_CALENDAR_NAME + " TEXT," +
                    DeadlineEntry.COLUMN_NAME_URL + " TEXT," +
                    DeadlineEntry.COLUMN_NAME_CALENDAR_ID + " TEXT," +
                    DeadlineEntry.COLUMN_NAME_START_DATE_TIME + " DATETIME," +
                    DeadlineEntry.COLUMN_NAME_END_DATE_TIME + " DATETIME)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + DeadlineEntry.TABLE_NAME;

    public DeadlineDbHelper(Context context) {
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
     * Read deadlines from database.
     * @return
     */
    public List<Deadline> readDeadlines() {
        SQLiteDatabase db = getWritableDatabase();

        String[] projection = {
                DeadlineEntry.COLUMN_NAME_TITLE,
                DeadlineEntry.COLUMN_NAME_ID,
                DeadlineEntry.COLUMN_NAME_CALENDAR_ID,
                DeadlineEntry.COLUMN_NAME_CALENDAR_NAME,
                DeadlineEntry.COLUMN_NAME_EVENT_TYPE,
                DeadlineEntry.COLUMN_NAME_URL,
                DeadlineEntry.COLUMN_NAME_START_DATE_TIME,
                DeadlineEntry.COLUMN_NAME_END_DATE_TIME
        };

        Cursor cursor = db.query(
                DeadlineEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        List<Deadline> deadlines = new ArrayList<>();
        while (cursor.moveToNext()) {
            Deadline deadline = new Deadline();
            deadline.title = cursor.getString(cursor.getColumnIndexOrThrow(DeadlineEntry.COLUMN_NAME_TITLE));
            deadline.id = cursor.getString(cursor.getColumnIndexOrThrow(DeadlineEntry.COLUMN_NAME_ID));
            deadline.calendarID = cursor.getString(cursor.getColumnIndexOrThrow(DeadlineEntry.COLUMN_NAME_CALENDAR_ID));
            deadline.calendarName = cursor.getString(cursor.getColumnIndexOrThrow(DeadlineEntry.COLUMN_NAME_CALENDAR_NAME));
            deadline.eventType = cursor.getString(cursor.getColumnIndexOrThrow(DeadlineEntry.COLUMN_NAME_EVENT_TYPE));
            deadline.url = cursor.getString(cursor.getColumnIndexOrThrow(DeadlineEntry.COLUMN_NAME_URL));
            deadline.startDateTime = cursor.getString(cursor.getColumnIndexOrThrow(DeadlineEntry.COLUMN_NAME_START_DATE_TIME));
            deadline.endDateTime = cursor.getString(cursor.getColumnIndexOrThrow(DeadlineEntry.COLUMN_NAME_END_DATE_TIME));
            deadlines.add(deadline);
        }
        cursor.close();
        return deadlines;
    }


    /**
     * Insert specific deadline into database.
     * @param deadline
     */
    public long insertDeadline(Deadline deadline) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DeadlineEntry.COLUMN_NAME_TITLE, deadline.title);
        values.put(DeadlineEntry.COLUMN_NAME_ID, deadline.id);
        values.put(DeadlineEntry.COLUMN_NAME_CALENDAR_ID, deadline.calendarID);
        values.put(DeadlineEntry.COLUMN_NAME_CALENDAR_NAME, deadline.calendarName);
        values.put(DeadlineEntry.COLUMN_NAME_EVENT_TYPE, deadline.eventType);
        values.put(DeadlineEntry.COLUMN_NAME_URL, deadline.url);
        values.put(DeadlineEntry.COLUMN_NAME_START_DATE_TIME, deadline.startDateTime);
        values.put(DeadlineEntry.COLUMN_NAME_END_DATE_TIME, deadline.endDateTime);

        long newRowId = db.insert(DeadlineEntry.TABLE_NAME, null, values);
        return newRowId;
    }
}
