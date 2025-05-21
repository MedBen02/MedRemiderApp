package com.mobileapp.medremiderapp.ui.fragments.profile;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import com.mobileapp.medremiderapp.MainActivity;
import com.mobileapp.medremiderapp.R;
import com.mobileapp.medremiderapp.databinding.DialogEditProfileBinding;
import com.mobileapp.medremiderapp.databinding.FragmentProfileBinding;
import com.mobileapp.medremiderapp.model.User;
import com.mobileapp.medremiderapp.viewmodels.ProfileViewModel;
import com.mobileapp.medremiderapp.viewmodels.UserViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private ProfileViewModel profileViewModel;
    private UserViewModel userViewModel;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault());
    private boolean isEditingProfile = false;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        setupObservers();
        setupUI();
    }


    private void setupObservers() {
        userViewModel.loggedInUser.observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                updateProfileUI(user);

                if (isEditingProfile) {
                    ensureOnProfileFragment();
                    isEditingProfile = false;
                }
            } else {
                // User logged out
                clearProfileUI();
                if (isAdded() && !isRemoving()) {  // Check fragment is still active
                    navigateToLoginSafely();
                }
            }
        });
    }

    private void ensureOnProfileFragment() {
        try {
            NavController navController = Navigation.findNavController(requireView());
            if (navController.getCurrentDestination() != null
                    && navController.getCurrentDestination().getId() != R.id.navigation_profile) {

                navController.popBackStack(R.id.navigation_profile, false);
            }
        } catch (Exception e) {
            // Handle navigation exception silently
        }
    }

    private void navigateToLoginSafely() {
        try {
            NavController navController = Navigation.findNavController(requireView());

            // Only navigate if we're not already on login
            if (navController.getCurrentDestination() != null
                    && navController.getCurrentDestination().getId() != R.id.loginFragment) {

                NavOptions navOptions = new NavOptions.Builder()
                        .setPopUpTo(R.id.navigation_home, true)
                        .build();

                navController.navigate(R.id.action_profile_to_login, null, navOptions);
            }
        } catch (Exception e) {
            // Fallback to activity navigation
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).navigateToAuth();
            }
        }
    }


    private void updateProfileUI(User user) {
        binding.textName.setText(user.getName());
        binding.textUsername.setText(user.getUsername());
        binding.textBirthdate.setText(profileViewModel.formatBirthDate(user.getBirthDay()));
    }

    private void clearProfileUI() {
        binding.textName.setText("");
        binding.textUsername.setText("");
        binding.textBirthdate.setText("");
    }

    private void setupUI() {
        binding.btnLogout.setOnClickListener(v -> {
            userViewModel.logout();
            navigateToLogin();
        });

        binding.btnEditProfile.setOnClickListener(v -> showEditProfileDialog());
    }

     private void showEditProfileDialog() {
         isEditingProfile = true;

         User currentUser = userViewModel.loggedInUser.getValue();
         if (currentUser == null) return;
    
         AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
         DialogEditProfileBinding dialogBinding = DialogEditProfileBinding.inflate(getLayoutInflater());
    
         // Initialize fields with current user data
         dialogBinding.etEditFullName.setText(currentUser.getName());
         dialogBinding.etEditUsername.setText(currentUser.getUsername());
         if (currentUser.getBirthDay() != null) {
             dialogBinding.etEditBirthDay.setText(dateFormat.format(currentUser.getBirthDay()));
         }
    
         // Set up date picker
         dialogBinding.etEditBirthDay.setOnClickListener(v -> showDatePicker(dialogBinding.etEditBirthDay));
    
         builder.setView(dialogBinding.getRoot())
                 .setTitle(R.string.edit_profile);
    
         AlertDialog dialog = builder.create();
    
         dialogBinding.btnSaveProfile.setOnClickListener(v -> {
             String newName = dialogBinding.etEditFullName.getText().toString().trim();
             Date newBirthDate = null;
             try {
                 if (!dialogBinding.etEditBirthDay.getText().toString().isEmpty()) {
                     newBirthDate = dateFormat.parse(dialogBinding.etEditBirthDay.getText().toString());
                 }
             } catch (Exception e) {
                 e.printStackTrace();
             }
    
             if (newName.isEmpty()) {
                 Toast.makeText(requireContext(), R.string.name_cannot_be_empty, Toast.LENGTH_SHORT).show();
                 return;
             }
    
             // Update user
             currentUser.setName(newName);
             currentUser.setBirthDay(newBirthDate);
    
             // Save changes (this will automatically update the UI through LiveData)
             userViewModel.updateUser(currentUser);
             dialog.dismiss(); // Just dismiss the dialog, no navigation
         });

         dialog.show();
     }
    private void showDatePicker(EditText editText) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    Calendar selectedDate = Calendar.getInstance();
                    selectedDate.set(selectedYear, selectedMonth, selectedDay);
                    editText.setText(dateFormat.format(selectedDate.getTime()));
                },
                year, month, day
        );
        datePickerDialog.show();
    }

    private void navigateToLogin() {
        NavOptions navOptions = new NavOptions.Builder()
                .setPopUpTo(R.id.navigation_home, true)
                .build();

        Navigation.findNavController(requireView())
                .navigate(R.id.action_profile_to_login, null, navOptions);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}