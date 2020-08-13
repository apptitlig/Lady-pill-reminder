package com.matilda.p_piller;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;


import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class CalendarActivity extends AppCompatActivity {

    private String TAG = "CalendarActivity : ";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.calendar_activity);
        final CalendarView calender = this.findViewById(R.id.calendar);
        Button mBtGoBack = findViewById(R.id.ok_button);


        final SimpleDateFormat sdf = new SimpleDateFormat(MainActivity.SDF_FORMAT);

        //Current
        final Calendar calendar = Calendar.getInstance(Locale.getDefault());
        final Date currentDay = calendar.getTime();

        Log.d(TAG, "CURRENT: " + sdf.format(currentDay));

        //Choosen
        final Date[] chosenDay = new Date[1];

        calender.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {

                Calendar calendarChosen = Calendar.getInstance();
                calendarChosen.set(Calendar.YEAR, year);
                calendarChosen.set(Calendar.MONTH, month);
                calendarChosen.set(Calendar.DAY_OF_MONTH, dayOfMonth);


                chosenDay[0] = calendarChosen.getTime();

                Log.d(TAG, "CHOSEN: " + sdf.format(chosenDay[0]));

            }
        });

        mBtGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();


                int days;
                if (chosenDay[0] == null) {
                    days = 0;
                } else {
                    days = daysBetween(currentDay, chosenDay[0]);
                }

                intent.putExtra("pause", days);
                setResult(RESULT_OK, intent);

                finish();
            }
        });
    }

    public int daysBetween(Date d1, Date d2){
        return (int)( (d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
    }
}
