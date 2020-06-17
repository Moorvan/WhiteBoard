package com.yuechen.whiteboard.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import com.yuechen.whiteboard.Model.Folder;

import java.util.ArrayList;
import java.util.List;

public class FolderDbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Folder.db";

    /* Inner class that defines the table contents */
    public static class FolderEntry implements BaseColumns {
        public static final String TABLE_NAME = "folder";
        public static final String COLUMN_NAME_FOLDER_ID = "folder_id";
        public static final String COLUMN_NAME_FOLDER_NAME = "folder_name";
    }

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + FolderEntry.TABLE_NAME + " (" +
                    FolderEntry.COLUMN_NAME_FOLDER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    FolderEntry.COLUMN_NAME_FOLDER_NAME + " TEXT)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + FolderEntry.TABLE_NAME;

    public FolderDbHelper(Context context) {
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
     * Read folders from database.
     *
     * @return
     */
    public List<Folder> readFolders() {
        SQLiteDatabase db = getWritableDatabase();

        String[] projection = {
                FolderEntry.COLUMN_NAME_FOLDER_NAME,
                FolderEntry.COLUMN_NAME_FOLDER_ID
        };

        Cursor cursor = db.query(
                FolderEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        List<Folder> folders = new ArrayList<>();
        while (cursor.moveToNext()) {
            Folder folder = new Folder();
            folder.setName(cursor.getString(cursor.getColumnIndexOrThrow(FolderEntry.COLUMN_NAME_FOLDER_NAME)));
            folder.setFolderId(cursor.getInt(cursor.getColumnIndexOrThrow(FolderEntry.COLUMN_NAME_FOLDER_ID)));
            folders.add(folder);
        }
        cursor.close();
        return folders;
    }


    /**
     * Insert specific folder into database.
     *
     * @param folder
     */
    public long insertFolder(Folder folder) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FolderEntry.COLUMN_NAME_FOLDER_NAME, folder.getName());

        long newRowId = db.insert(FolderEntry.TABLE_NAME, null, values);
        folder.setFolderId(newRowId);
        return newRowId;
    }

    public long updateFolder(Folder folder) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FolderEntry.COLUMN_NAME_FOLDER_NAME, folder.getName());

        // Which row to update, based on the id
        String selection = FolderEntry.COLUMN_NAME_FOLDER_ID + " = ? ";
        String[] selectionArgs = {String.valueOf(folder.getFolderId())};

        long count = db.update(FolderEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs
        );

        return count;
    }

    public long removeFolder(Folder folder) {
        SQLiteDatabase db = getWritableDatabase();

        // Which row to update, based on the id
        String selection = FolderEntry.COLUMN_NAME_FOLDER_ID + " = ? ";
        String[] selectionArgs = {String.valueOf(folder.getFolderId())};

        long count = db.delete(FolderEntry.TABLE_NAME,
                selection,
                selectionArgs);

        return count;
    }
}
