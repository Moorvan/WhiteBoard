package com.yuechen.whiteboard.Model;

public class Folder {
    private String name;
    private long folderId = -1;



    private String courseID;

    public Folder() {

    }

    public Folder(String name) {
        this.name = name;
    }

    public Folder(String name, String courseID) {
        this.name = name;
        this.courseID = courseID;
    }

    public String getCourseID() {
        return courseID;
    }

    public String getName() {
        return name;
    }

    public long getFolderId() {
        return folderId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFolderId(long folderId) {
        this.folderId = folderId;
    }
}
