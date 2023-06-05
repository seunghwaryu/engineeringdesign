package com.dryice.ed2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;

import com.dryice.ed2.adapter.MainRecyclerAdapter;
import com.dryice.ed2.database.Schedule;
import com.dryice.ed2.database.ScheduleDB;

import java.util.Comparator;
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

        // main thread에서 DB 접근 불가 => data 읽고 쓸 때 thread 사용하기
        class InsertRunnable implements Runnable {

            @Override
            public void run() {
                try {
                    scheduleDB = ScheduleDB.getInstance(mContext);
                    scheduleList = scheduleDB.scheduleDao().getAll();
                    scheduleList.sort(Comparator.comparing(Schedule::getSum).reversed().thenComparing(Schedule::getName)); // 우선순위 순 정렬
                    mainRecyclerAdapter = new MainRecyclerAdapter(scheduleList,scheduleDB);

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

        AppCompatButton rank_order_btn = findViewById(R.id.rank_order_btn);
        rank_order_btn.setOnClickListener(v -> {
            mainRecyclerAdapter.orderPriority();
        });
        AppCompatButton deadline_order_btn = findViewById(R.id.deadline_order_btn);
        deadline_order_btn.setOnClickListener(v -> {
            mainRecyclerAdapter.orderUrgency();
        });

        AppCompatButton imp_order_btn = findViewById(R.id.imp_order_btn);
        imp_order_btn.setOnClickListener(v -> {
            mainRecyclerAdapter.orderImportance();
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ScheduleDB.destroyInstance();
        scheduleDB = null;
    }
}
