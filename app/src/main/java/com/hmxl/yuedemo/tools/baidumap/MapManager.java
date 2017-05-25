package com.hmxl.yuedemo.tools.baidumap;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.model.LatLng;
import com.hmxl.yuedemo.R;
import com.hmxl.yuedemo.bean.Friend;
import com.hmxl.yuedemo.bean.RadarUser;
import com.hmxl.yuedemo.bean.User;
import com.hmxl.yuedemo.bmobMessage.BmobIMManager;
import com.hmxl.yuedemo.fragment.MapFragment;
import com.hmxl.yuedemo.bean.UserMarker;

import java.util.ArrayList;

import cn.bmob.v3.BmobUser;

import static android.content.Context.MODE_PRIVATE;
import static com.hmxl.yuedemo.activities.ApplicationDemo.SHAREDPREFERENCE_NAME;

/**
 * Created by HPC on 2017/5/11.
 */

public class MapManager {
    private static String TAG = "MapManager";

    private static MapManager mapManager = null;

    private ArrayList<UserMarker> userMarkersList = new ArrayList<>();

    private MapFragment defaultMapFragment;

    private User friend;
    private User stranger;
    private boolean isFriend = false;

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
     * 获取新的MapFragment,默认显示上次定位
     * @return
     */
    public MapFragment createMapFragment(Context context){
        LatLng pt = getPt(context);
        MapFragment fragmentMap = new MapFragment();
        Bundle bundle = new Bundle();
        bundle.putDouble("latitude",pt.latitude);
        bundle.putDouble("longitude",pt.longitude);
        fragmentMap.setArguments(bundle);
        defaultMapFragment = fragmentMap;
        return fragmentMap;
    }

    public void setUserMarksList(ArrayList<UserMarker> userMarkersList){
        this.userMarkersList.clear();
        this.userMarkersList.addAll(userMarkersList);
    }

