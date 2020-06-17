package com.yuechen.whiteboard;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.yuechen.whiteboard.DataSource.FolderDataSource;
import com.yuechen.whiteboard.Model.Folder;
import com.yuechen.whiteboard.adapter.FolderAdapter;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class ToDoListFragment extends Fragment {

    private ArrayList<Folder> folderList;
    private FloatingActionButton addBtn;

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

        initView();
    }


    private void initView() {
        addBtn = getView().findViewById(R.id.add_btn);
        addBtn.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("添加分类");
            View view = LayoutInflater.from(getContext()).inflate(R.layout.add_folder_item, null);
            final EditText newItem = view.findViewById(R.id.new_item);
            builder.setView(view);
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String newItemName = newItem.getText().toString();
                    FolderDataSource.insertFolder(getContext(), new Folder(newItemName));
                }
            });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            builder.show();
        });
    }

    private void initFolders() {
        FolderDataSource.readfolders(getContext());
        folderList = (ArrayList<Folder>)FolderDataSource.folders;
        if(folderList.size() == 0) {
            FolderDataSource.insertFolder(getContext(), new Folder("课程"));
        }
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
