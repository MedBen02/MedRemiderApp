package com.mobileapp.medremiderapp.utils;

import com.mobileapp.medremiderapp.model.rxnorm.DrugNameResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RxNormApi {
    @GET("drugs.json")
    Call<DrugNameResponse> searchDrugsByName(@Query("name") String name);
}
