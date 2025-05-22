package com.mobileapp.medremiderapp.ui.fragments.medicine;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.mobileapp.medremiderapp.databinding.DialogAddMedicineBinding;
import com.mobileapp.medremiderapp.databinding.FragmentMedicineBinding;
import com.mobileapp.medremiderapp.factory.MedicineViewModelFactory;
import com.mobileapp.medremiderapp.model.Medicine;
import com.mobileapp.medremiderapp.model.User;
import com.mobileapp.medremiderapp.ui.adapters.MedicineAdapter;
import com.mobileapp.medremiderapp.viewmodels.MedicineViewModel;
import com.mobileapp.medremiderapp.viewmodels.UserViewModel;
import java.util.ArrayList;

public class MedicineFragment extends Fragment {
    private FragmentMedicineBinding binding;
    private MedicineViewModel medicineViewModel;
    private MedicineAdapter adapter;
    private int currentUserId;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMedicineBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Get current user ID from UserViewModel
        UserViewModel userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        User currentUser = userViewModel.getLoggedInUser().getValue();
        if (currentUser != null) {
            currentUserId = currentUser.getId();
        }

        // Initialize ViewModel with user ID
        medicineViewModel = new ViewModelProvider(this,
                new MedicineViewModelFactory(requireActivity().getApplication(), currentUserId))
                .get(MedicineViewModel.class);

        setupRecyclerView();
        setupObservers();
        setupListeners();
    }

    private void setupRecyclerView() {
        adapter = new MedicineAdapter(new ArrayList<>());
        binding.rvMedications.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvMedications.setAdapter(adapter);
    }

    private void setupObservers() {
        medicineViewModel.getMedicinesForCurrentUser().observe(getViewLifecycleOwner(), medicines -> {
            adapter.setMedicines(medicines);
        });
    }

    private void setupListeners() {
        binding.btnAddMedicine.setOnClickListener(v -> showAddMedicineDialog());
    }

    private void showAddMedicineDialog() {
        DialogAddMedicineBinding dialogBinding = DialogAddMedicineBinding.inflate(getLayoutInflater());
        new android.app.AlertDialog.Builder(requireContext())
                .setView(dialogBinding.getRoot())
                .setTitle("Add New Medicine")
                .setPositiveButton("Save", (dialog, which) -> {
                    String name = dialogBinding.etMedicineName.getText().toString();
                    String dose = dialogBinding.etMedicineDose.getText().toString();
                    String stockStr = dialogBinding.etMedicinePillCount.getText().toString();
                    String description = dialogBinding.etMedicineDescription.getText().toString();

                    int stock = stockStr.isEmpty() ? 0 : Integer.parseInt(stockStr);

                    Medicine medicine = new Medicine(name, description, dose, stock, currentUserId);
                    medicineViewModel.insert(medicine);
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}