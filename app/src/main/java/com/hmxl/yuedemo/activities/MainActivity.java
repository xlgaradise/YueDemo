package com.hmxl.yuedemo.activities;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.baidu.mapapi.radar.RadarNearbyResult;
import com.baidu.mapapi.radar.RadarSearchError;
import com.baidu.mapapi.radar.RadarSearchListener;
import com.baidu.mapapi.radar.RadarSearchManager;
import com.baidu.mapapi.radar.RadarUploadInfo;
import com.hmxl.yuedemo.R;

/**
 * Created by HPC on 2017/5/5.
 */

public class MainActivity extends AppCompatActivity {

    RadarSearchManager mManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mManager = RadarSearchManager.getInstance();
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
        FragmentMap fragment1 = new FragmentMap();
        transaction.add(R.id.fragment_map, fragment1);
        transaction.commit();
    }


    private void upinfo(){
        //周边雷达设置监听
        mManager.addNearbyInfoListener(new RadarSearchListener() {

            @Override
            public void onGetNearbyInfoList(RadarNearbyResult radarNearbyResult, RadarSearchError radarSearchError) {

            }

            //监听上传结果
            @Override
            public void onGetUploadState(RadarSearchError radarSearchError) {
                // TODO Auto-generated method stub
                if (radarSearchError == RadarSearchError.RADAR_NO_ERROR) {
                    //上传成功
                    Toast.makeText(getBaseContext(), "单次上传位置成功", Toast.LENGTH_LONG)
                            .show();
                } else {
                    //上传失败
                    Toast.makeText(getBaseContext(), "单次上传位置失败", Toast.LENGTH_LONG)
                            .show();
                }
            }

            @Override
            public void onGetClearInfoState(RadarSearchError radarSearchError) {

            }
        });

        //周边雷达设置用户身份标识，id为空默认是设备标识
        mManager.setUserID("");
        //上传位置
        RadarUploadInfo info = new RadarUploadInfo();
        info.comments ="用户备注信息";
        //info.pt = pt;
        mManager.uploadInfoRequest(info);


    }



}
