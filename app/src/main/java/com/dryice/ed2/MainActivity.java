package com.dryice.ed2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.dryice.ed2.database.Schedule;
import com.dryice.ed2.database.ScheduleDB;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<Schedule> scheduleList;
    private ScheduleDB scheduleDB = null;
    private Context mContext = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = getApplicationContext();
        // DB 생성
        scheduleDB = ScheduleDB.getInstance(this);


        // main thread에서 DB 접근 불가 => data 읽고 쓸 때 thread 사용하기
        class InsertRunnable implements Runnable {

            @Override
            public void run() {
                try {
                    scheduleList = ScheduleDB.getInstance(mContext).scheduleDao().getAll();
                }
                catch (Exception e) {

                }
            }
        }
        InsertRunnable insertRunnable = new InsertRunnable();
        Thread t = new Thread(insertRunnable);
        t.start();

        Button add_button = findViewById(R.id.add_schedule_button);
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddScheduleActivity.class);
                startActivity(intent);
            }
        });

        Button list_button = findViewById(R.id.open_schedule_button);
        list_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainListActivity.class);
                startActivity(intent);
            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ScheduleDB.destroyInstance();
        scheduleDB = null;
    }
}