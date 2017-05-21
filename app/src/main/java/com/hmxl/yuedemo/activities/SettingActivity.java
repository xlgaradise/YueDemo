package com.hmxl.yuedemo.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.hmxl.yuedemo.R;

import cn.bmob.v3.BmobUser;

public class SettingActivity extends AppCompatActivity {
    private Button btn;
    public static final int LoginOut = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);
        btn  = (Button) findViewById(R.id.my_btn_exit);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BmobUser.logOut();   //清除缓存用户对象
                BmobUser currentUser = BmobUser.getCurrentUser(); // 现在的currentUser是null了
                if (currentUser == null){
                    Intent intent = new Intent(SettingActivity.this,LoginUsActivity.class);
                    intent.putExtra("where",LoginOut);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(SettingActivity.this, "退出失败", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}
