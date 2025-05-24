package com.mobileapp.medremiderapp.ui.fragments.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mobileapp.medremiderapp.databinding.FragmentHomeBinding;
import com.mobileapp.medremiderapp.model.DateModel;
import com.mobileapp.medremiderapp.model.MedNotification;
import com.mobileapp.medremiderapp.ui.adapters.DateAdapter;
import com.mobileapp.medremiderapp.ui.adapters.NotificationAdapter;
import com.mobileapp.medremiderapp.viewmodels.HomeViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment implements
        DateAdapter.OnDateClickListener,
        NotificationAdapter.OnNotificationActionListener {

    private FragmentHomeBinding binding;
    private HomeViewModel homeViewModel;
    private NotificationAdapter notificationAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        setupDateRecyclerView();
        setupNotificationRecyclerView();
        setupObservers();

        return root;
    }

    private void setupDateRecyclerView() {
        RecyclerView dateRecycler = binding.recyclerViewDates;
        List<DateModel> dates = generateDateList();

        DateAdapter dateAdapter = new DateAdapter(dates, this);
        LinearLayoutManager horizontalLayout = new LinearLayoutManager(
                getContext(), LinearLayoutManager.HORIZONTAL, false);
        dateRecycler.setLayoutManager(horizontalLayout);
        dateRecycler.setAdapter(dateAdapter);
    }

    private void setupNotificationRecyclerView() {
        RecyclerView notificationRecycler = binding.recyclerViewNotifications;
        notificationAdapter = new NotificationAdapter(new ArrayList<>(), this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        notificationRecycler.setLayoutManager(layoutManager);
        notificationRecycler.setAdapter(notificationAdapter);
    }

    private void setupObservers() {
        homeViewModel.getNotificationsWithDetails().observe(getViewLifecycleOwner(), notifications -> {
            notificationAdapter.notificationList.clear();
            notificationAdapter.notificationList.addAll(notifications);
            notificationAdapter.notifyDataSetChanged();
        });
    }

    private List<DateModel> generateDateList() {
        List<DateModel> dateList = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();

        for (int i = 0; i < 14; i++) {
            String day = new SimpleDateFormat("EEE", Locale.getDefault()).format(calendar.getTime());
            String date = new SimpleDateFormat("dd", Locale.getDefault()).format(calendar.getTime());
            dateList.add(new DateModel(day, date, i == 0));

            // Load notifications for today initially
            if (i == 0) {
                loadNotificationsForDate(calendar.getTimeInMillis());
            }

            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }

        return dateList;
    }

    @Override
    public void onDateClick(DateModel dateModel) {
        // Convert the selected date to milliseconds
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateModel.getDate()));

        // Get start and end of the selected day
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        long startOfDay = calendar.getTimeInMillis();

        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        long endOfDay = calendar.getTimeInMillis();

        loadNotificationsForDate(startOfDay);
    }

    private void loadNotificationsForDate(long dateInMillis) {
        homeViewModel.loadNotificationsForDate(dateInMillis);
    }

    @Override
    public void onDismissClick(MedNotification notification) {
        homeViewModel.updateNotificationStatus(notification.getId(), "DISMISSED");
    }

    @Override
    public void onTakeClick(MedNotification notification) {
        homeViewModel.updateNotificationStatus(notification.getId(), "TAKEN");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}