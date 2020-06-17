package com.yuechen.whiteboard.DataSource;

import android.content.Context;

import com.yuechen.whiteboard.Database.FolderDbHelper;
import com.yuechen.whiteboard.Model.Folder;

import java.util.ArrayList;
import java.util.List;

public class FolderDataSource {
    public static List<Folder> folders = new ArrayList<>();

    public static void readfolders(Context context) {
        FolderDbHelper dbHelper = new FolderDbHelper(context);
        folders.clear();
        for (Folder folder : dbHelper.readFolders()) {
            folders.add(folder);
        }
    }

    public static long insertFolder(Context context, Folder folder) {
        folders.add(folder);
        FolderDbHelper dbHelper = new FolderDbHelper(context);
        return dbHelper.insertFolder(folder);
    }

    public static long updateFolder(Context context, long folderId, String name) {
        for (Folder folder : folders) {
            if (folder.getFolderId() == folderId) {
                folder.setName(name);
                FolderDbHelper dbHelper = new FolderDbHelper(context);
                return dbHelper.updateFolder(folder);
            }
        }

        return -1;
    }

    public static void deleteFolder(Context context, Folder folder) {
        folders.remove(folder);
        FolderDbHelper dbHelper = new FolderDbHelper(context);
        dbHelper.removeFolder(folder);
    }
}
