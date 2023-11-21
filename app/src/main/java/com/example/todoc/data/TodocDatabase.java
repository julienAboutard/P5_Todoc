package com.example.todoc.data;

import android.content.Context;
import android.os.AsyncTask;

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

@Database(
    entities = {Task.class, Project.class},
    version = 1
)
public abstract class TodocDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "todoc_database";

    private static TodocDatabase instance;

    public abstract TaskDao getTaskDao();

    public abstract ProjectDao getProjectDao();

    public static synchronized TodocDatabase getInstance(
        @NonNull Context context,
        @NonNull Executor executor
    ) {
        if (instance == null) {
            instance = createDB(context, executor);
        }
        return instance;
    }

    public static synchronized TodocDatabase createDB(
        @NonNull Context context,
        @NonNull Executor executor
    ) {
        Builder<TodocDatabase> databaseBuilder = Room.databaseBuilder(
            context.getApplicationContext(),
            TodocDatabase.class,
            DATABASE_NAME
        );

        databaseBuilder.addCallback(new Callback() {
            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        ProjectDao projectDao = TodocDatabase.instance.getProjectDao();

                        projectDao.insert(
                            new Project(
                                context.getString(R.string.tartampionp),
                                ResourcesCompat.getColor(context.getResources(), R.color.tartampionc, null)
                            )
                        );
                    }
                });
            }
        });
    }
    private static RoomDatabase.Callback roomCallBack = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
        }
    };




}
