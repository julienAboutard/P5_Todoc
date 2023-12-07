package com.example.todoc.data.entity;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity(
    tableName = "task_table",
    foreignKeys = @ForeignKey(
        entity = Project.class,
        parentColumns = "project_id",
        childColumns = "projectId"
    )
)
public class Task {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "task_id")
    private final int taskId;

    @ColumnInfo(index = true)
    private final int projectId;

    @NonNull
    @ColumnInfo(name = "task_name")
    private final String taskName;

    @ColumnInfo(name = "task_timeStamp")
    private final long taskTimeStamp;

    @Ignore
    public Task(int projectId, @NonNull String taskName, long taskTimeStamp) {
        this(0, projectId, taskName, taskTimeStamp);
    }

    @VisibleForTesting
    public Task(int taskId, int projectId, @NonNull String taskName, long taskTimeStamp) {
        this.taskId = taskId;
        this.projectId = projectId;
        this.taskName = taskName;
        this.taskTimeStamp = taskTimeStamp;
    }


    public int getTaskId() {
        return taskId;
    }

    public int getProjectId() {
        return projectId;
    }

    @NonNull
    public String getTaskName() {
        return taskName;
    }

    public long getTaskTimeStamp() {
        return taskTimeStamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return taskId == task.taskId && projectId == task.projectId && taskTimeStamp == task.taskTimeStamp && taskName.equals(task.taskName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(taskId, projectId, taskName, taskTimeStamp);
    }

    @Override
    public String toString() {
        return "Task{" +
            "task_id=" + taskId +
            ", projectId=" + projectId +
            ", task_name='" + taskName + '\'' +
            ", task_timeStamp=" + taskTimeStamp +
            '}';
    }
}
