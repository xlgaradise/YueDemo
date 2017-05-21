package com.hmxl.yuedemo.activities;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;
import com.hmxl.yuedemo.tools.baidumap.LocationManager;

import cn.bmob.v3.Bmob;

/**
 * Created by HPC on 2017/5/5.
 */

public class ApplicationDemo extends Application {


    public static final String SHAREDPREFERENCE_NAME = "YueDemo_preference";

    @Override
    public void onCreate() {

        super.onCreate();
        SDKInitializer.initialize(getApplicationContext());
        LocationManager.initialize(getApplicationContext());
        LocationManager.getInstance().startLocationSDK();

        Bmob.initialize(getApplicationContext(),"3efba5cd8767b75e4e207e62f4698ab1");

    }
}
