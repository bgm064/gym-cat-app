package com.example.gymcat;

import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static android.content.Context.MODE_PRIVATE;

public class SecondFragment extends Fragment {
    View view;

    private EditText editTextInput;
    private TextView textViewCountDown, textViewMid;
    private Button btnSet, btnStartPause, btnReset, btnMinute;
    private CountDownTimer countDownTimer;
    private long startTimeInMillis = 0L;
    private long timeLeftInMillis = 0L;
    private long endTime = 0L;
    private int resCounter = 0;
    private int minCounter = 1;
    private boolean timerRunning;


    public SecondFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.second_fragment, container, false);
        return view;
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        editTextInput = (EditText) getView().findViewById(R.id.edit_text_input);
        textViewCountDown = (TextView) getView().findViewById(R.id.text_view_countdown);
        textViewMid = (TextView) getView().findViewById(R.id.text_view_mid);
        btnSet = (Button) getView().findViewById(R.id.button_set);
        btnStartPause = (Button) getView().findViewById(R.id.button_start_pause);
        btnReset = (Button) getView().findViewById(R.id.button_reset);
        btnMinute = (Button) getView().findViewById(R.id.button_minute);

        btnSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resCounter = 1;
                minCounter = 1;
                String input = editTextInput.getText().toString();

                if (timerRunning) {
                    return;
                } else {

                    if (input.length() == 0) {
                        Toast.makeText(getActivity(), "Field can't be empty", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    long millisInput = Long.parseLong(input) * 1000;
                    if (millisInput == 0) {
                        Toast.makeText(getActivity(), "Please enter a positive number", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    setTime(millisInput);
                    editTextInput.setText("");
                }
            }
        });


        btnStartPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resCounter = 0;
                if (timeLeftInMillis == 0) {
                    Toast.makeText(getActivity(), "Please input time first", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    if (timerRunning) {
                        pauseTimer();
                    } else {
                        startTimer();
                    }
                }
                btnReset.setText("Return");
            }
        });
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resCounter++;
                minCounter = 1;
                if (timerRunning) {
                    pauseTimer();
                    closeKeyboard();
                }
                if (resCounter == 2) {
                    setTime(0);
                    resCounter = 0;
                }
                resetTimer();
                btnReset.setText("Reset");
            }
        });


        btnMinute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resCounter = 1;
                minCounter++;
                for (int i = 0; i < minCounter; i++) {
                    long minute = i * 60000L;
                    setTime(minute);
                }
            }
        });
    }

    private void setTime(long milliseconds) {
        startTimeInMillis = milliseconds;
        resetTimer();
        closeKeyboard();
    }


    private void startTimer() {
        endTime = System.currentTimeMillis() + timeLeftInMillis;
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                timerRunning = false;
                updateWatchInterface();
            }
        }.start();
        timerRunning = true;
        updateWatchInterface();
    }

    private void pauseTimer() {
        countDownTimer.cancel();
        timerRunning = false;
        updateWatchInterface();
    }

    private void resetTimer() {
        timeLeftInMillis = startTimeInMillis;
        timerRunning = false;
        updateCountDownText();
        updateWatchInterface();
    }

    private void updateCountDownText() {
        final MediaPlayer mediaPlayer = MediaPlayer.create(getActivity(), R.raw.meow);
        int hours = (int) (timeLeftInMillis / 1000) / 3600;
        int minutes = (int) ((timeLeftInMillis / 1000) % 3600) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;
        String timeLeftFormatted;

        if (hours > 0) {
            timeLeftFormatted = String.format(Locale.getDefault(),
                    "%d:%02d:%02d", hours, minutes, seconds);
        } else {
            timeLeftFormatted = String.format(Locale.getDefault(),
                    "%02d:%02d", minutes, seconds);
        }
        textViewCountDown.setText(timeLeftFormatted);

        if (minutes == 0 && seconds == 0 && timerRunning) {
            mediaPlayer.start();
            mediaPlayer.setLooping(false);
        }
    }

    private void updateWatchInterface() {
        if (timerRunning) {
            btnMinute.setVisibility(View.INVISIBLE);
            btnSet.setVisibility(View.INVISIBLE);
            editTextInput.setVisibility(View.INVISIBLE);
            textViewMid.setVisibility(View.INVISIBLE);
            btnStartPause.setText("Pause");
        } else {
            btnMinute.setVisibility(View.VISIBLE);
            btnSet.setVisibility(View.VISIBLE);
            editTextInput.setVisibility(View.VISIBLE);
            textViewMid.setVisibility(View.VISIBLE);
            btnStartPause.setText("Start");
        }
    }

    private void closeKeyboard() {
        View view = this.getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    @Override
    public void onStop() {
        super.onStop();
        SharedPreferences prefs = getActivity().getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong("startTimeInMillis", startTimeInMillis);
        editor.putLong("millisLeft", timeLeftInMillis);
        editor.putBoolean("timerRunning", timerRunning);
        editor.putLong("endTime", endTime);
        editor.apply();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        updateCountDownText();
        updateWatchInterface();
        if (timerRunning) {
            timeLeftInMillis = endTime - System.currentTimeMillis();
            if (timeLeftInMillis < 0) {
                timeLeftInMillis = 0;
                timerRunning = false;
                updateCountDownText();
                updateWatchInterface();
            } else {
                startTimer();
            }
        }
    }
}
