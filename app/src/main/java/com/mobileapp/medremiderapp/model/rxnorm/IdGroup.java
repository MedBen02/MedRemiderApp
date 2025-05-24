package com.mobileapp.medremiderapp.model.rxnorm;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class IdGroup {
    @SerializedName("name")
    public String name;

    @SerializedName("rxnormId")
    public List<String> rxnormId;
}
