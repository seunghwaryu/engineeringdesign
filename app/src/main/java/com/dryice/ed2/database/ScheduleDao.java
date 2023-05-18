package com.dryice.ed2.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ScheduleDao {
    @Query("SELECT * FROM schedule")
    List<Schedule> getAll();

    @Query("SELECT * FROM schedule WHERE id IN (:scheduleIds)")
    List<Schedule> loadAllByIds(int[] scheduleIds);

    @Insert
    void insertAll(Schedule... schedules);

    @Delete
    void delete(Schedule schedule);
}