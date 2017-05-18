package com.hmxl.yuedemo.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.hmxl.yuedemo.R;

public class RegisterActivity extends AppCompatActivity {

    private Button btn_reg;
    private Button btn_phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        btn_reg = (Button) findViewById(R.id.reg_btn_register);
        btn_phone = (Button) findViewById(R.id.reg_btn_phone);

        btn_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到注册成功界面
            }
        });

        btn_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });

    }
}
