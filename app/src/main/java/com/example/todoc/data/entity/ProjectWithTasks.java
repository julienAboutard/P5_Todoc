package com.example.todoc.data.entity;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;
import java.util.Objects;

public class ProjectWithTasks {
    @Embedded public Project project;
    @Relation(
        parentColumn = "project_id",
        entityColumn = "projectId"
    )
    public List<Task> task;

    public ProjectWithTasks(Project project, List<Task> task) {
        this.project = project;
        this.task = task;
    }

    public Project getProject() {
        return project;
    }

    public List<Task> getTask() {
        return task;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProjectWithTasks that = (ProjectWithTasks) o;
        return Objects.equals(project, that.project) && Objects.equals(task, that.task);
    }

    @Override
    public int hashCode() {
        return Objects.hash(project, task);
    }

    @Override
    public String toString() {
        return "ProjectWithTasks{" +
            "project=" + project +
            ", task=" + task +
            '}';
    }
}
