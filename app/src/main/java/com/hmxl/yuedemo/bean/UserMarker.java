package com.hmxl.yuedemo.bean;

import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.hmxl.yuedemo.bean.User.Sex;
import com.hmxl.yuedemo.R;

/**
 * Created by HPC on 2017/5/14.
 */

public class UserMarker {

    private static final BitmapDescriptor ico_female =
            BitmapDescriptorFactory.fromResource(R.drawable.icon_map_mark_female);
    private static final BitmapDescriptor ico_male =
            BitmapDescriptorFactory.fromResource(R.drawable.icon_map_mark_male);

    private RadarUser radarUser;
    private LatLng pt;
    private BitmapDescriptor ico;
    private MarkerOptions markerOption;
    public Marker marker;

    public UserMarker(RadarUser radarUser, LatLng pt) {
        this.radarUser = radarUser;
        this.pt = pt;
        if(radarUser.sex == Sex.female){
            ico = ico_female;
        }else{
            ico = ico_male;
        }
        markerOption = new MarkerOptions().position(pt)
                .icon(ico).zIndex(0);
        markerOption.animateType(MarkerOptions.MarkerAnimateType.grow);
    }

    public RadarUser getRadarUser() {
        return radarUser;
    }

    public LatLng getPt() {
        return pt;
    }

    public BitmapDescriptor getIco() {
        return ico;
    }

    public MarkerOptions getMarkerOption() {
        return markerOption;
    }
}
