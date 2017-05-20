package com.hmxl.yuedemo.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.Text;
import com.hmxl.yuedemo.R;
import com.hmxl.yuedemo.activities.AvatarActivity;
import com.hmxl.yuedemo.activities.FriendManagerActivity;
import com.hmxl.yuedemo.activities.SelfEditActivity;
import com.hmxl.yuedemo.activities.SettingActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class MineFragment extends Fragment implements View.OnClickListener{
    private ImageView imageView;
    private TextView tv_set;
    private TextView tv_friend_manager;
    private TextView tv_edit;
    private TextView tv_collect;
    public MineFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // 初始化
        View view = inflater.inflate(R.layout.activity_center_of_user, container, false);
        tv_set = (TextView) view.findViewById(R.id.my_tv_systemset);
        tv_edit = (TextView) view.findViewById(R.id.my_tv_compile);
        tv_friend_manager = (TextView) view.findViewById(R.id.my_tv_manage);
        imageView = (ImageView) view.findViewById(R.id.my_iv_image);
        tv_collect = (TextView) view.findViewById(R.id.my_tv_collect);
        //添加事件
        tv_set.setOnClickListener(this);
        tv_edit.setOnClickListener(this);
        tv_friend_manager.setOnClickListener(this);
        imageView.setOnClickListener(this);
        tv_collect.setOnClickListener(this);


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
            Intent intent = new Intent(getActivity(), FriendManagerActivity.class);
            startActivity(intent);
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
