package com.example.todoc.ui.tasks;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;

import java.util.Objects;

public class TaskViewStateItem {

    private final int taskId;

    @ColorRes
    private final int projectColor;

    @NonNull
    private final String taskName;

    @NonNull
    private final String projectName;

    public TaskViewStateItem(int taskId, int projectColor, @NonNull String taskName, @NonNull String projectName) {
        this.taskId = taskId;
        this.projectColor = projectColor;
        this.taskName = taskName;
        this.projectName = projectName;
    }

    public int getTaskId() {
        return taskId;
    }

    public int getProjectColor() {
        return projectColor;
    }

    @NonNull
    public String getTaskName() {
        return taskName;
    }

    @NonNull
    public String getProjectName() {
        return projectName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskViewStateItem that = (TaskViewStateItem) o;
        return taskId == that.taskId && projectColor == that.projectColor && taskName.equals(that.taskName) && projectName.equals(that.projectName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(taskId, projectColor, taskName, projectName);
    }

    @Override
    public String toString() {
        return "TaskViewStateItem{" +
            "taskId=" + taskId +
            ", projectColor=" + projectColor +
            ", taskName='" + taskName + '\'' +
            ", projectName='" + projectName + '\'' +
            '}';
    }
}
