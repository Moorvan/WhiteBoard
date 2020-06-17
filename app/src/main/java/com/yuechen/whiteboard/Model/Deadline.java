package com.yuechen.whiteboard.Model;

public class Deadline {
    public String id;
    public String title;
    public String eventType;
    public String calendarName;
    public String url;
    public String calendarID;
    public String startDateTime;
    public String endDateTime;
    public String note = "";
    public boolean finished = false;

    @Override
    public String toString() {
        return "Deadline{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", eventType='" + eventType + '\'' +
                ", calendarName='" + calendarName + '\'' +
                ", url='" + url + '\'' +
                ", calendarID='" + calendarID + '\'' +
                ", startDateTime='" + startDateTime + '\'' +
                ", endDateTime='" + endDateTime + '\'' +
                ", note='" + note + '\'' +
                ", finished=" + finished +
                '}';
    }
}
