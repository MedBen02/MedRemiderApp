package com.mobileapp.medremiderapp.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import com.mobileapp.medremiderapp.database.AppDatabase;
import com.mobileapp.medremiderapp.database.NotificationDao;
import com.mobileapp.medremiderapp.database.ReminderDao;
import com.mobileapp.medremiderapp.model.MedNotification;
import com.mobileapp.medremiderapp.model.Reminder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ReminderRepository {
    private final ReminderDao reminderDao;
    private final NotificationDao notificationDao;
    private final AppDatabase db;
    private final Executor executor;

    public ReminderRepository(Application application) {
        this.db = AppDatabase.getInstance(application);
        this.reminderDao = db.reminderDao();
        this.notificationDao = db.notificationDao();
        this.executor = Executors.newSingleThreadExecutor();
    }

    public void insertReminderWithNotifications(Reminder reminder, List<String> times,
                                                Date startDate, Date endDate,
                                                OnCompleteListener listener) {
        executor.execute(() -> {
            try {
                db.runInTransaction(() -> {
                    long reminderId = reminderDao.insert(reminder);

                    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
                    for (String timeStr : times) {
                        try {
                            Date time = timeFormat.parse(timeStr);
                            Calendar timeCalendar = Calendar.getInstance();
                            timeCalendar.setTime(time);

                            Calendar currentDay = Calendar.getInstance();
                            currentDay.setTime(startDate);
                            currentDay.set(Calendar.HOUR_OF_DAY, timeCalendar.get(Calendar.HOUR_OF_DAY));
                            currentDay.set(Calendar.MINUTE, timeCalendar.get(Calendar.MINUTE));

                            Calendar endDay = Calendar.getInstance();
                            endDay.setTime(endDate);

                            while (!currentDay.after(endDay)) {
                                MedNotification notification = new MedNotification(
                                        currentDay.getTimeInMillis(),
                                        "SCHEDULED",
                                        (int) reminderId
                                );
                                notificationDao.insert(notification);
                                currentDay.add(Calendar.DAY_OF_MONTH, 1);
                            }
                        } catch (ParseException e) {
                            throw new RuntimeException("Invalid time format: " + timeStr);
                        }
                    }
                    return null;
                });
                listener.onSuccess();
            } catch (Exception e) {
                listener.onError(e.getMessage());
            }
        });
    }


    public void update(Reminder reminder) {
        new Thread(() -> reminderDao.update(reminder)).start();
    }

    public void delete(Reminder reminder) {
        new Thread(() -> reminderDao.delete(reminder)).start();
    }

    public void deleteRemindersForMedicine(int medicineId) {
        new Thread(() -> reminderDao.deleteRemindersForMedicine(medicineId)).start();
    }

    public LiveData<List<Reminder>> getRemindersForMedicine(int medicineId) {
        return reminderDao.getRemindersForMedicine(medicineId);
    }

    public interface OnCompleteListener {
        void onSuccess();
        void onError(String message);
    }
}