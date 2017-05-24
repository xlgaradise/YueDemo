package com.hmxl.yuedemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hmxl.yuedemo.R;
import com.hmxl.yuedemo.bean.Friend;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by liuli on 2017/5/15.
 */

public class UserListViewAdapter extends BaseAdapter {
    String objectId ;
    private ArrayList<Friend> list=new ArrayList<Friend>();
    private Context context;
    LayoutInflater inflater;

    public UserListViewAdapter(Context context){
        this.context=context;
        inflater= LayoutInflater.from(context);
    }
    // 添加一堆
    public void addAllData(List<Friend> templist){
        list.clear();
        list.addAll(templist);
    }
    // 添加一行
    public void addData(Friend friend){
        list.add(friend);
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Friend getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView=inflater.inflate(R.layout.inflater_lv_item,null);
        }
        ImageView iv_listImage= (ImageView) convertView.findViewById(R.id.iv_listImage);
        TextView tv_content= (TextView) convertView.findViewById(R.id.tv_content);
        Friend friend=list.get(position);
        //String remark = friend.getFriend_remak();
        //System.out.println(remark + "--");
        iv_listImage.setBackgroundResource(R.drawable.icon_map_male);
        tv_content.setText(friend.getFriendUser().getUsername());
        return convertView;
    }

}
