package com.streakup.app.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.streakup.app.R;
import com.streakup.app.data.model.Habit;
import com.streakup.app.ui.home.HabitViewModel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HabitAdapter extends RecyclerView.Adapter<HabitAdapter.HabitViewHolder> {
    private List<Habit> habits = new ArrayList<>();
    private final Map<Integer, Boolean> completedMap = new HashMap<>();
    private final Context context;
    private final HabitViewModel viewModel;

    public HabitAdapter(Context ctx, HabitViewModel vm) {
        this.context = ctx;
        this.viewModel = vm;
    }

    public void setHabits(List<Habit> habits) {
        this.habits = habits;
        completedMap.clear();
        for (Habit h : habits) {
            completedMap.put(h.id, viewModel.isCompletedToday(h.id));
        }
        notifyDataSetChanged();
    }

    public int getCompletedCount() {
        int count = 0;
        for (Boolean b : completedMap.values()) {
            if (Boolean.TRUE.equals(b)) count++;
        }
        return count;
    }

    @NonNull
    @Override
    public HabitViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_habit, parent, false);
        return new HabitViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull HabitViewHolder holder, int position) {
        Habit habit = habits.get(position);
        boolean done = Boolean.TRUE.equals(completedMap.get(habit.id));

        holder.tvEmoji.setText(habit.emoji);
        holder.tvName.setText(habit.name);
        holder.tvStreak.setText(habit.currentStreak + " day streak");

        // Color accent strip
        try {
            int color = Color.parseColor(habit.colorHex);
            holder.colorStrip.setBackgroundColor(color);
            if (done) {
                holder.cardView.setAlpha(0.7f);
                holder.ivCheck.setVisibility(View.VISIBLE);
            } else {
                holder.cardView.setAlpha(1f);
                holder.ivCheck.setVisibility(View.INVISIBLE);
            }
        } catch (Exception ignored) {}

        holder.btnToggle.setOnClickListener(v -> {
            boolean nowDone = !Boolean.TRUE.equals(completedMap.get(habit.id));
            completedMap.put(habit.id, nowDone);
            notifyItemChanged(position);
            viewModel.toggleToday(habit, null);
        });

        holder.cardView.setOnLongClickListener(v -> {
            new android.app.AlertDialog.Builder(context)
                    .setTitle("Delete habit?")
                    .setMessage("\"" + habit.name + "\" and all its history will be deleted.")
                    .setPositiveButton("Delete", (d, w) -> viewModel.deleteHabit(habit))
                    .setNegativeButton("Cancel", null)
                    .show();
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return habits.size();
    }

    static class HabitViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        View colorStrip;
        TextView tvEmoji, tvName, tvStreak;
        ImageView ivCheck;
        View btnToggle;

        HabitViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_habit);
            colorStrip = itemView.findViewById(R.id.color_strip);
            tvEmoji = itemView.findViewById(R.id.tv_emoji);
            tvName = itemView.findViewById(R.id.tv_habit_name);
            tvStreak = itemView.findViewById(R.id.tv_streak);
            ivCheck = itemView.findViewById(R.id.iv_check);
            btnToggle = itemView.findViewById(R.id.btn_toggle);
        }
    }
}
