package com.hmxl.yuedemo.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;


import com.hmxl.yuedemo.R;
import com.hmxl.yuedemo.bean.User;
import com.hmxl.yuedemo.db.UserManager;
import com.hmxl.yuedemo.tools.exception.MyLog;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;


public class SelfEditActivity extends BaseActivity {

    private static final String TAG = "SelfEditActivity";
    private EditText ex_nickname;
    private EditText ex_age;
    private EditText ex_location;
    private EditText ex_introduction;
    private Button btn_submit;
    private RadioGroup radioGroup;
    private RadioButton male;
    private RadioButton female;
    boolean sex;
    String nickname;
    String age;
    String location;
    String introduction;
    String objectId ;
    //UserManager userManager = new UserManager();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        ex_nickname = (EditText) findViewById(R.id.my_tv_nickname);
        ex_age = (EditText) findViewById(R.id.et_user_age);
        ex_location = (EditText) findViewById(R.id.lv_user_region);
        ex_introduction = (EditText) findViewById(R.id.et_introduction);
        btn_submit = (Button) findViewById(R.id.btn_info_change);
        male = (RadioButton) findViewById(R.id.rb_male);
        female = (RadioButton) findViewById(R.id.rb_female);
        objectId = BmobUser.getCurrentUser().getObjectId();
        updateView();

        // 性别
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_male){
                    sex = true;
                    //System.out.println("重新选择为男性");
                }else
                {
                    sex = false;
                    // System.out.println("重新选择为女性");
                }
            }
        });


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nickname = ex_nickname.getText().toString().trim();
                age = ex_age.getText().toString().trim();
                location = ex_location.getText().toString().trim();
                introduction = ex_introduction.getText().toString().trim();
                //System.out.println(nickname+age+sex+location+introduction+"");
                // 把信息更新到云端数据库 ,这里nickname 对应的就是remark(备注)
                //userManager.updateUserInfo(objectId, nickname, sex, Integer.parseInt(age), location, introduction);
                User user = new User();
                user.setAge(Integer.parseInt(age));
                user.setRemark(nickname);
                user.setIntroduction(introduction);
                user.setSex(sex);
                user.setLocation(location);
                user.update(objectId,new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if(e==null){
                            Toast.makeText(getBaseContext(),"更新成功",Toast.LENGTH_SHORT).show();
                            updateView();
                        }else{
                            Log.e(TAG,"upload info fail"+e.getErrorCode()+","+e.getMessage());
                            MyLog.e(TAG,"upload info fail",e);
                            Toast.makeText(getBaseContext(),"更新失败",Toast.LENGTH_SHORT).show();
                            updateView();
                        }
                    }
                });


            }
        });
    }


    private void updateView(){

        //通过objectId获取到user的全部信息
        BmobQuery<User> query = new BmobQuery<>();
        query.getObject(objectId, new QueryListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if(e==null){
                    ex_age.setText(user.getAge()+"");
                    //System.out.println(user.getAge());
                    //获得playerName的信息
                    ex_nickname.setText(user.getRemark());
                    //获得数据的objectId信息
                    //System.out.println(user.getRemark());
                    //System.out.println(user.getUsername());
                    //获得createdAt数据创建时间（注意是：createdAt，不是createAt）
                    ex_introduction.setText(user.getIntroduction());
                    //System.out.println(user.getAge());
                    ex_location.setText(user.getLocation());
                    if(user.getSex()){
                        male.setChecked(true);
                    }else{
                        female.setChecked(true);
                    }
                }else{
                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                }
            }

        });
    }


}
