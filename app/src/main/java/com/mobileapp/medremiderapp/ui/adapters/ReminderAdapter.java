package com.mobileapp.medremiderapp.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mobileapp.medremiderapp.R;
import com.mobileapp.medremiderapp.model.MedNotification;
import com.mobileapp.medremiderapp.model.Reminder;
import com.mobileapp.medremiderapp.model.DataFlowModels.ReminderWithNotifications;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ReminderViewHolder> {

    public final List<ReminderWithNotifications> reminderList;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
    private final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

    public ReminderAdapter(List<ReminderWithNotifications> reminderList) {
        this.reminderList = reminderList;
    }

    @NonNull
    @Override
    public ReminderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.basic_reminder_item, parent, false);
        return new ReminderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReminderViewHolder holder, int position) {
        ReminderWithNotifications reminderWithNotifications = reminderList.get(position);
        Reminder reminder = reminderWithNotifications.reminder;

        // Set frequency text directly
        holder.tvFrequency.setText(reminder.getFrequency() + " times per day");

        // Set units
        holder.tvUnits.setText(String.valueOf(reminder.getNumberOfUnits()));

        // Set dates
        holder.tvStartDate.setText(dateFormat.format(new Date(reminder.getStartDate())));
        holder.tvEndDate.setText(dateFormat.format(new Date(reminder.getEndDate())));

        // Display actual notification times from database
        StringBuilder timesBuilder = new StringBuilder();
        if (reminderWithNotifications.notifications != null && !reminderWithNotifications.notifications.isEmpty()) {
            for (MedNotification notification : reminderWithNotifications.notifications) {
                if (timesBuilder.length() > 0) {
                    timesBuilder.append(" - ");
                }
                timesBuilder.append(timeFormat.format(new Date(notification.getNotificationTime())));
            }
        }
        holder.tvTimes.setText(timesBuilder.toString());
    }

    @Override
    public int getItemCount() {
        return reminderList.size();
    }

    public static class ReminderViewHolder extends RecyclerView.ViewHolder {
        TextView tvFrequency, tvUnits, tvStartDate, tvEndDate, tvTimes;

        public ReminderViewHolder(View itemView) {
            super(itemView);
            tvFrequency = itemView.findViewById(R.id.tvFrequency);
            tvUnits = itemView.findViewById(R.id.tvUnits);
            tvStartDate = itemView.findViewById(R.id.tvStartDate);
            tvEndDate = itemView.findViewById(R.id.tvEndDate);
            tvTimes = itemView.findViewById(R.id.tvTimes);
        }
    }
}