package com.mobileapp.medremiderapp.model.rxnorm;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class DrugGroup {
    @SerializedName("conceptGroup")
    private List<DrugConceptGroup> conceptGroup;

    public List<DrugConceptGroup> getConceptGroup() {
        return conceptGroup;
    }

    public void setConceptGroup(List<DrugConceptGroup> conceptGroup) {
        this.conceptGroup = conceptGroup;
    }
}
