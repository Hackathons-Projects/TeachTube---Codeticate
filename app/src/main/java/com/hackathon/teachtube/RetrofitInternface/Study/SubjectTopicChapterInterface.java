package com.hackathon.teachtube.RetrofitInternface.Study;

import com.hackathon.teachtube.Models.ClassSubjectModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SubjectTopicChapterInterface {
    @GET("apistaff/study/chepter")
    Call<ClassSubjectModel> getData(
            @Query("branch_id") String branch_id,
            @Query("session_id") String session_id,
            @Query("teacher_id") String teacher_id
    );
}
