package edu.cnm.deepdive.codebreaker.controller;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import edu.cnm.deepdive.codebreaker.R;
import edu.cnm.deepdive.codebreaker.adapter.ScoreboardAdapter;
import edu.cnm.deepdive.codebreaker.databinding.FragmentScoreboardBinding;
import edu.cnm.deepdive.codebreaker.viewmodel.GameViewModel;
import java.util.function.BiConsumer;

public class ScoreboardFragment extends Fragment implements SeekBar.OnSeekBarChangeListener {

  private GameViewModel viewModel;
  private FragmentScoreboardBinding binding;
  private BiConsumer<Integer, Boolean> codeLengthUpdater;
  private BiConsumer<Integer, Boolean> poolSizeUpdater;
  private BiConsumer<Boolean, Boolean> sortedByTimeUpdater;

  public View onCreateView(
      @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    super.onCreateView(inflater, container, savedInstanceState);
    binding = FragmentScoreboardBinding.inflate(inflater, container, false);
    setupParameterChangeConsumers();
    setupParameterChangeListeners();
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    //noinspection ConstantConditions
    viewModel = new ViewModelProvider(getActivity()).get(GameViewModel.class);
    viewModel.getCodeLength().observe(getViewLifecycleOwner(), (codeLength) ->
        binding.codeLength.setProgress(codeLength));
    viewModel.getPoolSize().observe(getViewLifecycleOwner(), (poolSize) ->
        binding.poolSize.setProgress(poolSize));
    viewModel.getSortedByTime().observe(getViewLifecycleOwner(), (sortedByTime) ->
        sortedByTimeUpdater.accept(sortedByTime, false));
    viewModel.getScoreboard().observe(getViewLifecycleOwner(), (games) -> {
      //noinspection ConstantConditions
      ScoreboardAdapter adapter = new ScoreboardAdapter(getContext(), games);
      binding.games.setAdapter(adapter);
    });
  }

  @Override
  public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
    //noinspection unchecked
    ((BiConsumer<Integer, Boolean>) seekBar.getTag()).accept(progress, fromUser);
  }

  @Override
  public void onStartTrackingTouch(SeekBar seekBar) {
  }

  @Override
  public void onStopTrackingTouch(SeekBar seekBar) {
  }

  private void setupParameterChangeConsumers() {
    codeLengthUpdater = (value, fromUser) -> {
      binding.codeLengthDisplay.setText(String.valueOf(value));
      if (fromUser) {
        viewModel.setCodeLength(value);
      }
    };
    poolSizeUpdater = (value, fromUser) -> {
      binding.poolSizeDisplay.setText(String.valueOf(value));
      if (fromUser) {
        viewModel.setPoolSize(value);
      }
    };
    sortedByTimeUpdater = (sortedByTime, fromUser) -> {
      if (sortedByTime) {
        binding.timeHeader.setText(R.string.selected_time_header);
        binding.attemptsHeader.setText(R.string.unselected_attempts_header);
      } else {
        binding.timeHeader.setText(R.string.unselected_time_header);
        binding.attemptsHeader.setText(R.string.selected_attempts_header);
      }
      if (fromUser) {
        viewModel.setSortedByTime(sortedByTime);
      }
    };
  }

  private void setupParameterChangeListeners() {
    Resources res = getResources();
    binding.codeLengthDisplay.setText(
        String.valueOf(res.getInteger(R.integer.code_length_pref_default)));
    binding.codeLength.setTag(codeLengthUpdater);
    binding.codeLength.setOnSeekBarChangeListener(this);
    binding.poolSizeDisplay.setText(
        String.valueOf(res.getInteger(R.integer.pool_size_pref_default)));
    binding.poolSize.setTag(poolSizeUpdater);
    binding.poolSize.setOnSeekBarChangeListener(this);
    binding.attemptsHeader.setOnClickListener((v) -> sortedByTimeUpdater.accept(false, true));
    binding.timeHeader.setOnClickListener((v) -> sortedByTimeUpdater.accept(true, true));
  }

}