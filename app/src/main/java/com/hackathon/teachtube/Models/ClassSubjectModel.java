package com.hackathon.teachtube.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ClassSubjectModel {

    @SerializedName("class_id")
    @Expose
    private String classId;
    @SerializedName("class")
    @Expose
    private String _class;
    @SerializedName("subject")
    @Expose
    private ArrayList<Subject> subjects = null;

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String get_class() {
        return _class;
    }

    public void set_class(String _class) {
        this._class = _class;
    }

    public ArrayList<Subject> getSubjects() {
        return subjects;
    }

    public void setSubjects(ArrayList<Subject> subjects) {
        this.subjects = subjects;
    }

    public ClassSubjectModel(String classId, String _class, ArrayList<Subject> subjects) {
        this.classId = classId;
        this._class = _class;
        this.subjects = subjects;
    }

    public static class Subject {
        @SerializedName("subject_id")
        @Expose
        private String subjectId;
        @SerializedName("subject")
        @Expose
        private String subject;

        public Subject(String subjectId, String subject) {
            this.subjectId = subjectId;
            this.subject = subject;
        }

        public String getSubjectId() {
            return subjectId;
        }

        public void setSubjectId(String subjectId) {
            this.subjectId = subjectId;
        }

        public String getSubject() {
            return subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }
    }

}
