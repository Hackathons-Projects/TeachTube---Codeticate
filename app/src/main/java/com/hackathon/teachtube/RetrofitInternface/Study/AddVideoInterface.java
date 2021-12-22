package com.hackathon.teachtube.RetrofitInternface.Study;

import com.hackathon.teachtube.Models.AddResponseModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface AddVideoInterface {
    @FormUrlEncoded
    @POST("apistaff/study/lecture")
    Call<AddResponseModel> addVideo(
            @Field("name") String name,
            @Field("url") String url,
            @Field("branch_id") String branch_id,
            @Field("class_id") String class_id,
            @Field("subject_id") String subject_id,
            @Field("topic_id") String topic_id,
            @Field("chapter_id") String chapter_id,
            @Field("teacher_id") String teacher_id
    );
}
