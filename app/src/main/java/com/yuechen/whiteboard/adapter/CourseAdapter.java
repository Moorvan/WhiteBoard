package com.yuechen.whiteboard.adapter;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yuechen.whiteboard.Model.Course;
import com.yuechen.whiteboard.R;
import com.yuechen.whiteboard.TodoListActivity;

import java.util.ArrayList;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.ViewHolder> {
    private ArrayList<Course> mCourseList;

    private Context context;



    static class ViewHolder extends RecyclerView.ViewHolder {
        View courseView;
        TextView courseNameView;

        public ViewHolder(View view) {
            super(view);
            courseView = view;
            courseNameView = view.findViewById(R.id.course_name);
        }
    }

    public CourseAdapter(Context context, ArrayList<Course> mCourseList) {
        this.context = context;
        this.mCourseList = mCourseList;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);

        holder.courseView.setOnClickListener(v -> {
            int position = holder.getAdapterPosition();
            Course course = mCourseList.get(position);
            Intent intent = new Intent(context, TodoListActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("id", course.getCourseID());
            bundle.putBoolean("isLesson", true);
            intent.putExtras(bundle);
            context.startActivity(intent);
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Course course = mCourseList.get(position);
        holder.courseNameView.setText(course.getCourseName());
    }

    @Override
    public int getItemCount() {
        return mCourseList.size();
    }
}
