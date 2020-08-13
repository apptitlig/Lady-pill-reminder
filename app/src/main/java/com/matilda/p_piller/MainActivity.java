package com.matilda.p_piller;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.content.Intent;
import android.content.Context;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.TimePicker;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private String TAG = "MainActivity : ";
    public static String SDF_FORMAT = "yyyy-MM-dd";
    private TimePicker tp;
    private Settings s;
    private Context c;
    private static MainActivity instance;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        instance = this;
        setContentView(R.layout.activity_main);
        c = getApplicationContext();
        s = new Settings(c);
        tp = this.findViewById(R.id.time_picker);
        tp.setIs24HourView(true);

        tp.setHour(s.getSavedHour());
        tp.setMinute(s.getSavedMinute());

        if (s.getTextViewText().equals(c.getString(R.string.no_alarm))) {
            s.setAlarmStop(true);
            updateTextViewWithIngetLarm();
        } else {
            updateTextViewWithText();
        }

        NotificationManager mNotificationManager =
                (NotificationManager) c.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "notify_001",
                    c.getString(R.string.channel),
                    NotificationManager.IMPORTANCE_DEFAULT);
            mNotificationManager.createNotificationChannel(channel);
        }

    }

    public static MainActivity getInstance()
    {
        return instance;
    }

    public void updateTextViewWithIngetLarm()
    {
        MainActivity.this.runOnUiThread(new Runnable() {
            public void run() {
                TextView textView = (TextView) findViewById(R.id.textView);
                textView.setText(c.getString(R.string.no_alarm));
                textView.setBackgroundColor(Color.WHITE);
            }
        });
    }

    public void updateTextViewWithText()
    {
        Log.d(TAG, "uppdaterar text");
        MainActivity.this.runOnUiThread(new Runnable() {
            public void run() {

                Context c = getApplicationContext();
                // if the alarm rings update the text in the textView with one day
                Settings s = new Settings(c);
                TextView textView = (TextView) findViewById(R.id.textView);
                String string = s.getTextViewText();
                String hour = (s.getSavedHour() < 10) ?
                        "0" + s.getSavedHour() : "" + s.getSavedHour();
                String min = (s.getSavedMinute() < 10) ?
                        "0" + s.getSavedMinute() : "" + s.getSavedMinute();
                textView.setText(c.getString(R.string.next_alarm) + " :\n" + string + " " + hour + ":" + min);
                textView.setBackgroundColor(Color.parseColor("#A3C145"));
            }
        });
    }

    public void startAlarm(View view)
    {
        DatabaseHelper dbh = new DatabaseHelper();
        s.saveTime(tp.getHour(), tp.getMinute());

        List<Day> days = dbh.getAll();
        dbh.deleteDays(days);

        StartAlarmManager start = new StartAlarmManager();
        Context c = getApplicationContext();
        start.startAlarm(c);
        s.setAlarmStop(false);
    }

    public void periods(View view)
    {
        pause ( 7);
    }

    private void pause (int length)
    {
        DatabaseHelper dbh = new DatabaseHelper();
        List<Day> days = dbh.getAll();
        dbh.deleteDays(days);


        SimpleDateFormat sdf = new SimpleDateFormat(MainActivity.SDF_FORMAT);
        Calendar time = Calendar.getInstance(Locale.getDefault());
        time.set(Calendar.HOUR_OF_DAY, s.getSavedHour());
        time.set(Calendar.MINUTE, s.getSavedHour());
        for (int i = 0; i < length; i++)
        {

            String formatted = sdf.format(time.getTime());

            Day d = new Day();
            d.setDay(formatted);
            dbh.insertDay(d);
            time.add(Calendar.DATE, 1);
        }
        //Skriv en dag senare till display
        String formatted = sdf.format(time.getTime());
        s.saveTextViewText(formatted);
        try {
            MainActivity.getInstance().updateTextViewWithText();
        } catch (Exception ignored) { }
    }

    public void turnOffAlarm(View view)
    {
        Intent intent = new Intent(this, SendNotification.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
        s.saveTextViewText(c.getString(R.string.no_alarm));
        s.setAlarmStop(true);

        try {
            MainActivity.getInstance().updateTextViewWithIngetLarm();
        } catch (Exception ignored) { }
    }

    public void chooseDay(View view)
    {
        Intent myIntent = new Intent(MainActivity.this, CalendarActivity.class);
        //myIntent.putExtra("key", value); //Optional parameters
        MainActivity.this.startActivityForResult(myIntent, 1);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                int pauseLength = data.getIntExtra("pause", 0);

                pause ( pauseLength );
            }
        }
    }
}
