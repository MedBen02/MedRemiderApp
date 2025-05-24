package com.mobileapp.medremiderapp.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.mobileapp.medremiderapp.model.Reminder;
import com.mobileapp.medremiderapp.repository.ReminderRepository;

import java.util.Date;
import java.util.List;

public class ReminderViewModel extends AndroidViewModel {
    private final ReminderRepository repository;
    LiveData<List<Reminder>> remindersForMedicine;

    public ReminderViewModel(@NonNull Application application) {
        super(application);
        repository = new ReminderRepository(application);
    }

    public void insertReminderWithNotifications(Reminder reminder, List<String> times,
                                                Date startDate, Date endDate,
                                                ReminderRepository.OnCompleteListener listener) {
        repository.insertReminderWithNotifications(reminder, times, startDate, endDate, listener);
    }


    public void update(Reminder reminder) {
        repository.update(reminder);
    }

    public void delete(Reminder reminder) {
        repository.delete(reminder);
    }

    public void deleteRemindersForMedicine(int medicineId) {
        repository.deleteRemindersForMedicine(medicineId);
    }

    public LiveData<List<Reminder>> getRemindersForMedicine(int medicineId) {
        remindersForMedicine = repository.getRemindersForMedicine(medicineId);
        return remindersForMedicine;
    }


}


