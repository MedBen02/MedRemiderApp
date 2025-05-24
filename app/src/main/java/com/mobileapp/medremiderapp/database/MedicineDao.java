package com.mobileapp.medremiderapp.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.mobileapp.medremiderapp.model.DataFlowModels.ReminderWithNotifications;
import com.mobileapp.medremiderapp.model.Medicine;
import java.util.List;

@Dao
public interface MedicineDao {
    @Insert
    void insert(Medicine medicine);

    @Update
    void update(Medicine medicine);

    @Delete
    void delete(Medicine medicine);

    @Query("DELETE FROM Medicines WHERE id = :medicineId")
    void deleteById(int medicineId);

    @Query("SELECT * FROM Medicines ORDER BY name ASC")
    LiveData<List<Medicine>> getAllMedicines();

    @Query("SELECT * FROM Medicines WHERE id = :medicineId")
    LiveData<Medicine> getMedicineById(int medicineId);

    @Query("SELECT * FROM Medicines WHERE name LIKE :searchQuery || '%'")
    LiveData<List<Medicine>> searchMedicines(String searchQuery);

    @Query("SELECT * FROM Medicines WHERE userId = :userId")
    LiveData<List<Medicine>> getMedicinesByUserId(int userId);

    @Transaction
    @Query("SELECT * FROM reminders WHERE medicineId = :medicineId")
    LiveData<List<ReminderWithNotifications>> getRemindersWithNotifications(int medicineId);

}