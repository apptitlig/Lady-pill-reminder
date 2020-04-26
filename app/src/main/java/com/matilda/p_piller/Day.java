package com.matilda.p_piller;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

@Entity
public class Day {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String day;

    Day() {
    }

    public int getId(){
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    String getDay() {
        return day;
    }

    void setDay(String day) {
        this.day = day;
    }
}