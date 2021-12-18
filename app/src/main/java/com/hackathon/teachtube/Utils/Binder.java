package com.hackathon.teachtube.Utils;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

/**
 * Created by Harsh Parihar on 11/21/2021.
 */

public class Binder extends Application implements LifecycleObserver {

    public boolean appStatus;
    @Override
    public void onCreate() {
        super.onCreate();
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onAppBackgrounded() {
        appStatus = false;
        Log.d("MainActivity", "onAppBackgrounded: ");
        //App in background
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onAppForegrounded() {
        appStatus = true;
        Log.d("MainActivity", "onAppForegrounded: ");
        // App in foreground
    }

    public interface ValueChangeListener {
        void onChanged(Boolean value);
    }
    private ValueChangeListener visibilityChangeListener;
    public void setOnVisibilityChangeListener(ValueChangeListener listener) {
        this.visibilityChangeListener = listener;
    }
    private void isAppInBackground(Boolean isBackground) {
        if (null != visibilityChangeListener) {
            visibilityChangeListener.onChanged(isBackground);
        }
    }
    private static Binder mInstance;
    public static Binder getInstance() {
        return mInstance;
    }

}
