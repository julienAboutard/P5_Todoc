package com.example.todoc.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.todoc.data.entity.Project;

import java.util.List;

public interface ProjectDao {

    @Insert
    long insert(Project project);

    @Query("SELECT * FROM project_table")
    LiveData<List<Project>> getAll();


}
