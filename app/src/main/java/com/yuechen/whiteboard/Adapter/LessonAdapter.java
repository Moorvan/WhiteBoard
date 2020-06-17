package com.yuechen.whiteboard.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.yuechen.whiteboard.Model.Lesson;
import com.yuechen.whiteboard.R;

import java.util.List;

public class LessonAdapter extends ArrayAdapter<Lesson> {

    private int resourceId;

    public LessonAdapter(@NonNull Context context, int resource, @NonNull List<Lesson> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Lesson lesson = getItem(position);
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
        } else {
            view = convertView;
        }
        TextView lessonTimeView = (TextView) view.findViewById(R.id.lesson_time);
        TextView lessonNameView = (TextView) view.findViewById(R.id.lesson_name);
        TextView lessonInstructorView = (TextView) view.findViewById(R.id.lesson_instructor);
        TextView lessonLocaltionView = (TextView) view.findViewById(R.id.lesson_location);
        lessonTimeView.setText(lesson.startDateTime.substring(11, 16) + " - " + lesson.endDateTime.substring(11, 16));
        lessonNameView.setText(lesson.courseName);
        lessonInstructorView.setText(lesson.courseInstructor);
        lessonLocaltionView.setText(lesson.location);
        return view;
    }
}
