package com.mobileapp.medremiderapp.viewmodels;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.mobileapp.medremiderapp.model.MedNotification;
import com.mobileapp.medremiderapp.repository.MedNotificationRepository;
import java.util.List;

public class NotificationViewModel extends AndroidViewModel {
    private MedNotificationRepository repository;

    public NotificationViewModel(Application application) {
        super(application);
        repository = new MedNotificationRepository(application);
    }

    public LiveData<List<MedNotification>> getNotificationsForReminder(int reminderId) {
        return repository.getNotificationsForReminder(reminderId);
    }

    public void insert(MedNotification notification) {
        repository.insert(notification);
    }

    public void updateStatus(int notificationId, String status) {
        repository.updateStatus(notificationId, status);
    }
}