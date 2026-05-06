package com.streakup.app.data.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import com.streakup.app.data.db.AppDatabase;
import com.streakup.app.data.db.HabitDao;
import com.streakup.app.data.model.Habit;
import com.streakup.app.data.model.HabitLog;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HabitRepository {
    private final HabitDao dao;
    private final ExecutorService executor;

    public HabitRepository(Application app) {
        dao = AppDatabase.getInstance(app).habitDao();
        executor = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<Habit>> getAllHabits() {
        return dao.getAllHabits();
    }

    public void insertHabit(Habit habit, OnInsertCallback callback) {
        executor.execute(() -> {
            long id = dao.insertHabit(habit);
            if (callback != null) callback.onInserted((int) id);
        });
    }

    public void updateHabit(Habit habit) {
        executor.execute(() -> dao.updateHabit(habit));
    }

    public void deleteHabit(Habit habit) {
        executor.execute(() -> dao.deleteHabit(habit));
    }

    public void toggleHabitToday(Habit habit, Runnable onDone) {
        executor.execute(() -> {
            long today = LocalDate.now().toEpochDay();
            HabitLog existing = dao.getLogForDay(habit.id, today);
            if (existing != null) {
                // Un-check
                dao.deleteLog(existing);
                recalcStreak(habit);
            } else {
                // Check
                dao.insertLog(new HabitLog(habit.id, today, System.currentTimeMillis()));
                recalcStreak(habit);
            }
            if (onDone != null) onDone.run();
        });
    }

    public void recalcStreak(Habit habit) {
        List<Long> days = dao.getCompletedDays(habit.id);
        if (days.isEmpty()) {
            habit.currentStreak = 0;
            habit.longestStreak = 0;
            habit.lastCompletedDate = -1;
            dao.updateHabit(habit);
            return;
        }
        long today = LocalDate.now().toEpochDay();
        int current = 0;
        int longest = 0;
        int run = 1;
        for (int i = days.size() - 1; i >= 0; i--) {
            long day = days.get(i);
            if (i == days.size() - 1) {
                // Start from most recent
                if (day == today || day == today - 1) current = 1;
                run = 1;
            } else {
                long prev = days.get(i + 1);
                if (prev - day == 1) {
                    run++;
                    if (i == days.size() - 2 && current > 0) current = run;
                } else {
                    run = 1;
                }
            }
            if (run > longest) longest = run;
        }
        // Recheck current: if last day is not today or yesterday, streak is 0
        long lastDay = days.get(days.size() - 1);
        if (lastDay < today - 1) current = 0;
        habit.currentStreak = current;
        habit.longestStreak = Math.max(longest, habit.longestStreak);
        habit.lastCompletedDate = lastDay;
        dao.updateHabit(habit);
    }

    public boolean isCompletedToday(int habitId) {
        long today = LocalDate.now().toEpochDay();
        return dao.getLogForDay(habitId, today) != null;
    }

    public List<Long> getCompletedDays(int habitId) {
        return dao.getCompletedDays(habitId);
    }

    public int getTotalCompletions(int habitId) {
        return dao.getTotalCompletions(habitId);
    }

    public List<Habit> getAllHabitsSync() {
        return dao.getAllHabitsSync();
    }

    public interface OnInsertCallback {
        void onInserted(int id);
    }
}
