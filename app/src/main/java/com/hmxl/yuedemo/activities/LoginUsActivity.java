package com.hmxl.yuedemo.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.hmxl.yuedemo.R;

public class LoginUsActivity extends AppCompatActivity {

    private Button btn_login;
    private Button btn_phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_us);

        btn_login = (Button) findViewById(R.id.log_pwd_btn_login);
        btn_phone = (Button) findViewById(R.id.log_pwd_btn_phone);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //登陆成功后跳转到主界面
                Intent intent = new Intent(LoginUsActivity.this,All_fragmment_Activity.class);
                startActivity(intent);
            }
        });
        btn_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到手机验证码登陆界面
                Intent intent = new Intent(LoginUsActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
