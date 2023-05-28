package com.dryice.ed2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;

import com.dryice.ed2.adapter.MainRecyclerAdapter;
import com.dryice.ed2.database.Schedule;
import com.dryice.ed2.database.ScheduleDB;

import java.util.Collections;
import java.util.List;

public class MainListActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private MainRecyclerAdapter mainRecyclerAdapter;
    private List<Schedule> scheduleList;
    private ScheduleDB scheduleDB = null;
    private Context mContext = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_list);

        mRecyclerView = (RecyclerView) findViewById(R.id.main_recycler_view);

        mContext = getApplicationContext();
        mainRecyclerAdapter = new MainRecyclerAdapter(scheduleList);
        // DB 생성
        scheduleDB = ScheduleDB.getInstance(this);


        // main thread에서 DB 접근 불가 => data 읽고 쓸 때 thread 사용하기
        class InsertRunnable implements Runnable {

            @Override
            public void run() {
                try {
                    Priority priority = new Priority();
                    priority.cal_sum(mContext);

                    scheduleList = ScheduleDB.getInstance(mContext).scheduleDao().getAll();
                    Collections.sort(scheduleList, (a, b) -> b.sum - a.sum);
                    mainRecyclerAdapter = new MainRecyclerAdapter(scheduleList);
                    mainRecyclerAdapter.notifyDataSetChanged();

                    mRecyclerView.setAdapter(mainRecyclerAdapter);
                    LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
                    mRecyclerView.setLayoutManager(mLinearLayoutManager);
                }
                catch (Exception e) {

                }
            }
        }
        InsertRunnable insertRunnable = new InsertRunnable();
        Thread t = new Thread(insertRunnable);
        t.start();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ScheduleDB.destroyInstance();
        scheduleDB = null;
    }
}
