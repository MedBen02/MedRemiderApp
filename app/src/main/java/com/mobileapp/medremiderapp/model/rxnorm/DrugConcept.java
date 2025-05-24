package com.mobileapp.medremiderapp.model.rxnorm;

import com.google.gson.annotations.SerializedName;

public class DrugConcept {
    @SerializedName("name")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
