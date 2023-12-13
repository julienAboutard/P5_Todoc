package com.cleanup.todoc.data;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;

import androidx.lifecycle.LiveData;

import com.example.todoc.data.TaskRepository;
import com.example.todoc.data.dao.ProjectDao;
import com.example.todoc.data.dao.TaskDao;
import com.example.todoc.data.entity.Project;
import com.example.todoc.data.entity.ProjectWithTasks;
import com.example.todoc.data.entity.Task;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class TaskRepositoryTest {

    @Mock private TaskDao taskDao;
    @Mock private ProjectDao projectDao;

    private TaskRepository taskRepository;

    @Before
    public void setUp() {
        taskRepository = new TaskRepository(taskDao, projectDao);
    }

    @Test
    public void testGetAllProjects() {
        //Given
        LiveData<List<Project>> projectsLiveData = Mockito.mock();
        Mockito.doReturn(projectsLiveData).when(projectDao).getAllProjects();

        //When
        LiveData<List<Project>> result = taskRepository.getAllProjects();

        //Then
        assertEquals(projectsLiveData, result);
        verify(projectDao).getAllProjects();
        Mockito.verifyNoMoreInteractions(taskDao, projectDao);
    }

    @Test
    public void testGetAllProjectWithTasks() {
        //Given
        LiveData<List<ProjectWithTasks>> projectsWithTasksLiveData = Mockito.mock();
        Mockito.doReturn(projectsWithTasksLiveData).when(taskDao).getAllProjectWithTasks();

        //When
        LiveData<List<ProjectWithTasks>> result = taskRepository.getAllProjectWithTasks();

        //Then
        assertEquals(projectsWithTasksLiveData, result);
        verify(taskDao).getAllProjectWithTasks();
        Mockito.verifyNoMoreInteractions(taskDao, projectDao);
    }

    @Test
    public void  testGetAllTasks() {
        //Given
        LiveData<List<Task>> tasksLiveData = Mockito.mock();
        Mockito.doReturn(tasksLiveData).when(taskDao).getAllTasks();

        //When
        LiveData<List<Task>> result = taskRepository.getAllTasks();

        //Then
        assertEquals(tasksLiveData, result);
        verify(taskDao).getAllTasks();
        Mockito.verifyNoMoreInteractions(taskDao, projectDao);
    }

    @Test
    public void testAddTask() {
        //Given
        Task task = Mockito.mock();

        //When
        taskRepository.addTask(task);

        //Then
        verify(taskDao).insert(task);
        Mockito.verifyNoMoreInteractions(taskDao, projectDao);
    }

    @Test
    public void testDeleteTask() {
        //Given
        int taskId = 23;

        //When
        taskRepository.deleteTask(taskId);

        //Then
        verify(taskDao).delete(taskId);
        Mockito.verifyNoMoreInteractions(taskDao, projectDao);
    }
}
