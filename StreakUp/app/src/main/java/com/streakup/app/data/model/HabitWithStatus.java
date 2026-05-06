package com.streakup.app.data.model;

public class HabitWithStatus {
    public Habit habit;
    public boolean completedToday;

    public HabitWithStatus(Habit habit, boolean completedToday) {
        this.habit = habit;
        this.completedToday = completedToday;
    }
}
