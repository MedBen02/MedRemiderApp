package com.mobileapp.medremiderapp.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.mobileapp.medremiderapp.model.DataFlowModels.NotificationWithDetails;
import com.mobileapp.medremiderapp.repository.MedNotificationRepository;

import java.util.List;

public class HomeViewModel extends AndroidViewModel {
    private final MedNotificationRepository notificationRepository;
    private final MutableLiveData<List<NotificationWithDetails>> notificationsWithDetails = new MutableLiveData<>();
    public HomeViewModel(@NonNull Application application) {
        super(application);
        notificationRepository = new MedNotificationRepository(application);
    }

    public LiveData<List<NotificationWithDetails>> getNotificationsWithDetails() {
        return notificationsWithDetails;
    }

    public void loadNotificationsForDate(long dateInMillis) {
        long startOfDay = dateInMillis;
        long endOfDay = startOfDay + 24 * 60 * 60 * 1000 - 1;

        notificationRepository.getNotificationsWithDetails(startOfDay, endOfDay)
                .observeForever(notifications -> {
                    notificationsWithDetails.setValue(notifications);
                });
    }
    public void updateNotificationStatus(int notificationId, String status) {
        notificationRepository.updateStatus(notificationId, status);
    }
}