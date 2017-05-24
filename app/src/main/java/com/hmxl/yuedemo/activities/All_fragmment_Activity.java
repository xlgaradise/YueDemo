package com.hmxl.yuedemo.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.hmxl.yuedemo.R;
import com.hmxl.yuedemo.fragment.FriendFragment;
import com.hmxl.yuedemo.fragment.MapFragment;
import com.hmxl.yuedemo.fragment.MineFragment;
import com.hmxl.yuedemo.fragment.RequestInfoFragment;
import com.hmxl.yuedemo.tools.baidumap.LocationManager;
import com.hmxl.yuedemo.tools.baidumap.MapManager;
import com.hmxl.yuedemo.tools.exception.MyLog;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.listener.ConnectListener;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;

public class All_fragmment_Activity extends BaseActivity {

    private static final String TAG = "All_fragmment_Activity";
    //private ContactFragment communityFragment;
    private FriendFragment friendFragment;
    private MineFragment mineFragment;
    private RequestInfoFragment requestInfoFragment;
    private MapFragment mapFragment;
    private RadioGroup radioGroup;
    private PopupWindow mapPopupWindow;
    private PopupWindow friendPopupWindow;

    private Button btn_menu;
    private boolean isMapTabSelected = false;
    private boolean isMapShowing = true;
    private boolean isFriendTabSelected = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_fragmment);

        initView();

        Intent intent = getIntent();
        int index = intent.getIntExtra("index",1);
        showIndex(index);

        final BmobUser user = BmobUser.getCurrentUser();
        BmobIM.connect(user.getObjectId(), new ConnectListener() {
            @Override
            public void done(String uid, BmobException e) {
                if (e == null) {
                    Log.d(TAG,user.getUsername()+"登录IM服务器成功");
                    //Toast.makeText(ChatActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e(TAG,user.getUsername()+"登录失败:"+e.getErrorCode()+":"+e.getMessage());
                    MyLog.e(TAG,"登录IM服务器失败",e);
                    Toast.makeText(All_fragmment_Activity.this, "登录IM服务器失败", Toast.LENGTH_SHORT).show();
                }
            }
        });



    }

    private void initView(){
        radioGroup = (RadioGroup) findViewById(R.id.radiogruop);
        //communityFragment = new ContactFragment();
        friendFragment = new FriendFragment();
        mineFragment = new MineFragment();
        requestInfoFragment = new RequestInfoFragment();
        mapFragment = MapManager.getInstance().createMapFragment(getBaseContext());

        Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what == 1){//success
                    Log.d(TAG,"location success");
                    MyLocationData locationData = (MyLocationData) msg.obj;
                    LatLng pt = new LatLng(locationData.latitude,locationData.longitude);
                    MapManager.getInstance().savePt(All_fragmment_Activity.this,pt);
                    mapFragment = MapManager.getInstance().createMapFragment(getBaseContext());
                }else{
                    Log.d(TAG,"location failure");
                }
            }
        };
        LocationManager.getInstance().requestLocation(handler);


        btn_menu = (Button) findViewById(R.id.btn_menu);
        btn_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isMapTabSelected){
                    mapPopupWindow.showAsDropDown(view);
                }else if(isFriendTabSelected){
                    friendPopupWindow.showAsDropDown(view);
                }
            }
        });

        mapPopupWindow = initMapMenuPopupWindow(btn_menu);
        friendPopupWindow = initFriendMenuPopupWindow(btn_menu);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                isMapTabSelected = false;
                isFriendTabSelected = false;
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft= fm.beginTransaction();
                if (checkedId==R.id.rd_message){
                    isFriendTabSelected = true;
                    ft.replace(R.id.fg_container, friendFragment);
                } else if (checkedId==R.id.rd_find){
                    isMapTabSelected = true;
                    if(isMapShowing){
                        ft.replace(R.id.fg_container, mapFragment);
                    }else{
                        ft.replace(R.id.fg_container, requestInfoFragment);
                    }
                }else if(checkedId == R.id.rd_mine){
                    ft.replace(R.id.fg_container, mineFragment);
                }
//                else if (checkedId==R.id.rd_community){
//                    ft.replace(R.id.fg_container, communityFragment);
//                }
                ft.commit();
            }
        });
    }

    private void showIndex(int index){
        if(index == 1){
            ((RadioButton) radioGroup.getChildAt(0)).setChecked(true);
        }else if(index == 2){
            ((RadioButton) radioGroup.getChildAt(1)).setChecked(true);
        }else if(index == 3){
            ((RadioButton) radioGroup.getChildAt(2)).setChecked(true);
        }else{
            ((RadioButton) radioGroup.getChildAt(0)).setChecked(true);
        }
    }

    private PopupWindow initMapMenuPopupWindow(View view) {
        final Context context = view.getContext();

        // 一个自定义的布局，作为显示的内容
        View contentView = LayoutInflater.from(context).inflate(R.layout.popwindow_menu_map, null);

        final PopupWindow popupWindow = new PopupWindow(contentView,
                300, ViewGroup.LayoutParams.WRAP_CONTENT, true);

        // 设置按钮的点击事件
        Button btn_request = (Button) contentView.findViewById(R.id.btn_map_menu_request);
        btn_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isMapShowing = false;
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft= fm.beginTransaction();
                ft.replace(R.id.fg_container, requestInfoFragment);
                ft.commit();
                popupWindow.dismiss();
            }
        });

        Button btn_map = (Button) contentView.findViewById(R.id.btn_map_menu_map);
        btn_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isMapShowing = true;
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft= fm.beginTransaction();
                ft.replace(R.id.fg_container,mapFragment);
                ft.commit();
                popupWindow.dismiss();
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

        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        popupWindow.setBackgroundDrawable(context.getResources()
                .getDrawable(R.drawable.bg_filling));

        return popupWindow;
    }

    private PopupWindow initFriendMenuPopupWindow(View view) {
        final Context context = view.getContext();

        // 一个自定义的布局，作为显示的内容
        View contentView = LayoutInflater.from(context).inflate(R.layout.popwindow_menu_friend, null);

        final PopupWindow popupWindow = new PopupWindow(contentView,
                300, ViewGroup.LayoutParams.WRAP_CONTENT, true);

        // 设置按钮的点击事件
        Button btn_refresh = (Button) contentView.findViewById(R.id.btn_friend_menu_refresh);
        btn_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showIndex(1);
                popupWindow.dismiss();
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

        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        popupWindow.setBackgroundDrawable(context.getResources()
                .getDrawable(R.drawable.bg_filling));

        return popupWindow;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //BmobNotificationManager.getInstance(this).clearObserver();
    }

    //重写onKeyDown方法
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //判断当点击的是返回键
        if (keyCode == event.KEYCODE_BACK) {
            exit();//退出方法
        }
        return true;
    }

    private long time = 0;
    //退出方法
    private void exit() {
        //如果在两秒大于2秒
        if (System.currentTimeMillis() - time > 1500) {
            //获得当前的时间
            time = System.currentTimeMillis();
            Toast.makeText(this, "再点击一次退出应用程序", Toast.LENGTH_SHORT).show();
        } else {
            //点击在两秒以内
            removeALLActivity();//执行移除所以Activity方法
        }
    }
}
