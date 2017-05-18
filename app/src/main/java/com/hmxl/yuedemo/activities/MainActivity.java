package com.hmxl.yuedemo.activities;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.radar.RadarSearchManager;
import com.hmxl.yuedemo.R;
import com.hmxl.yuedemo.tools.baidumap.LocationManager;
import com.hmxl.yuedemo.tools.baidumap.MapManager;

/**
 * Created by HPC on 2017/5/5.
 */

public class MainActivity extends AppCompatActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        showMap();


        Button btn_map = (Button) findViewById(R.id.btn_upinfo);
        btn_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }



    private void showMap(){
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.fragment_map,
                MapManager.getInstance().createMapFragment(31.227, 121.481));
        transaction.commit();
    }





}
