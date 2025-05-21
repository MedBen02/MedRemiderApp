package com.mobileapp.medremiderapp.ui.fragments.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import com.mobileapp.medremiderapp.R;
import com.mobileapp.medremiderapp.databinding.FragmentProfileBinding;
import com.mobileapp.medremiderapp.model.User;
import com.mobileapp.medremiderapp.viewmodels.ProfileViewModel;
import com.mobileapp.medremiderapp.viewmodels.UserViewModel;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private ProfileViewModel profileViewModel;
    private UserViewModel userViewModel;

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
                profileViewModel.setUserProfile(user);
                updateProfileUI(user);
            } else {
                profileViewModel.clearProfile();
                clearProfileUI();
            }
        });
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