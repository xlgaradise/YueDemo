package com.hmxl.yuedemo.activities;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;

/**
 * Created by HPC on 2017/5/5.
 */

public class ApplicationDemo extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SDKInitializer.initialize(getApplicationContext());
    }
}
