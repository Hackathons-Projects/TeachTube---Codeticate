package com.hackathon.teachtube.RetrofitInternface.Study;

import com.hackathon.teachtube.Models.StudyMaterialsModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface StudyMaterialsInterface {
    @GET("apistaff/study/study_material")
    Call<StudyMaterialsModel> getStudyMaterials(
            @Query("branch_id") String branch_id,
            @Query("class_id") String class_id,
            @Query("subject_id") String subject_id,
            @Query("topic_id") String topic_id,
            @Query("chapter_id") String chepter_id
    );
}
