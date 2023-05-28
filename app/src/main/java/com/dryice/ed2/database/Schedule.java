package com.dryice.ed2.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity
public class Schedule {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name="name")
    public String name;

    @ColumnInfo(name="deadline")
    public Date deadline;

    @ColumnInfo(name="importance")
    public String importance;

    @ColumnInfo(name="sum")
    public int sum;

    @ColumnInfo(name="checked")
    public boolean checked;

    public String getName() {
        return name;
    }
    public Date getDeadline() {
        return deadline;
    }

    public int getSum() {
        return sum;
    }

    public String getImportance() {
        return importance;
    }

}
