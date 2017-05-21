package com.hmxl.yuedemo.activities;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import com.hmxl.yuedemo.R;
import com.hmxl.yuedemo.bean.User;
import com.hmxl.yuedemo.tools.exception.MyLog;

import java.text.SimpleDateFormat;

import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.QueryListener;

import static com.hmxl.yuedemo.R.id.log_phone_rbtn_us;


public class LoginActivity extends AppCompatActivity   {
    private static String TAG = "LoginActivity";
    private Button btn_code;
    private Button btn_login;
    private Button btn_email;
    private EditText et_number;
    private EditText et_code;
    String phoneNumber;
    String code;

    private RadioButton rbtn_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // 控件初始化
        btn_code = (Button) findViewById(R.id.btn_Code);
        btn_email = (Button) findViewById(R.id.btn_email);
        btn_login = (Button) findViewById(R.id.btn_login);
        et_code = (EditText) findViewById(R.id.et_checkcode);
        et_number = (EditText) findViewById(R.id.et_number);
        rbtn_date= (RadioButton) findViewById(log_phone_rbtn_us);
        btn_code.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                phoneNumber = et_number.getText().toString().trim();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                BmobSMS.requestSMSCode(phoneNumber, "审核通过后的短信内容",new QueryListener<Integer>() {
                    @Override
                    public void done(Integer integer, BmobException e) {
                        if(e==null){//验证码发送成功
                            Log.i("smile", "短信id："+integer);//用于查询本次短信发送详情
                        }else{
                            MyLog.e(TAG,"send code failure",e);
                        }
                    }
                });
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                code = et_code.getText().toString().trim();
                BmobUser.loginBySMSCode(phoneNumber, code, new LogInListener<User>() {
                    @Override
                    public void done(User user, BmobException e) {
                        if (user != null) {
                            Log.i("smile", "用户登陆成功");
                            Intent intent = new Intent(LoginActivity.this, All_fragmment_Activity.class);
                            startActivity(intent);
                        }else{
                            MyLog.e(TAG,"login failure",e);
                        }
                    }
                });
            }
        });

        //点击后跳转到邮箱注册界面
        btn_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });
        rbtn_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,LoginUsActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }



}

