# StreakUp — Smart Habit Tracker

A beautiful Android app to build and track daily habits with GitHub-style heatmaps, streak counters, and daily reminders.

---

## Features

- Add habits with custom emoji and color
- One-tap daily check-in
- Streak counter with fire emoji
- Daily reminder notifications (with exact time picker)
- Stats screen with bar chart (MPAndroidChart)
- GitHub-style 16-week heatmap per habit
- Summary stats: total habits, best streak, total completions
- Long-press to delete a habit
- Room DB persistence (data survives app restarts)
- Works offline — no internet needed

---

## Setup Instructions

### Step 1 — Add JitPack for MPAndroidChart

Open `settings.gradle` and add JitPack inside `dependencyResolutionManagement`:

```gradle
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven { url 'https://jitpack.io' }   // <-- add this
    }
}
```

### Step 2 — Open in Android Studio

1. Open Android Studio
2. Click **File → Open**
3. Select the `StreakUp` folder
4. Wait for Gradle sync to complete

### Step 3 — Run

- Connect a physical device **or** start an emulator (API 26+)
- Click the green **Run** button

---

## Project Structure

```
app/src/main/java/com/streakup/app/
├── data/
│   ├── db/         AppDatabase, HabitDao
│   ├── model/      Habit, HabitLog, HabitWithStatus
│   └── repository/ HabitRepository
├── ui/
│   ├── MainActivity.java
│   ├── home/       HomeFragment, HabitViewModel
│   ├── addhabit/   AddHabitActivity, EmojiAdapter, ColorAdapter
│   └── stats/      StatsFragment
├── adapter/        HabitAdapter, HabitStatsAdapter
└── util/           HeatmapView, NotificationHelper, AlarmReceiver
```

---

## Tech Stack

| Feature | Library |
|---|---|
| Database | Room |
| UI | Material Components |
| Charts | MPAndroidChart |
| Architecture | MVVM + LiveData |
| Reminders | AlarmManager + BroadcastReceiver |
| Heatmap | Custom View (Canvas drawing) |

---

## Screens

| Screen | Description |
|---|---|
| Home | Today's habits, check-in, progress bar |
| Add Habit | Name, emoji picker, color picker, time picker |
| Stats | Summary cards, streak bar chart, heatmaps |
