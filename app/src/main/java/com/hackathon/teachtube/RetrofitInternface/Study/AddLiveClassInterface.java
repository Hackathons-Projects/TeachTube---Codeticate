package com.hackathon.teachtube.RetrofitInternface.Study;

import com.hackathon.teachtube.Models.AddResponseModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface AddLiveClassInterface {
    @FormUrlEncoded
    @POST("apistaff/liveclass")
    Call<AddResponseModel> addLiveClassCisco(
            @Field("title") String title,
            @Field("url") String meeting_url,
            @Field("class_id") String class_id,
            @Field("section_id") String section_id,
            //@Field("remarks") String remarks,
            @Field("date") String date,
            @Field("start") String start_time,
            @Field("end") String end_time,
            @Field("branch_id") String branch_id,
            @Field("teacher_id") String teacher_id,
            @Field("platform") String platform
    );

    @FormUrlEncoded
    @POST("apistaff/liveclass")
    Call<AddResponseModel> addLiveClassZoom(
            @Field("title")             String title,
            @Field("meeting_id")        String meeting_id,
            @Field("meeting_password")  String meeting_password,
            @Field("class_id")          String class_id,
            @Field("section_id")        String section_id,
            //@Field("remarks")         String remarks,
            @Field("date")              String date,
            @Field("start")        String start_time,
            @Field("end")          String end_time,
            @Field("branch_id")         String branch_id,
            @Field("teacher_id")       String teacher_id,
            @Field("platform")          String platform
    );
}
