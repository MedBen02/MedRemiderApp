package com.mobileapp.medremiderapp.ui.fragments.medicine;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.mobileapp.medremiderapp.R;
import com.mobileapp.medremiderapp.databinding.FragmentMedicineDetailsBinding;
import com.mobileapp.medremiderapp.model.Medicine;

public class MedicineDetailsFragment extends Fragment {
    private FragmentMedicineDetailsBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        try {
            binding = FragmentMedicineDetailsBinding.inflate(inflater, container, false);
            return binding.getRoot();
        } catch (Exception e) {
            View view = inflater.inflate(R.layout.fragment_medicine_details, container, false);
            return view;
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Medicine medicine = getArguments().getParcelable("medicine");
        if (medicine != null) {
            bindMedicineData(medicine);
        }

        setupListeners();
    }

    private void bindMedicineData(Medicine medicine) {
        binding.tvName.setText(medicine.getName());
        binding.tvDose.setText(medicine.getDose());
        binding.tvStock.setText(String.valueOf(medicine.getStockQuantity()));
        binding.tvDescription.setText(medicine.getDescription());
        // Add more fields as needed
    }

    private void setupListeners() {
        binding.btnBack.setOnClickListener(v -> {
            // Navigate back to medicine fragment
            Navigation.findNavController(v).popBackStack();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}