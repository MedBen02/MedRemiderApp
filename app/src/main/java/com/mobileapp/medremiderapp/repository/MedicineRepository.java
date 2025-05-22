package com.mobileapp.medremiderapp.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import com.mobileapp.medremiderapp.database.AppDatabase;
import com.mobileapp.medremiderapp.database.MedicineDao;
import com.mobileapp.medremiderapp.model.Medicine;
import java.util.List;

public class MedicineRepository {
    private MedicineDao medicineDao;
    private LiveData<List<Medicine>> allMedicines;
    private LiveData<List<Medicine>> medicinesByUserId;

    public MedicineRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        medicineDao = db.medicineDao();
        allMedicines = medicineDao.getAllMedicines();
    }

    public LiveData<List<Medicine>> getAllMedicines() {
        return allMedicines;
    }
    public LiveData<List<Medicine>> getMedicinesByUserId(int userId) {
        medicinesByUserId = medicineDao.getMedicinesByUserId(userId);
        return medicinesByUserId;
    }

    public void insert(Medicine medicine) {
        new Thread(() -> medicineDao.insert(medicine)).start();
    }

    public void update(Medicine medicine) {
        new Thread(() -> medicineDao.update(medicine)).start();
    }

    public void delete(Medicine medicine) {
        new Thread(() -> medicineDao.delete(medicine)).start();
    }
}