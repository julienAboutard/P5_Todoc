package com.example.todoc.ui.tasks;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.todoc.data.TaskRepository;
import com.example.todoc.data.entity.Project;
import com.example.todoc.data.entity.ProjectWithTasks;
import com.example.todoc.data.entity.Task;
import com.example.todoc.data.sorting.AlphabeticalSortingType;
import com.example.todoc.data.sorting.ChronologicalSortingType;
import com.example.todoc.data.sorting.SortingParametersRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;

public class TaskViewModel extends ViewModel {

    @NonNull
    private TaskRepository taskRepository;

    @NonNull
    private Executor ioExecutor;

    private final MediatorLiveData<List<TaskViewStateItem>> mediatorLiveData = new MediatorLiveData<>();
    private final  SortingParametersRepository sortingParametersRepository = new SortingParametersRepository();

    public TaskViewModel(
        @NonNull TaskRepository taskRepository,
        @NonNull Executor ioExecutor
    ) {
        this.taskRepository = taskRepository;
        this.ioExecutor = ioExecutor;

        LiveData<List<Task>> allTasks = taskRepository.getAllTasks();
        LiveData<AlphabeticalSortingType> alphabeticalSortingTypeLiveData = sortingParametersRepository.getAlphabeticalSortingTypeLiveData();
        LiveData<ChronologicalSortingType> chronologicalSortingTypeLiveData = sortingParametersRepository.getChronologicalSortingTypeLiveData();

        mediatorLiveData.addSource(allTasks, projectWithTasks ->
            combine(projectWithTasks, alphabeticalSortingTypeLiveData.getValue(), chronologicalSortingTypeLiveData.getValue())
        );

        mediatorLiveData.addSource(alphabeticalSortingTypeLiveData, alphabeticalSortingType ->
            combine(allTasks.getValue(), alphabeticalSortingType, chronologicalSortingTypeLiveData.getValue())
        );
        mediatorLiveData.addSource(chronologicalSortingTypeLiveData, chronologicalSortingType ->
            combine(allTasks.getValue(), alphabeticalSortingTypeLiveData.getValue(), chronologicalSortingType)
        );
    }

    private void combine(
        @Nullable List<Task> taskList,
        @Nullable AlphabeticalSortingType alphabeticalSortingType,
        @Nullable ChronologicalSortingType chronologicalSortingType) {

        if (taskList == null) {
            return;
        }
        List<Project> allProjects = taskRepository.getAllProject().getValue();
        Collections.sort(
            taskList,
            (task1, task2) -> compareTasks(task1, task2, alphabeticalSortingType, chronologicalSortingType)
        );

        List<TaskViewStateItem> taskViewStateItemList = new ArrayList<>();
        for (Task task : taskList) {
            taskViewStateItemList.add(
                new TaskViewStateItem(
                    task.getTask_id(),
                    allProjects.get(task.getProjectId()).getProject_color(),
                    task.getTask_name(),
                    allProjects.get(task.getProjectId()).getProject_name()
                )
            );
        }
    }

    private int  compareTasks(
        @NonNull Task task1,
        @NonNull Task task2,
        @NonNull AlphabeticalSortingType alphabeticalSortingType,
        @NonNull ChronologicalSortingType chronologicalSortingType
    ) {
        if (alphabeticalSortingType.getComparator() != null) {
            int alphabeticalComparison = alphabeticalSortingType.getComparator().compare(task1, task2);

            if (alphabeticalComparison != 0) {
                return alphabeticalComparison;
            } else {
                if (chronologicalSortingType.getComparator() != null) {
                    int chronologicalComparison = chronologicalSortingType.getComparator().compare(task1, task2);

                    if (chronologicalComparison != 0) {
                        return chronologicalComparison;
                    }
                }
            }
        } else if (chronologicalSortingType.getComparator() != null) {
            int chronologicalComparison = chronologicalSortingType.getComparator().compare(task1, task2);

            if (chronologicalComparison != 0) {
                return chronologicalComparison;
            }
        }

        return task1.getTask_id() - task2.getTask_id();
    }

    public void onDeleteTaskButtonClicked(int taskId) {
        ioExecutor.execute(() -> taskRepository.deleteTask(taskId));
    }

    @NonNull
    public LiveData<List<TaskViewStateItem>> getViewStateLiveData() {
        return mediatorLiveData;
    }

}