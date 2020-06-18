package com.yuechen.whiteboard.Adapter;

import android.app.AlertDialog;
import android.app.Dialog;
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
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.github.chengang.library.TickView;
import com.google.android.material.card.MaterialCardView;
import com.yuechen.whiteboard.DataSource.DeadlineDataSource;
import com.yuechen.whiteboard.DataSource.TodoItemDataSource;
import com.yuechen.whiteboard.Model.Deadline;
import com.yuechen.whiteboard.Model.TodoItem;
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
        MaterialCardView deadlineCardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            deadlineView = itemView;
            deadlineDateView = deadlineView.findViewById(R.id.deadline_date);
            deadlineTimeView = deadlineView.findViewById(R.id.deadline_time);
            deadlineContentView = deadlineView.findViewById(R.id.deadline_content);
            deadlineNoteView = deadlineView.findViewById(R.id.deadline_note);
            deadlineCheckView = deadlineView.findViewById(R.id.deadline_check);
            deadlineCardView = deadlineView.findViewById(R.id.deadline_card);
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
        holder.deadlineCardView.setOnClickListener(v -> {
            int position = holder.getAdapterPosition();
            Deadline deadline = deadlines.get(position);
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("修改备注");
            View vw = LayoutInflater.from(context).inflate(R.layout.change_note_item, null);
            EditText changeNoteEditText = vw.findViewById(R.id.change_note);
            builder.setView(vw);
            changeNoteEditText.setText(deadline.note);
            Log.d("deadline", "SHOW");
            builder.setPositiveButton("确定",  (dialog, which) -> {
                deadline.note = changeNoteEditText.getText().toString();
                DeadlineDataSource.updateDeadline(context, deadline.id, deadline.note, deadline.finished);
                notifyDataSetChanged();
            });
            builder.setNegativeButton("取消", (dialog, which) -> {});
            builder.show();
        });
        holder.deadlineCheckView.setOnCheckedChangeListener((v, isCheck) -> {
            int position = holder.getAdapterPosition();
            Deadline deadline = deadlines.get(position);
            deadline.finished = isCheck;
            DeadlineDataSource.updateDeadline(context, deadline.id, isCheck);
        });

        holder.deadlineCardView.setOnLongClickListener(v -> {
            int position = holder.getAdapterPosition();
            Deadline deadline = deadlines.get(position);
            AlertDialog dialog = new AlertDialog.Builder(context)
                    .setTitle("确认删除")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
//                            DeadlineDataSource.de
                            deadlines.remove(deadline);
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
