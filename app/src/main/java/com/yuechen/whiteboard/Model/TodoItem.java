package com.yuechen.whiteboard.Model;

public class TodoItem {
    public long _id = -1;
    public String title;
    public long folderID;
    public String endDateTime;
    public String note;
    public boolean finished;

    public TodoItem() {}

    public TodoItem(String title, long folderID, String endDateTime, String note, boolean finished) {
        this.title = title;
        this.folderID = folderID;
        this.endDateTime = endDateTime;
        this.note = note;
        this.finished = finished;
    }
}
