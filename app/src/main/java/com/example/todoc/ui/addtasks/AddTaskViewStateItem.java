package com.example.todoc.ui.addtasks;

import androidx.annotation.NonNull;

import java.util.Objects;

/** @noinspection unused, unused, unused */
public class AddTaskViewStateItem {

    private final int projectId;

    @NonNull
    private final String taskName;

    public AddTaskViewStateItem(int projectId, @NonNull String taskName) {
        this.projectId = projectId;
        this.taskName = taskName;
    }

    public int getProjectId() {
        return projectId;
    }

    @NonNull
    public String getTaskName() {
        return taskName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AddTaskViewStateItem that = (AddTaskViewStateItem) o;
        return projectId == that.projectId && taskName.equals(that.taskName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(projectId, taskName);
    }

    @Override
    public String toString() {
        return "AddTaskViewStateItem{" +
            "projectId=" + projectId +
            ", taskName='" + taskName + '\'' +
            '}';
    }
}
