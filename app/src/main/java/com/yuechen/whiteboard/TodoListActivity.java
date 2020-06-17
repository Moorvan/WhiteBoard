package com.yuechen.whiteboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

public class TodoListActivity extends AppCompatActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);
        initView();
    }

    private void initView() {
        toolbar = findViewById(R.id.todolist_toolbar);
        toolbar.setTitle("待办事项");
        toolbar.setNavigationOnClickListener((v) -> finish());
    }
}