package com.hackathon.teachtube.Utils;

public class Regex
{
    public static final String VEHICLE_NO = "^[A-Z]{2}[0-9]{2}[A-Z]{2}[0-9]{4}|[A-Z]{2}[0-9]{2}[0-9]{4}|[A-Z]{2}[0-9]{2}[A-Z]{3}[0-9]{4}|[A-Z]{2}[0-9]{3}[A-Z][0-9]{4}|[A-Z]{2}[0-9]{2}[A-Z]{3}[0-9]{4}$";
    public static final String GSTIN = "^[0-9]{2}[A-Z]{5}[0-9]{4}[A-Z]{1}[1-9A-Z]{1}Z[0-9A-Z]{1}$";
    public static final String YOUTUBE_URL = "^(http(s)?://)?((w){3}.)?youtu(be|.be)?(.com)?/.+";
}
