package com.example.todoc.ui.tasks;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.todoc.R;
import com.example.todoc.data.sorting.SortingParametersRepository;
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
        setContentView(R.layout.activity_main);


        viewBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());
        viewModel = new ViewModelProvider(this).get(TaskViewModel.class);

        TaskAdapter adapter = new TaskAdapter(new TaskListener() {
            @Override
            public void onDeleteTaskButtonClicked(int taskId) {
                viewModel.onDeleteTaskButtonClicked(taskId);
            }
        });

        viewBinding.listTasks.setAdapter(adapter);
        viewBinding.fabAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddTaskDialogFragment dialogFragment = AddTaskDialogFragment.newInstance();
                dialogFragment.show(getSupportFragmentManager().beginTransaction(), "Add Task");
            }
        });

        viewModel.getViewStateLiveData().observe(this, taskViewStateItems -> {
            adapter.submitList(taskViewStateItems);
        });
    }
    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.actions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem item) {
                SortDialogFragment dialogFragment = SortDialogFragment.newInstance();
                dialogFragment.show(getSupportFragmentManager().beginTransaction(), "Sort Diag");
                return true;
            }
        });
        return super.onOptionsItemSelected(item);
    }
}