package com.example.orion.leitourgika;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.BatteryManager;
import android.os.Build;
import android.os.SystemClock;


import java.util.concurrent.TimeUnit;

/**
 * Created by Orion on 7/1/2015.
 */
public class Info {

    public static int TYPE_WIFI = 1;
    public static int TYPE_MOBILE = 2;
    public static int TYPE_NOT_CONNECTED = 0;

    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }

    private static String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }

    public static String getAndroidVersion() {
        return Build.VERSION.RELEASE;
    }

    public static String getKernelVersion() {
        return System.getProperty("os.version");
    }

    public static String BatteryLevel() {
        return String.valueOf(BatteryManager.EXTRA_LEVEL);
    }

    public static String Uptime(){
        long uptimeMillis = SystemClock.elapsedRealtime();
        return TimeUnit.MILLISECONDS.toHours(uptimeMillis)+"";
    }

    public static int getConnectivityStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if(activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                return TYPE_WIFI;

            if(activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                return TYPE_MOBILE;
        }
        return TYPE_NOT_CONNECTED;
    }

    public static String getConnectivityStatusString(Context context) {
        int conn = Info.getConnectivityStatus(context);
        String status = null;
        if (conn == Info.TYPE_WIFI) {
            status = "Wifi enabled";
        } else if (conn == Info.TYPE_MOBILE) {
            status = "Mobile data enabled";
        } else if (conn == Info.TYPE_NOT_CONNECTED) {
            status = "Not connected to Internet";
        }
        return status;
    }

}
