package com.dryice.ed2.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dryice.ed2.R;
import com.dryice.ed2.database.Schedule;
import com.dryice.ed2.database.ScheduleDB;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainRecyclerAdapter extends RecyclerView.Adapter<MainRecyclerAdapter.ViewHolder> {

    private List<Schedule> scheduleList;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
    private ScheduleDB scheduleDB;
    public MainRecyclerAdapter(List<Schedule> list) {
        scheduleList = list;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.main_list_item, parent, false);
        MainRecyclerAdapter.ViewHolder vh = new MainRecyclerAdapter.ViewHolder(view);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.onBind(scheduleList.get(position),position);

    }

    @Override
    public int getItemCount() {
        return scheduleList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView deadline;
        TextView imp;
        CheckBox checkBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.item_name);
            deadline = itemView.findViewById(R.id.item_deadline);
            imp = itemView.findViewById(R.id.item_imp);
            checkBox = itemView.findViewById(R.id.item_cheakBox);

        }

        public void onBind(Schedule item, int position) {
            name.setText(item.name);
            String date = dateFormat.format(item.deadline);
            deadline.setText(date);
            imp.setText(item.importance);

            checkBox.setOnClickListener(v ->  {
                scheduleList.remove(item);

                class InsertRunnable implements Runnable {
                    @Override
                    public void run() {
                        try {
                            scheduleDB.getInstance(itemView.getContext()).scheduleDao().delete(item);
                        }
                        catch (Exception e) {

                        }
                    }
                }
                InsertRunnable insertRunnable = new InsertRunnable();
                Thread t = new Thread(insertRunnable);
                t.start();
                checkBox.setChecked(false);
                notifyDataSetChanged();
            });
        }
    }

    public void orderPriority(){
        scheduleList.sort(Comparator.comparing(Schedule::getSum).reversed().thenComparing(Schedule::getName)); // 우선순위 순 정렬
        notifyDataSetChanged();
    }
    public void orderUrgency(){
        scheduleList.sort(Comparator.comparing(Schedule::getDeadline).thenComparing(Schedule::getName)); // 긴급도 순 정렬
        notifyDataSetChanged();
    }
    public void orderImportance(){
        scheduleList.sort(Comparator.comparing(Schedule::getImportance).thenComparing(Schedule::getName)); // 중요도 순 정렬
        notifyDataSetChanged();
    }

}
