package com.matilda.p_piller;

import java.util.List;

public class DatabaseHelper {

    private static String s;
    private static Day d;
    private static List<Day> days;

    void insertDay(Day day)
    {
        d = day;
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {

                App.get().getDB().dayDoa().insertDay(d);
            }
        });
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void insertDays(List<Day> d)
    {
        days = d;
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {

                for (int i = 0; i < days.size(); i++)
                {
                    App.get().getDB().dayDoa().insertDay(days.get(i));
                }
            }
        });
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    void deleteDays(List<Day> d)
    {
        days = d;
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {

                for (int i = 0; i < days.size(); i++)
                {
                    App.get().getDB().dayDoa().delete(days.get(i));
                }
            }
        });
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    Day getDay(String formatted)
    {
        s = formatted;
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                d = App.get().getDB().dayDoa().getDay(s);
            }
        });
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return d;
    }

    List<Day> getAll()
    {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                days = App.get().getDB().dayDoa().getAll();
            }
        });

        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return days;
    }



}
