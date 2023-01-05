package com.cm.projetoFinal.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.cm.projetoFinal.database.dao.ProfileDao;
import com.cm.projetoFinal.database.entities.Profile;

@Database(entities = {Profile.class}, exportSchema = false, version = 3)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    private static final String DB_NAME = "project_db";
    private static AppDatabase instance;

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, DB_NAME).fallbackToDestructiveMigration().build();
        }
        return instance;
    }

    public abstract ProfileDao profileDao();
}