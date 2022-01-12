package com.example.gymcat;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.os.SystemClock;

import android.widget.Toast;

public class FirstFragment extends Fragment {

    View view;

    private Button btnStartPause, btnLap, btnReset;
    private TextView txtTimer, msTimer;
    private Handler customHandler = new Handler();
    private LinearLayout container;
    private long startTime = 0L;
    private long timeInMilliseconds = 0L;
    private long timeSwapBuff = 0L;
    private long updateTime = 0L;
    private int lapCounter = 1;
    private boolean timerRunning = false;


    public FirstFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.first_fragment, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        txtTimer = (TextView) getView().findViewById(R.id.timer_value);
        msTimer = (TextView) getView().findViewById(R.id.ms_value);
        btnStartPause = (Button) getView().findViewById(R.id.button_start_pause);
        btnLap = (Button) getView().findViewById(R.id.button_lap);
        btnReset = (Button) getView().findViewById(R.id.button_reset);
        container = (LinearLayout) getView().findViewById(R.id.container);


        btnStartPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (timerRunning == false) {

                    startTime = SystemClock.uptimeMillis();

                    customHandler.postDelayed(updateTimerThread, 0);

                    LayoutInflater layoutInflater = (LayoutInflater) getActivity().getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    final View addView = layoutInflater.inflate(R.layout.row, null);
                    TextView txtValue = (TextView) addView.findViewById(R.id.timer_view);
                    txtValue.setText("");

                    lapCounter = 1;

                    timerRunning = true;
                } else {
                    timeSwapBuff += timeInMilliseconds;
                    customHandler.removeCallbacks(updateTimerThread);

                    timerRunning = false;
                }
                updateWatchInterface();
            }
        });


        btnLap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (startTime == 0) {
                    Toast.makeText(getActivity(), "Please start the stopwatch first", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    LayoutInflater layoutInflater = (LayoutInflater) getActivity().getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    final View addView = layoutInflater.inflate(R.layout.row, null);
                    TextView lapValue = (TextView) addView.findViewById(R.id.lap_view);
                    lapValue.setText("Lap #" + String.valueOf(lapCounter++));
                    TextView txtValue = (TextView) addView.findViewById(R.id.timer_view);
                    txtValue.setText(" " + txtTimer.getText());
                    TextView msValue = (TextView) addView.findViewById(R.id.ms_view);
                    msValue.setText(msTimer.getText());
                    container.addView(addView);
                }
            }
        });


        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startTime = SystemClock.uptimeMillis();
                customHandler.removeCallbacks(updateTimerThread);

                startTime = 0L;
                timeInMilliseconds = 0L;
                timeSwapBuff = 0L;
                updateTime = 0L;
                lapCounter = 1;
                timerRunning = false;
                updateWatchInterface();

                txtTimer.setText("00:00.");
                msTimer.setText("00");

                container.removeAllViews();

            }
        });


    }

    private void updateWatchInterface() {
        if (timerRunning == true) {
            btnStartPause.setText("Pause");
        } else {
            btnStartPause.setText("Start");
        }
    }

    Runnable updateTimerThread = new Runnable() {
        @Override
        public void run() {
            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
            updateTime = timeSwapBuff + timeInMilliseconds;
            int secs = (int) (updateTime / 1000);
            int mins = secs / 60;
            secs %= 60;
            int milliseconds = (int) (updateTime % 1000 / 10);

            txtTimer.setText("" + String.format("%02d", mins) + ":" + String.format("%02d", secs) + ".");
            msTimer.setText("" + String.format("%02d", milliseconds));

            customHandler.postDelayed(this, 0);
        }
    };


}
