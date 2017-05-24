package com.hmxl.yuedemo.bmobMessage;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.hmxl.yuedemo.activities.ChatActivity;
import com.hmxl.yuedemo.activities.FriendrequestActivity;
import com.hmxl.yuedemo.bean.Friend;
import com.hmxl.yuedemo.bean.User;
import com.hmxl.yuedemo.tools.exception.MyLog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.core.BmobIMClient;
import cn.bmob.newim.listener.MessageSendListener;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by HPC on 2017/5/24.
 */

public class BmobIMManager {

    private static String TAG = "BmobIMManager";

    private static BmobIMManager bmobIMManager;

    private BmobIMManager(){

    }

    public static BmobIMManager getInstance(){
        if(bmobIMManager == null){
            bmobIMManager = new BmobIMManager();
        }
        return  bmobIMManager;
    }

    public void sendAddFriendMessage(final Context context,String objectId,String name){
        BmobIMUserInfo info = new BmobIMUserInfo(objectId,name,null);
        //启动一个暂态会话，也就是isTransient为true,表明该会话仅执行发送消息的操作，不会保存会话和消息到本地数据库中，
        BmobIMConversation c = BmobIM.getInstance().startPrivateConversation(info, true,null);
        //这个obtain方法才是真正创建一个管理消息发送的会话
        BmobIMConversation conversation = BmobIMConversation.obtain(BmobIMClient.getInstance(), c);
        //新建一个添加好友的自定义消息实体
        AddFriendMessage msg =new AddFriendMessage();
        User currentUser = BmobUser.getCurrentUser(User.class);
        msg.setContent("很高兴认识你，可以加个好友吗?");//给对方的一个留言信息
        Map<String,Object> map =new HashMap<>();
        map.put("name", currentUser.getUsername());//发送者姓名，这里只是举个例子，其实可以不需要传发送者的信息过去
        map.put("avatar",currentUser.getAvatar());//发送者的头像
        map.put("uid",currentUser.getObjectId());//发送者的uid
        msg.setExtraMap(map);
        conversation.sendMessage(msg, new MessageSendListener() {
            @Override
            public void done(BmobIMMessage msg, BmobException e) {
                if (e == null) {//发送成功
                    Log.d(TAG, "好友请求发送成功，等待验证");
                    Toast.makeText(context, "好友请求发送成功，等待验证", Toast.LENGTH_SHORT).show();
                } else {//发送失败
                    Log.e(TAG,"好友请求发送失败:" + e.getMessage());
                    Toast.makeText(context, "发送失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



    public void getMyFriend(final String objectId,final Handler handler){

        BmobQuery<Friend> query = new BmobQuery<>();
        query.addWhereEqualTo("user", BmobUser.getCurrentUser());
        query.include("friendUser");
        query.findObjects(new FindListener<Friend>() {
            @Override
            public void done(List<Friend> list, BmobException e) {
                Message message = new Message();
                if(e==null){
                    message.what = 0;
                    for(Friend f:list){
                        if (f.getFriendUser().getObjectId().equals(objectId)) {
                            // 假如是你的好友
                            message.what = 1;
                            message.obj = f;
                        }
                    }
                }else{
                    Log.e(TAG,"查询失败："+e.getMessage()+","+e.getErrorCode());
                    MyLog.e(TAG,"查询失败：",e);
                }
                handler.sendMessage(message);
            }
        });
    }

    public void goToChatActivity(Context context,User user){
        BmobIMUserInfo info = new BmobIMUserInfo(user.getObjectId(), user.getUsername(), null);
        //启动一个会话，实际上就是在本地数据库的会话列表中先创建（如果没有）与该用户的会话信息，且将用户信息存储到本地的用户表中
        BmobIMConversation c = BmobIM.getInstance().startPrivateConversation(info, null);
        Bundle bundle = new Bundle();
        bundle.putSerializable("c", c);

        Intent intent = new Intent(context,ChatActivity.class);
        intent.putExtra("c",bundle);
        context.startActivity(intent);
    }

}
