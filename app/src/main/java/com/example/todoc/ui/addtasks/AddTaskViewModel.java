package com.example.todoc.ui.addtasks;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.example.todoc.data.TaskRepository;
import com.example.todoc.data.entity.Project;
import com.example.todoc.data.entity.Task;
import com.example.todoc.ui.util.SingleLiveEvent;

import java.util.List;
import java.util.concurrent.Executor;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class AddTaskViewModel extends ViewModel {

    @NonNull
    private TaskRepository taskRepository;

    @NonNull
    private Executor ioExecutor;

    private Project selectedProject;

    private String taskNameCreated;

    private SingleLiveEvent<Void> closeActivitySingleLiveEvent = new SingleLiveEvent<>();

    @Inject
    public AddTaskViewModel(
        @NonNull TaskRepository taskRepository,
        @NonNull Executor ioExecutor
    ) {
        this.taskRepository = taskRepository;
        this.ioExecutor = ioExecutor;
    }

    public List<Project> getAllProjects() {
        return taskRepository.getAllProject().getValue();
    }

    public void onProjectSelected(@NonNull Project project) {
        selectedProject = project;
    }

    public void  onTaskName(@NonNull String taskName) {
        taskNameCreated = taskName;
    }
    public SingleLiveEvent<Void> getCloseActivitySingleLiveEvent() {
        return closeActivitySingleLiveEvent;
    }

    public void onAddButtonClicked(@NonNull Long timestamp) {
        ioExecutor.execute(() -> {
            taskRepository.addTask(
                new Task(
                    selectedProject.getProject_id(),
                    taskNameCreated,
                    timestamp
                )
            );
        });

        closeActivitySingleLiveEvent.call();
    }

    public void onCancelButtonClicked() {
        closeActivitySingleLiveEvent.call();
    }
}
