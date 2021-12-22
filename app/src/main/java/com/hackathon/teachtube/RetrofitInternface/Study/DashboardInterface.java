package com.hackathon.teachtube.RetrofitInternface.Study;

import com.hackathon.teachtube.Models.DashboardModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface DashboardInterface {
    @GET("apistaff/dashboard")
    Call<DashboardModel> getData(
            @Query("branch_id")  String branch_id,
            @Query("session_id") String session_id,
            @Query("teacher_id") String teacher_id
    );
}
