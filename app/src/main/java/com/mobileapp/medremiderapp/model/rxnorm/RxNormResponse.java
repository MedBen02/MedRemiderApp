package com.mobileapp.medremiderapp.model.rxnorm;

import com.google.gson.annotations.SerializedName;

public class RxNormResponse {
    @SerializedName("drugGroup")
    private DrugGroup drugGroup;

    public DrugGroup getDrugGroup() {
        return drugGroup;
    }

    public void setDrugGroup(DrugGroup drugGroup) {
        this.drugGroup = drugGroup;
    }
}
