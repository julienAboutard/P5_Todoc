package com.example.todoc.data.entity;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class ProjectWithTasks {
    @Embedded public Project project;
    @Relation(
        parentColumn = "project_id",
        entityColumn = "projectId"
    )
    public List<Task> task;
}
