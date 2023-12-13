package com.cleanup.todoc.ui;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;

import com.cleanup.todoc.utils.TestExecutor;
import com.example.todoc.data.TaskRepository;
import com.example.todoc.data.entity.Project;
import com.example.todoc.data.entity.Task;
import com.example.todoc.ui.addtasks.AddTaskViewModel;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.concurrent.Executor;


@RunWith(MockitoJUnitRunner.class)
public class AddTaskViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock private TaskRepository taskRepository;
    private final Executor ioExecutor = Mockito.spy(new TestExecutor());

    private AddTaskViewModel viewModel;

    @Before
    public void setUp() {
        viewModel = new AddTaskViewModel(taskRepository, ioExecutor);
    }

    @Test
    public void testGetAllProject() {
        //Given
        LiveData<List<Project>> projectLiveData = Mockito.mock();
        Mockito.doReturn(projectLiveData).when(taskRepository).getAllProjects();

        //When
        LiveData<List<Project>> result = taskRepository.getAllProjects();

        //Then
        assertEquals(projectLiveData, result);
        verify(taskRepository).getAllProjects();
        Mockito.verifyNoMoreInteractions(taskRepository, ioExecutor);
    }

    @Test
    public void testAddTask() {
        //Given
        Project project = Mockito.mock();
        String taskName = "Task Test";
        long timeStamp = System.currentTimeMillis();

        //When
        viewModel.onProjectSelected(project);
        viewModel.onTaskNameChanged(taskName);
        viewModel.onAddButtonClicked(timeStamp);

        //Then
        verify(ioExecutor).execute(any());
        verify(taskRepository).addTask(new Task(project.getProjectId(), taskName, timeStamp));
        Mockito.verifyNoMoreInteractions(taskRepository, ioExecutor);
    }

}
