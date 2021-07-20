package edu.cnm.deepdive.codebreaker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import edu.cnm.deepdive.codebreaker.R;
import edu.cnm.deepdive.codebreaker.databinding.ItemScoreboardBinding;
import edu.cnm.deepdive.codebreaker.model.entity.Guess;
import edu.cnm.deepdive.codebreaker.model.pojo.GameWithGuesses;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.List;

public class ScoreboardAdapter extends ArrayAdapter<GameWithGuesses> {

  private static final int MILLISECONDS_PER_SECOND = 1000;
  private static final int SECONDS_PER_MINUTE = 60;
  private static final int MINUTES_PER_HOUR = 60;

  private final DateFormat dateFormat;
  private final DateFormat timeFormat;
  private final NumberFormat numberFormat;
  private final String dateTimeCombinationFormat;
  private final String solutionTimeFormat;

  public ScoreboardAdapter(@NonNull Context context, @NonNull List<GameWithGuesses> games) {
    super(context, R.layout.item_scoreboard, games);
    dateFormat = android.text.format.DateFormat.getDateFormat(context);
    timeFormat = android.text.format.DateFormat.getTimeFormat(context);
    numberFormat = NumberFormat.getIntegerInstance();
    dateTimeCombinationFormat = context.getString(R.string.date_time_combination_format);
    solutionTimeFormat = context.getString(R.string.solution_time_format);
  }

  @NonNull
  @Override
  public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
    ItemScoreboardBinding binding = (convertView != null)
        ? ItemScoreboardBinding.bind(convertView)
        : ItemScoreboardBinding.inflate(LayoutInflater.from(getContext()), parent, false);
    GameWithGuesses game = getItem(position);
    List<Guess> guesses = game.getGuesses();
    int numGuesses = guesses.size();
    Guess lastGuess = guesses.get(numGuesses - 1);
    Date completed = lastGuess.getCreated();
    long time = lastGuess.getCreated().getTime() - game.getCreated().getTime();
    binding.dateCompleted.setText(String.format(dateTimeCombinationFormat,
        dateFormat.format(completed), timeFormat.format(completed)));
    binding.attempts.setText(numberFormat.format(numGuesses));
    int seconds = (int) Math.round((double) time / MILLISECONDS_PER_SECOND);
    int minutes = seconds / SECONDS_PER_MINUTE;
    seconds %= SECONDS_PER_MINUTE;
    int hours = minutes / MINUTES_PER_HOUR;
    minutes %= MINUTES_PER_HOUR;
    binding.time.setText(String.format(solutionTimeFormat, hours, minutes, seconds));
    return binding.getRoot();
  }

}
