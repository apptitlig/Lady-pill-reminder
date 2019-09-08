package com.matilda.p_piller;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface DayDoa {
    @Query("SELECT * FROM Day WHERE Day = :day")
    Day getDay(String day);

    @Query("SELECT * FROM day")
    List<Day> getAll();

    @Query("SELECT day FROM Day WHERE Day = :day")
    String toString(String day);

    @Insert
    void insertDay(Day day);

    @Query("SELECT day FROM Day where ID = (SELECT MAX(id) FROM Day)")
    String getLatestDay();

    @Delete
    void delete(Day day);
}
