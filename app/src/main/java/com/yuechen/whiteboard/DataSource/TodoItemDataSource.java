package com.yuechen.whiteboard.DataSource;

import android.content.Context;

import com.yuechen.whiteboard.Database.TodoItemDbHelper;
import com.yuechen.whiteboard.Model.TodoItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TodoItemDataSource {
    public static List<TodoItem> todoItems = new ArrayList<>();
    public static Map<Long, List<TodoItem>> todoItemsMap = new HashMap<>();

    public static void readTodoItems(Context context) {
        TodoItemDbHelper dbHelper = new TodoItemDbHelper(context);
        todoItemsMap.clear();
        todoItemsMap = dbHelper.readTodoItems();
    }

    public static long insertTodoItem(Context context, TodoItem todoItem) {
        TodoItemDbHelper dbHelper = new TodoItemDbHelper(context);
        return dbHelper.insertTodoItem(todoItem);
    }

    public static long updateDeadline(Context context, long id, boolean finished) {
        for (TodoItem todoItem : todoItems) {
            if (todoItem._id == id) {
                todoItem.finished = finished;

                TodoItemDbHelper dbHelper = new TodoItemDbHelper(context);
                return dbHelper.updateTodoItem(todoItem);
            }
        }

        return -1;
    }

    public static long updateDeadline(Context context, long id, String title, long folderID, String endDateTime, String note, boolean finished) {
        for (TodoItem todoItem : todoItems) {
            if (todoItem._id == id) {
                todoItem.finished = finished;
                todoItem.title = title;
                todoItem.folderID = folderID;
                todoItem.endDateTime = endDateTime;
                todoItem.note = note;

                TodoItemDbHelper dbHelper = new TodoItemDbHelper(context);
                return dbHelper.updateTodoItem(todoItem);
            }
        }

        return -1;
    }

    public static void deleteTodoItem(Context context, TodoItem todoItem) {
        todoItemsMap.get(todoItem.folderID).remove(todoItem);
        TodoItemDbHelper dbHelper = new TodoItemDbHelper(context);
        dbHelper.removeTodoItem(todoItem);
    }
}
