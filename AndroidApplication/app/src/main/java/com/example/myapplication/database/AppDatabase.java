package com.example.myapplication.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.myapplication.dao.TodoDAO;
import com.example.myapplication.entity.TodoItem;

@Database(entities = {TodoItem.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract TodoDAO todoDAO();
    private static AppDatabase databaseInstance;
    private static final String DATABASE_NAME = "newDB";

    public static synchronized AppDatabase getInstance(Context context) {
        if(databaseInstance == null) {
            databaseInstance = Room
                    .databaseBuilder(context.getApplicationContext(), AppDatabase.class, DATABASE_NAME)
                    .build();
        }
        return databaseInstance;
    }

}



