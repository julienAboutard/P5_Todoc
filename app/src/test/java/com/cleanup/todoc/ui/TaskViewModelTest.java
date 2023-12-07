package com.cleanup.todoc.ui;

import static com.cleanup.todoc.utils.LiveDataTestUtils.getValueForTesting;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import androidx.annotation.NonNull;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import com.cleanup.todoc.utils.TestExecutor;
import com.example.todoc.data.TaskRepository;
import com.example.todoc.data.entity.Project;
import com.example.todoc.data.entity.ProjectWithTasks;
import com.example.todoc.data.entity.Task;
import com.example.todoc.data.sorting.AlphabeticalSortingType;
import com.example.todoc.data.sorting.ChronologicalSortingType;
import com.example.todoc.data.sorting.SortingParametersRepository;
import com.example.todoc.ui.tasks.TaskViewModel;
import com.example.todoc.ui.tasks.TaskViewStateItem;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

@RunWith(MockitoJUnitRunner.class)
public class TaskViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private static final String EXPECTED_PROJECT_NAME = "projectName";
    private static final int EXPECTED_PROJECT_COUNT = 3;
    private static final String EXPECTED_TASK_NAME = "taskName";
    private static final int EXPECTED_TASK_COUNT = 2;

    private final TaskRepository taskRepository = mock();
    private final SortingParametersRepository sortingParametersRepository = mock();
    private final Executor ioExecutor = Mockito.spy(new TestExecutor());

    private final MutableLiveData<List<ProjectWithTasks>> projectWithTasksMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<AlphabeticalSortingType> alphabeticalSortingTypeMutableLiveData = new MutableLiveData<>(AlphabeticalSortingType.NONE);
    private final MutableLiveData<ChronologicalSortingType> chronologicalSortingTypeMutableLiveData = new MutableLiveData<>(ChronologicalSortingType.NONE);
    private TaskViewModel viewModel;

    @Before
    public void setUp() {
        Mockito.doReturn(projectWithTasksMutableLiveData).when(taskRepository).getAllProjectWithTasks();
        Mockito.doReturn(alphabeticalSortingTypeMutableLiveData).when(sortingParametersRepository).getAlphabeticalSortingTypeLiveData();
        Mockito.doReturn(chronologicalSortingTypeMutableLiveData).when(sortingParametersRepository).getChronologicalSortingTypeLiveData();

        projectWithTasksMutableLiveData.setValue(getTestProjectsWithTasks());

        viewModel = new TaskViewModel(taskRepository, ioExecutor, sortingParametersRepository);
        Mockito.verify(taskRepository).getAllProjectWithTasks();
        Mockito.verify(sortingParametersRepository).getAlphabeticalSortingTypeLiveData();
        Mockito.verify(sortingParametersRepository).getChronologicalSortingTypeLiveData();
    }

    @Test
    public void nominal_case() {
        // When
        List<TaskViewStateItem> result = getValueForTesting(viewModel.getViewStateLiveData());

        // Then
        assertEquals(getTestTaskViewStateItems(), result);
        verify(taskRepository).getAllProjectWithTasks();
        Mockito.verifyNoMoreInteractions(taskRepository, ioExecutor, sortingParametersRepository);
    }

    @Test
    public void initial_case() {
        // Given
        projectWithTasksMutableLiveData.setValue(new ArrayList<>());

        // When
        List<TaskViewStateItem> result = getValueForTesting(viewModel.getViewStateLiveData());
        List<TaskViewStateItem> expected = new ArrayList<>();

        // Then
        assertEquals(expected, result);
    }


    @Test
    public void edge_case_with_alphabetical_AZ() {
        // Given
        alphabeticalSortingTypeMutableLiveData.setValue(AlphabeticalSortingType.AZ);

        // When
        List<TaskViewStateItem> result = getValueForTesting(viewModel.getViewStateLiveData());

        // Then
        assertEquals(getTestTaskViewStateItems(), result);
        verify(taskRepository).getAllProjectWithTasks();
        Mockito.verifyNoMoreInteractions(taskRepository, ioExecutor, sortingParametersRepository);
    }

    @Test
    public void edge_case_with_alphabetical_ZA() {
        // Given
        alphabeticalSortingTypeMutableLiveData.setValue(AlphabeticalSortingType.ZA);

        // When
        List<TaskViewStateItem> result = getValueForTesting(viewModel.getViewStateLiveData());

        // Then
        assertEquals(getTestTaskViewStateItemsSorted("ZA"), result);
        verify(taskRepository).getAllProjectWithTasks();
        Mockito.verifyNoMoreInteractions(taskRepository, ioExecutor, sortingParametersRepository);
    }

    @Test
    public void edge_case_with_chronological_inverted() {
        // Given
        chronologicalSortingTypeMutableLiveData.setValue(ChronologicalSortingType.NEWEST_FIRST);

        // When
        List<TaskViewStateItem> result = getValueForTesting(viewModel.getViewStateLiveData());

        // Then
        assertEquals(getTestTaskViewStateItemsSorted("Cinv"), result);
        verify(taskRepository).getAllProjectWithTasks();
        Mockito.verifyNoMoreInteractions(taskRepository, ioExecutor, sortingParametersRepository);
    }

    @Test
    public void edge_case_with_alphabetical_AZ_chronological_inverted() {
        // Given
        alphabeticalSortingTypeMutableLiveData.setValue(AlphabeticalSortingType.AZ);
        chronologicalSortingTypeMutableLiveData.setValue(ChronologicalSortingType.NEWEST_FIRST);

        // When
        List<TaskViewStateItem> result = getValueForTesting(viewModel.getViewStateLiveData());

        // Then
        assertEquals(getTestTaskViewStateItemsSorted("AZCinv"), result);
        verify(taskRepository).getAllProjectWithTasks();
        Mockito.verifyNoMoreInteractions(taskRepository, ioExecutor, sortingParametersRepository);
    }

    @Test
    public void sortByProjectAlphabetical_before_data_is_available() {
        // Given
        projectWithTasksMutableLiveData.setValue(null);
        alphabeticalSortingTypeMutableLiveData.setValue(AlphabeticalSortingType.ZA);

        // When
        List<TaskViewStateItem> result = getValueForTesting(viewModel.getViewStateLiveData());

        // Then
        assertNull(result);
    }

    @Test
    public void sortByProjectAlphabetical_without_data() {
        // Given
        projectWithTasksMutableLiveData.setValue(new ArrayList<>());
        alphabeticalSortingTypeMutableLiveData.setValue(AlphabeticalSortingType.ZA);

        // When
        List<TaskViewStateItem> result = getValueForTesting(viewModel.getViewStateLiveData());
        List<TaskViewStateItem> expected = new ArrayList<>();

        // Then
        assertEquals(expected, result);
    }
    @Test
    public void testOnDeleteTaskButtonClicked() {
        // Given
        int taskId = 23;

        // When
        viewModel.onDeleteTaskButtonClicked(taskId);

        // Then
        Mockito.verify(taskRepository).deleteTask(taskId);
        Mockito.verify(ioExecutor).execute(any());
        Mockito.verifyNoMoreInteractions(taskRepository, ioExecutor, sortingParametersRepository);
    }

    // region IN
    @NonNull
    private List<ProjectWithTasks> getTestProjectsWithTasks() {
        List<ProjectWithTasks> projectsWithTasks = new ArrayList<>();
        int taskId = 0;

        for (int i = 0; i < EXPECTED_PROJECT_COUNT; i++) {
            Project project = new Project(i + 1, EXPECTED_PROJECT_NAME + i, 1000 + i);
            List<Task> taskList = new ArrayList<>();

            for (int j = 0; j < EXPECTED_TASK_COUNT; j++) {
                taskId++;

                taskList.add(
                    new Task(
                        taskId,
                        i + 1,
                        EXPECTED_TASK_NAME + j,
                        10000 + taskId
                    )
                );
            }

            projectsWithTasks.add(
                new ProjectWithTasks(
                    project,
                    taskList
                )
            );
        }

        return projectsWithTasks;
    }
    // endregion IN


    // region OUT
    @NonNull
    private List<TaskViewStateItem> getTestTaskViewStateItems() {
        List<TaskViewStateItem> items = new ArrayList<>();
        int taskId = 0;

        for (int i = 0; i < EXPECTED_PROJECT_COUNT; i++) {
            for (int j = 0; j < EXPECTED_TASK_COUNT; j++) {
                taskId++;
                items.add(
                    new TaskViewStateItem(
                        taskId,
                        1000 + i,
                        EXPECTED_TASK_NAME + j,
                        EXPECTED_PROJECT_NAME + i
                    )
                );
            }
        }
        return items;
    }

    @NonNull
    private List<TaskViewStateItem> getTestTaskViewStateItemsSorted(@NonNull String type) {

        List<TaskViewStateItem> items = new ArrayList<>();
        int taskId;
        switch (type) {
            case "ZA" -> {
                for (int i = 0; i < EXPECTED_PROJECT_COUNT; i++) {
                    taskId = EXPECTED_TASK_COUNT * (EXPECTED_PROJECT_COUNT - i - 1);
                    for (int j = 0; j < EXPECTED_TASK_COUNT; j++) {
                        taskId++;
                        items.add(
                            new TaskViewStateItem(
                                taskId,
                                1000 + EXPECTED_PROJECT_COUNT - i - 1,
                                EXPECTED_TASK_NAME + j,
                                EXPECTED_PROJECT_NAME + (EXPECTED_PROJECT_COUNT - i - 1)
                            )
                        );
                    }
                }
            }
            case "Cinv" -> {
                taskId = EXPECTED_TASK_COUNT * EXPECTED_PROJECT_COUNT;
                for (int i = EXPECTED_PROJECT_COUNT; i > 0; i--) {
                    for (int j = EXPECTED_TASK_COUNT; j > 0; j--) {
                        items.add(
                            new TaskViewStateItem(
                                taskId,
                                1000 + i - 1,
                                EXPECTED_TASK_NAME + (j - 1),
                                EXPECTED_PROJECT_NAME + (i - 1)
                            )
                        );
                        taskId--;
                    }
                }
            }
            case "AZCinv" -> {
                for (int i = 0; i < EXPECTED_PROJECT_COUNT; i++) {
                    taskId = EXPECTED_TASK_COUNT * (i + 1);
                    for (int j = EXPECTED_TASK_COUNT; j > 0; j--) {
                        items.add(
                            new TaskViewStateItem(
                                taskId,
                                1000 + i,
                                EXPECTED_TASK_NAME + (j - 1),
                                EXPECTED_PROJECT_NAME + i
                            )
                        );
                        taskId--;
                    }
                }
            }
        }
        return items;
    }
    // endregion OUT
}
