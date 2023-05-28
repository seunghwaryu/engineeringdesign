package com.dryice.ed2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.dryice.ed2.database.Schedule;
import com.dryice.ed2.database.ScheduleDB;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddScheduleActivity extends AppCompatActivity {

    private ScheduleDB scheduleDB = null;
    private Context mContext;
    private EditText mEditTextName;
    private EditText mEditTextDeadline;
    private RadioGroup mradioGroup;
    private Button mAddButton;
    private String improtance = "not inputted";

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
    Date date = null;

    Calendar myCalendar = Calendar.getInstance();

    DatePickerDialog.OnDateSetListener myDatePicker = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            String input_date = year+"/"+(month+1)+"/"+dayOfMonth;
            mEditTextDeadline.setText(input_date);
            try {
                date = dateFormat.parse(input_date);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.activity_add_schedule);

        mAddButton = (Button) findViewById(R.id.button_add);
        mEditTextName = (EditText) findViewById(R.id.edit_name);
        mEditTextDeadline = (EditText) findViewById(R.id.edit_deadline);

        scheduleDB = ScheduleDB.getInstance(this);
        mContext = getApplicationContext();

        class InsertRunnable implements Runnable {

            @Override
            public void run() {
                Schedule schedule = new Schedule();
                schedule.name = mEditTextName.getText().toString();
                schedule.deadline = date;
                schedule.importance = improtance;
                schedule.sum = 0;
                ScheduleDB.getInstance(mContext).scheduleDao().insertAll(schedule);
            }
        }
        mEditTextDeadline.setInputType(0);
        mEditTextDeadline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(AddScheduleActivity.this, myDatePicker,
                        myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        mradioGroup = (RadioGroup) findViewById(R.id.radio_group);
        mradioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.raido_a:
                        improtance = "A";
                        break;
                    case R.id.raido_b:
                        improtance = "B";
                        break;
                    case R.id.raido_c:
                        improtance = "C";
                        break;
                }
            }
        });

        mAddButton.setOnClickListener(v -> {
            if(improtance.equals("not inputted")) {
                Toast.makeText(getApplicationContext(), "중요도를 선택해주세요.", Toast.LENGTH_SHORT).show();
            }
            else {
                InsertRunnable insertRunnable = new InsertRunnable();
                Thread addThread = new Thread(insertRunnable);
                addThread.start();

                Intent intent = new Intent(getApplicationContext(), MainListActivity.class);
                startActivity(intent);
                finish();
            }

        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ScheduleDB.destroyInstance();
    }

    //팝업 밖 터치시 팝업 종료 방지
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_OUTSIDE) {
            return false;
        }
        return true;
    }
}