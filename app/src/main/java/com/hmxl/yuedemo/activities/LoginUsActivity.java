package com.hmxl.yuedemo.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hmxl.yuedemo.R;
import com.hmxl.yuedemo.tools.exception.MyLog;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.SaveListener;

public class LoginUsActivity extends AppCompatActivity {

    private static String TAG = "LoginUsActivity";

    private Button btn_login;
    private Button btn_phone;
    private EditText et_account;
    private EditText et_pwd;
    private boolean isLoginOut = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_us);

        Intent intent = getIntent();
        int where = intent.getIntExtra("where",1);
        if(where == SettingActivity.LoginOut){
            isLoginOut = true;
            ApplicationDemo.removeALLActivity_();
        }
        MyLog.d(TAG,"展示LoginUs界面: isLogOut:"+isLoginOut);
        //初试化
        BmobUser bmobUser = BmobUser.getCurrentUser();
        if(bmobUser!=null){
            Intent intent1 = new Intent(LoginUsActivity.this,All_fragmment_Activity.class);
            startActivity(intent1);
            finish();
        }

        btn_login = (Button) findViewById(R.id.log_pwd_btn_login);
        btn_phone = (Button) findViewById(R.id.log_pwd_btn_phone);
        et_account = (EditText) findViewById(R.id.login_account);
        et_pwd = (EditText) findViewById(R.id.login_pwd);
        // 判断用户是否存在

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user_account = et_account.getText().toString().trim();
                String user_pwd = et_pwd.getText().toString().trim();
                if(user_account.equals("") || user_pwd.equals("")){
                    showToast("用户名或密码不能为空");
                    return;
                }
                BmobUser bmobUser= new BmobUser();
                bmobUser.setUsername(user_account);
                bmobUser.setPassword(user_pwd);
                bmobUser.login(new SaveListener<BmobUser>() {
                    @Override
                    public void done(BmobUser bmobUser, BmobException e) {
                        if(e==null){
                            Intent intent = new Intent(LoginUsActivity.this,All_fragmment_Activity.class);
                            startActivity(intent);
                            finish();
                        }else{
                            int errorCode = e.getErrorCode();
                            if(errorCode == 101){//用户名或密码错误
                                showToast("用户名或密码错误");
                                return;
                            }
                            showToast("登录失败:"+errorCode);
                            Log.e(TAG,"登录失败:"+errorCode+","+e.getMessage());
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
                finish();
            }
        });
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            if(isLoginOut){
                Log.d(TAG,"程序退出");
                ApplicationDemo.removeALLActivity_();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private void showToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
