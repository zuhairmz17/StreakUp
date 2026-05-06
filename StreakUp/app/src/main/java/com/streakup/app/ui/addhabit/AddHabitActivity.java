package com.streakup.app.ui.addhabit;

import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.streakup.app.R;
import com.streakup.app.data.model.Habit;
import com.streakup.app.databinding.ActivityAddHabitBinding;
import com.streakup.app.ui.home.HabitViewModel;
import com.streakup.app.util.NotificationHelper;

public class AddHabitActivity extends AppCompatActivity {
    private ActivityAddHabitBinding binding;
    private HabitViewModel viewModel;
    private String selectedColor = "#6C63FF";
    private String selectedEmoji = "⭐";
    private int reminderHour = 8;
    private int reminderMinute = 0;

    private static final String[] EMOJIS = {
            "⭐", "💪", "📚", "🧘", "🏃", "💧", "🎯", "🌱",
            "🎨", "🍎", "😴", "🧠", "✏️", "🎵", "🌞", "❤️"
    };

    private static final String[] COLORS = {
            "#6C63FF", "#FF6584", "#43A047", "#FB8C00",
            "#00ACC1", "#E53935", "#8E24AA", "#3949AB"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddHabitBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        viewModel = new ViewModelProvider(this).get(HabitViewModel.class);

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("New Habit");
        }

        setupEmojiGrid();
        setupColorGrid();
        setupTimePicker();

        binding.btnSave.setOnClickListener(v -> saveHabit());
    }

    private void setupEmojiGrid() {
        binding.emojiGrid.setNumColumns(8);
        EmojiAdapter adapter = new EmojiAdapter(this, EMOJIS, emoji -> {
            selectedEmoji = emoji;
            binding.tvSelectedEmoji.setText(emoji);
        });
        binding.emojiGrid.setAdapter(adapter);
        binding.tvSelectedEmoji.setText(selectedEmoji);
    }

    private void setupColorGrid() {
        binding.colorGrid.setNumColumns(8);
        ColorAdapter adapter = new ColorAdapter(this, COLORS, color -> {
            selectedColor = color;
            binding.viewColorPreview.setBackgroundColor(android.graphics.Color.parseColor(color));
        });
        binding.colorGrid.setAdapter(adapter);
        binding.viewColorPreview.setBackgroundColor(android.graphics.Color.parseColor(selectedColor));
    }

    private void setupTimePicker() {
        binding.timePicker.setIs24HourView(false);
        binding.timePicker.setHour(8);
        binding.timePicker.setMinute(0);
        binding.timePicker.setOnTimeChangedListener((view, hour, min) -> {
            reminderHour = hour;
            reminderMinute = min;
        });
        binding.switchReminder.setOnCheckedChangeListener((btn, isChecked) -> {
            binding.timePicker.setVisibility(isChecked ? View.VISIBLE : View.GONE);
        });
    }

    private void saveHabit() {
        String name = binding.etHabitName.getText().toString().trim();
        if (name.isEmpty()) {
            binding.etHabitName.setError("Please enter a habit name");
            return;
        }
        boolean reminderOn = binding.switchReminder.isChecked();
        Habit habit = new Habit(name, selectedEmoji, selectedColor,
                reminderHour, reminderMinute, reminderOn);

        viewModel.insertHabit(habit, id -> {
            habit.id = id;
            if (reminderOn) {
                NotificationHelper.scheduleReminder(this, habit);
            }
            runOnUiThread(this::finish);
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
