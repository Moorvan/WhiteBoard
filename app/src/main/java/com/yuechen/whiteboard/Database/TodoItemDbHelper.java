package com.yuechen.whiteboard.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import com.yuechen.whiteboard.Model.TodoItem;
import com.yuechen.whiteboard.Model.TodoItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TodoItemDbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "TodoItem.db";

    /* Inner class that defines the table contents */
    public static class TodoItemEntry implements BaseColumns {
        public static final String TABLE_NAME = "todo_item";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_FOLDER_ID = "folder_id";
        public static final String COLUMN_NAME_END_DATE_TIME = "end_date_time";
        public static final String COLUMN_NAME_FINISHED = "finished";
        public static final String COLUMN_NAME_NOTE = "note";
    }

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TodoItemEntry.TABLE_NAME + " (" +
                    TodoItemEntry._ID + " INTEGER PRIMARY KEY," +
                    TodoItemEntry.COLUMN_NAME_TITLE + " TEXT," +
                    TodoItemEntry.COLUMN_NAME_FOLDER_ID + " INTEGER," +
                    TodoItemEntry.COLUMN_NAME_END_DATE_TIME + " DATETIME," +
                    TodoItemEntry.COLUMN_NAME_NOTE + " TEXT," +
                    TodoItemEntry.COLUMN_NAME_FINISHED + " BOOLEAN," +
                    " FOREIGN KEY (" + TodoItemEntry.COLUMN_NAME_FOLDER_ID + ")" +
                    " REFERENCES " + FolderDbHelper.FolderEntry.TABLE_NAME + "(" + FolderDbHelper.FolderEntry.COLUMN_NAME_FOLDER_ID + "))";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TodoItemEntry.TABLE_NAME;

    public TodoItemDbHelper(Context context) {
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
     * Read todoItems from database.
     *
     * @return
     */
    public List<TodoItem> readTodoItems() {
        SQLiteDatabase db = getWritableDatabase();

        String[] projection = {
                TodoItemEntry._ID,
                TodoItemEntry.COLUMN_NAME_TITLE,
                TodoItemEntry.COLUMN_NAME_FOLDER_ID,
                TodoItemEntry.COLUMN_NAME_END_DATE_TIME,
                TodoItemEntry.COLUMN_NAME_NOTE,
                TodoItemEntry.COLUMN_NAME_FINISHED
        };

        Cursor cursor = db.query(
                TodoItemEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        List<TodoItem> todoItems = new ArrayList<>();
        while (cursor.moveToNext()) {
            TodoItem todoItem = new TodoItem();
            todoItem._id = cursor.getLong(cursor.getColumnIndexOrThrow(TodoItemEntry._ID));
            todoItem.title = cursor.getString(cursor.getColumnIndexOrThrow(TodoItemEntry.COLUMN_NAME_TITLE));
            todoItem.folderID = cursor.getLong(cursor.getColumnIndexOrThrow(TodoItemEntry.COLUMN_NAME_FOLDER_ID));
            todoItem.endDateTime = cursor.getString(cursor.getColumnIndexOrThrow(TodoItemEntry.COLUMN_NAME_END_DATE_TIME));
            todoItem.note = cursor.getString(cursor.getColumnIndexOrThrow(TodoItemEntry.COLUMN_NAME_NOTE));
            todoItem.finished = cursor.getInt(cursor.getColumnIndexOrThrow(TodoItemEntry.COLUMN_NAME_FINISHED)) > 0;

            todoItems.add(todoItem);
        }
        cursor.close();
        return todoItems;
    }


    /**
     * Insert specific todoItem into database.
     *
     * @param todoItem
     */
    public long insertTodoItem(TodoItem todoItem) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TodoItemEntry.COLUMN_NAME_TITLE, todoItem.title);
        values.put(TodoItemEntry.COLUMN_NAME_FOLDER_ID, todoItem.folderID);
        values.put(TodoItemEntry.COLUMN_NAME_END_DATE_TIME, todoItem.endDateTime);
        values.put(TodoItemEntry.COLUMN_NAME_NOTE, todoItem.note);
        values.put(TodoItemEntry.COLUMN_NAME_FINISHED, todoItem.finished);

        long newRowId = db.insert(TodoItemEntry.TABLE_NAME, null, values);
        todoItem._id = newRowId;
        return newRowId;
    }

    public long updateTodoItem(TodoItem todoItem) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TodoItemEntry.COLUMN_NAME_TITLE, todoItem.title);
        values.put(TodoItemEntry.COLUMN_NAME_FOLDER_ID, todoItem.folderID);
        values.put(TodoItemEntry.COLUMN_NAME_END_DATE_TIME, todoItem.endDateTime);
        values.put(TodoItemEntry.COLUMN_NAME_NOTE, todoItem.note);
        values.put(TodoItemEntry.COLUMN_NAME_FINISHED, todoItem.finished);

        // Which row to update, based on the id
        String selection = TodoItemEntry._ID + " = ? ";
        String[] selectionArgs = {String.valueOf(todoItem._id)};

        long count = db.update(TodoItemEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs
        );

        return count;
    }

    public long removeTodoItem(TodoItem todoItem) {
        SQLiteDatabase db = getWritableDatabase();

        // Which row to update, based on the id
        String selection = TodoItemEntry._ID + " = ? ";
        String[] selectionArgs = {String.valueOf(todoItem._id)};

        long count = db.delete(TodoItemEntry.TABLE_NAME,
                selection,
                selectionArgs);

        return count;
    }
}
