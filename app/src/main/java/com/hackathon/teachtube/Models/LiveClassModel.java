package com.hackathon.teachtube.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LiveClassModel
{
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("platform")
    @Expose
    private String platform;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("start_time")
    @Expose
    private String startTime;
    @SerializedName("end_time")
    @Expose
    private String endTime;
    @SerializedName("meeting_url")
    @Expose
    private String meeting_url;
    @SerializedName("meeting_password")
    @Expose
    private String meeting_password;
    @SerializedName("meeting_id")
    @Expose
    private String meeting_id;
    @SerializedName("class")
    @Expose
    private String _class;
    @SerializedName("subject")
    @Expose
    private String subject;

    public LiveClassModel(String id, String platform, String title, String url, String date, String startTime, String endTime, String meeting_url, String meeting_password, String meeting_id, String _class, String subject) {
        this.id = id;
        this.platform = platform;
        this.title = title;
        this.url = url;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.meeting_url = meeting_url;
        this.meeting_password = meeting_password;
        this.meeting_id = meeting_id;
        this._class = _class;
        this.subject = subject;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getMeeting_url() {
        return meeting_url;
    }

    public void setMeeting_url(String meeting_url) {
        this.meeting_url = meeting_url;
    }

    public String getMeeting_password() {
        return meeting_password;
    }

    public void setMeeting_password(String meeting_password) {
        this.meeting_password = meeting_password;
    }

    public String getMeeting_id() {
        return meeting_id;
    }

    public void setMeeting_id(String meeting_id) {
        this.meeting_id = meeting_id;
    }

    public String get_class() {
        return _class;
    }

    public void set_class(String _class) {
        this._class = _class;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
