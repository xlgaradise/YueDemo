package com.hmxl.yuedemo.fragment;

import android.app.Fragment;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.hmxl.yuedemo.R;
import com.hmxl.yuedemo.tools.baidumap.LocationManager;

/**
 * Created by HPC on 2017/5/5.
 */

public class MapFragment extends Fragment implements SensorEventListener {

    LatLng point = null;
    MapView mapView = null;
    BaiduMap baiduMap;
    View parentView = null;
    ImageButton btn_location = null;
    MyLocationData locationData;
    private SensorManager mSensorManager;
    boolean isRequestLocation = false;
    double lastX = 0.0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_mapview,container,false);
        return parentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        setMapOptions();
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            locationData = (MyLocationData) msg.obj;
            updateMap(locationData);
        }
    };
    private void initView(){
        mSensorManager = (SensorManager) parentView.getContext().getSystemService(Context.SENSOR_SERVICE);//获取传感器管理服务
        mapView = (MapView) parentView.findViewById(R.id.mapView);
        baiduMap = mapView.getMap();
        btn_location = (ImageButton) parentView.findViewById(R.id.btn_location);
        btn_location.setImageResource(R.drawable.ico_map_location_normal);
        btn_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_location.setImageResource(R.drawable.ico_map_location_normal);
                LocationManager.getInstance(null).requestLocation(handler);
                isRequestLocation = true;
            }
        });

        baiduMap.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {
            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus) {
                if(isRequestLocation){
                    isRequestLocation = false;
                    return;
                }
                btn_location.setImageResource(R.drawable.ico_map_location);
            }
            @Override
            public void onMapStatusChange(MapStatus mapStatus) {

            }
            @Override
            public void onMapStatusChangeFinish(MapStatus mapStatus) {

            }
        });
    }

    private void setMapOptions(){
        Bundle bundle = getArguments();
        //默认天安门
        Double latitude = bundle.getDouble("latitude",39.945);
        Double longitude = bundle.getDouble("longitude",116.404);
        point = new LatLng(latitude,longitude);

        //显示指南针，不显示缩放控件
        UiSettings uiSettings = baiduMap.getUiSettings();
        uiSettings.setCompassEnabled(true);
        mapView.showZoomControls(false);
        // 开启定位图层
        baiduMap.setMyLocationEnabled(true);
        baiduMap.setMyLocationConfiguration(new MyLocationConfiguration(
                MyLocationConfiguration.LocationMode.NORMAL, true, null));

        //设置坐标点，缩放级别，俯仰角
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.target(point).overlook(-20).zoom(12);

        baiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
    }

    public MapView getMapView(){
        return mapView;
    }


    public void updateMap(MyLocationData locationData){
        baiduMap.setMyLocationData(locationData);
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.target(new LatLng(locationData.latitude,locationData.longitude))
                .zoom(17.5f);
        baiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
    }
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
        //为系统的方向传感器注册监听器
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        //取消注册传感器监听
        mSensorManager.unregisterListener(this);
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        baiduMap.setMyLocationEnabled(false);
        mapView.onDestroy();
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if(locationData == null) return;
        double x = sensorEvent.values[SensorManager.DATA_X];
        if (Math.abs(x - lastX) > 1.0) {
           int direction = (int) x;
            MyLocationData date = new MyLocationData.Builder()
                    .accuracy(locationData.accuracy)
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(direction).latitude(locationData.latitude)
                    .longitude(locationData.longitude).build();
            baiduMap.setMyLocationData(date);
        }
        lastX = x;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
