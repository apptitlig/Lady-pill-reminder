package com.matilda.p_piller;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class Settings extends Activity {

    private final Context c;
    private String TAG = "Settings";

    private static SharedPreferences sp;
    private static SharedPreferences.Editor editor;

    public Settings(Context c) {
        this.c = c;
    }

    public String getTextViewText()
    {
        sp = c.getSharedPreferences("settings", Context.MODE_PRIVATE);
        return sp.getString("TextViewText", c.getString(R.string.no_alarm));
    }

    public void saveTextViewText(String s)
    {
        sp = c.getSharedPreferences("settings", MODE_PRIVATE);
        editor  = sp.edit();
        editor.putString("TextViewText", s);
        editor.commit();
    }

    public int getSavedHour()
    {
        sp = c.getSharedPreferences("settings", Context.MODE_PRIVATE);
        return sp.getInt("hour", 0);
    }

    public int getSavedMinute()
    {
        sp = c.getSharedPreferences("settings", Context.MODE_PRIVATE);
        return sp.getInt("minute", 0);
    }

    public void saveTime(int hour, int minute)
    {
        sp = c.getSharedPreferences("settings", MODE_PRIVATE);
        editor  = sp.edit();
        editor.putInt("hour", hour);
        editor.putInt("minute", minute);
        editor.commit();
    }

    public Boolean getStopAlarm()
    {
        sp = c.getSharedPreferences("settings", Context.MODE_PRIVATE);
        return sp.getBoolean("alarmstop", false);
    }

    public void setAlarmStop(Boolean alarmstop)
    {
        sp = c.getSharedPreferences("settings", MODE_PRIVATE);
        editor  = sp.edit();
        editor.putBoolean("alarmstop", alarmstop);
        editor.commit();
    }
}
