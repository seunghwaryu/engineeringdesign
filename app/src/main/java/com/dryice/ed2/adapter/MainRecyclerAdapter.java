package com.dryice.ed2.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.dryice.ed2.R;
import com.dryice.ed2.database.Schedule;
import com.dryice.ed2.database.ScheduleDB;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.List;

public class MainRecyclerAdapter extends RecyclerView.Adapter<MainRecyclerAdapter.ViewHolder> {
    private List<Schedule> scheduleList;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
    private ScheduleDB scheduleDB;
    public MainRecyclerAdapter(List<Schedule> list,ScheduleDB scheduleDB) {
        scheduleList = list;
        this.scheduleDB = scheduleDB;
    }

    Context context;

    public MainRecyclerAdapter(Context context){
        this.context = context;
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
        if (position % 2 == 0) {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.white));
        } else {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.gray));
        } //리사이클러뷰에서 홀짝마다 색상 변경
    }

    @Override
    public int getItemCount() {
        return scheduleList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView deadline;
        TextView imp;
        AppCompatButton trash_btn;
        CheckBox checkBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.item_name);
            deadline = itemView.findViewById(R.id.item_deadline);
            imp = itemView.findViewById(R.id.item_imp);
            trash_btn = itemView.findViewById(R.id.item_trash);
            checkBox = itemView.findViewById(R.id.item_checkBox);

        }

        public void onBind(Schedule item, int position) {
            name.setText(item.name);
            String date = dateFormat.format(item.deadline);
            deadline.setText(date);
            imp.setText(item.importance);
            checkBox.setChecked(item.checked);
            strikethrough(item.checked);

            //일정 삭제
            trash_btn.setOnClickListener(v ->  {
                scheduleList.remove(item);
                notifyDataSetChanged();

                class DeleteRunnable implements Runnable {
                    @Override
                    public void run() {
                        try {
                            scheduleDB.scheduleDao().delete(item);
                        }
                        catch (Exception e) {

                        }
                    }
                }
                DeleteRunnable deleteRunnable = new DeleteRunnable();
                Thread t = new Thread(deleteRunnable);
                t.start();
            });

            // 일정 밑줄 긋기
            checkBox.setOnClickListener(v -> {
                class UpateRunnable implements Runnable {
                    @Override
                    public void run() {
                        try {
                            scheduleDB.scheduleDao().updateChecked(item.id,!item.checked);
                            item.checked = scheduleDB.scheduleDao().getChecked(item.id);
                        }
                        catch (Exception e) {

                        }
                    }
                }
                UpateRunnable upateRunnable = new UpateRunnable();
                Thread t = new Thread(upateRunnable);

                t.start();
                strikethrough(!item.checked);

            });
        }

        // 체크 여부에 따라 밑줄 긋기
        void strikethrough(boolean cheak) {
            if(cheak) {
                name.setPaintFlags(name.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);

            } else {
                name.setPaintFlags(0);

            }
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
