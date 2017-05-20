package com.hmxl.yuedemo.activities;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;
import com.hmxl.yuedemo.tools.baidumap.LocationManager;



/**
 * Created by HPC on 2017/5/5.
 */

public class ApplicationDemo extends Application {
    @Override
    public void onCreate() {

        super.onCreate();
        SDKInitializer.initialize(getApplicationContext());
        LocationManager.getInstance(getApplicationContext());
        LocationManager.getInstance(null).startLocationSDK();


    }
}
