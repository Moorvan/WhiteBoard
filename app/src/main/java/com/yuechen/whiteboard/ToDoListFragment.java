package com.yuechen.whiteboard;


import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ItemDecoration;
import androidx.recyclerview.widget.RecyclerViewAccessibilityDelegate;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yuechen.whiteboard.Model.Folder;
import com.yuechen.whiteboard.adapter.FolderAdapter;
import com.yuechen.whiteboard.layoutUtil.MyItemDecoration;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class ToDoListFragment extends Fragment {

    private ArrayList<Folder> folderList = new ArrayList<>();

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initFolders();
        RecyclerView foldersView = getView().findViewById(R.id.outer_folder);
        StaggeredGridLayoutManager layoutManager = new
                StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        foldersView.setLayoutManager(layoutManager);
        FolderAdapter adapter = new FolderAdapter(getContext(), folderList);
//        foldersView.addItemDecoration(new MyItemDecoration());
        foldersView.setAdapter(adapter);
    }

    private void initFolders() {
        Folder folder1 = new Folder("课程", 1);
        folderList.add(folder1);
        Folder folder2 = new Folder("生活", 2);
        folderList.add(folder2);
        Folder folder3 = new Folder("其他", 3);
        folderList.add(folder3);
        Folder folder4 = new Folder("其他", 4);
        folderList.add(folder4);
        Folder folder5 = new Folder("其他", 5);
        folderList.add(folder5);
        Folder folder6 = new Folder("其他", 6);
        folderList.add(folder6);
        Folder folder7 = new Folder("其他", 7);
        folderList.add(folder7);
        Folder folder8 = new Folder("其他", 8);
        folderList.add(folder8);
        Folder folder9 = new Folder("其他", 9);
        folderList.add(folder9);
        Folder folder10 = new Folder("其他", 10);
        folderList.add(folder10);
        Folder folder11 = new Folder("其他", 11);
        folderList.add(folder11);
        Folder folder12 = new Folder("其他", 12);
        folderList.add(folder12);
    }

    public ToDoListFragment() {
        // Required empty public constructor
    }
    

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_to_do_list, container, false);
    }

}
