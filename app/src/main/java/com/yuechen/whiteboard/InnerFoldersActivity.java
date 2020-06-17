package com.yuechen.whiteboard;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.yuechen.whiteboard.DataSource.CourseDataSource;
import com.yuechen.whiteboard.DataSource.CourseObserver;
import com.yuechen.whiteboard.Model.Course;
import com.yuechen.whiteboard.Model.Folder;
import com.yuechen.whiteboard.adapter.FolderAdapter;

import java.util.ArrayList;
import java.util.List;

public class InnerFoldersActivity extends AppCompatActivity implements CourseObserver {

    private Toolbar toolbar;
    private ArrayList<Folder> folderList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inner_folders);

        initFolders();
        initView();
        RecyclerView foldersView = findViewById(R.id.inner_folder);
        StaggeredGridLayoutManager layoutManager = new
                StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        foldersView.setLayoutManager(layoutManager);
        FolderAdapter adapter = new FolderAdapter(this, folderList, true);
        foldersView.setAdapter(adapter);
    }

    private void initView() {
        toolbar = findViewById(R.id.second_toolbar);
        toolbar.setTitle("课程");
        toolbar.setNavigationOnClickListener(v -> {
            finish();
        });
    }

    private void initFolders() {
        CourseDataSource.subscribe(this);
    }

    @Override
    public void notifyCoursesUpdate(List<Course> courses) {

        for(Course course : courses) {
            Folder folder = new Folder(course.courseName, course.courseID);
        }
    }
}