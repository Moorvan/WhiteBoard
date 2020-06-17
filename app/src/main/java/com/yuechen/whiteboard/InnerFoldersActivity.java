package com.yuechen.whiteboard;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.yuechen.whiteboard.DataSource.CourseDataSource;
import com.yuechen.whiteboard.DataSource.CourseObserver;
import com.yuechen.whiteboard.Model.Course;
import com.yuechen.whiteboard.Adapter.CourseAdapter;

import java.util.ArrayList;
import java.util.List;

public class InnerFoldersActivity extends AppCompatActivity implements CourseObserver {

    private Toolbar toolbar;
    private ArrayList<Course> courseList = new ArrayList<>();

    private CourseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inner_folders);
        CourseDataSource.subscribe(this);

        initFolders();
        initView();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CourseDataSource.unsubscribe(this);
    }

    private void initView() {
        toolbar = findViewById(R.id.second_toolbar);
        toolbar.setTitle("课程");
        toolbar.setNavigationOnClickListener(v -> {
            finish();
        });

        RecyclerView foldersView = findViewById(R.id.inner_folder);
        StaggeredGridLayoutManager layoutManager = new
                StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        foldersView.setLayoutManager(layoutManager);
        adapter = new CourseAdapter(this, courseList);
        foldersView.setAdapter(adapter);
    }

    private void initFolders() {
        CourseDataSource.readCourses(getApplicationContext());
        courseList = (ArrayList<Course>) CourseDataSource.courses;
        if(CourseDataSource.courses.isEmpty()) {
            CourseDataSource.fetchCourses(getApplicationContext());
        }
    }

    @Override
    public void notifyCoursesUpdate(List<Course> courses) {
        adapter.notifyDataSetChanged();
    }

}