package com.mobileapp.medremiderapp.model;

public class DateModel {
    private String day;
    private String date;
    private boolean isSelected;

    public DateModel(String day, String date, boolean isSelected) {
        this.day = day;
        this.date = date;
        this.isSelected = isSelected;
    }

    public String getDay() { return day; }
    public String getDate() { return date; }
    public boolean isSelected() { return isSelected; }

    public void setSelected(boolean selected) { isSelected = selected; }
}
