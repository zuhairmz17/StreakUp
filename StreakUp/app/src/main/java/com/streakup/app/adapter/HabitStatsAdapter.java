package com.streakup.app.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.*;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.streakup.app.R;
import com.streakup.app.data.model.Habit;
import com.streakup.app.ui.home.HabitViewModel;
import com.streakup.app.util.HeatmapView;
import java.util.*;

public class HabitStatsAdapter extends RecyclerView.Adapter<HabitStatsAdapter.StatsVH> {
    private List<Habit> habits = new ArrayList<>();
    private final Context context;
    private final HabitViewModel viewModel;

    public HabitStatsAdapter(Context ctx, HabitViewModel vm) {
        this.context = ctx;
        this.viewModel = vm;
    }

    public void setHabits(List<Habit> habits) {
        this.habits = habits;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public StatsVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_habit_stats, parent, false);
        return new StatsVH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull StatsVH holder, int position) {
        Habit habit = habits.get(position);
        holder.tvName.setText(habit.emoji + "  " + habit.name);
        holder.tvCurrentStreak.setText(habit.currentStreak + " day streak");
        holder.tvLongest.setText("Best: " + habit.longestStreak + " days");
        int total = viewModel.getTotalCompletions(habit.id);
        holder.tvTotal.setText(total + " total completions");

        try {
            int color = Color.parseColor(habit.colorHex);
            holder.colorStrip.setBackgroundColor(color);
        } catch (Exception ignored) {}

        // Load heatmap
        List<Long> days = viewModel.getCompletedDays(habit.id);
        int color = Color.parseColor("#6C63FF");
        try { color = Color.parseColor(habit.colorHex); } catch (Exception ignored) {}
        holder.heatmapView.setData(days, color);
    }

    @Override
    public int getItemCount() { return habits.size(); }

    static class StatsVH extends RecyclerView.ViewHolder {
        TextView tvName, tvCurrentStreak, tvLongest, tvTotal;
        View colorStrip;
        HeatmapView heatmapView;

        StatsVH(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_stat_name);
            tvCurrentStreak = itemView.findViewById(R.id.tv_stat_streak);
            tvLongest = itemView.findViewById(R.id.tv_stat_longest);
            tvTotal = itemView.findViewById(R.id.tv_stat_total);
            colorStrip = itemView.findViewById(R.id.stat_color_strip);
            heatmapView = itemView.findViewById(R.id.heatmap_view);
        }
    }
}
