package com.cleanup.todoc.ui;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import androidx.annotation.NonNull;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.cleanup.todoc.utils.TestExecutor;
import com.example.todoc.data.TaskRepository;
import com.example.todoc.data.entity.Project;
import com.example.todoc.data.entity.ProjectWithTasks;
import com.example.todoc.data.entity.Task;
import com.example.todoc.data.sorting.SortingParametersRepository;
import com.example.todoc.ui.tasks.TaskViewModel;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
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

    @Mock private TaskRepository taskRepository;
    @Mock private SortingParametersRepository sortingParametersRepository;
    private Executor ioExecutor = Mockito.spy(new TestExecutor());

    private final MutableLiveData<List<ProjectWithTasks>> projectWithTasksMutableLiveData = new MutableLiveData<>();
    private TaskViewModel viewModel;

    @Before
    public void setUp() {
        Mockito.doReturn(projectWithTasksMutableLiveData).when(taskRepository).getAllProjectWithTasks();

        projectWithTasksMutableLiveData.setValue(getTestProjectsWithTasks());

        sortingParametersRepository = new SortingParametersRepository();
        viewModel = new TaskViewModel(taskRepository, ioExecutor, sortingParametersRepository);

    }

    @Test
    public void testGetAllProjectWithTasks() {
        LiveData<List<ProjectWithTasks>> result = taskRepository.getAllProjectWithTasks();

        assertEquals(projectWithTasksMutableLiveData, result);
        verify(taskRepository).getAllProjectWithTasks();
        Mockito.verifyNoMoreInteractions(taskRepository, ioExecutor, sortingParametersRepository);
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




    @NonNull
    private List<ProjectWithTasks> getTestProjectsWithTasks() {
        List<ProjectWithTasks> projectsWithTasks = new ArrayList<>();

        for (int i = 0; i < EXPECTED_PROJECT_COUNT; i++) {
            Project project = new Project(EXPECTED_PROJECT_NAME + i, i);
            List<Task> taskList = new ArrayList<>();

            for (int j = 0; j < EXPECTED_TASK_COUNT; j++) {
                taskList.add(
                    new Task(
                        i,
                        EXPECTED_TASK_NAME + j,
                        System.currentTimeMillis()+j
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
}
