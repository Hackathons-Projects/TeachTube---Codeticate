package com.hackathon.teachtube.RetrofitInternface.Study;

import com.hackathon.teachtube.Models.AddResponseModel;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface AddNoteInterface {
    @Multipart
    @POST("apistaff/study/notes")
    Call<AddResponseModel> addNotes(
            @Part("name") RequestBody  name,
            @Part("url") RequestBody  url,
            @Part("branch_id") RequestBody  branch_id,
            @Part("class_id") RequestBody  class_id,
            @Part("subject_id") RequestBody  subject_id,
            @Part("topic_id") RequestBody  topic_id,
            @Part("chapter_id") RequestBody  chapter_id,
            @Part("teacher_id") String teacher_id,
            @Part MultipartBody.Part document
            );
}
