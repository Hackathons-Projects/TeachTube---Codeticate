package com.hackathon.teachtube.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DataSharedManager {

    public static final String ASPID = "ASPID";
    public static final String ASPID_PASSWORD = "ASPID_PASSWORD";

    //public static final String CHECK_SETTINGS_FINGERPRINT_STATUS = "CHECK_SETTINGS_FINGERPRINT_STATUS";
    public static final String FLAG_MAIN_CREATE_MPIN = "FLAG_MAIN_CREATE_MPIN";
    public static final String FLAG_MAIN_SPLASH_SCREEN = "FLAG_MAIN_SPLASH_SCREEN";
    public static final String LOCAl_FLAGS = "LOCAl_FLAGS";
    //because we have to verify it with mpin and to hit the api to get customer first time details.
    public static final String FLAG_FIRST_TIME_LOGIN_MPIN = "FLAG_FIRST_TIME_LOGIN_MPIN";
    /////////////////////////
    public static final String FLAG_FORGOT_MPIN_FROM_ENTER_CREDINTALS = "FLAG_FORGOT_MPIN_FROM_ENTER_CREDINTALS";
    public static final String LATEST_APP_VERSION = "LATEST_APP_VERSION";
    public static final String REFERRAL_DISC_MATTER = "REFERRAL_DISC_MATTER";
    public static final String SELECT_SUBJECT_DEFAULT_CHAPTER_DATA = "SELECT_SUBJECT_DEFAULT_CHAPTER_DATA";
    public static final String DASHBOARD_BACKUP_DATA = "DASHBOARD_BACKUP_DATA";

    private static Map<String, String> mUserData;

    public static final String USER_SUITCASE = "USER_SUITCASE";
    public static final String HOLIDAY_FESTIVAL_ACTIVITY_DATA = "HOLIDAY_FESTIVAL_ACTIVITY_DATA";

    //////////CREATE
    public static void CreateDataStorage(Context context, String StorageKeyName, Map<String, String> userData) {
        mUserData = new HashMap<>();
        mUserData = userData;
        SharedPreferences sharedPreferences = context.getSharedPreferences(StorageKeyName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        for (String key : mUserData.keySet()) {
            editor.putString(key, (String) mUserData.get(key));
        }
        editor.apply();
    }

    /////////READ
    public static Map<String, String> ReadDataStorage(ArrayList<String> arrayList, String StorageKeyName, Context context) {
        Map<String, String> hashMap = new HashMap<>();
        SharedPreferences sharedPreferences = context.getSharedPreferences(StorageKeyName, Context.MODE_PRIVATE);
        for (int i = 0; arrayList.size() > i; i++) {
            hashMap.put(arrayList.get(i), sharedPreferences.getString(arrayList.get(i), ""));
        }
        return hashMap;
    }

    ///////////UPDATE
    public static void UpdateDataStorage(Map<String, String> userData, String StorageKeyName, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(StorageKeyName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        for (String key : userData.keySet()) {
            editor.putString(key, userData.get(key));
        }
        editor.apply();
    }

    ///////////DELETE
    public static void DeleteDataStorage(ArrayList<String> ketToDelete, String StorageKeyName, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(StorageKeyName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        for (String key : ketToDelete) {
            editor.remove(key);
        }
        editor.apply();
    }

    ///////////CLEAR
    public static void ClearDataStorage(String StorageKeyName, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(StorageKeyName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

}
