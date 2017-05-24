package com.hmxl.yuedemo.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.hmxl.yuedemo.R;
import com.hmxl.yuedemo.activities.All_fragmment_Activity;
import com.hmxl.yuedemo.activities.ChatActivity;
import com.hmxl.yuedemo.bean.NewFriend;
import com.hmxl.yuedemo.bean.NewFriendManager;
import com.hmxl.yuedemo.bean.User;
import com.hmxl.yuedemo.bmobMessage.AgreeAddFriendMessage;
import com.hmxl.yuedemo.bmobMessage.Config;
import com.hmxl.yuedemo.bmobMessage.model.UserModel;
import com.hmxl.yuedemo.tools.exception.MyLog;

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
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by Nate on 2017/5/23.
 */

public class NewFridAdapter extends BaseAdapter {

    private List<NewFriend> list=new ArrayList<>();
    Context context;
    LayoutInflater inflater;
    public NewFridAdapter(Context context){
        this.context=context;
        inflater=LayoutInflater.from(context);

    }

    public void addData(List<NewFriend> templist){
        list.clear();
        list.addAll(templist);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public NewFriend getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        if(convertView==null){
            convertView=inflater.inflate(R.layout.item_new_friend,null);
        }

        TextView name= (TextView) convertView.findViewById(R.id.tv_recent_name);
        final Button btn= (Button) convertView.findViewById(R.id.btn_aggree);
        final NewFriend friend=list.get(position);
        name.setText(friend.getName());
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                agreeAdd(friend, new SaveListener<Object>() {
                    @Override
                    public void done(Object o, BmobException e) {
                        if (e == null) {
                            Intent intent = new Intent(context,All_fragmment_Activity.class);
                            context.startActivity(intent);
                        } else {
                            btn.setEnabled( true);
                            MyLog.e("TAG","添加好友失败:" + e.getMessage());
                            Toast.makeText(context, "添加好友失败"+ e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            }
        });

        return convertView;
    }



    /**
     * 添加到好友表中...
     *
     * @param add
     * @param listener
     */
    private void agreeAdd(final NewFriend add, final SaveListener<Object> listener) {
        User user = new User();
        user.setObjectId(add.getUid());
        UserModel.getInstance()
                .agreeAddFriend(user, new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        if (e == null) {
                            sendAgreeAddFriendMessage(add, listener);
                        } else {
                            MyLog.i("tage",e.getMessage());
                            listener.done(null, e);
                        }
                    }
                });
    }


    /**
     * 发送同意添加好友的请求
     */
    private void sendAgreeAddFriendMessage(final NewFriend add, final SaveListener<Object> listener) {
        BmobIMUserInfo info = new BmobIMUserInfo(add.getUid(), add.getName(), add.getAvatar());
        //如果为true,则表明为暂态会话，也就是说该会话仅执行发送消息的操作，不会保存会话和消息到本地数据库中
        BmobIMConversation c = BmobIM.getInstance().startPrivateConversation(info, true, null);
        //这个obtain方法才是真正创建一个管理消息发送的会话
        BmobIMConversation conversation = BmobIMConversation.obtain(BmobIMClient.getInstance(), c);
        //而AgreeAddFriendMessage的isTransient设置为false，表明我希望在对方的会话数据库中保存该类型的消息
        AgreeAddFriendMessage msg = new AgreeAddFriendMessage();
        final User currentUser = BmobUser.getCurrentUser(User.class);
        msg.setContent("我通过了你的好友验证请求，我们可以开始 聊天了!");//---这句话是直接存储到对方的消息表中的
        Map<String, Object> map = new HashMap<>();
        map.put("msg", currentUser.getUsername() + "同意添加你为好友");//显示在通知栏上面的内容
        map.put("uid", add.getUid());//发送者的uid-方便请求添加的发送方找到该条添加好友的请求
        map.put("time", add.getTime());//添加好友的请求时间
        msg.setExtraMap(map);
        conversation.sendMessage(msg, new MessageSendListener() {
            @Override
            public void done(BmobIMMessage msg, BmobException e) {
                if (e == null) {//发送成功
                    //修改本地的好友请求记录
                    NewFriendManager.getInstance(context).updateNewFriend(add, Config.STATUS_VERIFIED);
                    listener.done(msg, e);
                } else {//发送失败
                    MyLog.i("tage",e.getMessage());
                    listener.done(msg, e);
                }
            }
        });
    }

}
