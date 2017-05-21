package com.hmxl.yuedemo.tools.baidumap;


import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.hmxl.yuedemo.R;
import com.hmxl.yuedemo.bean.User;
import com.hmxl.yuedemo.fragment.MapFragment;
import com.hmxl.yuedemo.bean.UserMarker;

import java.util.ArrayList;

/**
 * Created by HPC on 2017/5/11.
 */

public class MapManager {

    private static MapManager mapManager = null;

    private ArrayList<UserMarker> userMarkersList = new ArrayList<>();

    private MapFragment defaultMapFragment;

    private MapManager(){
        //34.819295,114.312589

    }

    public static MapManager getInstance(){
        if(mapManager == null){
            mapManager = new MapManager();
        }
        return mapManager;
    }

    /**
     * 获取新的MapFragment
     * @param latitude
     * @param longitude
     * @return
     */
    public MapFragment createMapFragment(double latitude,double longitude){
        MapFragment fragmentMap = new MapFragment();
        Bundle bundle = new Bundle();
        bundle.putDouble("latitude",latitude);
        bundle.putDouble("longitude",longitude);
        fragmentMap.setArguments(bundle);
        defaultMapFragment = fragmentMap;
        return fragmentMap;
    }


    /**
     * 在地图上显示Marks
     * @param mapFragment 可为null
     * @param userMarkersList
     */
    public void showMarks(MapFragment mapFragment,ArrayList<UserMarker> userMarkersList){
        if(mapFragment == null){
            if(defaultMapFragment != null)
                defaultMapFragment.showMarks(userMarkersList);
            else return;
        }else{
            mapFragment.showMarks(userMarkersList);
        }
        this.userMarkersList = userMarkersList;
    }

    /**
     * 清除地图上的Marks
     * @param mapFragment 可为null
     */
    public void clearMarks(MapFragment mapFragment){
        if(mapFragment == null){
            if(defaultMapFragment != null){
                defaultMapFragment.clearMarks();
            }else{
                return;
            }
        }else{
            mapFragment.clearMarks();
        }
    }

    public ArrayList<UserMarker> getUserMarkersList(){
        return userMarkersList;
    }

    public void showMarkPopupWindow(View view, UserMarker userMarker) {
        final Context context = view.getContext();
        // 一个自定义的布局，作为显示的内容
        View contentView = LayoutInflater.from(context).inflate(
                R.layout.popwindow, null);
        // 初始化各组件
        ImageView iv_avatar = (ImageView) contentView.findViewById(R.id.iv_card_avatar);
        TextView tv_name = (TextView) contentView.findViewById(R.id.tv_card_name);
        TextView tv_signature = (TextView) contentView.findViewById(R.id.tv_card_signature);
        Button btn_chat = (Button) contentView.findViewById(R.id.btn_chat);
        Button btn_addfriend = (Button) contentView.findViewById(R.id.btn_addfriend);

        if(userMarker.getRadarUser().sex == User.Sex.male){
            iv_avatar.setBackgroundResource(R.drawable.icon_map_male);
        }else{
            iv_avatar.setBackgroundResource(R.drawable.icon_map_female);
        }
        tv_name.setText(userMarker.getRadarUser().name);
        tv_signature.setText(userMarker.getRadarUser().requsetOption.message);

        btn_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btn_addfriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        final PopupWindow popupWindow = new PopupWindow(contentView,
                600,ViewGroup.LayoutParams.WRAP_CONTENT , true);

//        final PopupWindow popupWindow = new PopupWindow(contentView);
        popupWindow.setTouchable(true);
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });

        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        popupWindow.setBackgroundDrawable(context.getResources()
                .getDrawable(R.drawable.bg_filling));

        // 设置好参数之后再show
        popupWindow.showAtLocation(view, Gravity.CENTER,0,0);

    }
}
