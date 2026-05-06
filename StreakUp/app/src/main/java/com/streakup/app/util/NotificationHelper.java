package com.streakup.app.util;

import android.app.*;
import android.content.*;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import com.streakup.app.R;
import com.streakup.app.data.model.Habit;
import java.util.Calendar;

public class NotificationHelper {
    public static final String CHANNEL_ID = "streakup_reminders";
    public static final String CHANNEL_NAME = "Habit Reminders";

    public static void createChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("Daily habit reminder notifications");
            NotificationManager nm = context.getSystemService(NotificationManager.class);
            if (nm != null) nm.createNotificationChannel(channel);
        }
    }

    public static void scheduleReminder(Context context, Habit habit) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (am == null) return;

        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra("habit_id", habit.id);
        intent.putExtra("habit_name", habit.name);
        intent.putExtra("habit_emoji", habit.emoji);

        PendingIntent pi = PendingIntent.getBroadcast(
                context, habit.id, intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, habit.reminderHour);
        cal.set(Calendar.MINUTE, habit.reminderMinute);
        cal.set(Calendar.SECOND, 0);
        if (cal.getTimeInMillis() < System.currentTimeMillis()) {
            cal.add(Calendar.DAY_OF_YEAR, 1);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pi);
        } else {
            am.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, pi);
        }
    }

    public static void cancelReminder(Context context, int habitId) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (am == null) return;
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(
                context, habitId, intent,
                PendingIntent.FLAG_NO_CREATE | PendingIntent.FLAG_IMMUTABLE);
        if (pi != null) am.cancel(pi);
    }

    public static void showNotification(Context context, int habitId, String name, String emoji) {
        createChannel(context);
        Intent tapIntent = new Intent(context, com.streakup.app.ui.MainActivity.class);
        tapIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent tapPi = PendingIntent.getActivity(
                context, habitId, tapIntent, PendingIntent.FLAG_IMMUTABLE);

        Notification notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("Time for your habit!")
                .setContentText(emoji + " " + name + " — keep your streak alive!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(tapPi)
                .setAutoCancel(true)
                .build();

        NotificationManager nm = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (nm != null) nm.notify(habitId, notification);
    }
}
