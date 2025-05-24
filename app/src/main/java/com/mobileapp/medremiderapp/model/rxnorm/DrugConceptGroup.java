package com.mobileapp.medremiderapp.model.rxnorm;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class DrugConceptGroup {
    @SerializedName("conceptProperties")
    private List<DrugConcept> conceptProperties;

    public List<DrugConcept> getConceptProperties() {
        return conceptProperties;
    }

    public void setConceptProperties(List<DrugConcept> conceptProperties) {
        this.conceptProperties = conceptProperties;
    }
}
