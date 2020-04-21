package com.apptitlig.p_piller;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class StartAlarmManager {
    private static String TAG = "StartAlarmManager";

    public boolean startAlarm(Context c)
    {
        Settings s = new Settings(c);
        int hour = s.getSavedHour();
        int minute = s.getSavedMinute();
        Calendar calendar;

        PendingIntent pendingIntent;
        calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);

        Calendar currentTime = Calendar.getInstance(Locale.getDefault());

        // if we do not add a day to the date, and the date is set to a time that is earlier than
        // current time, the alarm will go off immediately
        boolean returnvalue = calendar.getTime().compareTo(currentTime.getTime()) < 0;
        if (returnvalue )
        {
            calendar.add(Calendar.DATE, 1);
        }
        Intent intent = new Intent(c, SendNotification.class);
        pendingIntent = PendingIntent.getBroadcast(c, 0,
                   intent, 0);
        AlarmManager alarm = (AlarmManager) c.getSystemService(Context.ALARM_SERVICE);

        alarm.setRepeating(AlarmManager.RTC,
                calendar.getTimeInMillis(),
                //repeatInterval,
                AlarmManager.INTERVAL_DAY,
                pendingIntent);

        SimpleDateFormat sdf = new SimpleDateFormat(MainActivity.SDF_FORMAT);
        String formatted = sdf.format(calendar.getTime());;
        s.saveTextViewText(formatted);
        try {
            MainActivity.getInstance().updateTextViewWithText();
        } catch (Exception e) {

        }
        return returnvalue;
   }
}
