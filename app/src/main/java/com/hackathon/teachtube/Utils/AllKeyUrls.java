package com.hackathon.teachtube.Utils;

public class AllKeyUrls
{

    public static final String BASE_URL = "http://hhelper.scelon.com/";

    public static final String PRIVACY_POLICY = BASE_URL + "privacy_policy";
    public static final String TERMS_CONDITIONS = BASE_URL + "terms_and_conditions";
    public static final String ABOUT_US = BASE_URL + "about_us";
    public static final String EMAIL_ID = "info.humanhelper@gmail.com";
    public static final String APP_LOGO = BASE_URL + "uploads/logo.png";

    public static final String CAMP_IMAGE_PATH = BASE_URL + "uploads/camp/";
    public static final String PROFILE_IMAGE_PATH = BASE_URL + "uploads/user_image/";

    public static final String FIREBASE_URL = "https://human-helper-313815-default-rtdb.asia-southeast1.firebasedatabase.app/";

    public static String UPLOAD_DOCUMENT_PATH;

    public static String getUploadNoteDocumentPath(String name)
    {
        UPLOAD_DOCUMENT_PATH = BASE_URL + "/uploads/attachments/studynotes/" + name;
        return UPLOAD_DOCUMENT_PATH;
    }

    public static String getUploadExerciseDocumentPath(String name)
    {
        UPLOAD_DOCUMENT_PATH = BASE_URL + "/uploads/attachments/studyexercise/" + name;
        return UPLOAD_DOCUMENT_PATH;
    }

    public static String getMeetingUrl(String id , String pwd)
    {
        return "https://us05web.zoom.us/j/"+id+"?pwd="+pwd;
    }
    public static String getYoutubeUrl(String id)
    {
        return "https://www.youtu.be/" + id;
    }


}
