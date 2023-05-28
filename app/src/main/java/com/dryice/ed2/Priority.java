package com.dryice.ed2;

import android.content.Context;
import android.util.Log;

import com.dryice.ed2.database.Schedule;
import com.dryice.ed2.database.ScheduleDB;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Priority {
    private List<Schedule> scheduleList;
    private ScheduleDB scheduleDB = null;

    HashMap<String,Integer> map = new HashMap<String,Integer>(){{
        put("A",10);
        put("B",5);
        put("C",0);
    }};

    public void cal_sum(Context mContext) {
        scheduleDB = ScheduleDB.getInstance(mContext);
        scheduleList = scheduleDB.scheduleDao().getAll();
        ArrayList<Schedule> arrayList = new ArrayList<Schedule>();
        arrayList.addAll(scheduleList);
        int temp = 0;
        for(int i = 0;i<arrayList.size();i++) {
            int tid = arrayList.get(i).id;
            temp = map.get(scheduleDB.scheduleDao().getImportance(tid))+(13-cal_date(arrayList.get(i).deadline)*3);
            scheduleDB.scheduleDao().updateSum(tid,temp);
        }
    } //우선순위 합계 계산
    public int cal_date(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        Date now = new Date();
        String conversion = dateFormat.format(now);
        ParsePosition position = new ParsePosition(0);
        Date today = dateFormat.parse(conversion,position);

        long Sec = (date.getTime() - today.getTime()) / 1000; // 초
        long Days = (Sec / (24*60*60)); // 일자수

        return (int)Days;
    } //날짜 계산
}
