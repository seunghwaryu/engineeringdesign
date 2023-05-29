package com.dryice.ed2.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Dao
public interface ScheduleDao {
    @Query("SELECT * FROM schedule")
    List<Schedule> getAll();

    @Query("SELECT importance FROM schedule WHERE id = :tid")
    String getImportance(int tid);

    @Query("SELECT checked FROM schedule WHERE id = :tid")
    boolean getChecked(int tid);

    @Query("SELECT * FROM schedule WHERE deadline = :today")
    List<Schedule> getTodayList(Date today);

    @Query("SELECT * FROM schedule WHERE id IN (:scheduleIds)")
    List<Schedule> loadAllByIds(int[] scheduleIds);
    @Query("UPDATE schedule SET sum = :rank WHERE id = :tid")
    void updateSum(int tid,int rank);

    @Query("UPDATE schedule SET checked = :check WHERE id = :tid")
    void updateChecked(int tid,boolean check);

    @Insert
    void insertAll(Schedule... schedules);

    @Delete
    void delete(Schedule schedule);

}