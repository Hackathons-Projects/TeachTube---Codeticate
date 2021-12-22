package com.hackathon.teachtube.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class DashboardModel {

    @SerializedName("live_class")
    @Expose
    private ArrayList<LiveClassModel> liveClassModels = null;
    @SerializedName("subject")
    @Expose
    private ArrayList<ClassSubjectModel> classSubjectModels;

    public ArrayList<LiveClassModel> getLiveClassModels() {
        return liveClassModels;
    }

    public void setLiveClassModels(ArrayList<LiveClassModel> liveClassModels) {
        this.liveClassModels = liveClassModels;
    }

    public ArrayList<ClassSubjectModel> getSubjectModel() {
        return classSubjectModels;
    }

    public void setSubjectModel(ArrayList<ClassSubjectModel> classSubjectModels) {
        this.classSubjectModels = classSubjectModels;
    }

}