    public ArrayList<UserMarker> getUserMarkersList(){
        return userMarkersList;
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

    public void showMarkPopupWindow(View view, final UserMarker userMarker) {
        final Context context = view.getContext();
        // 一个自定义的布局，作为显示的内容
        View contentView = LayoutInflater.from(context).inflate(
                R.layout.popwindow, null);
        // 初始化各组件
        ImageView iv_avatar = (ImageView) contentView.findViewById(R.id.iv_card_avatar);
        TextView tv_name = (TextView) contentView.findViewById(R.id.tv_card_name);
        TextView tv_signature = (TextView) contentView.findViewById(R.id.tv_card_signature);
        final Button btn_chat = (Button) contentView.findViewById(R.id.btn_chat);
        final Button btn_addfriend = (Button) contentView.findViewById(R.id.btn_addfriend);

        final PopupWindow popupWindow = new PopupWindow(contentView,
                600,ViewGroup.LayoutParams.WRAP_CONTENT , true);

        if(userMarker.getRadarUser().sex == User.Sex.male){
            iv_avatar.setBackgroundResource(R.drawable.icon_map_male);
        }else{
            iv_avatar.setBackgroundResource(R.drawable.icon_map_female);
        }
        tv_name.setText(userMarker.getRadarUser().name);
        tv_signature.setText(userMarker.getRadarUser().requsetOption.message);
        btn_chat.setVisibility(View.GONE);
        btn_addfriend.setVisibility(View.GONE);

        Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what == 1){
                    final User user = ((Friend)msg.obj).getFriendUser();
                    btn_chat.setVisibility(View.VISIBLE);
                    btn_addfriend.setVisibility(View.GONE);
                    btn_chat.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            BmobIMManager.getInstance().goToChatActivity(context,user);
                            popupWindow.dismiss();
                        }
                    });
                }else{
                    btn_chat.setVisibility(View.GONE);
                    btn_addfriend.setVisibility(View.VISIBLE);
                    btn_addfriend.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            BmobIMManager.getInstance().sendAddFriendMessage(context,
                                    userMarker.getRadarUser().id,
                                    userMarker.getRadarUser().name);
                            popupWindow.dismiss();
                        }
                    });
                }
            }
        };

        BmobIMManager.getInstance().getMyFriend(userMarker.getRadarUser().id,handler);

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


    public void showFunctionPopupWindow(View view, final ImageButton btn_function) {
        final Context context = view.getContext();

        // 一个自定义的布局，作为显示的内容
        View contentView = LayoutInflater.from(context).inflate(R.layout.popwindow_function_map, null);

        final PopupWindow popupWindow = new PopupWindow(contentView,
                200, ViewGroup.LayoutParams.WRAP_CONTENT, true);

        // 设置按钮的点击事件
        Button btn_show = (Button) contentView.findViewById(R.id.btn_function_show);
        btn_show.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showMarks(null,userMarkersList);
                popupWindow.dismiss();
                Toast.makeText(context,userMarkersList.size()+"个结果已展示",Toast.LENGTH_SHORT).show();
            }
        });

        Button btn_clear = (Button) contentView.findViewById(R.id.btn_function_clear);
        btn_clear.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                clearMarks(null);
                popupWindow.dismiss();
                Toast.makeText(context,"结果已清除",Toast.LENGTH_SHORT).show();
            }
        });

        Button btn_refresh = (Button) contentView.findViewById(R.id.btn_function_refresh);
        btn_refresh.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                RadarUser radarUser = RadarManager.getInstance(context).getRadaruser();
                if(radarUser == null){
                    Toast.makeText(context,"请先上传发布信息",Toast.LENGTH_SHORT).show();
                    return;
                }
                clearMarks(null);
                Handler handler = new Handler(){
                    @Override
                    public void handleMessage(Message msg) {
                        ArrayList<UserMarker> list = (ArrayList<UserMarker>)msg.obj;
                        setUserMarksList(list);
                        showMarks(null,list);
                        Toast.makeText(context,list.size()+"个结果已搜索到",Toast.LENGTH_SHORT).show();
                    }
                };
                RadarManager.getInstance(context).uploadOnce(radarUser,handler);
                popupWindow.dismiss();
            }
        });

        Button btn_clearInfo = (Button) contentView.findViewById(R.id.btn_function_clearInfo);
        btn_clearInfo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(BmobUser.getCurrentUser() != null){
                    clearMarks(null);
                    userMarkersList.clear();
                    RadarManager.getInstance(context).clearUserInfo(BmobUser.getCurrentUser().getObjectId());
                }else{
                    Toast.makeText(context,"当前没有用户",Toast.LENGTH_SHORT).show();
                }

                popupWindow.dismiss();
                //Toast.makeText(context,"信息已清除",Toast.LENGTH_SHORT).show();
            }
        });

        popupWindow.setTouchable(true);
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                btn_function.setImageResource(R.drawable.icon_map_function_normal);
            }
        });

        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        popupWindow.setBackgroundDrawable(context.getResources()
                .getDrawable(R.drawable.bg_filling));

        // 设置好参数之后再show
        //popupWindow.showAtLocation(view, Gravity.TOP,700,260);
        popupWindow.showAsDropDown(view.findViewById(R.id.btn_function));
    }


    public LatLng getPt(Context context){
        // 读取SharedPreferences中需要的数据
        // 使用SharedPreferences来记录程序的使用次数
        SharedPreferences preferences = context.getSharedPreferences(
                SHAREDPREFERENCE_NAME, MODE_PRIVATE);
        // 取得相应的值，如果没有该值，说明还未写入，默认值为天安门
        double latitude = preferences.getFloat("latitude", 39.945f);
        double longitude = preferences.getFloat("longitude", 116.404f);
        return new LatLng(latitude,longitude);
    }

    public void savePt(Context context,LatLng pt){
        SharedPreferences sf = context.getSharedPreferences(
                SHAREDPREFERENCE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sf.edit();
        editor.putFloat("latitude",Float.parseFloat(pt.latitude+""));
        editor.putFloat("longitude",Float.parseFloat(pt.longitude+""));
        editor.commit();
    }
}
