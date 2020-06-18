package com.yuechen.whiteboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Bundle;
import android.util.Log;

import com.yuechen.whiteboard.Adapter.DeadlineAdapter;

import com.yuechen.whiteboard.Adapter.ToDoItemAdapter;

import com.yuechen.whiteboard.DataSource.DeadlineDataSource;
import com.yuechen.whiteboard.DataSource.DeadlineObserver;
import com.yuechen.whiteboard.Model.Deadline;
import com.yuechen.whiteboard.Model.TodoItem;

import java.util.ArrayList;
import java.util.List;

public class TodoListActivity extends AppCompatActivity implements DeadlineObserver {

    RecyclerView todoListView;

    private List<Deadline> deadlines;
    private DeadlineAdapter deadlineAdapter;
    private List<TodoItem> todoItems;
    private ToDoItemAdapter toDoItemAdapter;

    private static boolean firstRead = true;
    private boolean isLesson;
    private long folderId;
    private String courseId;

    private Toolbar toolbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);
        initToolBar();
        Bundle bundle = getIntent().getExtras();
        isLesson = bundle.getBoolean("isLesson");
        if (isLesson) {
            courseId = bundle.getString("id");
        } else {
            folderId = bundle.getLong("id");
        }

        DeadlineDataSource.subscribe(this);

        todoListView = findViewById(R.id.todolist);
        StaggeredGridLayoutManager layoutManager = new
                StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        todoListView.setLayoutManager(layoutManager);

        initTodoList();

    }

    private void initToolBar() {
        toolbar = findViewById(R.id.todolist_toolbar);
        toolbar.setTitle("待办事项");
        toolbar.setNavigationOnClickListener((v) -> finish());
    }

    private void initTodoList() {
//        Deadline deadline = new Deadline();
//        deadline.endDateTime = "2020-06-15 23:59:59";
//        deadline.title = "编译原理";
//        deadline.note = "尽快保质保量完成";
//        deadline.finished = false;
//        Deadline deadline1 = new Deadline();
//        deadline1.endDateTime = "2020-06-16 23:59:59";
//        deadline1.title = "Android";
//        deadline1.note = "快交";
//        deadline1.finished = true;
//        deadlines = new ArrayList<>();
//        deadlines.add(deadline);
//        deadlines.add(deadline1);
        if (isLesson) {
            if (firstRead) {
                DeadlineDataSource.readDeadlines(getApplicationContext());
                firstRead = false;
                Log.d("deadlines", "First read");
            }
            List<Deadline> readDeadlines = DeadlineDataSource.deadlineCourseMap.get(courseId);
            deadlines = new ArrayList<>();
            if (readDeadlines != null) {
                deadlines.addAll(readDeadlines);
            }
            deadlineAdapter = new DeadlineAdapter(deadlines, this);
            todoListView.setAdapter(deadlineAdapter);
            DeadlineDataSource.fetchNewDeadlines(this);
        }
    }


    @Override
    public void notifyDeadlinesInsert(List<Deadline> newDeadlines) {
        for (Deadline deadline : newDeadlines) {
            String course = deadline.calendarID.substring(0, 17);
            if (course.equals(courseId)) {
                deadlines.add(deadline);
            }
        }
        Log.d("deadlines", "fetch");
        deadlineAdapter.notifyDataSetChanged();
    }
}