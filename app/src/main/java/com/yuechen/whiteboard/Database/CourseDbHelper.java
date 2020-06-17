package com.yuechen.whiteboard.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import com.yuechen.whiteboard.Model.Course;

import java.util.ArrayList;
import java.util.List;

public class CourseDbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Course.db";

    /* Inner class that defines the table contents */
    public static class CourseEntry implements BaseColumns {
        public static final String TABLE_NAME = "course";
        public static final String COLUMN_NAME_COURSE_ID = "course_id";
        public static final String COLUMN_NAME_COURSE_NAME = "course_name";
        public static final String COLUMN_NAME_COURSE_INSTRUCTOR = "course_instructor";
    }

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + CourseDbHelper.CourseEntry.TABLE_NAME + " (" +
                    CourseDbHelper.CourseEntry.COLUMN_NAME_COURSE_ID + " TEXT PRIMARY KEY," +
                    CourseDbHelper.CourseEntry.COLUMN_NAME_COURSE_NAME + " TEXT," +
                    CourseDbHelper.CourseEntry.COLUMN_NAME_COURSE_INSTRUCTOR + " TEXT)";

    private static final String SQL_DELETE_CONTENTS =
            "DELETE FROM " + CourseDbHelper.CourseEntry.TABLE_NAME;

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + CourseDbHelper.CourseEntry.TABLE_NAME;

    public CourseDbHelper(Context context) {
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
     * Read courses from database.
     *
     * @return
     */
    public List<Course> readCourses() {
        SQLiteDatabase db = getWritableDatabase();

        String[] projection = {
                CourseDbHelper.CourseEntry.COLUMN_NAME_COURSE_ID,
                CourseDbHelper.CourseEntry.COLUMN_NAME_COURSE_NAME,
                CourseDbHelper.CourseEntry.COLUMN_NAME_COURSE_INSTRUCTOR
        };

        Cursor cursor = db.query(
                CourseDbHelper.CourseEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        List<Course> courses = new ArrayList<>();
        while (cursor.moveToNext()) {
            Course course = new Course();
            course.courseID = cursor.getString(cursor.getColumnIndexOrThrow(CourseDbHelper.CourseEntry.COLUMN_NAME_COURSE_ID));
            course.courseName = cursor.getString(cursor.getColumnIndexOrThrow(CourseDbHelper.CourseEntry.COLUMN_NAME_COURSE_NAME));
            course.courseInstructor = cursor.getString(cursor.getColumnIndexOrThrow(CourseDbHelper.CourseEntry.COLUMN_NAME_COURSE_INSTRUCTOR));
            courses.add(course);
        }
        cursor.close();
        return courses;
    }

    public long insertCourses(List<Course> courses) {
        SQLiteDatabase db = getWritableDatabase();

        long count = 0;
        for (Course course : courses) {
            ContentValues values = new ContentValues();
            values.put(CourseDbHelper.CourseEntry.COLUMN_NAME_COURSE_ID, course.courseID);
            values.put(CourseDbHelper.CourseEntry.COLUMN_NAME_COURSE_NAME, course.courseName);
            values.put(CourseDbHelper.CourseEntry.COLUMN_NAME_COURSE_INSTRUCTOR, course.courseInstructor);
            long newRowId = db.insert(CourseDbHelper.CourseEntry.TABLE_NAME, null, values);
            if (newRowId != -1) count++;
        }
        return count;
    }

    public void clearCourses() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(SQL_DELETE_CONTENTS);
    }
}
