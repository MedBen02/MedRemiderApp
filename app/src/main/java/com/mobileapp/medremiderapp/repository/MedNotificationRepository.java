package com.mobileapp.medremiderapp.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import com.mobileapp.medremiderapp.database.AppDatabase;
import com.mobileapp.medremiderapp.database.NotificationDao;
import com.mobileapp.medremiderapp.model.MedNotification;
import com.mobileapp.medremiderapp.model.DataFlowModels.NotificationWithDetails;

import java.util.List;

public class MedNotificationRepository {
    private NotificationDao notificationDao;

    public MedNotificationRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        notificationDao = db.notificationDao();
    }

    public LiveData<List<MedNotification>> getNotificationsForReminder(int reminderId) {
        return notificationDao.getNotificationsForReminder(reminderId);
    }

    public void insert(MedNotification notification) {
        new Thread(() -> notificationDao.insert(notification)).start();
    }

    public void updateStatus(int notificationId, String status) {
        new Thread(() -> notificationDao.updateNotificationStatus(notificationId, status)).start();
    }

    public LiveData<List<MedNotification>> getNotificationsForDateRange(long startDate, long endDate) {
        return notificationDao.getNotificationsForDateRange(startDate, endDate);
    }

    public LiveData<List<NotificationWithDetails>> getNotificationsWithDetails(long startDate, long endDate) {
        return notificationDao.getNotificationsWithDetails(startDate, endDate);
    }
}