package com.apostpapad.dailytips;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface ApiInterface {

    @Headers("Content-Type: application/json")
    @GET("uploadTip.php")
    Call<Tip> uploadTip(@Query("tip_string") String tipString);


}
