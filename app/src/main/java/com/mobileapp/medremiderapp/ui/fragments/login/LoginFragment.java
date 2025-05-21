package com.mobileapp.medremiderapp.ui.fragments.login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.mobileapp.medremiderapp.MainActivity;
import com.mobileapp.medremiderapp.R;
import com.mobileapp.medremiderapp.viewmodels.UserViewModel;

public class LoginFragment extends Fragment {
    private EditText etUsername, etPassword;
    private Button btnLogin;
    private TextView tvRegisterLink;
    private UserViewModel userViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        etUsername = view.findViewById(R.id.et_username);
        etPassword = view.findViewById(R.id.et_password);
        btnLogin = view.findViewById(R.id.btn_login);
        tvRegisterLink = view.findViewById(R.id.tv_register_link);

        btnLogin.setOnClickListener(v -> attemptLogin());
        tvRegisterLink.setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.action_loginFragment_to_registerFragment));

        return view;
    }

    private void navigateToHome() {
        NavController navController = Navigation.findNavController(requireView());
        if (navController.getCurrentDestination() != null &&
                navController.getCurrentDestination().getId() == R.id.loginFragment) {
            navController.navigate(R.id.action_loginFragment_to_navigation_home);
        }

    }

    private void setupObservers() {
        userViewModel.loggedInUser.observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                navigateToHome();
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

    private void attemptLogin() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        userViewModel.loginUser(username,password);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).hideBottomNavigation();
        }
    }
}
