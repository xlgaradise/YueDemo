package com.hmxl.yuedemo.activities;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.hmxl.yuedemo.R;

import cn.bmob.v3.BmobUser;

public class WelcomeActivity extends AppCompatActivity {

    //delay 1.5 seconds
    private static final long SPLASH_DELAY_MILLIS = 3000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                //初试化
                BmobUser bmobUser = BmobUser.getCurrentUser();
                if(bmobUser!=null){
                    Intent intent = new Intent(WelcomeActivity.this,All_fragmment_Activity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Intent intent = new Intent(WelcomeActivity.this,LoginUsActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };
        handler.sendEmptyMessageDelayed(1, SPLASH_DELAY_MILLIS);
    }
}
