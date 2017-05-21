package com.hmxl.yuedemo.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hmxl.yuedemo.R;
import com.hmxl.yuedemo.tools.exception.MyLog;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class RegisterActivity extends AppCompatActivity {
    private static String TAG = "RegisterActivity";

    private Button btn_reg;
    private Button btn_phone;
    private EditText et_username;
    private EditText et_pwd;
    private EditText et_pwd_again;
    private EditText et_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        et_username = (EditText) findViewById(R.id.et_username);
        et_email = (EditText) findViewById(R.id.et_email);
        et_pwd = (EditText) findViewById(R.id.et_pwd);
        et_pwd_again = (EditText) findViewById(R.id.et_pwd_again);

        btn_reg = (Button) findViewById(R.id.reg_btn_register);
        btn_phone = (Button) findViewById(R.id.reg_btn_phone);

        btn_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到注册成功界面
                String username = et_username.getText().toString().trim();
                String email = et_email.getText().toString().trim();
                String pwd = et_pwd.getText().toString().trim();
                String pwd_again = et_pwd_again.getText().toString().trim();
                if (pwd.equals(pwd_again)){
                    BmobUser bu = new BmobUser();
                    bu.setUsername(username);
                    bu.setPassword(pwd);
                    bu.setEmail(email);
                    bu.signUp(new SaveListener<BmobUser>() {
                        @Override
                        public void done(BmobUser bmobUser, BmobException e) {
                            if (e==null){
                                Toast.makeText(RegisterActivity.this,"注册成功",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(RegisterActivity.this,All_fragmment_Activity.class);
                                startActivity(intent);
                                finish();
                            }else {
                                MyLog.e(TAG,"register failure",e);

                            }
                        }
                    });
                }else{
                    Toast.makeText(RegisterActivity.this,"两次输入密码不一致",Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}
