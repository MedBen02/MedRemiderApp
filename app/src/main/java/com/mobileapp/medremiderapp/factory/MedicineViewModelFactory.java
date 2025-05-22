package com.mobileapp.medremiderapp.factory;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.mobileapp.medremiderapp.viewmodels.MedicineViewModel;

public class MedicineViewModelFactory implements ViewModelProvider.Factory {
    private Application application;
    private int userId;

    public MedicineViewModelFactory(Application application, int userId) {
        this.application = application;
        this.userId = userId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new MedicineViewModel(application, userId);
    }
}