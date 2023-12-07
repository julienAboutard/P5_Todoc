package com.example.todoc.ui.addtasks;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.todoc.R;
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
    private final TaskRepository taskRepository;

    @NonNull
    private final Executor ioExecutor;

    private Project selectedProject;

    private String taskNameCreated;

    private final SingleLiveEvent<Void> closeDialogSingleLiveEvent = new SingleLiveEvent<>();

    private final SingleLiveEvent<Integer> displayToastSingleLiveEvent = new SingleLiveEvent<>();

    @Inject
    public AddTaskViewModel(
        @NonNull TaskRepository taskRepository,
        @NonNull Executor ioExecutor
    ) {
        this.taskRepository = taskRepository;
        this.ioExecutor = ioExecutor;
    }

    public LiveData<List<Project>> getAllProjects() {
        return taskRepository.getAllProject();
    }

    public void onProjectSelected(@NonNull Project project) {
        selectedProject = project;
    }

    public void onTaskNameChanged(String taskName) {
        taskNameCreated = taskName;
    }

    public SingleLiveEvent<Void> getCloseDialogSingleLiveEvent() {
        return closeDialogSingleLiveEvent;
    }

    public SingleLiveEvent<Integer> getDisplayToastSingleLiveEvent() {
        return displayToastSingleLiveEvent;
    }
    public void onAddButtonClicked(@NonNull Long timestamp) {
        if (taskNameCreated == null) {
            displayToastSingleLiveEvent.setValue(R.string.error_empty_task_name);
        } else {
            ioExecutor.execute(() -> taskRepository.addTask(
                new Task(
                    selectedProject.getProjectId(),
                    taskNameCreated,
                    timestamp
                )
            ));

            closeDialogSingleLiveEvent.call();
        }
    }

    public void onCancelButtonClicked() {
        closeDialogSingleLiveEvent.call();
    }
}
