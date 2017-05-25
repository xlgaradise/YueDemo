package com.hmxl.yuedemo.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.hmxl.yuedemo.R;

import com.hmxl.yuedemo.activities.SelfEditActivity;
import com.hmxl.yuedemo.activities.SettingActivity;
import com.hmxl.yuedemo.bean.User;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class MineFragment extends Fragment implements View.OnClickListener{
    private ImageView imageView;
    private TextView tv_set;
    private TextView tv_friend_manager;
    private TextView tv_edit;
    private TextView tv_collect;
    private TextView my_tv_remark;
    private TextView my_tv_username;

    public MineFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // 初始化
        View view = inflater.inflate(R.layout.activity_center_of_user, container, false);

        tv_edit = (TextView) view.findViewById(R.id.my_tv_compile);
        tv_collect = (TextView) view.findViewById(R.id.my_tv_collect);
        tv_friend_manager = (TextView) view.findViewById(R.id.my_tv_manage);
        tv_set = (TextView) view.findViewById(R.id.my_tv_systemset);

        imageView = (ImageView) view.findViewById(R.id.my_iv_image);
        my_tv_remark = (TextView) view.findViewById(R.id.my_tv_remark);
        my_tv_username = (TextView) view.findViewById(R.id.my_tv_username);

        //添加事件
        tv_set.setOnClickListener(this);
        tv_edit.setOnClickListener(this);
        tv_friend_manager.setOnClickListener(this);
        imageView.setOnClickListener(this);
        tv_collect.setOnClickListener(this);

        my_tv_remark.setText("昵称：");
        my_tv_username.setText("用户名：");
        BmobUser bmobUser = BmobUser.getCurrentUser();
        if(bmobUser != null){
            String objectId = bmobUser.getObjectId();
            //通过objectId获取到user的全部信息
            BmobQuery<User> query = new BmobQuery<User>();
            query.getObject(objectId, new QueryListener<User>() {
                @Override
                public void done(User user, BmobException e) {
                    if(e==null){
                        my_tv_remark.setText("昵称："+user.getRemark());
                        my_tv_username.setText("用户名："+user.getUsername());
                        if(user.getSex() != null){
                            if (user.getSex()) {
                                imageView.setImageResource(R.drawable.icon_map_male);
                            } else {
                                imageView.setImageResource(R.drawable.icon_map_female);
                            }
                        }
                    }else{
                        Log.e("MineFragment","查询用户失败："+e.getMessage()+","+e.getErrorCode());
                    }
                }
            });
        }
        return  view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.my_tv_systemset){
            // 系统设置
             Intent intent = new Intent(getActivity(), SettingActivity.class);
             startActivity(intent);

        }else if(v.getId() == R.id.my_tv_compile){
            //编辑资料
            Intent intent = new Intent(getActivity(), SelfEditActivity.class);
            startActivity(intent);
        }
        else if(v.getId() == R.id.my_tv_manage){
            //好友管理
//            Intent intent = new Intent(getActivity(), FriendManagerActivity.class);
//            startActivity(intent);
            Toast.makeText(getActivity(),"静等更新",Toast.LENGTH_SHORT).show();
        }else if(v.getId() == R.id.my_tv_collect){
            //个性装扮
//            Intent intent = new Intent(getActivity(), SettingActivity.class);
//            startActivity(intent);
            Toast.makeText(getActivity(),"静等更新",Toast.LENGTH_SHORT).show();
        }else{
            //点击头像
            Intent intent = new Intent(getActivity(), SelfEditActivity.class);
            startActivity(intent);
        }
    }
}
