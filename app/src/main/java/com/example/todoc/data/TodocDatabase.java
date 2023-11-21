package com.example.todoc.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.todoc.data.dao.ProjectDao;
import com.example.todoc.data.dao.TaskDao;
import com.example.todoc.data.entity.Project;
import com.example.todoc.data.entity.Task;

@Database(
    entities = {Task.class, Project.class},
    version = 1
)
public class TodocDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "todoc_database";

    private static final TodocDatabase instance;

    public abstract TaskDao taskDao();

    public abstract ProjectDao projectDao;


}
