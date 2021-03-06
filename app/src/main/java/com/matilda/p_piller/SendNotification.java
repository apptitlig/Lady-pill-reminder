package com.matilda.p_piller;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class SendNotification extends BroadcastReceiver {
    private final String TAG = "SendNotification : ";
    public static boolean ringalarm = true;

    public void onReceive(Context c, Intent intent)
    {
        Log.d(TAG, "On recieve");
        Settings s = new Settings(c);

        // Do nothing if alarm is not started
        if (s.getStopAlarm())
        {
            return;
        }

        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction()) ||
            Intent.ACTION_TIMEZONE_CHANGED.equals(intent.getAction()))
        {
            StartAlarmManager start = new StartAlarmManager();
            boolean earlier = start.startAlarm(c);
            if (earlier)
            {
                sendNotification(c);
            }
            Log.d(TAG, "Startup");
        }
        else  /* timer */
        {
            Log.d(TAG, "Timer");
            sendNotification(c);
        }

        StartAlarmManager start = new StartAlarmManager();
        start.startAlarm(c);
    }


    private void sendNotification(Context c)
    {
        Settings s = new Settings(c);
        DatabaseHelper dbh = new DatabaseHelper();

        SimpleDateFormat sdf = new SimpleDateFormat(MainActivity.SDF_FORMAT);
        Calendar calendar = Calendar.getInstance(Locale.getDefault());

        String formatted = sdf.format(calendar.getTime());

        SendNotification.ringalarm = (dbh.getDay(formatted) == null);

        if (SendNotification.ringalarm) {

            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(c.getApplicationContext(), "notify_001");
            Intent ii = new Intent(c.getApplicationContext(), MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(c, 0, ii, 0);

            NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
            bigText.bigText(c.getString(R.string.dont_get_pregnant));
            bigText.setBigContentTitle(c.getString(R.string.take_lady_pill));
            bigText.setSummaryText("");

            mBuilder.setContentIntent(pendingIntent);
            mBuilder.setSmallIcon(R.drawable.umbrella2);
            mBuilder.setContentTitle(c.getString(R.string.reminder));
            mBuilder.setContentText(c.getString(R.string.take_lady_pill));
            mBuilder.setPriority(1);
            mBuilder.setStyle(bigText);
            mBuilder.setOngoing(false); // let notification be removed at any time
            mBuilder.setAutoCancel(true); //remove notification when clicked on;

            NotificationManager mNotificationManager =
                    (NotificationManager) c.getSystemService(Context.NOTIFICATION_SERVICE);

            mNotificationManager.notify(0, mBuilder.build());

            String d = sdf.format(new Date());
            Day day = new Day();

            day.setDay(d);
            dbh.insertDay(day);

            calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, s.getSavedHour());
            calendar.set(Calendar.MINUTE, s.getSavedHour());

            calendar.add(Calendar.DATE, 1);

            formatted = sdf.format(calendar.getTime());
            s.saveTextViewText(formatted);
            try {
                MainActivity.getInstance().updateTextViewWithText();
            } catch (Exception e) { }
        }
    }
}