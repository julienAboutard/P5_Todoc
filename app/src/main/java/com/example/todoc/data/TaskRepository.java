package com.example.todoc.data;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.example.todoc.data.dao.ProjectDao;
import com.example.todoc.data.dao.TaskDao;
import com.example.todoc.data.entity.Project;
import com.example.todoc.data.entity.ProjectWithTasks;
import com.example.todoc.data.entity.Task;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class TaskRepository {

    @NonNull
    private final TaskDao taskDao;

    @NonNull
    private final ProjectDao projectDao;

    @Inject
    public TaskRepository(
        @NonNull TaskDao taskDao,
        @NonNull ProjectDao projectDao
    ) {
        this.taskDao = taskDao;
        this.projectDao = projectDao;
    }
    public LiveData<List<Project>> getAllProjects() {
        return projectDao.getAllProjects();
    }

    public LiveData<List<ProjectWithTasks>> getAllProjectWithTasks() {
        return taskDao.getAllProjectWithTasks();
    }

    public LiveData<List<Task>> getAllTasks() { return taskDao.getAllTasks(); }

    public void addTask(Task task) {
        taskDao.insert(task);
    }

    public void deleteTask(int taskId) {
        taskDao.delete(taskId);
    }
}
