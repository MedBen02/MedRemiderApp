package com.mobileapp.medremiderapp.ui.fragments.medicine;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.mobileapp.medremiderapp.databinding.FragmentMedicineBinding;
import com.mobileapp.medremiderapp.viewmodels.MedicineViewModel;

public class MedicineFragment extends Fragment {

    private FragmentMedicineBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        MedicineViewModel medicineViewModel =
                new ViewModelProvider(this).get(MedicineViewModel.class);

        binding = FragmentMedicineBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}