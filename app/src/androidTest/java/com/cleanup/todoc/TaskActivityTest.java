package com.cleanup.todoc;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.Matchers.allOf;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.matcher.RootMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.cleanup.todoc.utils.MyViewAction;
import com.cleanup.todoc.utils.RecyclerViewItemAssertion;
import com.cleanup.todoc.utils.RecyclerViewItemCountAssertion;
import com.example.todoc.R;
import com.example.todoc.ui.tasks.TaskActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class TaskActivityTest {

    private static final String EXPECTED_TASK_NAME_1 = "EXPECTED_TASK_NAME_1";

    private static final String EXPECTED_TASK_NAME_2 = "EXPECTED_TASK_NAME_2";

    private static final String EXPECTED_TASK_NAME_3 = "EXPECTED_TASK_NAME_3";

    private TaskActivity taskTest;

    @Before
    public void setUp() {
        ActivityScenario<TaskActivity> activityScenario = ActivityScenario.launch(TaskActivity.class);
        activityScenario.onActivity(activity -> taskTest = activity);
    }

    @After
    public void tearDown() {
        taskTest = null;
    }

    @Test
    public void addDeleteSortTask() throws InterruptedException{

        //Add Task
        addTask(EXPECTED_TASK_NAME_1, ProjectTest.TARTAMPION);

        assertItemInRecyclerView(0, EXPECTED_TASK_NAME_1, ProjectTest.TARTAMPION);
        onView(withId(R.id.list_tasks)).check(new RecyclerViewItemCountAssertion(1));

        //Delete Task
        deleteTask(0);
        onView(withId(R.id.list_tasks)).check(new RecyclerViewItemCountAssertion(0));

        //Add multiple Tasks
        addTask(EXPECTED_TASK_NAME_1, ProjectTest.TARTAMPION);
        addTask(EXPECTED_TASK_NAME_2, ProjectTest.CIRCUS);
        addTask(EXPECTED_TASK_NAME_3, ProjectTest.LUCIDIA);

        assertItemInRecyclerView(0, EXPECTED_TASK_NAME_1, ProjectTest.TARTAMPION);
        assertItemInRecyclerView(1, EXPECTED_TASK_NAME_2, ProjectTest.CIRCUS);
        assertItemInRecyclerView(2, EXPECTED_TASK_NAME_3, ProjectTest.LUCIDIA);

        onView(withId(R.id.list_tasks)).check(new RecyclerViewItemCountAssertion(3));

        //Delete one Task that not the first
        deleteTask(2);
        onView(withId(R.id.list_tasks)).check(new RecyclerViewItemCountAssertion(2));

        //Delete all tasks
        deleteAllTask();
        onView(withId(R.id.list_tasks)).check(new RecyclerViewItemCountAssertion(0));

        //Sort 3 tasks and 3 projects, 1 task in each project
        addTask(EXPECTED_TASK_NAME_1, ProjectTest.TARTAMPION);
        addTask(EXPECTED_TASK_NAME_2, ProjectTest.CIRCUS);
        addTask(EXPECTED_TASK_NAME_3, ProjectTest.LUCIDIA);

        //Sort project by alphabetic
        onView(withId(R.id.action_filter)).perform(click());
        onView(withId(R.id.sorting_dialog_ll_alphabetical)).perform(click());
        onView(withId(R.id.sorting_dialog_tv_alphabetical)).check(matches(withText(R.string.sorting_alphabetic_sorted)));
        onView(withId(R.id.sorting_dialog_tv_chronological)).check(matches(withText(R.string.sorting_chronological_none)));
        pressBack();

        assertItemInRecyclerView(2, EXPECTED_TASK_NAME_1, ProjectTest.TARTAMPION);
        assertItemInRecyclerView(0, EXPECTED_TASK_NAME_2, ProjectTest.CIRCUS);
        assertItemInRecyclerView(1, EXPECTED_TASK_NAME_3, ProjectTest.LUCIDIA);

        //Sort project by alphabetic reversed
        onView(withId(R.id.action_filter)).perform(click());
        onView(withId(R.id.sorting_dialog_ll_alphabetical)).perform(click());
        onView(withId(R.id.sorting_dialog_tv_alphabetical)).check(matches(withText(R.string.sorting_alphabetic_inverted_sorted)));
        onView(withId(R.id.sorting_dialog_tv_chronological)).check(matches(withText(R.string.sorting_chronological_none)));
        pressBack();

        assertItemInRecyclerView(0, EXPECTED_TASK_NAME_1, ProjectTest.TARTAMPION);
        assertItemInRecyclerView(2, EXPECTED_TASK_NAME_2, ProjectTest.CIRCUS);
        assertItemInRecyclerView(1, EXPECTED_TASK_NAME_3, ProjectTest.LUCIDIA);

        //Back to initial state
        onView(withId(R.id.action_filter)).perform(click());
        onView(withId(R.id.sorting_dialog_ll_alphabetical)).perform(click());
        onView(withId(R.id.sorting_dialog_tv_alphabetical)).check(matches(withText(R.string.sorting_alphabetic_none)));
        onView(withId(R.id.sorting_dialog_tv_chronological)).check(matches(withText(R.string.sorting_chronological_none)));
        pressBack();

        assertItemInRecyclerView(0, EXPECTED_TASK_NAME_1, ProjectTest.TARTAMPION);
        assertItemInRecyclerView(1, EXPECTED_TASK_NAME_2, ProjectTest.CIRCUS);
        assertItemInRecyclerView(2, EXPECTED_TASK_NAME_3, ProjectTest.LUCIDIA);

        //Sort tasks by chronological
        onView(withId(R.id.action_filter)).perform(click());
        onView(withId(R.id.sorting_dialog_ll_chronological)).perform(click());
        onView(withId(R.id.sorting_dialog_tv_alphabetical)).check(matches(withText(R.string.sorting_alphabetic_none)));
        onView(withId(R.id.sorting_dialog_tv_chronological)).check(matches(withText(R.string.sorting_chronological_sorted)));
        pressBack();

        assertItemInRecyclerView(0, EXPECTED_TASK_NAME_1, ProjectTest.TARTAMPION);
        assertItemInRecyclerView(1, EXPECTED_TASK_NAME_2, ProjectTest.CIRCUS);
        assertItemInRecyclerView(2, EXPECTED_TASK_NAME_3, ProjectTest.LUCIDIA);

        //Sort tasks by chronological reversed
        onView(withId(R.id.action_filter)).perform(click());
        onView(withId(R.id.sorting_dialog_ll_chronological)).perform(click());
        onView(withId(R.id.sorting_dialog_tv_alphabetical)).check(matches(withText(R.string.sorting_alphabetic_none)));
        onView(withId(R.id.sorting_dialog_tv_chronological)).check(matches(withText(R.string.sorting_chronological_inverted_sorted)));
        pressBack();

        assertItemInRecyclerView(2, EXPECTED_TASK_NAME_1, ProjectTest.TARTAMPION);
        assertItemInRecyclerView(1, EXPECTED_TASK_NAME_2, ProjectTest.CIRCUS);
        assertItemInRecyclerView(0, EXPECTED_TASK_NAME_3, ProjectTest.LUCIDIA);

        //Back to initial state
        onView(withId(R.id.action_filter)).perform(click());
        onView(withId(R.id.sorting_dialog_ll_chronological)).perform(click());
        onView(withId(R.id.sorting_dialog_tv_alphabetical)).check(matches(withText(R.string.sorting_alphabetic_none)));
        onView(withId(R.id.sorting_dialog_tv_chronological)).check(matches(withText(R.string.sorting_chronological_none)));
        pressBack();

        assertItemInRecyclerView(0, EXPECTED_TASK_NAME_1, ProjectTest.TARTAMPION);
        assertItemInRecyclerView(1, EXPECTED_TASK_NAME_2, ProjectTest.CIRCUS);
        assertItemInRecyclerView(2, EXPECTED_TASK_NAME_3, ProjectTest.LUCIDIA);

        //Sort 4 tasks and 3 projects, 2 projects with only 1 task and 1 project with 2 tasks
        addTask(EXPECTED_TASK_NAME_3, ProjectTest.TARTAMPION);

        //Sort tasks by chronological
        onView(withId(R.id.action_filter)).perform(click());
        onView(withId(R.id.sorting_dialog_ll_chronological)).perform(click());
        onView(withId(R.id.sorting_dialog_tv_alphabetical)).check(matches(withText(R.string.sorting_alphabetic_none)));
        onView(withId(R.id.sorting_dialog_tv_chronological)).check(matches(withText(R.string.sorting_chronological_sorted)));
        pressBack();

        assertItemInRecyclerView(0, EXPECTED_TASK_NAME_1, ProjectTest.TARTAMPION);
        assertItemInRecyclerView(1, EXPECTED_TASK_NAME_2, ProjectTest.CIRCUS);
        assertItemInRecyclerView(2, EXPECTED_TASK_NAME_3, ProjectTest.LUCIDIA);
        assertItemInRecyclerView(3, EXPECTED_TASK_NAME_3, ProjectTest.TARTAMPION);

        //Sort projects by alphabetical and tasks by chronological
        onView(withId(R.id.action_filter)).perform(click());
        onView(withId(R.id.sorting_dialog_ll_alphabetical)).perform(click());
        onView(withId(R.id.sorting_dialog_tv_alphabetical)).check(matches(withText(R.string.sorting_alphabetic_sorted)));
        onView(withId(R.id.sorting_dialog_tv_chronological)).check(matches(withText(R.string.sorting_chronological_sorted)));
        pressBack();

        assertItemInRecyclerView(2, EXPECTED_TASK_NAME_1, ProjectTest.TARTAMPION);
        assertItemInRecyclerView(0, EXPECTED_TASK_NAME_2, ProjectTest.CIRCUS);
        assertItemInRecyclerView(1, EXPECTED_TASK_NAME_3, ProjectTest.LUCIDIA);
        assertItemInRecyclerView(3, EXPECTED_TASK_NAME_3, ProjectTest.TARTAMPION);

        //Sort projects by alphabetical and tasks by chronological reversed
        onView(withId(R.id.action_filter)).perform(click());
        onView(withId(R.id.sorting_dialog_ll_chronological)).perform(click());
        onView(withId(R.id.sorting_dialog_tv_alphabetical)).check(matches(withText(R.string.sorting_alphabetic_sorted)));
        onView(withId(R.id.sorting_dialog_tv_chronological)).check(matches(withText(R.string.sorting_chronological_inverted_sorted)));
        pressBack();

        assertItemInRecyclerView(3, EXPECTED_TASK_NAME_1, ProjectTest.TARTAMPION);
        assertItemInRecyclerView(0, EXPECTED_TASK_NAME_2, ProjectTest.CIRCUS);
        assertItemInRecyclerView(1, EXPECTED_TASK_NAME_3, ProjectTest.LUCIDIA);
        assertItemInRecyclerView(2, EXPECTED_TASK_NAME_3, ProjectTest.TARTAMPION);

        //Sort projects by alphabetical reversed and tasks by chronological reversed
        onView(withId(R.id.action_filter)).perform(click());
        onView(withId(R.id.sorting_dialog_ll_alphabetical)).perform(click());
        onView(withId(R.id.sorting_dialog_tv_alphabetical)).check(matches(withText(R.string.sorting_alphabetic_inverted_sorted)));
        onView(withId(R.id.sorting_dialog_tv_chronological)).check(matches(withText(R.string.sorting_chronological_inverted_sorted)));
        pressBack();

        assertItemInRecyclerView(1, EXPECTED_TASK_NAME_1, ProjectTest.TARTAMPION);
        assertItemInRecyclerView(3, EXPECTED_TASK_NAME_2, ProjectTest.CIRCUS);
        assertItemInRecyclerView(2, EXPECTED_TASK_NAME_3, ProjectTest.LUCIDIA);
        assertItemInRecyclerView(0, EXPECTED_TASK_NAME_3, ProjectTest.TARTAMPION);

        //Sort projects by alphabetical reversed
        onView(withId(R.id.action_filter)).perform(click());
        onView(withId(R.id.sorting_dialog_ll_chronological)).perform(click());
        onView(withId(R.id.sorting_dialog_tv_alphabetical)).check(matches(withText(R.string.sorting_alphabetic_inverted_sorted)));
        onView(withId(R.id.sorting_dialog_tv_chronological)).check(matches(withText(R.string.sorting_chronological_none)));
        pressBack();

        assertItemInRecyclerView(0, EXPECTED_TASK_NAME_1, ProjectTest.TARTAMPION);
        assertItemInRecyclerView(3, EXPECTED_TASK_NAME_2, ProjectTest.CIRCUS);
        assertItemInRecyclerView(2, EXPECTED_TASK_NAME_3, ProjectTest.LUCIDIA);
        assertItemInRecyclerView(1, EXPECTED_TASK_NAME_3, ProjectTest.TARTAMPION);

        //Back to initial State
        onView(withId(R.id.action_filter)).perform(click());
        onView(withId(R.id.sorting_dialog_ll_alphabetical)).perform(click());
        onView(withId(R.id.sorting_dialog_tv_alphabetical)).check(matches(withText(R.string.sorting_alphabetic_none)));
        onView(withId(R.id.sorting_dialog_tv_chronological)).check(matches(withText(R.string.sorting_chronological_none)));
        pressBack();

        assertItemInRecyclerView(0, EXPECTED_TASK_NAME_1, ProjectTest.TARTAMPION);
        assertItemInRecyclerView(1, EXPECTED_TASK_NAME_2, ProjectTest.CIRCUS);
        assertItemInRecyclerView(2, EXPECTED_TASK_NAME_3, ProjectTest.LUCIDIA);
        assertItemInRecyclerView(3, EXPECTED_TASK_NAME_3, ProjectTest.TARTAMPION);
    }

    private void addTask(
        @NonNull final String taskName,
        @NonNull final ProjectTest projectTest
    ) {
        onView(withId(R.id.fab_add_task)).perform(click());

        onView(withId(R.id.txt_task_name)).perform(replaceText(taskName), closeSoftKeyboard());
        onView(withId(R.id.project_spinner)).perform(click());
        onView(allOf(withId(R.id.spinner_project_name), withText(projectTest.projectName))).inRoot(RootMatchers.isPlatformPopup()).perform(scrollTo(), click());

        onView(withId(R.id.button_ok)).perform(click());
    }

    private void deleteTask(int position) throws InterruptedException{
        onView(withId(R.id.list_tasks)).perform(actionOnItemAtPosition(position, MyViewAction.clickChildViewWithId(R.id.img_delete)));
        Thread.sleep(100);
    }

    private void deleteAllTask() throws InterruptedException{
        do {
            onView(withId(R.id.list_tasks)).perform(actionOnItemAtPosition(0, MyViewAction.clickChildViewWithId(R.id.img_delete)));
            Thread.sleep(100);
        }
        while (RecyclerViewItemCountAssertion.getCountFromRecyclerView(R.id.list_tasks) != 0) ;
    }

    private void assertItemInRecyclerView(
        int positionOnRecyclerView,
        @NonNull String taskName,
        @NonNull ProjectTest projectTest
    ) {
        onView(withId(R.id.list_tasks)).check(
            new RecyclerViewItemAssertion(
                positionOnRecyclerView,
                R.id.lbl_task_name,
                withText(taskName)
            )
        );
        onView(withId(R.id.list_tasks)).check(
            new RecyclerViewItemAssertion(
                positionOnRecyclerView,
                R.id.lbl_project_name,
                withText(projectTest.projectName)
            )
        );
    }

    private enum ProjectTest {
        TARTAMPION(0, R.string.tartampionp, R.color.tartampionc),
        CIRCUS(1, R.string.circusp, R.color.circusc),
        LUCIDIA(2, R.string.lucdiap, R.color.lucidac);


        private final int projectName;

        ProjectTest(int spinnerIndex, @StringRes int projectName, @ColorRes int projectColor) {
            this.projectName = projectName;
        }
    }
}
