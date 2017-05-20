package com.hmxl.yuedemo.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hmxl.yuedemo.R;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;

public class LoginUsActivity extends AppCompatActivity {

    private Button btn_login;
    private Button btn_phone;
    private EditText et_account;
    private EditText et_pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_us);
        Bmob.initialize(this,"3efba5cd8767b75e4e207e62f4698ab1");


        //初试化
        BmobUser bmobUser = BmobUser.getCurrentUser();
        if(bmobUser!=null){
            Intent intent = new Intent(LoginUsActivity.this,All_fragmment_Activity.class);
            startActivity(intent);
           // finish();
        }
        btn_login = (Button) findViewById(R.id.log_pwd_btn_login);
        btn_phone = (Button) findViewById(R.id.log_pwd_btn_phone);
        et_account = (EditText) findViewById(R.id.login_account);
        et_pwd = (EditText) findViewById(R.id.login_pwd);
        // 判断用户是否存在

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ///
                System.out.println("zhixing");
              String user_account =   et_account.getText().toString().trim();
              String user_pwd = et_pwd.getText().toString().trim();
                BmobUser bmobUser= new BmobUser();
                bmobUser.setUsername(user_account);
                bmobUser.setPassword(user_pwd);
                bmobUser.loginByAccount(user_account, user_pwd, new LogInListener<BmobUser>() {
                    @Override
                    public void done(BmobUser bmobUser, BmobException e) {
                        if(e==null){
                            System.out.println("zhixing2");
                            Toast.makeText(LoginUsActivity.this,"登录成功:",Toast.LENGTH_SHORT).show();
                            //通过BmobUser user = BmobUser.getCurrentUser()获取登录成功后的本地用户信息
                            //如果是自定义用户对象MyUser，可通过MyUser user = BmobUser.getCurrentUser(MyUser.class)获取自定义用户信息
                            //登陆成功后跳转到主界面
                            Intent intent = new Intent(LoginUsActivity.this,All_fragmment_Activity.class);
                            startActivity(intent);
                        }else{
                            Toast.makeText(LoginUsActivity.this,"Login fail",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
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
