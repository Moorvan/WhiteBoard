package com.yuechen.whiteboard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.chengang.library.TickView;
import com.yuechen.whiteboard.Model.Deadline;
import com.yuechen.whiteboard.R;

import java.util.List;

public class DeadlineAdapter extends RecyclerView.Adapter<DeadlineAdapter.ViewHolder> {

    private List<Deadline> deadlines;

    private Context context;

    static class ViewHolder extends RecyclerView.ViewHolder {
        View deadlineView;
        TextView deadlineDateView;
        TextView deadlineTimeView;
        TextView deadlineContentView;
        TextView deadlineNoteView;
        TickView deadlineCheckView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            deadlineView = itemView;
            deadlineDateView = deadlineView.findViewById(R.id.deadline_date);
            deadlineTimeView = deadlineView.findViewById(R.id.deadline_time);
            deadlineContentView = deadlineView.findViewById(R.id.deadline_content);
            deadlineNoteView = deadlineView.findViewById(R.id.deadline_note);
            deadlineCheckView = deadlineView.findViewById(R.id.deadline_check);
        }
    }

    public DeadlineAdapter(List<Deadline> deadlines, Context context) {
        this.deadlines = deadlines;
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
        Deadline deadline = deadlines.get(position);
        String date = deadline.endDateTime.substring(0, 10);
        String time = deadline.endDateTime.substring(11, 16);
        holder.deadlineDateView.setText(date);
        holder.deadlineTimeView.setText(time);
        holder.deadlineContentView.setText(deadline.title);
        holder.deadlineNoteView.setText(deadline.note);
        holder.deadlineCheckView.setChecked(deadline.finished);
    }

    @Override
    public int getItemCount() {
        return deadlines.size();
    }
}
