package com.streakup.app.ui.stats;

import android.graphics.Color;
import android.os.Bundle;
import android.view.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.*;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.streakup.app.adapter.HabitStatsAdapter;
import com.streakup.app.data.model.Habit;
import com.streakup.app.databinding.FragmentStatsBinding;
import com.streakup.app.ui.home.HabitViewModel;
import java.util.*;

public class StatsFragment extends Fragment {
    private FragmentStatsBinding binding;
    private HabitViewModel viewModel;
    private HabitStatsAdapter statsAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentStatsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(HabitViewModel.class);

        statsAdapter = new HabitStatsAdapter(requireContext(), viewModel);
        binding.rvStats.setLayoutManager(
                new androidx.recyclerview.widget.LinearLayoutManager(requireContext()));
        binding.rvStats.setAdapter(statsAdapter);

        viewModel.habits.observe(getViewLifecycleOwner(), habits -> {
            statsAdapter.setHabits(habits);
            setupBarChart(habits);
            updateSummary(habits);
        });
    }

    private void updateSummary(List<Habit> habits) {
        int totalHabits = habits.size();
        int longestStreak = 0;
        int totalCompletions = 0;
        for (Habit h : habits) {
            if (h.longestStreak > longestStreak) longestStreak = h.longestStreak;
            totalCompletions += viewModel.getTotalCompletions(h.id);
        }
        binding.tvTotalHabits.setText(String.valueOf(totalHabits));
        binding.tvLongestStreak.setText(longestStreak + " days");
        binding.tvTotalCompletions.setText(String.valueOf(totalCompletions));
    }

    private void setupBarChart(List<Habit> habits) {
        if (habits.isEmpty()) return;
        BarChart chart = binding.barChart;
        chart.getDescription().setEnabled(false);
        chart.setDrawGridBackground(false);
        chart.getLegend().setEnabled(false);
        chart.setTouchEnabled(false);

        List<BarEntry> entries = new ArrayList<>();
        List<String> labels = new ArrayList<>();
        for (int i = 0; i < habits.size(); i++) {
            Habit h = habits.get(i);
            entries.add(new BarEntry(i, h.currentStreak));
            labels.add(h.emoji);
        }

        BarDataSet dataSet = new BarDataSet(entries, "Streaks");
        List<Integer> colors = new ArrayList<>();
        for (Habit h : habits) {
            try { colors.add(Color.parseColor(h.colorHex)); }
            catch (Exception e) { colors.add(Color.parseColor("#6C63FF")); }
        }
        dataSet.setColors(colors);
        dataSet.setValueTextSize(12f);

        BarData data = new BarData(dataSet);
        data.setBarWidth(0.6f);
        chart.setData(data);

        XAxis xAxis = chart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setDrawGridLines(false);

        chart.getAxisLeft().setDrawGridLines(false);
        chart.getAxisRight().setEnabled(false);
        chart.animateY(800);
        chart.invalidate();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
