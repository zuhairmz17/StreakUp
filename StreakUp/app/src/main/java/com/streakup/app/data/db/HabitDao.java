package com.streakup.app.data.db;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.streakup.app.data.model.Habit;
import com.streakup.app.data.model.HabitLog;
import java.util.List;

@Dao
public interface HabitDao {

    // --- Habit CRUD ---
    @Insert
    long insertHabit(Habit habit);

    @Update
    void updateHabit(Habit habit);

    @Delete
    void deleteHabit(Habit habit);

    @Query("SELECT * FROM habits ORDER BY createdAt ASC")
    LiveData<List<Habit>> getAllHabits();

    @Query("SELECT * FROM habits WHERE id = :id")
    Habit getHabitById(int id);

    @Query("SELECT * FROM habits")
    List<Habit> getAllHabitsSync();

    // --- HabitLog CRUD ---
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertLog(HabitLog log);

    @Delete
    void deleteLog(HabitLog log);

    @Query("SELECT * FROM habit_logs WHERE habitId = :habitId ORDER BY epochDay ASC")
    List<HabitLog> getLogsForHabit(int habitId);

    @Query("SELECT * FROM habit_logs WHERE habitId = :habitId AND epochDay = :epochDay LIMIT 1")
    HabitLog getLogForDay(int habitId, long epochDay);

    @Query("SELECT COUNT(*) FROM habit_logs WHERE habitId = :habitId")
    int getTotalCompletions(int habitId);

    @Query("SELECT epochDay FROM habit_logs WHERE habitId = :habitId ORDER BY epochDay ASC")
    List<Long> getCompletedDays(int habitId);
}
