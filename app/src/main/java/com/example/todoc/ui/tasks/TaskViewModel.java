package com.example.todoc.ui.tasks;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.example.todoc.data.TaskRepository;
import com.example.todoc.data.entity.Project;
import com.example.todoc.data.entity.ProjectWithTasks;
import com.example.todoc.data.entity.Task;
import com.example.todoc.data.sorting.AlphabeticalSortingType;
import com.example.todoc.data.sorting.ChronologicalSortingType;
import com.example.todoc.data.sorting.SortingParametersRepository;
import com.example.todoc.ui.util.SingleLiveEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class TaskViewModel extends ViewModel {

    @NonNull
    private final TaskRepository taskRepository;

    @NonNull
    private final Executor ioExecutor;

    private final MediatorLiveData<List<TaskViewStateItem>> mediatorLiveData = new MediatorLiveData<>();

    private final SingleLiveEvent<TaskViewAction> viewActionLiveEvent = new SingleLiveEvent<>();

    @Inject
    public TaskViewModel(
        @NonNull TaskRepository taskRepository,
        @NonNull Executor ioExecutor,
        @NonNull SortingParametersRepository sortingParametersRepository
    ) {
        this.taskRepository = taskRepository;
        this.ioExecutor = ioExecutor;

        LiveData<List<ProjectWithTasks>> allProjectWithTasks = taskRepository.getAllProjectWithTasks();
        LiveData<AlphabeticalSortingType> alphabeticalSortingTypeLiveData = sortingParametersRepository.getAlphabeticalSortingTypeLiveData();
        LiveData<ChronologicalSortingType> chronologicalSortingTypeLiveData = sortingParametersRepository.getChronologicalSortingTypeLiveData();

        mediatorLiveData.addSource(allProjectWithTasks, projectWithTasksList ->
            combine(projectWithTasksList, alphabeticalSortingTypeLiveData.getValue(), chronologicalSortingTypeLiveData.getValue())
        );
        mediatorLiveData.addSource(alphabeticalSortingTypeLiveData, alphabeticalSortingType ->
            combine(allProjectWithTasks.getValue(), alphabeticalSortingType, chronologicalSortingTypeLiveData.getValue())
        );
        mediatorLiveData.addSource(chronologicalSortingTypeLiveData, chronologicalSortingType ->
            combine(allProjectWithTasks.getValue(), alphabeticalSortingTypeLiveData.getValue(), chronologicalSortingType)
        );
    }

    private void combine(
        @Nullable List<ProjectWithTasks> projectWithTasksList,
        @Nullable AlphabeticalSortingType alphabeticalSortingType,
        @Nullable ChronologicalSortingType chronologicalSortingType) {

        if (alphabeticalSortingType == null) {
            throw new IllegalArgumentException("parameter alphabeticalSortingType should never be null because repository guarantees an initial value");
        }
        if (chronologicalSortingType == null) {
            throw new IllegalArgumentException("parameter chronologicalSortingType should never be null because repository guarantees an initial value");
        }
        if (projectWithTasksList == null) {
            return;
        }

        List<Task> taskList = new ArrayList<>();
        List<Project> projectList = new ArrayList<>();

        if (alphabeticalSortingType.getComparator() == null) {
            for (ProjectWithTasks projectWithTasks : projectWithTasksList) {
                projectList.add(projectWithTasks.getProject());
                for (Task task : projectWithTasks.getTask()) {
                    taskList.add(task);
                }
            }


            Collections.sort(
                taskList,
                (task1, task2) -> compareTasks(task1, task2, chronologicalSortingType)
            );
        } else {
            Collections.sort(
                projectWithTasksList,
                (projectWithTasksList1, projectWithTasksList2) -> compareProjectsWithTasks(
                    projectWithTasksList1,
                    projectWithTasksList2,
                    alphabeticalSortingType
                )
            );

            if (chronologicalSortingType.getComparator() != null) {
                for (ProjectWithTasks projectWithTasks : projectWithTasksList) {
                    Collections.sort(
                        projectWithTasks.getTask(),
                        (task1, task2) -> compareTasks(task1, task2, chronologicalSortingType)
                    );
                    projectList.add(projectWithTasks.getProject());
                    for (Task task : projectWithTasks.getTask()) {
                        taskList.add(task);
                    }
                }
            } else {
                for (ProjectWithTasks projectWithTasks : projectWithTasksList) {
                    projectList.add(projectWithTasks.getProject());
                    for (Task task : projectWithTasks.getTask()) {
                        taskList.add(task);
                    }
                }
            }
        }


        List<TaskViewStateItem> taskViewStateItemList = new ArrayList<>();
        for (Task task : taskList) {
            taskViewStateItemList.add(
                new TaskViewStateItem(
                    task.getTask_id(),
                    projectList.get(task.getProjectId() - 1).getProject_color(),
                    task.getTask_name(),
                    projectList.get(task.getProjectId() - 1).getProject_name()
                )
            );
        }
        mediatorLiveData.setValue(taskViewStateItemList);
    }

    private int compareTasks(
        @NonNull Task task1,
        @NonNull Task task2,
        @NonNull ChronologicalSortingType chronologicalSortingType
    ) {
        if (chronologicalSortingType.getComparator() != null) {
            int chronologicalComparison = chronologicalSortingType.getComparator().compare(task1, task2);

            if (chronologicalComparison != 0) {
                return chronologicalComparison;
            }
        }

        return task1.getTask_id() - task2.getTask_id();
    }

    private int compareProjectsWithTasks(
        @NonNull ProjectWithTasks projectWithTasks1,
        @NonNull ProjectWithTasks projectWithTasks2,
        @NonNull AlphabeticalSortingType alphabeticalSortingType
    ) {
        if (alphabeticalSortingType.getComparator() != null) {
            int alphabeticalComparison = alphabeticalSortingType.getComparator().compare(projectWithTasks1, projectWithTasks2);

            if (alphabeticalComparison != 0) {
                return alphabeticalComparison;
            }
        }

        return projectWithTasks1.getProject().getProject_id() - projectWithTasks2.getProject().getProject_id();
    }

    public void onDeleteTaskButtonClicked(int taskId) {
        ioExecutor.execute(() -> taskRepository.deleteTask(taskId));
    }

    public SingleLiveEvent<TaskViewAction> getViewActionSingleLiveEvent() {
        return viewActionLiveEvent;
    }

    public void onDisplaySortingButtonClicked() {
        viewActionLiveEvent.setValue(TaskViewAction.DISPLAY_SORTING_DIALOG);
    }

    @NonNull
    public LiveData<List<TaskViewStateItem>> getViewStateLiveData() {
        return mediatorLiveData;
    }

}
