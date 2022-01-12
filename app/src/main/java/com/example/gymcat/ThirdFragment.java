package com.example.gymcat;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import static android.content.Context.MODE_PRIVATE;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ThirdFragment extends Fragment {

    View view;

    private Button btnA, btnB;
    private LinearLayout containerA, containerB;
    private int counterA = 0, counterB = 0;
    private Boolean layoutA, layoutB;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public ThirdFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.third_fragment, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        sharedPreferences = (SharedPreferences) getActivity().getSharedPreferences("prefs", MODE_PRIVATE);
        editor = (SharedPreferences.Editor) sharedPreferences.edit();
        counterA = sharedPreferences.getInt("counterA", 0);
        counterB = sharedPreferences.getInt("counterB", 0);
        layoutA = sharedPreferences.getBoolean("layoutA", false);
        layoutB = sharedPreferences.getBoolean("layoutB", false);

        btnA = (Button) getView().findViewById(R.id.button_a);
        btnB = (Button) getView().findViewById(R.id.button_b);
        containerA = (LinearLayout) getView().findViewById(R.id.container_a);
        containerB = (LinearLayout) getView().findViewById(R.id.container_b);

        if (layoutA) {
            setTextA();
        }

        if (layoutB) {
            setTextB();
        }


        btnA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                counterA++;
                containerB.removeAllViews();
                if (counterA == 1) {
                    setTextA();
                    counterB = 0;
                    layoutA = true;
                    layoutB = false;
                    editor();
                }
            }
        });

        btnB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                counterB++;
                containerA.removeAllViews();
                if (counterB == 1) {
                    setTextB();
                    counterA = 0;
                    layoutB = true;
                    layoutA = false;
                    editor();
                }
            }
        });
    }

    private void setTextA() {
        LayoutInflater layoutInflater = (LayoutInflater) getActivity().getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View addView = layoutInflater.inflate(R.layout.layout_a, null);
        containerA.addView(addView);
        btnA.setText(Html.fromHtml("<br>Workout<br>Day A<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>&#8254;</b>"));
        btnB.setText("\nWorkout\nDay B\n");
    }

    private void setTextB() {
        LayoutInflater layoutInflater = (LayoutInflater) getActivity().getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View addView = layoutInflater.inflate(R.layout.layout_b, null);
        containerB.addView(addView);
        btnB.setText(Html.fromHtml("<br>Workout<br>Day B<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>&#8254;</b>"));
        btnA.setText("\nWorkout\nDay A\n");
    }

    private void editor() {
        editor.putInt("counterA", counterA);
        editor.putInt("counterB", counterB);
        editor.putBoolean("layoutA", layoutA);
        editor.putBoolean("layoutB", layoutB);
        editor.apply();
    }
}