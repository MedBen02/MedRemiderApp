package com.mobileapp.medremiderapp.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mobileapp.medremiderapp.R;
import com.mobileapp.medremiderapp.model.MedNotification;
import com.mobileapp.medremiderapp.model.Medicine;
import com.mobileapp.medremiderapp.model.DataFlowModels.NotificationWithDetails;
import com.mobileapp.medremiderapp.model.Reminder;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    public final List<NotificationWithDetails> notificationList;
    private final OnNotificationActionListener listener;

    public interface OnNotificationActionListener {
        void onDismissClick(MedNotification notification);
        void onTakeClick(MedNotification notification);
    }

    public NotificationAdapter(List<NotificationWithDetails> notificationList,
                               OnNotificationActionListener listener) {
        this.notificationList = notificationList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notification, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        NotificationWithDetails notificationWithDetails = notificationList.get(position);
        MedNotification notification = notificationWithDetails.notification;
        Reminder reminder = notificationWithDetails.reminderWithMedicine.reminder;
        Medicine medicine = notificationWithDetails.reminderWithMedicine.medicine;

        // Format time
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a", Locale.getDefault());
        String time = timeFormat.format(notification.getNotificationTime());
        holder.tvTime.setText(time);

        // Set status text and background
        holder.tvStatus.setText(notification.getStatus());
        int backgroundResId;
        switch (notification.getStatus()) {
            case MedNotification.STATUS_TAKEN:
                backgroundResId = R.drawable.status_taken_background;
                break;
            case MedNotification.STATUS_DISMISSED:
                backgroundResId = R.drawable.status_missed_background;
                break;
            case MedNotification.STATUS_SCHEDULED:
            default:
                backgroundResId = R.drawable.status_pending_background;
                break;
        }
        holder.tvStatus.setBackgroundResource(backgroundResId);

        // Set medicine information
        if (medicine != null) {
            holder.tvMedicineName.setText(medicine.getName());
            holder.tvDose.setText(medicine.getDose());
        }

        if (reminder != null) {
            holder.tvUnits.setText(String.valueOf(reminder.getNumberOfUnits()));
        }

        // Set button listeners
        holder.btnDismiss.setOnClickListener(v -> listener.onDismissClick(notification));
        holder.btnTake.setOnClickListener(v -> listener.onTakeClick(notification));

        // Show/hide buttons based on status
        if (notification.getStatus().equals(MedNotification.STATUS_SCHEDULED)) {
            holder.btnDismiss.setVisibility(View.VISIBLE);
            holder.btnTake.setVisibility(View.VISIBLE);
        } else {
            holder.btnDismiss.setVisibility(View.GONE);
            holder.btnTake.setVisibility(View.GONE);
        }
    }
    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    public static class NotificationViewHolder extends RecyclerView.ViewHolder {
        TextView tvTime, tvStatus, tvMedicineName, tvDose, tvUnits;
        Button btnDismiss, btnTake;

        public NotificationViewHolder(View itemView) {
            super(itemView);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvMedicineName = itemView.findViewById(R.id.tvMedicineName);
            tvDose = itemView.findViewById(R.id.tvDose);
            tvUnits = itemView.findViewById(R.id.tvUnits);
            btnDismiss = itemView.findViewById(R.id.btnDismiss);
            btnTake = itemView.findViewById(R.id.btnTake);
        }
    }
}