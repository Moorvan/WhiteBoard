package com.yuechen.whiteboard.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yuechen.whiteboard.DataSource.FolderDataSource;
import com.yuechen.whiteboard.InnerFoldersActivity;
import com.yuechen.whiteboard.Model.Folder;
import com.yuechen.whiteboard.R;
import com.yuechen.whiteboard.TodoListActivity;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.ViewHolder> {

    private ArrayList<Folder> mFolderList;

    private Context context;

    static class ViewHolder extends RecyclerView.ViewHolder {
        View folderView;
        TextView folderNameView;


        public ViewHolder(View view) {
            super(view);
            folderView = view;
            folderNameView = view.findViewById(R.id.folder_name);
        }
    }

    public FolderAdapter(Context context, ArrayList<Folder> mFolderList) {
        this.context = context;
        this.mFolderList = mFolderList;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.folder_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);

        holder.folderView.setOnClickListener(v -> {
            int position = holder.getAdapterPosition();
            Folder folder = mFolderList.get(position);
            if(position == 0) {
                Intent intent = new Intent(context, InnerFoldersActivity.class);
                context.startActivity(intent);
            } else {
                Intent intent = new Intent(context, TodoListActivity.class);
                Bundle bundle = new Bundle();
                bundle.putLong("id", folder.getFolderId());
                bundle.putBoolean("isLesson", false);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });

        holder.folderView.setOnLongClickListener(v -> {
            int position = holder.getAdapterPosition();
            Folder folder = mFolderList.get(position);
            if(position == 0) {
                return false;
            } else {
                AlertDialog dialog = new AlertDialog.Builder(context)
                        .setTitle("确认删除")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FolderDataSource.deleteFolder(context, folder);
                                notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .create();
                dialog.show();
                dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.RED);

            }
            return true;
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Folder folder = mFolderList.get(position);
        holder.folderNameView.setText(folder.getName());
    }

    @Override
    public int getItemCount() {
        return mFolderList.size();
    }
}
