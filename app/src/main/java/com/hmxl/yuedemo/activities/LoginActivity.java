package com.hmxl.yuedemo.activities;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hmxl.yuedemo.R;
import com.hmxl.yuedemo.tools.exception.Log;


import java.util.HashMap;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;


public class LoginActivity extends AppCompatActivity {
    private  static  String TAG = "LoginActivity";
    private Button btn_login;
    private TextView email_register;
    private Button btn_check;
    private EditText phoneNumber;
    private EditText check_code;
    String InputPhoneNumber; //获取输入手机号
    String InputCheckCode;//获取输入验证码


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        //密钥
        SMSSDK.initSDK(this, "1dcd0bf3f2441", "80c0045ba4080a8ac0d131e165d703fe");
        //注册回掉
        SMSSDK.registerEventHandler(eventHandler);
        btn_login = (Button) findViewById(R.id.login);
        email_register = (TextView) findViewById(R.id.email_register);
        btn_check = (Button) findViewById(R.id.getCheckNumber);
        phoneNumber = (EditText)findViewById(R.id.phoneNumber);
        check_code = (EditText) findViewById(R.id.check_code);

        //获取输入的手机号,之前的phone是默认写死用来测试的.
         InputPhoneNumber =phoneNumber.getText().toString().trim() ;


        //格式判断，如果输入的手机号为空，或者格式不正确

            btn_check.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //System.out.println("验证码已经发送.");
                    Toast.makeText(getBaseContext(),"asdasd",Toast.LENGTH_SHORT).show();

                    //发送验证码
                    SMSSDK.getVerificationCode("+86", InputPhoneNumber);
                }
            });

        //获取验证码
        InputCheckCode = check_code.getText().toString().trim();

        //判断，如果验证码填写的验证码正确，点击登陆则跳转

            btn_login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(LoginActivity.this,All_fragmment_Activity.class);
                    startActivity(intent);
                }
            });


        email_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Todo 跳转到注册界面
            }
        });

    }
    private EventHandler eventHandler = new EventHandler() {
        @Override
        public void afterEvent(int event, int result, Object data) {
            super.afterEvent(event, result, data);
            if (result == SMSSDK.RESULT_COMPLETE) {
                //回调完成
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                    //提交验证码成功
                    @SuppressWarnings("unchecked") HashMap<String, Object> phoneMap = (HashMap<String, Object>) data;
                    String country = (String) phoneMap.get("country");
                    String phone = (String) phoneMap.get("phone");
                    Log.d(TAG, "提交验证码成功--country=" + country + "--phone" + phone);
                } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                    //获取验证码成功
                    Log.d(TAG, "获取验证码成功");
                } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
                    //返回支持发送验证码的国家列表
                }
            } else {
                ((Throwable) data).printStackTrace();
            }
        }
    };
}
