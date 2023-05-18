package com.dryice.ed2.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {Schedule.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class ScheduleDB extends RoomDatabase {
    private static ScheduleDB INSTANCE = null;

    public abstract ScheduleDao scheduleDao();

    public static ScheduleDB getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    ScheduleDB.class, "schedule.db").build();

        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}
