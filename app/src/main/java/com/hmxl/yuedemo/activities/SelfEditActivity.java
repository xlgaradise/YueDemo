package com.hmxl.yuedemo.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.hmxl.yuedemo.R;
import com.hmxl.yuedemo.bean.User;
import com.hmxl.yuedemo.tools.exception.MyLog;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;


public class SelfEditActivity extends BaseActivity {

    private static final String TAG = "SelfEditActivity";
    private EditText ex_remark;
    private TextView tv_username;
    private ImageView iv_avatar;
    private EditText ex_age;
    private EditText ex_location;
    private EditText ex_introduction;
    private Button btn_submit;
    private RadioGroup radioGroup;
    private RadioButton rb_male;
    private RadioButton rb_female;


    private boolean sex;
    private String remark;
    private String age;
    private String location;
    private String introduction;
    private String objectId ;
    //UserManager userManager = new UserManager();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        iv_avatar = (ImageView) findViewById(R.id.iv_avatar_edit);
        ex_remark = (EditText) findViewById(R.id.my_tv_remark);
        tv_username = (TextView) findViewById(R.id.my_tv_username);
        ex_age = (EditText) findViewById(R.id.et_user_age);
        ex_location = (EditText) findViewById(R.id.lv_user_region);
        ex_introduction = (EditText) findViewById(R.id.et_introduction);
        btn_submit = (Button) findViewById(R.id.btn_info_change);
        rb_male = (RadioButton) findViewById(R.id.rb_male);
        rb_female = (RadioButton) findViewById(R.id.rb_female);

        updateView();

        // 性别
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_male){
                    sex = true;
                }else {
                    sex = false;
                }
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remark = ex_remark.getText().toString().trim();
                age = ex_age.getText().toString().trim();
                location = ex_location.getText().toString().trim();
                introduction = ex_introduction.getText().toString().trim();
                //System.out.println(remark+age+sex+location+introduction+"");
                // 把信息更新到云端数据库 ,这里nickname 对应的就是remark(备注)
                //userManager.updateUserInfo(objectId, remark, sex, Integer.parseInt(age), location, introduction);
                User user = new User();
                user.setAge(Integer.parseInt(age));
                user.setRemark(remark);
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
        if(BmobUser.getCurrentUser() != null) {
            btn_submit.setClickable(true);
            objectId = BmobUser.getCurrentUser().getObjectId();
            //通过objectId获取到user的全部信息
            BmobQuery<User> query = new BmobQuery<>();
            query.getObject(objectId, new QueryListener<User>() {
                @Override
                public void done(User user, BmobException e) {
                    if (e == null) {
                        if(user.getSex() != null){
                            if (user.getSex()) {
                                rb_male.setChecked(true);
                                iv_avatar.setImageResource(R.drawable.icon_map_male);
                            } else {
                                rb_female.setChecked(true);
                                iv_avatar.setImageResource(R.drawable.icon_map_female);
                            }
                        }else{
                            rb_male.setChecked(false);
                            rb_female.setChecked(false);
                        }
                        ex_remark.setText(user.getRemark());
                        tv_username.setText("用户名："+user.getUsername());
                        ex_age.setText(user.getAge() + "");

                        ex_introduction.setText(user.getIntroduction());
                        ex_location.setText(user.getLocation());

                    } else {
                        MyLog.e(TAG, "查询用户失败：" + e.getErrorCode()+","+e.getMessage());
                        showToast("查询用户失败：" + e.getErrorCode());
                    }
                }

            });
        }else{
            btn_submit.setClickable(false);
        }

    }


}
