package com.streakup.app.data.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "habits")
public class Habit {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String name;
    public String emoji;
    public String colorHex;
    public int reminderHour;
    public int reminderMinute;
    public boolean reminderEnabled;
    public long createdAt;
    public int currentStreak;
    public int longestStreak;
    public long lastCompletedDate; // epoch day

    public Habit(String name, String emoji, String colorHex,
                 int reminderHour, int reminderMinute, boolean reminderEnabled) {
        this.name = name;
        this.emoji = emoji;
        this.colorHex = colorHex;
        this.reminderHour = reminderHour;
        this.reminderMinute = reminderMinute;
        this.reminderEnabled = reminderEnabled;
        this.createdAt = System.currentTimeMillis();
        this.currentStreak = 0;
        this.longestStreak = 0;
        this.lastCompletedDate = -1;
    }
}
