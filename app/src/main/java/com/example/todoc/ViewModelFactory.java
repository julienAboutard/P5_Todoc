package com.example.todoc;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.todoc.data.TaskRepository;
import com.example.todoc.data.TodocDatabase;
import com.example.todoc.ui.addtasks.AddTaskViewModel;
import com.example.todoc.ui.tasks.TaskViewModel;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ViewModelFactory implements ViewModelProvider.Factory {

    private static ViewModelFactory factory;
    private final Executor ioExecutor = Executors.newFixedThreadPool(3);

    private final TaskRepository taskRepository;


    private ViewModelFactory() {
        TodocDatabase todocDatabase = TodocDatabase.getInstance(MainApplication.getApplication(), ioExecutor);

        taskRepository= new TaskRepository(
            todocDatabase.getTaskDao(),
            todocDatabase.getProjectDao()
        );
    }
    public static ViewModelFactory getInstance() {
        if (factory == null) {
            synchronized (ViewModelFactory.class) {
                if (factory == null) {
                    factory = new ViewModelFactory();
                }
            }
        }
        return factory;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(TaskViewModel.class)) {
            return (T) new TaskViewModel(taskRepository, ioExecutor);
        } else if (modelClass.isAssignableFrom(AddTaskViewModel.class)) {
            //return (T) new AddTaskViewModel();
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}