package com.yuechen.whiteboard.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.chengang.library.TickView;
import com.google.android.material.card.MaterialCardView;
import com.yuechen.whiteboard.DataSource.FolderDataSource;
import com.yuechen.whiteboard.DataSource.TodoItemDataSource;
import com.yuechen.whiteboard.Model.TodoItem;
import com.yuechen.whiteboard.R;

import java.util.List;

public class ToDoItemAdapter extends RecyclerView.Adapter<ToDoItemAdapter.ViewHolder> {
    private List<TodoItem> todoItems;
    private Context context;

    static class ViewHolder extends RecyclerView.ViewHolder {
        View todoListView;
        TextView todoListDateView;
        TextView todoListTimeView;
        TextView todoListContentView;
        TextView todoListNoteView;
        TickView todoListCheckView;
        MaterialCardView todoListCardView;

        public ViewHolder(View itemView) {
            super(itemView);
            todoListView = itemView;
            todoListDateView = todoListView.findViewById(R.id.deadline_date);
            todoListTimeView = todoListView.findViewById(R.id.deadline_time);
            todoListContentView = todoListView.findViewById(R.id.deadline_content);
            todoListNoteView = todoListView.findViewById(R.id.deadline_note);
            todoListCheckView = todoListView.findViewById(R.id.deadline_check);
            todoListCardView = todoListView.findViewById(R.id.deadline_card);
        }
    }

    public ToDoItemAdapter(List<TodoItem> todoItems, Context context) {
        this.todoItems = todoItems;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_item_layout, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.todoListCardView.setOnClickListener((v) -> {
            int position = holder.getAdapterPosition();
            TodoItem item = todoItems.get(position);
            View vw = LayoutInflater.from(context).inflate(R.layout.add_todo_item, parent, false);
            EditText editTitle = vw.findViewById(R.id.item_title);
            EditText editNote = vw.findViewById(R.id.item_note);
            EditText editDate = vw.findViewById(R.id.item_date);
            EditText editTime = vw.findViewById(R.id.item_time);
            editTitle.setText(item.title);
            editNote.setText(item.note);
            editDate.setText(item.endDateTime.substring(0, 10));
            editTime.setText(item.endDateTime.substring(11, 16));
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setView(vw);
            builder.setTitle("修改待办事项");
            builder.setPositiveButton("确定", (dialog, which) -> {
                String title = editTitle.getText().toString();
                String note = editNote.getText().toString();
                String dateTime = editDate.getText().toString() + " " + editTime.getText().toString();
                item.title = title;
                item.note = note;
                item.endDateTime = dateTime;
                TodoItemDataSource.updateDeadline(context, item._id, item.title,
                        item.folderID, item.endDateTime, item.note, item.finished);
                notifyDataSetChanged();
            });
            builder.setNegativeButton("取消", (dialog, which) -> {});
            builder.show();
        });

        holder.todoListCardView.setOnLongClickListener(v -> {
            int position = holder.getAdapterPosition();
            TodoItem todoItem = todoItems.get(position);
            AlertDialog dialog = new AlertDialog.Builder(context)
                    .setTitle("确认删除")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            TodoItemDataSource.deleteTodoItem(context, todoItem);
                            todoItems.remove(todoItem);
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
            return true;
        });

        holder.todoListCheckView.setOnCheckedChangeListener((v, isChecked) -> {
            int position = holder.getAdapterPosition();
            TodoItem item = todoItems.get(position);
            item.finished = isChecked;
            // TODO: 更新暂时有点问题
            long res = TodoItemDataSource.updateDeadline(context, item._id, item.finished);
            Log.d("todo", String.valueOf(res));
            notifyDataSetChanged();
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TodoItem todoItem = todoItems.get(position);
        String date = todoItem.endDateTime.substring(0, 10);
        String time = todoItem.endDateTime.substring(11, 16);
        holder.todoListDateView.setText(date);
        holder.todoListTimeView.setText(time);
        holder.todoListContentView.setText(todoItem.title);
        holder.todoListNoteView.setText(todoItem.note);
        holder.todoListCheckView.setChecked(todoItem.finished);
    }

    @Override
    public int getItemCount() {
        return todoItems.size();
    }
}
