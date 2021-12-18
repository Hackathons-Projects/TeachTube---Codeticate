package com.hackathon.teachtube.Utils;

import android.Manifest;
import android.animation.Animator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.hardware.fingerprint.FingerprintManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;

import com.google.android.material.snackbar.Snackbar;
import com.hackathon.teachtube.R;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@SuppressLint("SimpleDateFormat")
public class ImpMethods {

    private static final String TAG = "ImpMethods";

    public static boolean isAppOnForeground(Context context) {
        boolean ret = false;
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if(appProcesses != null){
            String packageName = context.getPackageName();
            for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
                if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND && appProcess.processName.equals(packageName)) {
                    ret = true;
                }
            }
        }
        return ret;
    }

    public static File getMainAppFolder(Context context)
    {
        File root = new File(Environment.getExternalStorageDirectory(), context.getResources().getString(R.string.app_name));
        if (!root.exists()) {
            root.mkdir();
        }
        return root;
    }

    public static View getViewFromContext(Context context)
    {
        return ((Activity) context).getWindow().getDecorView().getRootView();
    }

    public static boolean callDexterPermission(Context context , String permission)
    {
        boolean[] b = new boolean[1];
        Dexter.withActivity((Activity) context)
                .withPermission(permission)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        b[0] = true;
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        b[0] = false;
                        Toast.makeText(context, "Permission Denied!! :(\n Please Try again", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, final PermissionToken token) {
                        new AlertDialog.Builder(context).setTitle("Permission Required")
                                .setMessage("Permission is required to proceed")
                                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        token.cancelPermissionRequest();
                                    }
                                })
                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        token.continuePermissionRequest();
                                    }
                                })
                                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                                    @Override
                                    public void onDismiss(DialogInterface dialog) {
                                        token.cancelPermissionRequest();
                                    }
                                })
                                .show();
                    }
                }).check();
        return b[0];
    }

    public static Boolean isConnected(Context context)
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnectedOrConnecting())
        {
            return networkInfo != null && networkInfo.isConnected();
        }else
        {
            return false;
        }
    }

    public static void setClipboard(Context context, String text) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(text);
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", text);
            clipboard.setPrimaryClip(clip);
        }
    }

    public static void createAppFolder(Context context) {
        File file = new File(Environment
                .getExternalStorageDirectory()
                + "/" + context.getResources().getString(R.string.app_name)
                + "/Documents");
        file.mkdirs();
    }

    static public String containsString(String s, String[] array) {
        for (int i = 0; array.length > i; i++) {
            if (s.equals(array[i])) {
                return array[i];
            }
        }
        return "";
    }

    static public int containsInt(String s, String[] array) {
        for (int i = 0; array.length > i; i++) {
            if (s.equals(array[i])) {
                return i;
            }
        }
        return -1;
    }

    static public String containsState(String s) {
        for (int i = 0; ImpMethods.stateCode.length > i; i++) {
            if (s.equals(ImpMethods.stateCode[i])) {
                return (String) ImpMethods.stateName[i];
            }
        }
        return "";
    }

    static public String containsStateCode(String s) {
        for (int i = 0; ImpMethods.stateName.length > i; i++) {
            if (s.equals(ImpMethods.stateName[i])) {
                return (String) ImpMethods.stateCode[i];
            }
        }
        return "";
    }

    public static void showErrorSnackbar(Context context, View v, String str) {
        Snackbar snack = Snackbar.make(v, str, Snackbar.LENGTH_LONG);
        TextView tv = (TextView) (snack.getView()).findViewById(R.id.snackbar_text);
        Typeface font = Typeface.createFromAsset(context.getAssets(), "manrope_medium.ttf");
        tv.setTypeface(font);
        View view = snack.getView();
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
        params.gravity = Gravity.TOP;
        params.setMargins(0, ImpMethods.DimensToPx(R.dimen._52sdp, context), 0, 0);
        view.setLayoutParams(params);
        snack.getView().setBackgroundColor(context.getResources().getColor(R.color.ErrorMsg));
        snack.show();
    }

    /*public static boolean isFingerprintScannerAvailable(Context context) {
        FingerprintManager fingerprintManager = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            fingerprintManager = (FingerprintManager) context.getSystemService(Context.FINGERPRINT_SERVICE);
            if (!fingerprintManager.isHardwareDetected()) {
                return false;
                // Device doesn't support fingerprint authentication
            } else if (!fingerprintManager.hasEnrolledFingerprints()) {
                // User hasn't enrolled any fingerprints to authenticate with
                return false;
            } else {
                // Everything is ready for fingerprint authentication
                return true;
            }
        }
        return false;
    }*/

    public static void oneTimeClickCountdownMenu(MenuItem menuItem) {
        menuItem.setEnabled(false);
        new CountDownTimer(2000, 1000) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                menuItem.setEnabled(true);
            }
        }.start();
    }

    public static void oneTimeClickCountdown(View menuItem) {
        menuItem.setEnabled(false);
        new CountDownTimer(2000, 1000) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                menuItem.setEnabled(true);
            }
        }.start();
    }

    /*public static ArrayList<ListSuitcase> getListSuitcaseFilter(String toFilter, ArrayList<ListSuitcase> filterListSuitcase) {
        if (!toFilter.trim().equals("")) {
            ArrayList<ListSuitcase> FilteredArray = new ArrayList<>();
            // perform your search here using the searchConstraint String.
            for (int i = 0; i < filterListSuitcase.size(); i++) {
                ListSuitcase data = filterListSuitcase.get(i);
                if (data.FirmName.toLowerCase().startsWith(toFilter) || data.FirmName.toLowerCase().contains(toFilter)) {
                    FilteredArray.add(data);
                } else if (data.FirmName.toUpperCase().startsWith(toFilter) || data.FirmName.toUpperCase().contains(toFilter)) {
                    FilteredArray.add(data);
                }
            }
            return FilteredArray;
        } else {
            return filterListSuitcase;
        }
    }*/

    /*public static ArrayList<VehicleListSuitcase> getVehicleListSuitcaseFilter(String toFilter, ArrayList<VehicleListSuitcase> filterListSuitcase) {
        if (!toFilter.trim().equals("")) {
            ArrayList<VehicleListSuitcase> FilteredArray = new ArrayList<>();
            // perform your search here using the searchConstraint String.
            for (int i = 0; i < filterListSuitcase.size(); i++) {
                VehicleListSuitcase data = filterListSuitcase.get(i);
                if (data.vehicleNo.toLowerCase().startsWith(toFilter) || data.vehicleNo.toLowerCase().contains(toFilter)) {
                    FilteredArray.add(data);
                } else if (data.vehicleNo.toUpperCase().startsWith(toFilter) || data.vehicleNo.toUpperCase().contains(toFilter)) {
                    FilteredArray.add(data);
                }
            }
            return FilteredArray;
        } else {
            return filterListSuitcase;
        }
    }*/

    public static ArrayList<String> getStringFilter(String toFilter, ArrayList<String> strings) {
        ArrayList<String> FilteredArray = new ArrayList<>();

        // perform your search here using the searchConstraint String.
        for (int i = 0; i < strings.size(); i++) {
            String data = strings.get(i);
            if (data.toLowerCase().startsWith(toFilter) || data.toLowerCase().contains(toFilter)) {
                FilteredArray.add(data);
            } else if (data.toUpperCase().startsWith(toFilter) || data.toUpperCase().contains(toFilter)) {
                FilteredArray.add(data);
            }
        }
        return FilteredArray;
    }

    public static void closeSoftKeyboard(Context context) {
        ((InputMethodManager) Objects.requireNonNull(context).getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(((Activity) context).getWindow().getDecorView().getRootView().getApplicationWindowToken(), 0);
    }

    public static int DimensToPx(int dimen, Context context) {
        Log.d(TAG, "DimensToPx: " + (int) (context.getResources().getDimension(dimen)));
        return (int) (context.getResources().getDimension(dimen));
    }

    public static String[] supplyType = {
            "Outward",
            "Inward"
    };

    public static String[] supplyType_outward = {
            "Supply",
            "Export",
            "Job Work",
            "SKD/CKD",
            "Recipient  Not Known",
            "For Own Use",
            "Exhibition or Fairs",
            "Line Sales",
            "Others"
    };

    public static String[] supplyType_inward = {
            "Supply",
            "Import",
            "SKD/CKD",
            "Job work Returns",
            "Sales Return",
            "Exhibition or Fair",
            "For Own Use",
            "Others"
    };

    public static String[] transactionType = {
            "Regular",
            "Bill to - Ship to",
            "Bill from - Dispatch from",
            "Both"
    };

    public static String[] supply =
            {
                    "Tax Invoice",
                    "Bill of Supply"
            };

    public static String[] delivery =
            {
                    "Delivery Challan"
            };

    public static String[] delivery_supply =
            {
                    "Tax Invoice",
                    "Bill of Supply",
                    "Delivery Challan"
            };

    public static String[] delivery_other =
            {
                    "Tax Invoice",
                    "Others"
            };

    public static String[] billEntry =
            {
                    "Bill of Entry"
            };

    public static String[] billEntry_delivery_supply =
            {
                    "Tax Invoice",
                    "Bill of Supply",
                    "Delivery Challan",
                    "Bill of Entry"
            };


    public static String[] stateName = {"Select State",
            "Other Country",
            "Andaman and Nicobar Islands",
            "Andhra Pradesh",
            "Arunachal Pradesh",
            "Assam",
            "Bihar",
            "Chandigarh",
            "Chhattisgarh",
            "Dadra and Nagar Haveli",
            "Daman and Diu",
            "Delhi",
            "Goa",
            "Gujarat",
            "Haryana",
            "Himachal Pradesh",
            "Jammu and Kashmir",
            "Jharkhand",
            "Karnataka",
            "Kerala",
            "Lakshadweep",
            "Madhya Pradesh",
            "Maharashtra",
            "Manipur",
            "Meghalaya",
            "Mizoram",
            "Nagaland",
            "Odisha",
            "Puducherry",
            "Punjab",
            "Rajasthan",
            "Sikkim",
            "Tamil Nadu",
            "Telangana",
            "Tripura",
            "Uttarakhand",
            "Uttar Pradesh",
            "West Bengal"
    };

    public static String[] stateCode = {"0",
            "99",
            "35",
            "28",
            "12",
            "18",
            "10",
            "4",
            "22",
            "26",
            "25",
            "7",
            "30",
            "24",
            "6",
            "2",
            "1",
            "20",
            "29",
            "32",
            "31",
            "23",
            "27",
            "14",
            "17",
            "15",
            "13",
            "21",
            "34",
            "3",
            "8",
            "11",
            "33",
            "36",
            "16",
            "5",
            "9",
            "19"};

    public static String[] gstRate = {
            "Select GST Rate",
            "5",
            "12",
            "18",
            "28",
            "3",
            "1",
            "1.5",
            "7.5",
            "0",
            "0.1",
            "0.25",
            "0.5",
            "Not Applicable"
    };

    public static String[] cessRate = {
            "0",
            "1",
            "3",
            "5",
            "11",
            "12",
            "12.5",
            "15",
            "17",
            "20",
            "21",
            "22",
            "36",
            "49",
            "60",
            "61",
            "65",
            "71",
            "72",
            "89",
            "96",
            "142",
            "160",
            "204",
            "290",
            "Not Applicable"
    };

    public static String[] unitesCode = {"BAG",
            "BAL",
            "BDL",
            "BKL",
            "BOU",
            "BOX",
            "BTL",
            "BUN",
            "CAN",
            "CBM",
            "CCM",
            "CMS",
            "CTN",
            "DOZ",
            "DRM",
            "GGR",
            "GMS",
            "GRS",
            "GYD",
            "KGS",
            "KLR",
            "KME",
            "MLT",
            "MTR",
            "MTS",
            "NOS",
            "PAC",
            "PCS",
            "PRS",
            "QTL",
            "ROL",
            "SET",
            "SQF",
            "SQM",
            "SQY",
            "TBS",
            "TGM",
            "THD",
            "TON",
            "TUB",
            "UGS",
            "UNT",
            "YDS",
            "OTH"
    };

    public static String[] unitesName = {
            "BAGS",
            "BALE",
            "BUNDLES",
            "BUCKLES",
            "BILLIONS OF UNITS",
            "BOX",
            "BOTTLES",
            "BUNCHES",
            "CANS",
            "CUBIC METER",
            "CUBIC CENTIMETER",
            "CENTIMETER",
            "CARTONS",
            "DOZEN",
            "DRUM",
            "GREAT GROSS",
            "GRAMS",
            "GROSS",
            "GROSS YARDS",
            "KILOGRAMS",
            "KILOLITRE",
            "KILOMETRE",
            "MILLILITRE",
            "METERS",
            "METRIC TON",
            "NUMBERS",
            "PACKS",
            "PIECES",
            "PAIRS",
            "QUINTAL",
            "ROLLS",
            "SETS",
            "SQUARE FEET",
            "SQUARE METERS",
            "SQUARE YARDS",
            "TABLETS",
            "TEN GRAMS",
            "THOUSANDS",
            "TONNES",
            "TUBES",
            "US GALLONS",
            "UNITS",
            "YARDS",
            "OTHERS"
    };

    public static String[] VehicleMode = {
            "Road",
            "Rail",
            "Air",
            "Ship"
    };
    public static String[] VehicleModeTransit = {
            "Road",
            "Rail",
            "Air",
            "Ship",
            "inTransit"
    };

    public static String[] VehicleType = {
            "Regular",
            "Over Dimensional Cargo",
    };

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static boolean isMobileNoValid(String mobile) {
        return mobile.matches("\\d{10}");
    }

    public static long ConvertDateintoMillies(String date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date mDate = sdf.parse(date);
        return mDate.getTime();
    }

    public static long ConvertDateDdMMyyyyIntoMillies(String date) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            Date mDate = sdf.parse(date);
            return mDate.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }


    @SuppressLint("MissingPermission")
    public static String getIMEIDeviceId(Context context) {

        String deviceId;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        } else {
            final TelephonyManager mTelephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (context.checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    return "";
                }
            }
            assert mTelephony != null;
            if (mTelephony.getDeviceId() != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    deviceId = mTelephony.getImei();
                } else {
                    deviceId = mTelephony.getDeviceId();
                }
            } else {
                deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            }
        }
        Log.d("deviceId", "   " + deviceId);
        return deviceId;
    }


    public static String ChangeDateFormate(String inputDateStr, String format) {
        try {
            DateFormat inputFormat = new SimpleDateFormat(format);
            DateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date date = inputFormat.parse(inputDateStr);
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.d("parseError", "ChangeDateFormate: " + e.toString());
            return "";
        }
    }

    public static String getFormatedEwayDate(String inputDateStr) {
        DateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
        DateFormat outputFormat = new SimpleDateFormat("dd MMM, yyyy");
        Date date = null;
        try {
            date = inputFormat.parse(inputDateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.d("parseError", "ChangeDateFormate: " + e.toString());
        }
        String outputDateStr = outputFormat.format(date);
        return outputDateStr;
    }

    public static String getDateFromMillies(long milliSeconds, String formate) {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(formate);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    public static String ChangeDateFormat(String inputFormat, String outputFormat, String data) {
        DateFormat input = new SimpleDateFormat(inputFormat);
        DateFormat output = new SimpleDateFormat(outputFormat);
        Date date = null;
        try {
            date = input.parse(data);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.d("parseError", "ChangeDateFormate: " + e.toString());
        }
        return output.format(date);
    }

    public static String getDateMMMFromMillies(long milliSeconds) {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMM, yyyy");

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    public static String ChangeTimeFormate(String inputDateStr) throws ParseException {
        DateFormat inputFormat = new SimpleDateFormat("hh:mm:ss");
        DateFormat outputFormat = new SimpleDateFormat("hh:mm a");
        Date date = inputFormat.parse(inputDateStr);
        String outputDateStr = outputFormat.format(date);
        return outputDateStr;
    }

    public static long getTimeInMillies(String inputFormate, String data) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(inputFormate); // I assume d-M, you may refer to M-d for month-day instead.
            Date date = null; // You will need try/catch around this

            date = formatter.parse(data);
            //assert date != null;
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            Log.d(TAG, "getTimeInMillies: " + e.toString());
            return 0;
        }
    }

    public static String FullMonthName(String inputDateStr) throws ParseException {
        DateFormat inputFormat = new SimpleDateFormat("MM");
        DateFormat outputFormat = new SimpleDateFormat("MMMM");
        Date date = inputFormat.parse(inputDateStr);
        String outputDateStr = outputFormat.format(date);
        return outputDateStr;
    }

    public static String ChangeFullDate(String inputDateStr) throws ParseException {
        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat outputFormat = new SimpleDateFormat("dd");
        Date date = inputFormat.parse(inputDateStr);
        String outputDateStr = outputFormat.format(date);
        return outputDateStr;
    }

    public static String ChangeDateFormateToFull(String inputDateStr) {
        try {
            DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
            DateFormat outputFormat = new SimpleDateFormat("dd MMM, yyyy");
            Date date = inputFormat.parse(inputDateStr);
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.d("parse error", "ChangeDateFormateToFull: " + e.toString());
            return null;
        }
    }

    public static int getDifferebceOfDates(String startDate, String endDate) {
        try {
            Date date1;
            Date date2;
            SimpleDateFormat dates = new SimpleDateFormat("yyyy-MM-dd");
            date1 = dates.parse(startDate);
            date2 = dates.parse(endDate);
            long difference = Math.abs(date1.getTime() - date2.getTime());
            long differenceDates = difference / (24 * 60 * 60 * 1000);
            String dayDifference = Long.toString(differenceDates);
            return Integer.parseInt(dayDifference);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.d(TAG, "getDifferebceOfDates: " + e.toString());
            return 0;
        }
    }

    public static String getRangeDifferebceOfDates(String startDate, String endDate) throws ParseException {
        Date date1;
        Date date2;
        SimpleDateFormat dates = new SimpleDateFormat("dd MMM");
        date1 = dates.parse(startDate);
        date2 = dates.parse(endDate);
        long difference = Math.abs(date1.getTime() - date2.getTime());
        long differenceDates = difference / (24 * 60 * 60 * 1000);
        String dayDifference = Long.toString(differenceDates);
        return dayDifference;
    }

    public static int getMonthPositions(String inputDateStr) {
        try {
            DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
            DateFormat outputFormat = new SimpleDateFormat("MM");
            Date date = null;
            date = inputFormat.parse(inputDateStr);
            return Integer.parseInt(outputFormat.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
            Log.d(TAG, "getMonthPositions: " + e.toString());
            return -1;
        }
    }

    private static long DownloadFiles(ProgressBar view, ImageView imageView, String url, String filename, Context context) {
        view.setVisibility(View.VISIBLE);
        imageView.setVisibility(View.GONE);

        Uri uri = Uri.parse(url);

        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        Log.d("download_URL", url);

        request.setTitle("School App");
        request.setDescription("Downloading file...");

        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, filename);
        request.setMimeType("*");

        return downloadManager.enqueue(request);
    }

    public static long PermissionRequest(ProgressBar view, ImageView imageView, String url, String filename, Context context) {
        final long[] dowloadId = new long[1];
        Dexter.withActivity((Activity) context)
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        Toast.makeText(context, "Downloading file... ", Toast.LENGTH_SHORT).show();
                        dowloadId[0] = DownloadFiles(view, imageView, url, filename, context);
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        Toast.makeText(context, "Permission Denied!! :(\n Please Try again to download file", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, final PermissionToken token) {
                        new AlertDialog.Builder(context).setTitle("Permission Required")
                                .setMessage("Permission is required to proceed")
                                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        token.cancelPermissionRequest();
                                    }
                                })
                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        token.continuePermissionRequest();
                                    }
                                })
                                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                                    @Override
                                    public void onDismiss(DialogInterface dialog) {
                                        token.cancelPermissionRequest();
                                    }
                                })
                                .show();
                    }
                }).check();
        return dowloadId[0];
    }

    public static ArrayList<String> convertIntoArrayList(String[] strings) {
        ArrayList<String> stringArrayList = new ArrayList<>();
        for (String s : strings)
        {
            stringArrayList.add(s);
        }
        return stringArrayList;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void startCircularEffect(View myView) {
        // get the center for the clipping circle
        int cx = (myView.getLeft() + myView.getRight()) / 2;
        int cy = (myView.getTop() + myView.getBottom()) / 2;

        // get the final radius for the clipping circle
        int dx = Math.max(cx, myView.getWidth() - cx);
        int dy = Math.max(cy, myView.getHeight() - cy);
        float finalRadius = (float) Math.hypot(dx, dy);

        // Android native animator
        Animator animator = ViewAnimationUtils.createCircularReveal(myView, cx, cy, 0, finalRadius);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setDuration(500);

        animator.start();

    }

    public static void improveWebViewPerformance(WebView webView) {
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webSettings.setAppCacheEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webSettings.setDomStorageEnabled(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSettings.setUseWideViewPort(true);
        webSettings.setSavePassword(true);
        webSettings.setSaveFormData(true);
        webSettings.setEnableSmoothTransition(true);
    }

    public static String getCurrentYear() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        return formatter.format(calendar.getTime());
    }

    public static String getFormatedFinancialYear(String inputDateStr) {
        String[] string = inputDateStr.split("\\+|\\-|\\*|\\/");
        DateFormat inputFormat = new SimpleDateFormat("yyyy");
        DateFormat outputFormat = new SimpleDateFormat("yy");
        Date date = null;
        try {
            date = inputFormat.parse(string[1].trim());
        } catch (ParseException e) {
            Log.d("parseError:", e.toString());
            e.printStackTrace();
        }
        String outputDateStr = outputFormat.format(date);
        return string[0].trim() + "-" + outputDateStr;
    }

    public static String getFormatedReturnPeriod(String inputDateStr) {
        String month = inputDateStr.substring(0, 2);
        String year = inputDateStr.substring(2, 6);
        DateFormat inputFormat = new SimpleDateFormat("MM yyyy");
        DateFormat outputFormat = new SimpleDateFormat("MMM, yyyy");
        Date date = null;
        try {
            date = inputFormat.parse(month + " " + year);
        } catch (ParseException e) {
            Log.d("parseError:", e.toString());
            e.printStackTrace();
        }
        String outputDateStr = outputFormat.format(date);
        Log.d(TAG, "getFormatedReturnPeriod: " + month + " " + year);
        return outputDateStr;
    }

}
