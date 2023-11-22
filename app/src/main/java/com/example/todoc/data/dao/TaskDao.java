package com.example.todoc.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.todoc.data.entity.ProjectWithTasks;
import com.example.todoc.data.entity.Task;

import java.util.List;

@Dao
public interface TaskDao {

    @Insert
    long insert(Task task);

    @Transaction
    @Query("SELECT * FROM task_table")
    LiveData<List<Task>> getAllTasks();

    @Transaction
    @Query("SELECT * FROM project_table")
    LiveData<List<ProjectWithTasks>> getAllProjectWithTasks();

    @Query("DELETE FROM task_table WHERE task_id=:taskId")
    int delete(long taskId);
}
