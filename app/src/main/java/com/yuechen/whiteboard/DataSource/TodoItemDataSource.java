package com.yuechen.whiteboard.DataSource;

import android.content.Context;

import com.yuechen.whiteboard.Database.TodoItemDbHelper;
import com.yuechen.whiteboard.Model.TodoItem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TodoItemDataSource {
    public static Map<Integer, List<TodoItem>> todoItemsMap = new HashMap<>();

    public static void readTodoItems(Context context) {
        TodoItemDbHelper dbHelper = new TodoItemDbHelper(context);
        todoItemsMap.clear();
        todoItemsMap = dbHelper.readTodoItems();
    }

    public static long insertTodoItem(Context context, TodoItem todoItem) {
        TodoItemDbHelper dbHelper = new TodoItemDbHelper(context);
        return dbHelper.insertTodoItem(todoItem);
    }

    public static long updateTodoItem(Context context, TodoItem todoItem) {
        TodoItemDbHelper dbHelper = new TodoItemDbHelper(context);
        return dbHelper.updateTodoItem(todoItem);
    }

    public static void deleteTodoItem(Context context, TodoItem todoItem) {
        todoItemsMap.get(todoItem.folderID).remove(todoItem);
        TodoItemDbHelper dbHelper = new TodoItemDbHelper(context);
        dbHelper.removeTodoItem(todoItem);
    }
}
