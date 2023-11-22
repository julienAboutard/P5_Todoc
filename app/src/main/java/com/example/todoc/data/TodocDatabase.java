package com.example.todoc.data;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.todoc.R;
import com.example.todoc.data.dao.ProjectDao;
import com.example.todoc.data.dao.TaskDao;
import com.example.todoc.data.entity.Project;
import com.example.todoc.data.entity.Task;

import java.util.concurrent.Executor;

import javax.inject.Provider;

@Database(
    entities = {Task.class, Project.class},
    version = 1
)
public abstract class TodocDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "todoc_database";

    public abstract TaskDao getTaskDao();

    public abstract ProjectDao getProjectDao();

    public static TodocDatabase createDB(
        @NonNull Application application,
        @NonNull Executor executor,
        @NonNull Provider<ProjectDao> projectDaoProvider
    ) {
        Builder<TodocDatabase> databaseBuilder = Room.databaseBuilder(
            application,
            TodocDatabase.class,
            DATABASE_NAME
        );

        databaseBuilder.addCallback(new Callback() {
            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        projectDaoProvider.get().insert(
                            new Project(
                                application.getString(R.string.tartampionp),
                                ResourcesCompat.getColor(application.getResources(), R.color.tartampionc, null)
                            )
                        );
                    }
                });
            }
        });
        databaseBuilder.fallbackToDestructiveMigration();

        return databaseBuilder.build();
    }
}
