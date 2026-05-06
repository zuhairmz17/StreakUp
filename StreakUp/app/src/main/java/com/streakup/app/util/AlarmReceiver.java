package com.streakup.app.util;

import android.content.*;
import com.streakup.app.data.db.AppDatabase;
import com.streakup.app.data.model.Habit;
import java.util.List;
import java.util.concurrent.Executors;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (Intent.ACTION_BOOT_COMPLETED.equals(action)) {
            // Re-schedule all reminders after reboot
            Executors.newSingleThreadExecutor().execute(() -> {
                List<Habit> habits = AppDatabase.getInstance(context).habitDao().getAllHabitsSync();
                for (Habit h : habits) {
                    if (h.reminderEnabled) {
                        NotificationHelper.scheduleReminder(context, h);
                    }
                }
            });
            return;
        }

        int habitId = intent.getIntExtra("habit_id", -1);
        String name = intent.getStringExtra("habit_name");
        String emoji = intent.getStringExtra("habit_emoji");

        if (habitId != -1 && name != null) {
            NotificationHelper.showNotification(context, habitId, name, emoji != null ? emoji : "⭐");

            // Re-schedule for next day (for exact alarm API)
            Executors.newSingleThreadExecutor().execute(() -> {
                Habit h = AppDatabase.getInstance(context).habitDao().getHabitById(habitId);
                if (h != null && h.reminderEnabled) {
                    NotificationHelper.scheduleReminder(context, h);
                }
            });
        }
    }
}
