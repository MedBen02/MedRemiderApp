package com.mobileapp.medremiderapp.model.rxnorm;

import java.util.List;

public class DrugNameResponse {
    public DrugGroup drugGroup;

    public static class DrugGroup {
        public List<ConceptGroup> conceptGroup;
    }

    public static class ConceptGroup {
        public List<ConceptProperties> conceptProperties;
    }

    public static class ConceptProperties {
        public String name;
        public String rxcui;
    }
}
