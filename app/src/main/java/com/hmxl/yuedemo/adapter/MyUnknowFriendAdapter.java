package com.hmxl.yuedemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hmxl.yuedemo.R;

import com.hmxl.yuedemo.bean.Friend;
import com.hmxl.yuedemo.bean.User;
import com.hmxl.yuedemo.bmobMessage.AddFriendMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.core.BmobIMClient;
import cn.bmob.newim.listener.MessageSendListener;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;

/**
 * Created by Nate on 2017/5/22.
 */

public class MyUnknowFriendAdapter extends BaseAdapter{
    private TextView tv_info;
    private ArrayList<BmobObject> list=new ArrayList<>();
    private ArrayList<Boolean> isFriendList = new ArrayList<>();

    private Context context;
    LayoutInflater inflater;
    BmobIMUserInfo info;
    User unknow_user;

    public MyUnknowFriendAdapter(Context context){
        this.context=context;
        inflater= LayoutInflater.from(context);
    }
    // 添加一堆
    public void addAllData(ArrayList<BmobObject> templist,ArrayList<Boolean> isFriendList){
        list.clear();
        list.addAll(templist);
        this.isFriendList.clear();
        this.isFriendList.addAll(isFriendList);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    public boolean getItemBoolean(int position){
        return isFriendList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView=inflater.inflate(R.layout.inflater_unknowfriendlist_item,null);
        }
        ImageView iv_listImage= (ImageView) convertView.findViewById(R.id.iv_listImage);
        TextView tv_content= (TextView) convertView.findViewById(R.id.tv_content);
        tv_info = (TextView) convertView.findViewById(R.id.tv_search_info);

        BmobObject object = list.get(position);
        String username = "";
        if(isFriendList.get(position)){
            username = ((Friend) object).getFriendUser().getUsername();
            tv_info.setText("已是好友");
        }else{
            username = ((User) object).getUsername();
            tv_info.setText("陌生人");
        }
        System.out.println(username + "--");
        iv_listImage.setBackgroundResource(R.drawable.icon_map_male);
        tv_content.setText(username);
        return convertView;
    }


}
