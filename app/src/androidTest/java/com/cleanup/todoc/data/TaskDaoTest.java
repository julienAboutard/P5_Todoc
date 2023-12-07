package com.cleanup.todoc.data;

import static org.junit.Assert.assertEquals;

import android.content.Context;
import android.database.sqlite.SQLiteException;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.cleanup.todoc.utils.LiveDataTestUtils;
import com.example.todoc.data.TodocDatabase;
import com.example.todoc.data.dao.TaskDao;
import com.example.todoc.data.entity.Project;
import com.example.todoc.data.entity.ProjectWithTasks;
import com.example.todoc.data.entity.Task;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class TaskDaoTest {

    private static final int EXPECTED_PROJECT_ID_1 = 1;
    private static final String EXPECTED_PROJECT_NAME_1 = "EXPECTED_PROJECT_NAME_1";
    private static final int EXPECTED_PROJECT_COLOR_1 = 1;

    private static final int EXPECTED_PROJECT_ID_2 = 2;
    private static final String EXPECTED_PROJECT_NAME_2 = "EXPECTED_PROJECT_NAME_2";
    private static final int EXPECTED_PROJECT_COLOR_2 = 2;

    private static final int EXPECTED_TASK_ID_1 = 1;
    private static final int EXPECTED_TASK_PROJECT_ID_1 = EXPECTED_PROJECT_ID_1;
    private static final String EXPECTED_TASK_NAME_1 = "EXPECTED_TASK_NAME_1";
    private static final long EXPECTED_TASK_TIMESTAMP_1 = 600;

    private static final int EXPECTED_TASK_ID_2 = 2;
    private static final int EXPECTED_TASK_PROJECT_ID_2 = EXPECTED_PROJECT_ID_1;
    private static final String EXPECTED_TASK_NAME_2 = "EXPECTED_TASK_NAME_2";
    private static final long EXPECTED_TASK_TIMESTAMP_2 = 3600;

    private static final int EXPECTED_TASK_ID_3 = 3;
    private static final int EXPECTED_TASK_PROJECT_ID_3 = EXPECTED_PROJECT_ID_2;
    private static final String EXPECTED_TASK_NAME_3 = "EXPECTED_TASK_NAME_3";
    private static final long EXPECTED_TASK_TIMESTAMP_3 = 400;

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private TodocDatabase todocDatabase;
    private TaskDao taskDao;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        todocDatabase = Room
            .inMemoryDatabaseBuilder(context, TodocDatabase.class)
            .build();
        taskDao = todocDatabase.getTaskDao();

        todocDatabase.getProjectDao().insert(createFirstProject());
    }

    @After
    public void closeDb() throws IOException {
        todocDatabase.close();
    }

    @Test
    public void insertOneTask() throws Exception {
        // When
        taskDao.insert(createFirstTask());
        List<Task> resultsTasksList = LiveDataTestUtils.getValueForTesting(taskDao.getAllTasks());
        List<ProjectWithTasks> resultsProjectWithTasksList = LiveDataTestUtils.getValueForTesting(taskDao.getAllProjectWithTasks());

        // Then
        assertEquals(1, resultsTasksList.size());
        assertTask(resultsTasksList, 0, 1);
        assertEquals(1, resultsProjectWithTasksList.size());
        assertEquals(1, resultsProjectWithTasksList.get(0).getTask().size());
        assertProject(resultsProjectWithTasksList.get(0).getProject(), 1);
        assertTask(resultsProjectWithTasksList.get(0).getTask(), 0, 1);

    }

    @Test
    public void insertTwoTasks() throws Exception {
        // When
        taskDao.insert(createFirstTask());
        taskDao.insert(createSecondTask());
        List<Task> resultsTasksList = LiveDataTestUtils.getValueForTesting(taskDao.getAllTasks());
        List<ProjectWithTasks> resultsProjectWithTasksList = LiveDataTestUtils.getValueForTesting(taskDao.getAllProjectWithTasks());

        // Then
        assertEquals(2, resultsTasksList.size());
        assertTask(resultsTasksList, 0, 1);
        assertTask(resultsTasksList, 1, 2);
        assertEquals(1, resultsProjectWithTasksList.size());
        assertEquals(2, resultsProjectWithTasksList.get(0).getTask().size());
        assertProject(resultsProjectWithTasksList.get(0).getProject(), 1);
        assertTask(resultsProjectWithTasksList.get(0).getTask(), 0, 1);
        assertTask(resultsProjectWithTasksList.get(0).getTask(), 1, 2);

    }

    @Test
    public void insertThreeTasksTwoProjects() throws Exception {
        // When
        todocDatabase.getProjectDao().insert(createSecondProject());
        taskDao.insert(createFirstTask());
        taskDao.insert(createSecondTask());
        taskDao.insert(createThridTask());
        List<Task> resultsTasksList = LiveDataTestUtils.getValueForTesting(taskDao.getAllTasks());
        List<ProjectWithTasks> resultsProjectWithTasksList = LiveDataTestUtils.getValueForTesting(taskDao.getAllProjectWithTasks());

        // Then
        assertEquals(3, resultsTasksList.size());
        assertTask(resultsTasksList, 0, 1);
        assertTask(resultsTasksList, 1, 2);
        assertTask(resultsTasksList, 2, 3);
        assertEquals(2, resultsProjectWithTasksList.size());
        assertEquals(2, resultsProjectWithTasksList.get(0).getTask().size());
        assertProject(resultsProjectWithTasksList.get(0).getProject(), 1);
        assertTask(resultsProjectWithTasksList.get(0).getTask(), 0, 1);
        assertTask(resultsProjectWithTasksList.get(0).getTask(), 1, 2);
        assertEquals(1, resultsProjectWithTasksList.get(1).getTask().size());
        assertProject(resultsProjectWithTasksList.get(1).getProject(), 2);
        assertTask(resultsProjectWithTasksList.get(1).getTask(), 0, 3);

    }
    @Test
    public void deleteTask() throws Exception{
        // Given
        Task task = new Task(EXPECTED_TASK_PROJECT_ID_1, EXPECTED_TASK_NAME_1, EXPECTED_TASK_TIMESTAMP_1);

        // When
        taskDao.insert(task);
        taskDao.delete(1);
        List<Task> result = LiveDataTestUtils.getValueForTesting(taskDao.getAllTasks());

        // Then
        assertEquals(0, result.size());
    }
    @Test(expected = SQLiteException.class)
    public void throwExceptionBadForeignKey() throws Exception{
        Task task = new Task(3, EXPECTED_TASK_NAME_1, EXPECTED_TASK_TIMESTAMP_1);

        taskDao.insert(task);
    }

    private Project createFirstProject() {
        return new Project(EXPECTED_PROJECT_ID_1, EXPECTED_PROJECT_NAME_1, EXPECTED_PROJECT_COLOR_1);
    }

    private Project createSecondProject() {
        return new Project(EXPECTED_PROJECT_ID_2, EXPECTED_PROJECT_NAME_2, EXPECTED_PROJECT_COLOR_2);
    }

    private void assertProject(Project project, int projectNumber) {

        switch (projectNumber) {
            case 1 -> {
                assertEquals(EXPECTED_PROJECT_ID_1, project.getProjectId());
                assertEquals(EXPECTED_PROJECT_NAME_1, project.getProjectName());
                assertEquals(EXPECTED_PROJECT_COLOR_1, project.getProjectColor());
            }
            case 2 -> {
                assertEquals(EXPECTED_PROJECT_ID_2, project.getProjectId());
                assertEquals(EXPECTED_PROJECT_NAME_2, project.getProjectName());
                assertEquals(EXPECTED_PROJECT_COLOR_2, project.getProjectColor());
            }
        }

    }
    private Task createFirstTask() {
        return new Task(EXPECTED_TASK_ID_1, EXPECTED_TASK_PROJECT_ID_1, EXPECTED_TASK_NAME_1, EXPECTED_TASK_TIMESTAMP_1);
    }

    private Task createSecondTask() {
        return new Task(EXPECTED_TASK_ID_2, EXPECTED_TASK_PROJECT_ID_2, EXPECTED_TASK_NAME_2, EXPECTED_TASK_TIMESTAMP_2);
    }

    private Task createThridTask() {
        return new Task(EXPECTED_TASK_ID_3, EXPECTED_TASK_PROJECT_ID_3, EXPECTED_TASK_NAME_3, EXPECTED_TASK_TIMESTAMP_3);
    }

    private void assertTask(List<Task> taskList, int position, int taskNumber) {

        switch (taskNumber) {
            case 1 -> {
                assertEquals(EXPECTED_TASK_ID_1, taskList.get(position).getTaskId());
                assertEquals(EXPECTED_TASK_PROJECT_ID_1, taskList.get(position).getProjectId());
                assertEquals(EXPECTED_TASK_NAME_1, taskList.get(position).getTaskName());
                assertEquals(EXPECTED_TASK_TIMESTAMP_1, taskList.get(position).getTaskTimeStamp());
            }
            case 2 -> {
                assertEquals(EXPECTED_TASK_ID_2, taskList.get(position).getTaskId());
                assertEquals(EXPECTED_TASK_PROJECT_ID_2, taskList.get(position).getProjectId());
                assertEquals(EXPECTED_TASK_NAME_2, taskList.get(position).getTaskName());
                assertEquals(EXPECTED_TASK_TIMESTAMP_2, taskList.get(position).getTaskTimeStamp());
            }
            case 3 -> {
                assertEquals(EXPECTED_TASK_ID_3, taskList.get(position).getTaskId());
                assertEquals(EXPECTED_TASK_PROJECT_ID_3, taskList.get(position).getProjectId());
                assertEquals(EXPECTED_TASK_NAME_3, taskList.get(position).getTaskName());
                assertEquals(EXPECTED_TASK_TIMESTAMP_3, taskList.get(position).getTaskTimeStamp());
            }
        }
    }

}
