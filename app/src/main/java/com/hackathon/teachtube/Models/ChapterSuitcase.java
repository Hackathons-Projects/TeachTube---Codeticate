package com.hackathon.teachtube.Models;

public class ChapterSuitcase
{
    public String subject_id , subject , class_id , class_name;

    public ChapterSuitcase(String subject_id, String subject, String class_id, String class_name) {
        this.subject_id = subject_id;
        this.subject = subject;
        this.class_id = class_id;
        this.class_name = class_name;
    }

    public String getSubject_id() {
        return subject_id;
    }

    public void setSubject_id(String subject_id) {
        this.subject_id = subject_id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getClass_id() {
        return class_id;
    }

    public void setClass_id(String class_id) {
        this.class_id = class_id;
    }

    public String getClass_name() {
        return class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }
}
