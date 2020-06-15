package com.yuechen.whiteboard.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import com.yuechen.whiteboard.Model.Lesson;

import java.util.ArrayList;
import java.util.List;

public class LessonDbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Lesson.db";

    /* Inner class that defines the table contents */
    public static class LessonEntry implements BaseColumns {
        public static final String TABLE_NAME = "lesson";
        public static final String COLUMN_NAME_COURSE_ID = "course_id";
        public static final String COLUMN_NAME_COURSE_NAME = "course_name";
        public static final String COLUMN_NAME_LOCATION = "location";
        public static final String COLUMN_NAME_COURSE_INSTRUCTOR = "course_instructor";
        public static final String COLUMN_NAME_WEEK_OFFSET = "week_offset";
        public static final String COLUMN_NAME_DAY_OFFSET = "day_offset";
        public static final String COLUMN_NAME_START_TIME_OFFSET = "start_time_offset";
        public static final String COLUMN_NAME_END_TIME_OFFSET = "end_time_offset";
        public static final String COLUMN_NAME_START_DATE_TIME = "start_date_time";
        public static final String COLUMN_NAME_END_DATE_TIME = "end_date_time";
    }

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + LessonEntry.TABLE_NAME + " (" +
                    LessonEntry._ID + " INTEGER PRIMARY KEY," +
                    LessonEntry.COLUMN_NAME_COURSE_ID + " TEXT," +
                    LessonEntry.COLUMN_NAME_COURSE_NAME + " TEXT," +
                    LessonEntry.COLUMN_NAME_LOCATION + " TEXT," +
                    LessonEntry.COLUMN_NAME_COURSE_INSTRUCTOR + " TEXT," +
                    LessonEntry.COLUMN_NAME_WEEK_OFFSET + " INTEGER," +
                    LessonEntry.COLUMN_NAME_DAY_OFFSET + " INTEGER," +
                    LessonEntry.COLUMN_NAME_START_TIME_OFFSET + " INTEGER," +
                    LessonEntry.COLUMN_NAME_END_TIME_OFFSET + " INTEGER," +
                    LessonEntry.COLUMN_NAME_START_DATE_TIME + " DATETIME," +
                    LessonEntry.COLUMN_NAME_END_DATE_TIME + " DATETIME)";

    private static final String SQL_DELETE_CONTENTS =
            "DELETE FROM " + LessonEntry.TABLE_NAME;

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + LessonEntry.TABLE_NAME;

    public LessonDbHelper(Context context) {
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
     * Read lessons from database.
     *
     * @return
     */
    public List<Lesson> readLessons() {
        SQLiteDatabase db = getWritableDatabase();

        String[] projection = {
                LessonEntry.COLUMN_NAME_COURSE_ID,
                LessonEntry.COLUMN_NAME_COURSE_NAME,
                LessonEntry.COLUMN_NAME_LOCATION,
                LessonEntry.COLUMN_NAME_COURSE_INSTRUCTOR,
                LessonEntry.COLUMN_NAME_WEEK_OFFSET,
                LessonEntry.COLUMN_NAME_DAY_OFFSET,
                LessonEntry.COLUMN_NAME_START_TIME_OFFSET,
                LessonEntry.COLUMN_NAME_END_TIME_OFFSET,
                LessonEntry.COLUMN_NAME_START_DATE_TIME,
                LessonEntry.COLUMN_NAME_END_DATE_TIME
        };

        Cursor cursor = db.query(
                LessonEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        List<Lesson> lessons = new ArrayList<>();
        while (cursor.moveToNext()) {
            Lesson lesson = new Lesson();
            lesson.courseID = cursor.getString(cursor.getColumnIndexOrThrow(LessonEntry.COLUMN_NAME_COURSE_ID));
            lesson.courseName = cursor.getString(cursor.getColumnIndexOrThrow(LessonEntry.COLUMN_NAME_COURSE_NAME));
            lesson.location = cursor.getString(cursor.getColumnIndexOrThrow(LessonEntry.COLUMN_NAME_LOCATION));
            lesson.courseInstructor = cursor.getString(cursor.getColumnIndexOrThrow(LessonEntry.COLUMN_NAME_COURSE_INSTRUCTOR));
            lesson.weekOffset = cursor.getInt(cursor.getColumnIndexOrThrow(LessonEntry.COLUMN_NAME_WEEK_OFFSET));
            lesson.dayOffset = cursor.getInt(cursor.getColumnIndexOrThrow(LessonEntry.COLUMN_NAME_DAY_OFFSET));
            lesson.startTimeOffset = cursor.getInt(cursor.getColumnIndexOrThrow(LessonEntry.COLUMN_NAME_START_TIME_OFFSET));
            lesson.endTimeOffset = cursor.getInt(cursor.getColumnIndexOrThrow(LessonEntry.COLUMN_NAME_END_TIME_OFFSET));
            lesson.startDateTime = cursor.getString(cursor.getColumnIndexOrThrow(LessonEntry.COLUMN_NAME_START_DATE_TIME));
            lesson.endDateTime = cursor.getString(cursor.getColumnIndexOrThrow(LessonEntry.COLUMN_NAME_END_DATE_TIME));
            lessons.add(lesson);
        }
        cursor.close();
        return lessons;
    }

    public long insertLessons(List<Lesson> lessons) {
        SQLiteDatabase db = getWritableDatabase();

        long count = 0;
        for (Lesson lesson : lessons) {
            ContentValues values = new ContentValues();
            values.put(LessonEntry.COLUMN_NAME_COURSE_ID, lesson.courseID);
            values.put(LessonEntry.COLUMN_NAME_COURSE_NAME, lesson.courseName);
            values.put(LessonEntry.COLUMN_NAME_LOCATION, lesson.location);
            values.put(LessonEntry.COLUMN_NAME_COURSE_INSTRUCTOR, lesson.courseInstructor);
            values.put(LessonEntry.COLUMN_NAME_WEEK_OFFSET, lesson.weekOffset);
            values.put(LessonEntry.COLUMN_NAME_DAY_OFFSET, lesson.dayOffset);
            values.put(LessonEntry.COLUMN_NAME_START_TIME_OFFSET, lesson.startTimeOffset);
            values.put(LessonEntry.COLUMN_NAME_END_TIME_OFFSET, lesson.endTimeOffset);
            values.put(LessonEntry.COLUMN_NAME_START_DATE_TIME, lesson.startDateTime);
            values.put(LessonEntry.COLUMN_NAME_END_DATE_TIME, lesson.endDateTime);
            long newRowId = db.insert(LessonEntry.TABLE_NAME, null, values);
            if (newRowId != -1) count++;
        }
        return count;
    }

    public void clearLessons() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(SQL_DELETE_CONTENTS);
    }
}
