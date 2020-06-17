package com.yuechen.whiteboard.Model;

public class Folder {
    private String name;
    private int folderId;

    public Folder(String name, int folderId) {
        this.name = name;
        this.folderId = folderId;
    }

    public String getName() {
        return name;
    }

    public int getFolderId() {
        return folderId;
    }
}
