package com.hmxl.yuedemo.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mapapi.map.Text;
import com.hmxl.yuedemo.R;

import java.util.ArrayList;
import java.util.Date;

import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.v3.BmobUser;

/**
 * Created by Nate on 2017/5/23.
 */

public class MyChatAdapter extends BaseAdapter {

    private static String TAG = "MyChatAdapter";
    private ArrayList<BmobIMMessage> messagesList = new ArrayList<>();
    private String objectId = "";
    private LayoutInflater inflater;
    //private long lastTime = 0;


    public MyChatAdapter(Context context){
        if(BmobUser.getCurrentUser() != null){
            objectId = BmobUser.getCurrentUser().getObjectId();
        }
        inflater= LayoutInflater.from(context);
    }

    public void addData(BmobIMMessage message){
        messagesList.add(message);
    }

    public void clearData(){
        messagesList.clear();
    }

    @Override
    public int getCount() {
        return messagesList.size();
    }

    @Override
    public BmobIMMessage getItem(int position) {
        return messagesList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BmobIMMessage message = messagesList.get(position);
        String time = "";
        if(position == 0){
            Date date = new Date();
            date.setTime(message.getCreateTime());
            time = date.getHours()+":"+date.getMinutes()+":"+date.getSeconds();
        }else{
            if((message.getCreateTime()-messagesList.get(position-1).getCreateTime())>30000){
                Date date = new Date();
                date.setTime(message.getCreateTime());
                time = date.getHours()+":"+date.getMinutes()+":"+date.getSeconds();
            }else{
                time="";
            }
        }
        if(objectId.equals("")) return null;
        if(message.getFromId().equals(objectId)){//发送的信息
            convertView=inflater.inflate(R.layout.item_chat_sent,null);
            ImageView iv_sent = (ImageView) convertView.findViewById(R.id.chatlist_image_sent);
            TextView tv_sent = (TextView) convertView.findViewById(R.id.chatlist_text_sent);
            TextView tv_sent_time = (TextView) convertView.findViewById(R.id.chatlist_text_sent_time);
            iv_sent.setImageResource(R.drawable.icon_map_female);
            tv_sent.setText(message.getContent());
            tv_sent_time.setText(time);
        }else{//接受的信息
            convertView=inflater.inflate(R.layout.item_chat_received,null);
            ImageView iv_re = (ImageView) convertView.findViewById(R.id.chatlist_image_received);
            TextView tv_re = (TextView) convertView.findViewById(R.id.chatlist_text_received);
            TextView tv_re_time = (TextView) convertView.findViewById(R.id.chatlist_text_receive_time);
            iv_re.setImageResource(R.drawable.icon_map_male);
            tv_re.setText(message.getContent());
            tv_re_time.setText(time);
        }
        return convertView;
    }
}
