package com.mobileapp.medremiderapp.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.mobileapp.medremiderapp.model.MedNotification;
import com.mobileapp.medremiderapp.model.Medicine;
import com.mobileapp.medremiderapp.model.Reminder;
import com.mobileapp.medremiderapp.model.User;
import com.mobileapp.medremiderapp.utils.Converters;

@Database(entities = {User.class, Medicine.class, Reminder.class, MedNotification.class},
        version = 2,
        exportSchema = false)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    private static volatile AppDatabase INSTANCE;


    public abstract UserDao userDao();
    public abstract MedicineDao medicineDao();
    public abstract ReminderDao reminderDao();
    public abstract NotificationDao notificationDao();

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "med_reminder.db"
                    )
//                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
