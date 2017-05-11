package com.hmxl.yuedemo.activities;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;

/**
 * Created by HPC on 2017/5/5.
 */

public class FragmentMap extends Fragment {

    LatLng GEO_SHANGHAI = new LatLng(31.227, 121.481);
    MapView mMapView = null;

    public FragmentMap(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mMapView = new MapView(container.getContext(),getMapOptions());
        return  mMapView;
    }

    private BaiduMapOptions getMapOptions(){
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.target(GEO_SHANGHAI);
        builder.overlook(-20).zoom(12);
        BaiduMapOptions options = new BaiduMapOptions();
        options.mapStatus(builder.build())
                .compassEnabled(true).zoomControlsEnabled(false);
        return options;
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mMapView.onDestroy();
    }
}
