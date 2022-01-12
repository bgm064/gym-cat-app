package com.example.gymcat;

import android.content.Context;
import android.content.SharedPreferences;

public class UserSettings {

    SharedPreferences theme;

    public UserSettings(Context context) {
        theme = context.getSharedPreferences("prefs", Context.MODE_PRIVATE);
    }

    public void setThemeState(boolean state) {
        SharedPreferences.Editor editor = theme.edit();
        editor.putBoolean("NightMode", state);
        editor.commit();
    }

    public Boolean loadThemeState() {
        Boolean state = theme.getBoolean("NightMode", false);
        return state;
    }

}
