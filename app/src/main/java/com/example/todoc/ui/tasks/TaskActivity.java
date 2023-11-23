package com.example.todoc.ui.tasks;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import com.example.todoc.R;
import com.example.todoc.databinding.ActivityMainBinding;

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

        TaskAdapter adapter = new TaskAdapter(taskId -> viewModel.onDeleteTaskButtonClicked(taskId));

        viewBinding.listTasks.setAdapter(adapter);
        viewBinding.fabAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddTaskDialog();
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


    private void showAddTaskDialog() {

    }

}