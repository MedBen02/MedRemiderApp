package com.mobileapp.medremiderapp.viewmodels;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.mobileapp.medremiderapp.model.DataFlowModels.ReminderWithNotifications;
import com.mobileapp.medremiderapp.model.Medicine;
import com.mobileapp.medremiderapp.repository.MedicineRepository;
import java.util.List;

public class MedicineViewModel extends AndroidViewModel {
    private MedicineRepository repository;
    private LiveData<List<Medicine>> allMedicines;
    private int currentUserId;

    public MedicineViewModel(Application application, int userId) {
        super(application);
        repository = new MedicineRepository(application);
        currentUserId = userId;
        allMedicines = repository.getAllMedicines();
    }

    public void insert(Medicine medicine) {
        medicine.setUserId(currentUserId);
        repository.insert(medicine);
    }

    public void update(Medicine medicine) {
        repository.update(medicine);
    }

    public void delete(Medicine medicine) {
        repository.delete(medicine);
    }

    public LiveData<List<Medicine>> getAllMedicines() {
        return allMedicines;
    }

    public LiveData<List<Medicine>> getMedicinesForCurrentUser() {
        return repository.getMedicinesByUserId(currentUserId);
    }

    public LiveData<List<ReminderWithNotifications>> getRemindersWithNotifications(int medicineId) {
        return repository.getRemindersWithNotifications(medicineId);
    }
}