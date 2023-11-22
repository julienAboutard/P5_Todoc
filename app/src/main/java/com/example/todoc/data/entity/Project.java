package com.example.todoc.data.entity;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity(tableName = "project_table")
public class Project {

    @PrimaryKey(autoGenerate = true)
    private int project_id;

    @NonNull
    private final String project_name;

    @ColorRes
    private final int project_color;

    public Project(@NonNull String project_name, int project_color) {
        this.project_name = project_name;
        this.project_color = project_color;
    }

    public void setProject_id(int id) {
        this.project_id = id;
    }

    public int getProject_id() {
        return project_id;
    }

    @NonNull
    public String getProject_name() {
        return project_name;
    }

    @ColorRes
    public int getProject_color() {
        return project_color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Project project = (Project) o;
        return project_id == project.project_id && project_color == project.project_color && project_name.equals(project.project_name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(project_id, project_name, project_color);
    }

    @Override
    public String toString() {
        return "Project{" +
            "project_id=" + project_id +
            ", project_name='" + project_name + '\'' +
            ", project_color=" + project_color +
            '}';
    }
}
