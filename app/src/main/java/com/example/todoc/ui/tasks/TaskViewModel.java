package com.example.todoc.ui.tasks;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.example.todoc.data.TaskRepository;
import com.example.todoc.data.entity.Task;
import com.example.todoc.data.sorting.AlphabeticalSortingType;
import com.example.todoc.data.sorting.ChronologicalSortingType;
import com.example.todoc.data.sorting.SortingParametersRepository;

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

    @Inject
    public TaskViewModel(
        @NonNull TaskRepository taskRepository,
        @NonNull Executor ioExecutor,
        @NonNull SortingParametersRepository sortingParametersRepository
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

        if (alphabeticalSortingType == null) {
            throw new IllegalArgumentException("parameter alphabeticalSortingType should never be null because repository guarantees an initial value");
        }
        if (chronologicalSortingType == null) {
            throw new IllegalArgumentException("parameter chronologicalSortingType should never be null because repository guarantees an initial value");
        }
        if (taskList == null) {
            return;
        }

        Collections.sort(
            taskList,
            (task1, task2) -> compareTasks(task1, task2, alphabeticalSortingType, chronologicalSortingType)
        );

        List<TaskViewStateItem> taskViewStateItemList = new ArrayList<>();
        /*for (Task task : taskList) {
            taskViewStateItemList.add(
                new TaskViewStateItem(
                    task.getTask_id(),
                    allProjects.getValue().get(task.getProjectId()-1).getProject_color(),
                    task.getTask_name(),
                    allProjects.getValue().get(task.getProjectId()-1).getProject_name()
                )
            );
        }*/
        mediatorLiveData.setValue(taskViewStateItemList);
    }

    private int compareTasks(
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
