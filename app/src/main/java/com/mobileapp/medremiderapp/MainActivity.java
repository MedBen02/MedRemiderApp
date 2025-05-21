package com.mobileapp.medremiderapp;

import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mobileapp.medremiderapp.databinding.ActivityMainBinding;
import com.mobileapp.medremiderapp.viewmodels.UserViewModel;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private NavController navController;
    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize ViewModel
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        userViewModel.loggedInUser.observe(this, user -> {
            if (user == null) {
                hideBottomNavigation();
                navController.navigate(R.id.loginFragment);
            } else {
                showBottomNavigation();
            }
        });

        // Setup Navigation
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        BottomNavigationView navView = binding.navView;
        NavigationUI.setupWithNavController(navView, navController);

        // Setup authentication observers
        setupAuthObservers();

        // Check initial authentication state
        checkAuthentication();
    }

    public void hideBottomNavigation() {
        binding.navView.setVisibility(View.GONE);
    }

    public void showBottomNavigation() {
        binding.navView.setVisibility(View.VISIBLE);
    }

    private void setupAuthObservers() {
        userViewModel.loggedInUser.observe(this, user -> {
            if (user != null) {
                onAuthSuccess();
            }
        });

        userViewModel.registrationSuccess.observe(this, success -> {
            if (success != null && success) {
                onAuthSuccess();
            }
        });
    }

    private void checkAuthentication() {
        if (userViewModel.isLoggedIn()) {
            // User is logged in, show main UI
            binding.navView.setVisibility(View.VISIBLE);
            userViewModel.checkLoggedInUser(); // This will trigger loggedInUser observer
        } else {
            // User not logged in, show auth flow
            binding.navView.setVisibility(View.GONE);
            navigateToAuth();
        }
    }

    private void navigateToAuth() {
        // Clear back stack and navigate to login
        navController.navigate(R.id.loginFragment);
    }

    public void onAuthSuccess() {
        runOnUiThread(() -> {
            // Show bottom navigation
            binding.navView.setVisibility(View.VISIBLE);

            // Navigate to home and clear back stack
            navController.navigate(R.id.navigation_home, null, new NavOptions.Builder()
                    .setPopUpTo(R.id.loginFragment, true)
                    .setPopUpTo(R.id.registerFragment, true)
                    .build());
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        return navController.navigateUp() || super.onSupportNavigateUp();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Clear references to prevent leaks
        binding = null;
    }
}