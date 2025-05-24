package com.mobileapp.medremiderapp.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mobileapp.medremiderapp.R;
import com.mobileapp.medremiderapp.model.DateModel;

import java.util.List;

public class DateAdapter extends RecyclerView.Adapter<DateAdapter.DateViewHolder> {

    private final List<DateModel> dateList;
    private int selectedPosition = -1;

    public interface OnDateClickListener {
        void onDateClick(DateModel dateModel);
    }

    private final OnDateClickListener listener;

    public DateAdapter(List<DateModel> dateList, OnDateClickListener listener) {
        this.dateList = dateList;
        this.listener = listener;
    }

    public static class DateViewHolder extends RecyclerView.ViewHolder {
        TextView tvDay, tvDate;
        View container;

        public DateViewHolder(View itemView) {
            super(itemView);
            tvDay = itemView.findViewById(R.id.tvDay);
            tvDate = itemView.findViewById(R.id.tvDate);
            container = itemView;
        }
    }

    @NonNull
    @Override
    public DateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_date, parent, false);
        return new DateViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DateViewHolder holder, int position) {
        DateModel model = dateList.get(position);
        holder.tvDay.setText(model.getDay());
        holder.tvDate.setText(model.getDate());

        holder.container.setBackgroundResource(model.isSelected()
                ? R.drawable.bg_date_selected
                : R.drawable.bg_date_unselected);

        holder.container.setOnClickListener(v -> {
            for (int i = 0; i < dateList.size(); i++) {
                dateList.get(i).setSelected(i == position);
            }
            notifyDataSetChanged();
            listener.onDateClick(model);
        });
    }

    @Override
    public int getItemCount() {
        return dateList.size();
    }
}
