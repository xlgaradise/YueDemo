package com.hmxl.yuedemo.tools.baidumap;


import android.os.Bundle;

import com.hmxl.yuedemo.fragment.MapFragment;

/**
 * Created by HPC on 2017/5/11.
 */

public class MapManager {

    private static MapManager mapManager = null;

    private MapManager(){

    }

    public static MapManager getInstance(){
        if(mapManager == null){
            mapManager = new MapManager();
        }
        return mapManager;
    }

    public MapFragment createMapFragment(double latitude,double longitude){
        MapFragment fragmentMap = new MapFragment();
        Bundle bundle = new Bundle();
        bundle.putDouble("latitude",latitude);
        bundle.putDouble("longitude",longitude);
        fragmentMap.setArguments(bundle);
        return fragmentMap;
    }



}
