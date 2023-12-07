package com.example.todoc.data.entity;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity(tableName = "project_table")
public class Project {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "project_id")
    private final int projectId;

    @NonNull
    @ColumnInfo(name = "project_name")
    private final String projectName;

    @ColorRes
    @ColumnInfo(name = "project_color")
    private final int projectColor;

    @Ignore
    public Project(@NonNull String projectName, int projectColor) {
        this(0, projectName, projectColor);
    }


    @VisibleForTesting
    public Project(int projectId, @NonNull String projectName, int projectColor) {
        this.projectId = projectId;
        this.projectName = projectName;
        this.projectColor = projectColor;
    }

    public int getProjectId() {
        return projectId;
    }

    @NonNull
    public String getProjectName() {
        return projectName;
    }

    @ColorRes
    public int getProjectColor() {
        return projectColor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Project project = (Project) o;
        return projectId == project.projectId && projectColor == project.projectColor && projectName.equals(project.projectName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(projectId, projectName, projectColor);
    }

    @Override
    public String toString() {
        return "Project{" +
            "project_id=" + projectId +
            ", projectName='" + projectName + '\'' +
            ", projectColor=" + projectColor +
            '}';
    }
}
