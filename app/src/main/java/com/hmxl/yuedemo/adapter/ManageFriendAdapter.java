package com.hmxl.yuedemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.hmxl.yuedemo.R;
import com.hmxl.yuedemo.bean.Friend;

import java.util.ArrayList;



/**
 * Created by liuli on 2017/5/16.
 */

public class ManageFriendAdapter extends BaseAdapter {
    private ArrayList<Friend> list=new ArrayList<Friend>();
    private Context context;
    LayoutInflater inflater;

    public ArrayList<Friend> getListAll(){
        return list;
    }
    public ManageFriendAdapter(Context context){
        this.context=context;
        inflater= LayoutInflater.from(context);
    }

    public void addAllData(ArrayList<Friend> templist){
        list.addAll(templist);
    }

    public void addData(Friend manageFriend){
        list.add(manageFriend);
    }
    public void deleteAllData(ArrayList<Friend> templist){
        list.removeAll(templist);
    }
    public void deleteData(int position){
        list.remove(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView=inflater.inflate(R.layout.inflater_manage_item,null);
        }

        CheckBox cb= (CheckBox) convertView.findViewById(R.id.cb_manage);
        ImageView iv_manage_image= (ImageView) convertView.findViewById(R.id.iv_manage_image);
        TextView tv_manage_name= (TextView) convertView.findViewById(R.id.tv_manage_name);
        final Friend manageFriend=list.get(position);
        iv_manage_image.setBackgroundResource(R.drawable.icon_map_female);
        tv_manage_name.setText("测试用户");

        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //manageFriend.checked=isChecked;
            }
        });
        cb.setChecked(false);
        return convertView;
    }
}
