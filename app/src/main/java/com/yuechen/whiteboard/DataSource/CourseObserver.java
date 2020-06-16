package com.yuechen.whiteboard.DataSource;

import com.yuechen.whiteboard.Model.Course;

import java.util.List;

public interface CourseObserver {
    public void notifyCoursesUpdate(List<Course> courses);
}
