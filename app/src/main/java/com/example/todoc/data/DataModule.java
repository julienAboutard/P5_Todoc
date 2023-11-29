package com.example.todoc.data;

import android.app.Application;

import androidx.annotation.NonNull;

import com.example.todoc.data.dao.ProjectDao;
import com.example.todoc.data.dao.TaskDao;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Provider;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class DataModule {

    @Provides
    @Singleton
    public Executor provideExecutor() {
        return Executors.newFixedThreadPool(8);
    }

    @Provides
    @Singleton
    public TodocDatabase provideTodocDatabase(
        @NonNull Application application,
        @NonNull Executor ioExecutor,
        @NonNull Provider<ProjectDao> projectDaoProvider,
        @NonNull Provider<TaskDao> taskDaoProvider
    ) {
        return TodocDatabase.createDB(application, ioExecutor, projectDaoProvider, taskDaoProvider);
    }

    @Provides
    @Singleton
    public ProjectDao provideProjectDao(@NonNull TodocDatabase todocDatabase) {
        return todocDatabase.getProjectDao();
    }

    @Provides
    @Singleton
    public TaskDao provideTaskDao(@NonNull TodocDatabase todocDatabase) {
        return todocDatabase.getTaskDao();
    }
}
