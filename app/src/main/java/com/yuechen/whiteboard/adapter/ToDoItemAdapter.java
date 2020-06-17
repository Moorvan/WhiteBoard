package com.yuechen.whiteboard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.chengang.library.TickView;
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

        public ViewHolder(View itemView) {
            super(itemView);
            todoListView = itemView;
            todoListDateView = todoListView.findViewById(R.id.deadline_date);
            todoListTimeView = todoListView.findViewById(R.id.deadline_time);
            todoListContentView = todoListView.findViewById(R.id.deadline_content);
            todoListNoteView = todoListView.findViewById(R.id.deadline_check);
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
