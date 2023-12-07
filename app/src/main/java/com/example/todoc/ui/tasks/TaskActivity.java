package com.example.todoc.ui.tasks;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.todoc.R;
import com.example.todoc.databinding.ActivityMainBinding;
import com.example.todoc.ui.addtasks.AddTaskDialogFragment;
import com.example.todoc.ui.tasks.sort.SortDialogFragment;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class TaskActivity extends AppCompatActivity {

    private TaskViewModel viewModel;
    private ActivityMainBinding viewBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());
        viewModel = new ViewModelProvider(this).get(TaskViewModel.class);

        TaskAdapter adapter = new TaskAdapter(taskId -> viewModel.onDeleteTaskButtonClicked(taskId));

        viewBinding.listTasks.setLayoutManager(new LinearLayoutManager(this));
        viewBinding.listTasks.setAdapter(adapter);
        viewBinding.fabAddTask.setOnClickListener(v -> {
            AddTaskDialogFragment dialogFragment = AddTaskDialogFragment.newInstance();
            dialogFragment.show(getSupportFragmentManager().beginTransaction(), "Add Task");
        });

        viewModel.getViewStateLiveData().observe(this, taskViewStateItems -> {
            if (!taskViewStateItems.isEmpty()) {
                viewBinding.lblNoTask.setVisibility(View.INVISIBLE);
            }
            adapter.submitList(taskViewStateItems);
        });

        initSortingDialog();
    }
    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.actions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_filter) {
            viewModel.onDisplaySortingButtonClicked();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initSortingDialog() {
        viewModel.getViewActionSingleLiveEvent().observe(this, viewAction -> {
            if (viewAction == TaskViewAction.DISPLAY_SORTING_DIALOG) {
                SortDialogFragment.newInstance().show(getSupportFragmentManager(), null);
            }
        });
    }
}