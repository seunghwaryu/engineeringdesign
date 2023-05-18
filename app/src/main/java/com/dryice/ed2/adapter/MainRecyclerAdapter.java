package com.dryice.ed2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dryice.ed2.R;
import com.dryice.ed2.database.Schedule;

import java.text.SimpleDateFormat;
import java.util.List;

public class MainRecyclerAdapter extends RecyclerView.Adapter<MainRecyclerAdapter.ViewHolder> {

    private List<Schedule> scheduleList;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public MainRecyclerAdapter(List<Schedule> list) {
        scheduleList = list;
    }

    public interface OnItemClickListener {
        void onItemClicked(int position);
    }

    private MainRecyclerAdapter.OnItemClickListener itemClickListener;


    public void setOnItemClickListener (MainRecyclerAdapter.OnItemClickListener listener) {
        itemClickListener = listener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.main_list_item, parent, false);
        MainRecyclerAdapter.ViewHolder vh = new MainRecyclerAdapter.ViewHolder(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = vh.getAdapterPosition();
//                Context context = view.getContext();
//                Intent intent = new Intent(context,BookListActivity.class);
//                ((MainActivity)context).startActivity(intent);
            }
        });

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Schedule item = scheduleList.get(position);
        holder.name.setText(item.name);
        String date = dateFormat.format(item.deadline);
        holder.deadline.setText(date);
        holder.imp.setText(item.importance);
    }

    @Override
    public int getItemCount() {
        return scheduleList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView deadline;
        TextView imp;

        ViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.item_name);
            deadline = itemView.findViewById(R.id.item_deadline);
            imp = itemView.findViewById(R.id.item_imp);

        }
    }
}
