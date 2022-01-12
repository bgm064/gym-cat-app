package com.example.gymcat;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.viewpager.widget.ViewPager;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CompoundButton;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private AppBarLayout appBarLayout;
    private ViewPager viewPager;
    private SwitchCompat switchCompat;
    private int counter = 0;
    UserSettings settings;
    SharedPreferences sharedPreferences;
    Boolean firstSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        settings = (UserSettings) new UserSettings(this);

        if (settings.loadThemeState() == true) {
            setTheme(R.style.Theme_Dark);
        } else {
            setTheme(R.style.Theme_Light);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = (SharedPreferences) getSharedPreferences("prefs", MODE_PRIVATE);

        firstSwitch = (Boolean) sharedPreferences.getBoolean("firstSwitch", true);

        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        switchCompat = (SwitchCompat) findViewById(R.id.btn_switch);

        if (settings.loadThemeState() == true) {
            switchCompat.setChecked(true);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (firstSwitch) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    firstSwitch = false;
                    editor.putBoolean("firstSwitch", firstSwitch);
                    editor.apply();

                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setCancelable(false);
                    builder.setMessage("Enable Dark Mode?\n\nChanging the mode resets all timers.");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (isChecked) {
                                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                                switchCompat.setChecked(true);
                                settings.setThemeState(true);
                            } else {
                                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                                switchCompat.setChecked(false);
                                settings.setThemeState(false);
                            }
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            switchCompat.setChecked(false);
                            firstSwitch = true;
                            editor.putBoolean("firstSwitch", firstSwitch);
                            editor.apply();
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                } else {
                    if (isChecked) {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                        switchCompat.setChecked(true);
                        settings.setThemeState(true);
                    } else {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                        switchCompat.setChecked(false);
                        settings.setThemeState(false);
                    }
                }
            }
        });

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.AddFragment(new FirstFragment(), "Stopwatch");
        adapter.AddFragment(new SecondFragment(), "Timer");
        adapter.AddFragment(new ThirdFragment(), "Chart");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onBackPressed() {
        counter++;
        if (counter == 2) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(false);
            builder.setMessage("Do you want to Exit?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();

                    counter = 0;
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();

                    counter = 0;
                }
            });
            AlertDialog alert = builder.create();
            alert.show();

        }
    }

}