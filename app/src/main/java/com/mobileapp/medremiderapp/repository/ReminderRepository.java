package com.mobileapp.medremiderapp.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import com.mobileapp.medremiderapp.database.AppDatabase;
import com.mobileapp.medremiderapp.database.ReminderDao;
import com.mobileapp.medremiderapp.model.Reminder;
import java.util.List;

public class ReminderRepository {
    private ReminderDao reminderDao;

    public ReminderRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        reminderDao = db.reminderDao();
    }

    public LiveData<List<Reminder>> getRemindersForMedicine(int medicineId) {
        return reminderDao.getRemindersForMedicine(medicineId);
    }

    public void insert(Reminder reminder) {
        new Thread(() -> reminderDao.insert(reminder)).start();
    }

    public void delete(Reminder reminder) {
        new Thread(() -> reminderDao.delete(reminder)).start();
    }
}