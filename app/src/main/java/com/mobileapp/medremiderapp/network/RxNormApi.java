package com.mobileapp.medremiderapp.network;

import com.mobileapp.medremiderapp.model.rxnorm.RxNormResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RxNormApi {
    @GET("REST/drugs.json")
    Call<RxNormResponse> searchDrugs(@Query("name") String name);
}
