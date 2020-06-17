package com.yuechen.whiteboard;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.yuechen.whiteboard.Model.Folder;
import com.yuechen.whiteboard.adapter.FolderAdapter;

import java.util.ArrayList;

public class InnerFoldersActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ArrayList<Folder> folderList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inner_folders);

        initView();

        initFolders();
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
        Folder folder1 = new Folder("语文", 1);
        folderList.add(folder1);
        Folder folder2 = new Folder("数学", 2);
        folderList.add(folder2);
        Folder folder3 = new Folder("英语", 3);
        folderList.add(folder3);
        Folder folder4 = new Folder("政治", 4);
        folderList.add(folder4);
        Folder folder5 = new Folder("历史", 5);
        folderList.add(folder5);
        Folder folder6 = new Folder("生物", 6);
        folderList.add(folder6);
        Folder folder7 = new Folder("物理", 7);
        folderList.add(folder7);
    }
}