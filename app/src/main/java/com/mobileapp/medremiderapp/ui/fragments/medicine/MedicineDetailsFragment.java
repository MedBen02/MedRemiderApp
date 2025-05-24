package com.mobileapp.medremiderapp.ui.fragments.medicine;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.mobileapp.medremiderapp.R;
import com.mobileapp.medremiderapp.databinding.FragmentMedicineDetailsBinding;
import com.mobileapp.medremiderapp.factory.MedicineViewModelFactory;
import com.mobileapp.medremiderapp.model.Medicine;
import com.mobileapp.medremiderapp.model.MedNotification;
import com.mobileapp.medremiderapp.model.Reminder;
import com.mobileapp.medremiderapp.model.DataFlowModels.ReminderWithNotifications;
import com.mobileapp.medremiderapp.model.User;
import com.mobileapp.medremiderapp.ui.adapters.ReminderAdapter;
import com.mobileapp.medremiderapp.viewmodels.MedicineViewModel;
import com.mobileapp.medremiderapp.viewmodels.NotificationViewModel;
import com.mobileapp.medremiderapp.viewmodels.ReminderViewModel;
import com.mobileapp.medremiderapp.viewmodels.UserViewModel;
import com.mobileapp.medremiderapp.repository.ReminderRepository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MedicineDetailsFragment extends Fragment {
    private FragmentMedicineDetailsBinding binding;
    private MedicineViewModel medicineViewModel;
    private ReminderViewModel reminderViewModel;
    private NotificationViewModel notificationViewModel;
    private Medicine currentMedicine;
    private int currentUserId;
    private ReminderAdapter reminderAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        reminderViewModel = new ViewModelProvider(this).get(ReminderViewModel.class);
        notificationViewModel = new ViewModelProvider(this).get(NotificationViewModel.class);
    }

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

        // Get current user ID
        UserViewModel userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        User currentUser = userViewModel.getLoggedInUser().getValue();
        if (currentUser != null) {
            currentUserId = currentUser.getId();
        }

        // Initialize ViewModel
        medicineViewModel = new ViewModelProvider(this,
                new MedicineViewModelFactory(requireActivity().getApplication(), currentUserId))
                .get(MedicineViewModel.class);

        // Setup RecyclerView for reminders
        setupRemindersRecyclerView();

        // Get the medicine passed as argument
        if (getArguments() != null) {
            currentMedicine = getArguments().getParcelable("medicine");
            if (currentMedicine != null) {
                bindMedicineData(currentMedicine);
                loadRemindersForMedicine(currentMedicine.getId());
            }
        }

        setupListeners();
    }

    private void setupRemindersRecyclerView() {
        reminderAdapter = new ReminderAdapter(new ArrayList<>());
        binding.remindersList.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.remindersList.setAdapter(reminderAdapter);
    }

    private void loadRemindersForMedicine(int medicineId) {
        medicineViewModel.getRemindersWithNotifications(medicineId).observe(getViewLifecycleOwner(), reminders -> {
            if (reminders != null && !reminders.isEmpty()) {
                reminderAdapter.reminderList.clear();
                reminderAdapter.reminderList.addAll(reminders);
                reminderAdapter.notifyDataSetChanged();
            } else {
                reminderAdapter.reminderList.clear();
                reminderAdapter.notifyDataSetChanged();
            }
        });
    }

    private void bindMedicineData(Medicine medicine) {
        binding.etName.setText(medicine.getName());
        binding.etDose.setText(medicine.getDose());
        binding.etStock.setText(String.valueOf(medicine.getStockQuantity()));
        binding.etDescription.setText(medicine.getDescription());
    }

    private void setupListeners() {
        binding.btnBack.setOnClickListener(v -> Navigation.findNavController(v).popBackStack());
        binding.btnSave.setOnClickListener(v -> {
            if (currentMedicine != null) saveMedicineChanges();
        });

        binding.btnAddReminder.setOnClickListener(v -> showAddReminderDialog());
    }

    private void showAddReminderDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_reminder, null);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();

        // Initialize views
        TextInputEditText etFrequency = dialogView.findViewById(R.id.etFrequencyInput);
        TextInputEditText etUnits = dialogView.findViewById(R.id.etUnitsInput);
        TextInputEditText etStartDate = dialogView.findViewById(R.id.etStartDateInput);
        TextInputEditText etEndDate = dialogView.findViewById(R.id.etEndDateInput);
        LinearLayout timePickersContainer = dialogView.findViewById(R.id.timePickersContainerInput);
        Button btnAddReminder = dialogView.findViewById(R.id.btnAddMedReminder);

        // Date pickers
        setupDatePicker(etStartDate);
        setupDatePicker(etEndDate);

        // Frequency change listener
        etFrequency.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                timePickersContainer.removeAllViews();
                try {
                    int frequency = Integer.parseInt(s.toString());
                    if (frequency > 0 && frequency <= 10) { // Limit to 10 times per day
                        for (int i = 0; i < frequency; i++) {
                            addTimePicker(timePickersContainer, i + 1);
                        }
                    }
                } catch (NumberFormatException e) {
                    // Ignore
                }
            }
        });

        btnAddReminder.setOnClickListener(v -> {
            if (validateReminderInputs(etFrequency, etUnits, etStartDate, etEndDate, timePickersContainer)) {
                try {
                    // Get input values
                    int frequency = Integer.parseInt(etFrequency.getText().toString());
                    int units = Integer.parseInt(etUnits.getText().toString());

                    // Parse dates
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                    Date startDate = sdf.parse(etStartDate.getText().toString());
                    Date endDate = sdf.parse(etEndDate.getText().toString());

                    // Collect times
                    List<String> times = new ArrayList<>();
                    for (int i = 0; i < timePickersContainer.getChildCount(); i++) {
                        View timePickerView = timePickersContainer.getChildAt(i);
                        TextView etTimePicker = timePickerView.findViewById(R.id.etTimePicker);
                        times.add(etTimePicker.getText().toString());
                    }

                    // Create reminder
                    Reminder reminder = new Reminder(
                            frequency,
                            units,
                            startDate.getTime(),
                            endDate.getTime(),
                            currentMedicine.getId()
                    );

                    // Show progress
                    ProgressDialog progressDialog = new ProgressDialog(requireContext());
                    progressDialog.setMessage("Saving reminder...");
                    progressDialog.show();

                    // Use the properly initialized ViewModel
                    reminderViewModel.insertReminderWithNotifications(
                            reminder,
                            times,
                            startDate,
                            endDate,
                            new ReminderRepository.OnCompleteListener() {
                                @Override
                                public void onSuccess() {
                                    requireActivity().runOnUiThread(() -> {
                                        progressDialog.dismiss();
                                        Toast.makeText(requireContext(),
                                                "Reminder and notifications saved successfully",
                                                Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                        loadRemindersForMedicine(currentMedicine.getId()); // Refresh the list
                                    });
                                }

                                @Override
                                public void onError(String message) {
                                    requireActivity().runOnUiThread(() -> {
                                        progressDialog.dismiss();
                                        Toast.makeText(requireContext(),
                                                "Error: " + message,
                                                Toast.LENGTH_SHORT).show();
                                    });
                                }
                            });

                } catch (ParseException e) {
                    Toast.makeText(requireContext(),
                            "Invalid date format",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialog.show();
    }

    private void setupDatePicker(TextInputEditText editText) {
        Calendar calendar = Calendar.getInstance();
        editText.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    requireContext(),
                    (view, year, month, dayOfMonth) -> {
                        Calendar selectedDate = Calendar.getInstance();
                        selectedDate.set(year, month, dayOfMonth);
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                        editText.setText(sdf.format(selectedDate.getTime()));
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            );
            datePickerDialog.show();
        });
    }

    private void addTimePicker(LinearLayout container, int index) {
        View timePickerView = LayoutInflater.from(requireContext()).inflate(R.layout.item_time_picker, container, false);
        TextView tvTimeLabel = timePickerView.findViewById(R.id.tvTimeLabel);
        TextView etTimePicker = timePickerView.findViewById(R.id.etTimePicker);

        tvTimeLabel.setText("Time " + index + ":");
        etTimePicker.setOnClickListener(v -> showTimePicker(etTimePicker));

        container.addView(timePickerView);
    }

    private void showTimePicker(TextView timeTextView) {
        Calendar calendar = Calendar.getInstance();
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                requireContext(),
                (view, hourOfDay, minute) -> {
                    String time = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute);
                    timeTextView.setText(time);
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
        );
        timePickerDialog.show();
    }

    private boolean validateReminderInputs(TextInputEditText etFrequency, TextInputEditText etUnits,
                                           TextInputEditText etStartDate, TextInputEditText etEndDate,
                                           LinearLayout timePickersContainer) {
        boolean isValid = true;

        if (etFrequency.getText().toString().trim().isEmpty()) {
            etFrequency.setError("Frequency is required");
            isValid = false;
        } else {
            try {
                int frequency = Integer.parseInt(etFrequency.getText().toString());
                if (frequency < 1 || frequency > 10) {
                    etFrequency.setError("Must be between 1-10");
                    isValid = false;
                }
            } catch (NumberFormatException e) {
                etFrequency.setError("Invalid number");
                isValid = false;
            }
        }

        if (etUnits.getText().toString().trim().isEmpty()) {
            etUnits.setError("Units is required");
            isValid = false;
        } else {
            try {
                int units = Integer.parseInt(etUnits.getText().toString());
                if (units < 1) {
                    etUnits.setError("Must be at least 1");
                    isValid = false;
                }
            } catch (NumberFormatException e) {
                etUnits.setError("Invalid number");
                isValid = false;
            }
        }

        if (etStartDate.getText().toString().trim().isEmpty()) {
            etStartDate.setError("Start date is required");
            isValid = false;
        }

        if (etEndDate.getText().toString().trim().isEmpty()) {
            etEndDate.setError("End date is required");
            isValid = false;
        }

        // Validate time pickers
        if (timePickersContainer.getChildCount() == 0) {
            Toast.makeText(requireContext(), "Please enter frequency to add times", Toast.LENGTH_SHORT).show();
            isValid = false;
        } else {
            for (int i = 0; i < timePickersContainer.getChildCount(); i++) {
                View timePickerView = timePickersContainer.getChildAt(i);
                TextView etTimePicker = timePickerView.findViewById(R.id.etTimePicker);
                if (etTimePicker.getText().toString().equals("Choose time")) {
                    etTimePicker.setError("Time is required");
                    isValid = false;
                }
            }
        }

        return isValid;
    }

    private void saveMedicineChanges() {
        // Get updated values from EditTexts
        String name = binding.etName.getText().toString().trim();
        String dose = binding.etDose.getText().toString().trim();
        String stockStr = binding.etStock.getText().toString().trim();
        String description = binding.etDescription.getText().toString().trim();

        // Validate inputs
        if (name.isEmpty()) {
            binding.etName.setError("Name is required");
            return;
        }

        int stock;
        try {
            stock = stockStr.isEmpty() ? 0 : Integer.parseInt(stockStr);
        } catch (NumberFormatException e) {
            binding.etStock.setError("Invalid number");
            return;
        }

        // Update the medicine object
        currentMedicine.setName(name);
        currentMedicine.setDose(dose);
        currentMedicine.setStockQuantity(stock);
        currentMedicine.setDescription(description);

        // Save to database
        medicineViewModel.update(currentMedicine);

        // Show success message
        Toast.makeText(requireContext(), "Medicine updated", Toast.LENGTH_SHORT).show();

        // Navigate back
        Navigation.findNavController(requireView()).popBackStack();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}