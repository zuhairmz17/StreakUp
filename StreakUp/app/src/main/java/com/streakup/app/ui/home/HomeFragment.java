package com.streakup.app.ui.home;

import android.os.Bundle;
import android.view.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.streakup.app.adapter.HabitAdapter;
import com.streakup.app.databinding.FragmentHomeBinding;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private HabitViewModel viewModel;
    private HabitAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(HabitViewModel.class);

        // Date header
        LocalDate today = LocalDate.now();
        binding.tvDate.setText(today.format(DateTimeFormatter.ofPattern("EEEE, MMM d", Locale.getDefault())));

        // RecyclerView
        adapter = new HabitAdapter(requireContext(), viewModel);
        binding.rvHabits.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvHabits.setAdapter(adapter);

        viewModel.habits.observe(getViewLifecycleOwner(), habits -> {
            adapter.setHabits(habits);
            binding.tvEmpty.setVisibility(habits.isEmpty() ? View.VISIBLE : View.GONE);
            updateProgress(habits.size());
        });
    }

    private void updateProgress(int total) {
        if (total == 0) {
            binding.progressBar.setMax(1);
            binding.progressBar.setProgress(0);
            binding.tvProgress.setText("0 / 0");
            return;
        }
        // Count completed today using adapter's data
        int done = adapter.getCompletedCount();
        binding.progressBar.setMax(total);
        binding.progressBar.setProgress(done);
        binding.tvProgress.setText(done + " / " + total + " done");

        if (done == total && total > 0) {
            binding.tvMotivation.setText("Perfect day! All habits done!");
            binding.tvMotivation.setVisibility(View.VISIBLE);
        } else if (done == 0) {
            binding.tvMotivation.setText("Let's start your streak!");
            binding.tvMotivation.setVisibility(View.VISIBLE);
        } else {
            binding.tvMotivation.setText("Keep going, you're doing great!");
            binding.tvMotivation.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
