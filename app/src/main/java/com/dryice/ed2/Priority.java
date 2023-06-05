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
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
    Date now = new Date();
    String conversion = dateFormat.format(now);
    ParsePosition position = new ParsePosition(0);
    Date today = dateFormat.parse(conversion,position);

    HashMap<String,Integer> importance = new HashMap<String,Integer>(){{
        put("A",10);
        put("B",5);
        put("C",0);
    }};

    public void cal_sum(ScheduleDB scheduleDB) {
        this.scheduleDB = scheduleDB;
        scheduleList = scheduleDB.scheduleDao().getAll();
        int temp = 0;
        for(int i = 0;i<scheduleList.size();i++) {
            int tid = scheduleList.get(i).id;
            int dDay = cal_date(scheduleList.get(i).deadline);
            if(dDay != 0)
                temp = importance.get(scheduleDB.scheduleDao().getImportance(tid))+(10-(dDay-1)*3);
            else
                temp = importance.get(scheduleDB.scheduleDao().getImportance(tid))+15;
            scheduleDB.scheduleDao().updateSum(tid,temp);
        }
    } //우선순위 합계 계산
    public int cal_date(Date date) {
        long Sec = (date.getTime() - today.getTime()) / 1000; // 초
        long Days = (Sec / (24*60*60)); // 일자수

        return (int)Days;
    } //날짜 계산

    public int get_newSum(Date newDate, String imp) {
        int temp = 0;
        int dDay = cal_date(newDate);
        if(dDay != 0)
            temp = importance.get(imp)+(10-(dDay-1)*3);
        else
            temp = importance.get(imp)+15;
        return temp;
    }


}
