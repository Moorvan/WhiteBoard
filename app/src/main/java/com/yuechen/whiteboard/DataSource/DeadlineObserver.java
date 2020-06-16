package com.yuechen.whiteboard.DataSource;

import com.yuechen.whiteboard.Model.Deadline;

import java.util.List;

public interface DeadlineObserver {
    public void notifyDeadlinesInsert(List<Deadline> newDeadlines);
}
