package com.dryice.ed2;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.dryice.ed2.database.Schedule;
import com.dryice.ed2.database.ScheduleDB;

import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<Schedule> scheduleList;
    private ScheduleDB scheduleDB = null;
    private Context mContext = null;
    private Button zeroth_button;
    private Button first_button;
    private Button second_button;
    private Button third_button;
    private Button list_button;
    private Button info_button;
    private Button add_button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = getApplicationContext();

        zeroth_button = findViewById(R.id.zeroth_button);
        first_button = findViewById(R.id.first_button);
        second_button = findViewById(R.id.second_button);
        third_button = findViewById(R.id.third_button);


        add_button = findViewById(R.id.add_schedule_button);
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddScheduleActivity.class);
                startActivity(intent);
            }
        });

        list_button = findViewById(R.id.open_schedule_button);
        list_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainListActivity.class);
                startActivity(intent);
            }
        });

        info_button = findViewById(R.id.info_btn);
        info_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), InfoActivity.class);
                startActivity(intent);
            }
        });

        zeroth_button.setOnClickListener(v -> {
            dialog(0);
        });

        first_button.setOnClickListener(v -> {
            dialog(1);
        });

        second_button.setOnClickListener(v -> {
            dialog(2);
        });

        third_button.setOnClickListener(v -> {
            dialog(3);
        });
    }
    class ReadRunnable implements Runnable {

        @Override
        public void run() {
            try {
                Priority priority = new Priority();
                scheduleDB = ScheduleDB.getInstance(mContext);
                scheduleList = scheduleDB.scheduleDao().getTodayList(priority.today);
                scheduleList.sort(Comparator.comparing(Schedule::getSum).reversed().thenComparing(Schedule::getName));
                show_todaySchedule(scheduleList);
                priority.cal_sum(scheduleDB);

            }
            catch (Exception e) {

            }
        }
    }

    public void show_todaySchedule(List<Schedule> list) {
        switch (list.size()) {
            case 0:
                zeroth_button.setText("오늘의 일정 완료");
                break;
            case 1:
                zeroth_button.setText(list.get(0).name);
                first_button.setText("1순위");
                break;
            case 2:
                zeroth_button.setText(list.get(0).name);
                first_button.setText(list.get(1).name);
                second_button.setText("2순위");
                break;
            case 3:
                zeroth_button.setText(list.get(0).name);
                first_button.setText(list.get(1).name);
                second_button.setText(list.get(2).name);
                third_button.setText("3순위");
                break;
            case 4:
                zeroth_button.setText(list.get(0).name);
                first_button.setText(list.get(1).name);
                second_button.setText(list.get(2).name);
                third_button.setText(list.get(3).name);
                break;
        }
    }

    //일정 완료 확인 팝업창
    public void dialog(int index) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("일정을 완료했나요?");
        builder.setPositiveButton(" YES", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int id)
            {
                class DeleteRunnable implements Runnable {

                    @Override
                    public void run() {
                        try {
                            scheduleDB = ScheduleDB.getInstance(mContext);
                            scheduleDB.scheduleDao().delete(scheduleList.get(index));
                            scheduleList.remove(index);
                            show_todaySchedule(scheduleList);

                        }
                        catch (Exception e) {

                        }
                    }
                }
                DeleteRunnable deleteRunnable = new DeleteRunnable();
                Thread t = new Thread(deleteRunnable);
                t.start();
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int id)
            {

            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        ReadRunnable readRunnable = new ReadRunnable();
        Thread t = new Thread(readRunnable);
        t.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ScheduleDB.destroyInstance();
        scheduleDB = null;
    } //메모리 누수 방지
}