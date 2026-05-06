package com.streakup.app.ui.home;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.streakup.app.data.model.Habit;
import com.streakup.app.data.repository.HabitRepository;
import java.util.List;

public class HabitViewModel extends AndroidViewModel {
    private final HabitRepository repo;
    public final LiveData<List<Habit>> habits;

    public HabitViewModel(Application app) {
        super(app);
        repo = new HabitRepository(app);
        habits = repo.getAllHabits();
    }

    public void insertHabit(Habit habit, HabitRepository.OnInsertCallback cb) {
        repo.insertHabit(habit, cb);
    }

    public void deleteHabit(Habit habit) {
        repo.deleteHabit(habit);
    }

    public void toggleToday(Habit habit, Runnable done) {
        repo.toggleHabitToday(habit, done);
    }

    public boolean isCompletedToday(int habitId) {
        return repo.isCompletedToday(habitId);
    }

    public List<Long> getCompletedDays(int habitId) {
        return repo.getCompletedDays(habitId);
    }

    public int getTotalCompletions(int habitId) {
        return repo.getTotalCompletions(habitId);
    }
}
