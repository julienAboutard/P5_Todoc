package com.cleanup.todoc.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import android.content.Context;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.cleanup.todoc.utils.LiveDataTestUtils;
import com.example.todoc.data.TodocDatabase;
import com.example.todoc.data.dao.ProjectDao;
import com.example.todoc.data.entity.Project;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class ProjectDaoTest {

    private static final int EXPECTED_PROJECT_ID_1 = 1;
    private static final String EXPECTED_PROJECT_NAME_1 = "EXPECTED_PROJECT_NAME_1";
    private static final int EXPECTED_PROJECT_COLOR_1 = 1;
    private static final int EXPECTED_PROJECT_ID_2 = 2;
    private static final String EXPECTED_PROJECT_NAME_2 = "EXPECTED_PROJECT_NAME_2";
    private static final int EXPECTED_PROJECT_COLOR_2 = 2;

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private TodocDatabase todocDatabase;
    private ProjectDao projectDao;


    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        todocDatabase = Room
            .inMemoryDatabaseBuilder(context, TodocDatabase.class)
            .build();
        projectDao = todocDatabase.getProjectDao();
    }

    @After
    public void closeDb() throws IOException {
        todocDatabase.close();
    }

    @Test
    public void insertOneProject() throws Exception {
        // Given
        Project project = new Project(EXPECTED_PROJECT_ID_1, EXPECTED_PROJECT_NAME_1, EXPECTED_PROJECT_COLOR_1);


        // When
        projectDao.insert(project);
        List<Project> results = LiveDataTestUtils.getValueForTesting(projectDao.getAllProjects());

        // Then
        assertEquals(1, results.size());
        assertEquals(EXPECTED_PROJECT_ID_1, results.get(0).getProjectId());
        assertEquals(EXPECTED_PROJECT_NAME_1, results.get(0).getProjectName());
        assertEquals(EXPECTED_PROJECT_COLOR_1, results.get(0).getProjectColor());
    }

    @Test
    public void insertTwoProjects() throws Exception {
        // Given
        Project project1 = new Project(EXPECTED_PROJECT_ID_1, EXPECTED_PROJECT_NAME_1, EXPECTED_PROJECT_COLOR_1);
        Project project2 = new Project(EXPECTED_PROJECT_ID_2, EXPECTED_PROJECT_NAME_2, EXPECTED_PROJECT_COLOR_2);


        // When
        projectDao.insert(project1);
        projectDao.insert(project2);
        List<Project> results = LiveDataTestUtils.getValueForTesting(projectDao.getAllProjects());

        // Then
        assertEquals(2, results.size());

        assertEquals(EXPECTED_PROJECT_ID_1, results.get(0).getProjectId());
        assertEquals(EXPECTED_PROJECT_NAME_1, results.get(0).getProjectName());
        assertEquals(EXPECTED_PROJECT_COLOR_1, results.get(0).getProjectColor());

        assertEquals(EXPECTED_PROJECT_ID_2, results.get(1).getProjectId());
        assertEquals(EXPECTED_PROJECT_NAME_2, results.get(1).getProjectName());
        assertEquals(EXPECTED_PROJECT_COLOR_2, results.get(1).getProjectColor());
    }

    @Test
    public void insertZeroProjectShouldReturnEmpty() {
        // When
        List<Project> results = LiveDataTestUtils.getValueForTesting(projectDao.getAllProjects());

        // Then
        assertTrue(results.isEmpty());
    }

}
