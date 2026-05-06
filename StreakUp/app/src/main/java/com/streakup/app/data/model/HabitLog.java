package com.streakup.app.data.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

@Entity(
    tableName = "habit_logs",
    primaryKeys = {"habitId", "epochDay"},
    foreignKeys = @ForeignKey(
        entity = Habit.class,
        parentColumns = "id",
        childColumns = "habitId",
        onDelete = ForeignKey.CASCADE
    ),
    indices = {@Index("habitId")}
)
public class HabitLog {
    public int habitId;
    public long epochDay;   // LocalDate.toEpochDay()
    public long completedAt; // System.currentTimeMillis()

    public HabitLog(int habitId, long epochDay, long completedAt) {
        this.habitId = habitId;
        this.epochDay = epochDay;
        this.completedAt = completedAt;
    }
}
