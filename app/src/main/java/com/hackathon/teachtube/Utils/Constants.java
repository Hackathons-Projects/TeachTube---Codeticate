package com.hackathon.teachtube.Utils;

import android.graphics.Color;

import java.util.ArrayList;

public class Constants
{

    public static final String CAMP_IMAGE = "Camp Image";
    public static final String PROFILE_IMAGE = "Profile Image";

    public static String deviceToken = "deviceToken";
    public static String notifications = "notifications";
    public static String notifications_channels = "notifications_channels";
    public static String tones = "tones";
    public static String ringtone = "ringtone";
    public static String vibration = "vibration";
    public static String vibrationVC = "vibrationVC";
    public static String[] vibrationModes = {
            "Deactivated", "Standard", "Short", "Long"
    };
    public static long[] STANDARD_VIBRATION_PATTERN = {1000 , 1000};
    public static long[] SHORT_VIBRATION_PATTERN = {500 , 500};
    public static long[] LONG_VIBRATION_PATTERN = {1500 , 2500};
    public static long[] VIBRATION_NULL = {0};

    public static String light = "light";
    public static String lightDeactivated = "Deactivated";
    public static String[] lightModes = {
            "Red", "Orange", "Yellow", "Green", "Cyan", "Blue", "Violet", "Pink", "White"
    };
    public static Integer[] lightModesValues = {
            Color.parseColor("#E24A4E"), // Red
            Color.parseColor("#F39218"), // Orange
            Color.parseColor("#F8CD37"), // Yellow
            Color.parseColor("#80CB6E"), // Green
            Color.parseColor("#55E5E9"), // Cyan
            Color.parseColor("#5ABAED"), // Blue
            Color.parseColor("#CA83E4"), // Violet
            Color.parseColor("#EC5A95"), // Pink
            Color.parseColor("#C0C0C0") // White
    };

    public static String[] bloodGroup = {
            "Select Blood",
            "O+",
            "O-",
            "A+",
            "A-",
            "B+",
            "B-",
            "AB+",
            "AB-"
    };

    public static String[] chip_bloodTypes = {
            "O+",
            "O-",
            "A+",
            "A-",
            "B+",
            "B-",
            "AB+",
            "AB-"
    };

    public static String[] chip_serviceTypes = {
            "Blood",
            "Plasma"
    };
}
