package com.mobileapp.medremiderapp.ui.fragments.register;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import com.mobileapp.medremiderapp.R;
import com.mobileapp.medremiderapp.model.User;
import com.mobileapp.medremiderapp.viewmodels.UserViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class RegisterFragment extends Fragment {

    private EditText etUsername, etPassword, etName, etBirthdate;
    private Button btnRegister;
    private TextView tvLoginLink;
    private Calendar calendar;
    private UserViewModel userViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        etUsername = view.findViewById(R.id.et_username);
        etPassword = view.findViewById(R.id.et_password);
        etName = view.findViewById(R.id.et_name);
        etBirthdate = view.findViewById(R.id.et_birthdate);
        btnRegister = view.findViewById(R.id.btn_register);
        tvLoginLink = view.findViewById(R.id.tv_login_link);
        calendar = Calendar.getInstance();

        setupBirthdatePicker();

        btnRegister.setOnClickListener(v -> attemptRegistration());
        tvLoginLink.setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.action_registerFragment_to_loginFragment));

        return view;
    }

    private void setupBirthdatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateBirthdateField();
            }
        };

        etBirthdate.setOnClickListener(v -> new DatePickerDialog(
                requireContext(),
                dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        ).show());
    }

    private void updateBirthdateField() {
        String dateFormat = "yyyy-MM-dd"; // unified format
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);
        etBirthdate.setText(sdf.format(calendar.getTime()));
    }


    private Date parseDate(String dateString) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        format.setLenient(false);
        try {
            return format.parse(dateString);
        } catch (ParseException e) {
            etBirthdate.setError("Please use a valid date format: yyyy-MM-dd");
            return null;
        }
    }

    private void navigateToLogin() {
        NavController navController = Navigation.findNavController(requireView());
        NavDestination currentDestination = navController.getCurrentDestination();

        if (currentDestination != null && currentDestination.getId() == R.id.registerFragment) {
            navController.navigate(R.id.action_registerFragment_to_loginFragment);
        }
    }



    private void setupObservers() {
        userViewModel.registrationSuccess.observe(getViewLifecycleOwner(), success -> {
            if (success != null && success) {
                navigateToLogin();
            }
        });

        userViewModel.errorMessage.observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        setupObservers();
    }



    private void attemptRegistration() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String name = etName.getText().toString().trim();
        String birthdate = etBirthdate.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty() || name.isEmpty() || birthdate.isEmpty()) {
            Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        User user = new User(username, password, name, parseDate(birthdate));
        userViewModel.registerUser(user);
        Toast.makeText(getContext(), "Registering: " + username, Toast.LENGTH_SHORT).show();
    }
}