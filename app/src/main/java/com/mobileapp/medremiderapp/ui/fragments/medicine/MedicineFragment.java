package com.mobileapp.medremiderapp.ui.fragments.medicine;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.mobileapp.medremiderapp.R;
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
    private Medicine selectedMedicine = null;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMedicineBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        UserViewModel userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        User currentUser = userViewModel.getLoggedInUser().getValue();
        if (currentUser != null) {
            currentUserId = currentUser.getId();
        }

        medicineViewModel = new ViewModelProvider(this,
                new MedicineViewModelFactory(requireActivity().getApplication(), currentUserId))
                .get(MedicineViewModel.class);

        setupRecyclerView();
        setupObservers();
        setupListeners();
    }

    private void setupRecyclerView() {
        adapter = new MedicineAdapter(new ArrayList<>(), (medicine, isSelected) -> {
            if (isSelected) {
                selectedMedicine = medicine;
                binding.btnEditMedicine.setVisibility(View.VISIBLE);
                binding.btnDeleteMedicine.setVisibility(View.VISIBLE);
            } else {
                selectedMedicine = null;
                binding.btnEditMedicine.setVisibility(View.GONE);
                binding.btnDeleteMedicine.setVisibility(View.GONE);
            }
        });
        binding.rvMedications.setAdapter(adapter);
    }

    private void setupObservers() {
        medicineViewModel.getMedicinesForCurrentUser().observe(getViewLifecycleOwner(), medicines -> {
            adapter.setMedicines(medicines);
            selectedMedicine = null;
            binding.btnDeleteMedicine.setVisibility(View.GONE);
            binding.btnEditMedicine.setVisibility(View.GONE);
        });
    }

    private void setupListeners() {
        binding.btnAddMedicine.setOnClickListener(v -> showAddMedicineDialog());

        binding.btnEditMedicine.setOnClickListener(v -> {
            if (selectedMedicine != null) {
                navigateToMedicineDetails(selectedMedicine);
            }
        });

        binding.btnDeleteMedicine.setOnClickListener(v -> {
            if (selectedMedicine != null) {
                showDeleteConfirmation(selectedMedicine);
            }
        });
    }

    private void navigateToMedicineDetails(Medicine medicine) {
        try {
            Bundle args = new Bundle();
            args.putParcelable("medicine", medicine);

            NavController navController = Navigation.findNavController(requireView());
            navController.navigate(R.id.action_medicine_to_details, args);

        } catch (Exception e) {
            Toast.makeText(requireContext(), "Couldn't open details", Toast.LENGTH_SHORT).show();
        }
    }

    private void showAddMedicineDialog() {
        DialogAddMedicineBinding dialogBinding = DialogAddMedicineBinding.inflate(getLayoutInflater());
        new AlertDialog.Builder(requireContext())
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

    private void showDeleteConfirmation(Medicine medicine) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Delete Medicine")
                .setMessage("Delete " + medicine.getName() + "?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    medicineViewModel.delete(medicine);
                    selectedMedicine = null;
                    binding.btnDeleteMedicine.setVisibility(View.GONE);
                    Toast.makeText(requireContext(), "Deleted", Toast.LENGTH_SHORT).show();
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