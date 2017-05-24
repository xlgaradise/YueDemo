package com.hmxl.yuedemo.activities;

import android.app.Activity;
import android.app.Application;
import android.util.Log;

import com.baidu.mapapi.SDKInitializer;
import com.hmxl.yuedemo.bmobMessage.DemoMessageHandler;
import com.hmxl.yuedemo.tools.baidumap.LocationManager;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.newim.BmobIM;
import cn.bmob.v3.Bmob;

/**
 * Created by HPC on 2017/5/5.
 */

public class ApplicationDemo extends Application {

    private static ApplicationDemo INSTANCE;
    public static ApplicationDemo INSTANCE(){
        return INSTANCE;
    }
    private void setInstance(ApplicationDemo app) {
        setBmobIMApplication(app);
    }
    private static void setBmobIMApplication(ApplicationDemo a) {
        ApplicationDemo.INSTANCE = a;
    }
    public static final String SHAREDPREFERENCE_NAME = "YueDemo_preference";

    private static List<Activity> oList;//用于存放所有启动的Activity的集合


    @Override
    public void onCreate() {

        super.onCreate();
        oList = new ArrayList<>();
        SDKInitializer.initialize(getApplicationContext());
        LocationManager.initialize(getApplicationContext());
        LocationManager.getInstance().startLocationSDK();

        Bmob.initialize(getApplicationContext(),"3efba5cd8767b75e4e207e62f4698ab1");


        Log.d("Application","BmobIM init");
        //im初始化
        BmobIM.init(this);
        //注册消息接收器
        BmobIM.registerDefaultMessageHandler(new DemoMessageHandler(this));

    }




    /**
     * 添加Activity
     */
    public void addActivity_(Activity activity) {
        // 判断当前集合中不存在该Activity
        if (!oList.contains(activity)) {
            oList.add(activity);//把当前Activity添加到集合中
        }
    }

    /**
     * 销毁单个Activity
     */
    public void removeActivity_(Activity activity) {
        //判断当前集合中存在该Activity
        if (oList.contains(activity)) {
            oList.remove(activity);//从集合中移除
            activity.finish();//销毁当前Activity
        }
    }

    /**
     * 销毁所有的Activity
     */
    public static void removeALLActivity_() {
        //通过循环，把集合中的所有Activity销毁
        for (Activity activity : oList) {
            activity.finish();
        }
        BmobIM.getInstance().disConnect();
    }


}
