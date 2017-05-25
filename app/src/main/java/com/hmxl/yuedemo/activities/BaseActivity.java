package com.hmxl.yuedemo.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.hmxl.yuedemo.tools.baidumap.LocationManager;
import com.hmxl.yuedemo.tools.baidumap.RadarManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.core.ConnectionStatus;
import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.listener.ConnectStatusChangeListener;

public class BaseActivity extends AppCompatActivity {

    private ApplicationDemo application;
    private BaseActivity oContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if (application == null) {
            // 得到Application对象
            application = (ApplicationDemo) getApplication();
        }
        oContext = this;// 把当前的上下文对象赋值给BaseActivity
        addActivity();// 调用添加方法
    }

    // 添加Activity方法
    public void addActivity() {
        application.addActivity_(oContext);// 调用myApplication的添加Activity方法
    }
    //销毁当个Activity方法
    public void removeActivity() {
        application.removeActivity_(oContext);// 调用myApplication的销毁单个Activity方法
    }
    //销毁所有Activity方法
    public void removeALLActivity() {
        ApplicationDemo.removeALLActivity_();// 调用myApplication的销毁所有Activity方法
        LocationManager.getInstance().stopLocationSDK();
    }



    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        BmobIM.getInstance().setOnConnectStatusChangeListener(new ConnectStatusChangeListener() {
            @Override
            public void onChange(ConnectionStatus status) {
                if(status == ConnectionStatus.DISCONNECT){
                    Log.d("BaseActivity","连接已断开");
                    Toast.makeText(BaseActivity.this, "IM服务断开", Toast.LENGTH_SHORT).show();
                }
            }


        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }


    /**接收到聊天消息
     * @param event
     */
    @Subscribe
    public void onEventMainThread(MessageEvent event){
        Toast.makeText(this,
                "收到来自"+event.getConversation().getConversationTitle()+"的信息",
                Toast.LENGTH_SHORT).show();
    }


    protected void showToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
