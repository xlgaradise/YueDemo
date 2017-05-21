package com.hmxl.yuedemo.tools.baidumap;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.MyLocationData;

/**
 * Created by HPC on 2017/5/13.
 */

public class LocationManager {

    private static LocationManager locationManager = null;
    private LocationClient locationClient;
    private boolean isListened;
    private boolean isStarted;
    private boolean isRequested;
    private MyLocationListener myLocationListener;
    private MyLocationData locationData;

    private LocationManager(Context context){
        this.locationClient = new LocationClient(context);
        this.locationClient.setLocOption(initLocationOption());
        isListened = false;
        isStarted = false;
        isRequested = false;
    }

    public static LocationManager getInstance(){
        return  locationManager;
    }

    public static void initialize(Context context){
        if(locationManager == null){
            locationManager = new LocationManager(context);
        }
    }

    /**
     * 启动定位SDK
     */
    public void startLocationSDK(){
        if(!isStarted){
            registerLocationListener();
            this.locationClient.start();
            isStarted = true;
        }
    }

    /**
     * 关闭定位SDK
     */
    public void stopLocationSDK(){
        if(isStarted){
            unRegisterLocationListener();
            this.locationClient.stop();
        }
    }

    /**
     * 注册监听函数
     */
    private void registerLocationListener(){
        myLocationListener = new MyLocationListener();
        locationClient.registerLocationListener(myLocationListener);
        isListened = true;
    }

    /**
     * 取消监听函数
     */
    private void unRegisterLocationListener(){
        locationClient.unRegisterLocationListener(myLocationListener);
        isListened = false;
    }

    /**
     *发起定位请求
     */
    public boolean requestLocation(Handler handler){
        if (locationClient.isStarted() && isListened){
            isRequested = true;
            myLocationListener.setHandler(handler);
            locationClient.requestLocation();
            return true;
        }
        return false;
    }

    //初始化定位参数
    private LocationClientOption initLocationOption(){
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//设置定位模式
        option.setCoorType("bd09ll");//返回的定位结果是百度经纬度，默认值gcj02
        option.setScanSpan(500);//设置发起定位请求的间隔时间
        option.setIsNeedAddress(false);//设置是否需要地址信息，默认为无地址
        option.setOpenGps(true);
        return option;
    }


    //设置监听函数,接收定位结果
    private class MyLocationListener implements BDLocationListener {

        private Handler hd = null;
        public void setHandler(Handler handler){
            this.hd = handler;
        }

        @Override
        public void onReceiveLocation(BDLocation location) {
            //Receive Location
            if(!isRequested) return;
            isRequested = false;
            if(location != null){
                int type = location.getLocType();
                if (type == 61 || type == 161) {//success
                    locationData = new MyLocationData.Builder()
                            .accuracy(location.getRadius())
                            // 此处设置开发者获取到的方向信息，顺时针0-360
                            //.direction(mCurrentDirection)
                            .latitude(location.getLatitude())
                            .longitude(location.getLongitude()).build();
                    if (hd != null) {
                        Message m = Message.obtain();
                        m.what = 1;
                        m.obj = locationData;
                        hd.sendMessage(m);
                        hd = null;
                    }
                    return;
                }
            }
            locationData = null;
            if (hd != null) {
                Message m = Message.obtain();
                m.what = 0;
                m.obj = locationData;
                hd.sendMessage(m);
                hd = null;
            }
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }
    }


}
