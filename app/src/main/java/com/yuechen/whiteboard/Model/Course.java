package com.yuechen.whiteboard.Model;

public class Course {
    public String courseID;
    public String courseName;
    public String courseInstructor;

    public String getCourseID() {
        return courseID;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getCourseInstructor() {
        return courseInstructor;
    }

    public Course() {}
    public Course(String courseName) {
        this.courseName = courseName;
    }

}
