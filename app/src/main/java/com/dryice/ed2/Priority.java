package com.dryice.ed2;

import com.dryice.ed2.database.Schedule;
import com.dryice.ed2.database.ScheduleDB;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
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

    public void cal_sum(ScheduleDB scheduleDB) {
        this.scheduleDB = scheduleDB;
        scheduleList = scheduleDB.scheduleDao().getAll();
        ArrayList<Schedule> arrayList = new ArrayList<Schedule>();
        arrayList.addAll(scheduleList);
        int temp = 0;
        for(int i = 0;i<arrayList.size();i++) {
            int tid = arrayList.get(i).id;
            int dDay = cal_date(arrayList.get(i).deadline);
            if(dDay != 0)
                temp = map.get(scheduleDB.scheduleDao().getImportance(tid))+(10-(dDay-1)*3);
            else
                temp = map.get(scheduleDB.scheduleDao().getImportance(tid))+15;
            scheduleDB.scheduleDao().updateSum(tid,temp);
        }
    } //우선순위 합계 계산
    public int cal_date(Date date) {
        Date today = getToday();

        long Sec = (date.getTime() - today.getTime()) / 1000; // 초
        long Days = (Sec / (24*60*60)); // 일자수

        return (int)Days;
    } //날짜 계산

    // 오늘 날짜 구하기
    public Date getToday() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        Date now = new Date();
        String conversion = dateFormat.format(now);
        ParsePosition position = new ParsePosition(0);
        Date today = dateFormat.parse(conversion,position);

        return today;
    }
}
