package com.yuechen.whiteboard.Model;

public class Lesson {
    public String courseID;
    public String courseName;
    public String location;
    public String courseInstructor;
    public int weekOffset;
    public int dayOffset;
    public int startTimeOffset;
    public int endTimeOffset;
    public String startDateTime;
    public String endDateTime;

    @Override
    public String toString() {
        return "Lesson{" +
                "courseID='" + courseID + '\'' +
                ", courseName='" + courseName + '\'' +
                ", location='" + location + '\'' +
                ", courseInstructor='" + courseInstructor + '\'' +
                ", weekOffset=" + weekOffset +
                ", dayOffset=" + dayOffset +
                ", startTimeOffset=" + startTimeOffset +
                ", endTimeOffset=" + endTimeOffset +
                ", startDateTime='" + startDateTime + '\'' +
                ", endDateTime='" + endDateTime + '\'' +
                '}';
    }
}
