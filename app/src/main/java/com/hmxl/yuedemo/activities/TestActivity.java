package com.hmxl.yuedemo.activities;


import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.hmxl.yuedemo.R;
import com.hmxl.yuedemo.bean.RadarRequsetOption;
import com.hmxl.yuedemo.bean.RadarUser;
import com.hmxl.yuedemo.bean.User;
import com.hmxl.yuedemo.bean.UserMarker;
import com.hmxl.yuedemo.fragment.MapFragment;
import com.hmxl.yuedemo.tools.baidumap.MapManager;
import com.hmxl.yuedemo.tools.baidumap.RadarManager;

import java.util.ArrayList;

/**
 * Created by HPC on 2017/5/5.
 */

public class TestActivity extends AppCompatActivity {


    MapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        showMap();


        Button btn_up = (Button) findViewById(R.id.btn_upinfo);
        btn_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                RadarUser radarUser = new RadarUser();
                //radarUser.id = "4b13902af6";
                radarUser.id = "nullUser";
                radarUser.sex = User.Sex.male;
                radarUser.name = "erbi";
                radarUser.requsetOption.searchType = RadarRequsetOption.SearchType.all;
                radarUser.requsetOption.searchSex = "female";
                radarUser.requsetOption.date = "2017-05-20";
                radarUser.requsetOption.message = "我我我我我我我我 我我我 我我问问 我    我我我我我我我我我我我我 我我我 我我问问 我    我我我我我我我我我我我我 我我我 我我问问 我    我我我我";
//                RadarManager.getInstance(getBaseContext()).uploadOnce(radarUser);

                Handler handler = new Handler(){
                    @Override
                    public void handleMessage(Message msg) {
                        //ArrayList<UserMarker> list = (ArrayList<UserMarker>)msg.obj;
                        //MapManager.getInstance().showMarks(null,list);
                    }
                };
                RadarManager.getInstance(getBaseContext())
                        .uploadOnce(radarUser,handler);
            }
        });

        Button btn_down = (Button) findViewById(R.id.btn_downinfo);
        btn_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Handler handler = new Handler(){
                    @Override
                    public void handleMessage(Message msg) {
                        ArrayList<UserMarker> list = (ArrayList<UserMarker>)msg.obj;
                        MapManager.getInstance().showMarks(null,list);
                    }
                };
                RadarManager.getInstance(getBaseContext())
                        .requestUserInfoWithNullUser(handler);


//                RadarManager.getInstance(getBaseContext()).requestUserInfo();
            }
        });

        Button btn_clear = (Button) findViewById(R.id.btn_clearinfo);
        btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RadarManager.getInstance(getBaseContext()).clear();
                MapManager.getInstance().clearMarks(null);
            }
        });
    }



    private void showMap(){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft= fm.beginTransaction();
        ft.add(R.id.fragment_map,MapManager.getInstance().createMapFragment(getBaseContext()));
        ft.commit();


    }






}
