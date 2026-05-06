package com.streakup.app.data.db;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.streakup.app.data.model.Habit;
import com.streakup.app.data.model.HabitLog;

@Database(entities = {Habit.class, HabitLog.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase INSTANCE;

    public abstract HabitDao habitDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(
                    context.getApplicationContext(),
                    AppDatabase.class,
                    "streakup_db"
            ).build();
        }
        return INSTANCE;
    }
}
