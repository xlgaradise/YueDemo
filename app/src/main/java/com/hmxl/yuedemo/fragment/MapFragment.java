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
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.hmxl.yuedemo.R;
import com.hmxl.yuedemo.bean.UserMarker;
import com.hmxl.yuedemo.tools.baidumap.LocationManager;
import com.hmxl.yuedemo.tools.baidumap.MapManager;

import java.util.ArrayList;

/**
 * Created by HPC on 2017/5/5.
 */

public class MapFragment extends Fragment {

    View parentView = null;
    Handler handler;

    MapView mapView = null;
    BaiduMap baiduMap;

    ImageButton btn_location = null;
    MyLocationData locationData;
    SensorManager mSensorManager;
    SensorEventListener sensorEventListener;

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


    /**
     * 初始化地图视图
     */
    private void initView(){
        //传感器
        mSensorManager = (SensorManager) parentView.getContext().getSystemService(Context.SENSOR_SERVICE);//获取传感器管理服务
        sensorEventListener = new SensorEventListener() {
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
        };

        //map 地图
        mapView = (MapView) parentView.findViewById(R.id.mapView);
        baiduMap = mapView.getMap();
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what == 1){//success
                    locationData = (MyLocationData) msg.obj;
                    //System.out.println(locationData.latitude+","+locationData.longitude);
                    updateMap(locationData);
                }else{
                    Toast.makeText(parentView.getContext(),"获取定位失败！",Toast.LENGTH_SHORT).show();
                }
            }
        };

        btn_location = (ImageButton) parentView.findViewById(R.id.btn_location);
        btn_location.setImageResource(R.drawable.icon_map_location_normal);
        btn_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_location.setImageResource(R.drawable.icon_map_location_normal);
                LocationManager.getInstance().requestLocation(handler);
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
                btn_location.setImageResource(R.drawable.icon_map_location);
            }
            @Override
            public void onMapStatusChange(MapStatus mapStatus) {

            }
            @Override
            public void onMapStatusChangeFinish(MapStatus mapStatus) {

            }
        });

        baiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                ArrayList<UserMarker> userMarkers = MapManager.getInstance().getUserMarkersList();
                for(UserMarker userMarker:userMarkers){
                    if(marker == userMarker.marker){
//                        Toast.makeText(parentView.getContext(),
//                                userMarker.getUserID()+"",Toast.LENGTH_SHORT).show();
                        MapManager.getInstance().showMarkPopupWindow(parentView,userMarker);
                        break;
                    }
                }
                return true;
            }
        });
    }

    /**
     * 设置地图初始化参数
     */
    private void setMapOptions(){
        Bundle bundle = getArguments();
        //默认天安门
        Double latitude = bundle.getDouble("latitude",39.945);
        Double longitude = bundle.getDouble("longitude",116.404);
        LatLng point = new LatLng(latitude,longitude);

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


    /**
     * 更新地图
     * @param locationData
     */
    public void updateMap(MyLocationData locationData){
        baiduMap.setMyLocationData(locationData);
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.target(new LatLng(locationData.latitude,locationData.longitude))
                .zoom(17.5f);
        baiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
    }

    /**
     * 显示marks图标
     * @param userMarkers
     */
    public void showMarks(ArrayList<UserMarker> userMarkers){
        Marker marker;
        baiduMap.clear();
        for(UserMarker userMarker: userMarkers){
            marker = (Marker) (baiduMap.addOverlay(userMarker.getMarkerOption()));
            userMarker.marker = marker;
        }
    }

    /**
     * 清除marks图标
     */
    public void clearMarks(){
        baiduMap.clear();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
        //为系统的方向传感器注册监听器
        mSensorManager.registerListener(sensorEventListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
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
        mSensorManager.unregisterListener(sensorEventListener);
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        baiduMap.setMyLocationEnabled(false);
        mapView.onDestroy();
    }
}
