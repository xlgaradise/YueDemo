package com.hmxl.yuedemo.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hmxl.yuedemo.R;
import com.hmxl.yuedemo.bean.Friend;
import com.hmxl.yuedemo.bean.User;
import com.hmxl.yuedemo.bmobMessage.AddFriendMessage;
import com.hmxl.yuedemo.bmobMessage.BmobIMManager;

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
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class FriendrequestActivity extends BaseActivity {

    private static final String TAG = "FriendrequestActivity";
    private ImageView iv_avator;
    private TextView tv_name;
    private Button btn_add_friend;
    private Button btn_send;
    User user;
    Friend friend;
    BmobIMUserInfo info;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_launch);

        iv_avator = (ImageView) findViewById(R.id.iv_avator);
        tv_name = (TextView) findViewById(R.id.tv_name);
        btn_add_friend = (Button) findViewById(R.id.btn_add_friend);
        btn_send = (Button) findViewById(R.id.btn_send);

        Intent intent= getIntent();
        BmobObject object = (BmobObject) intent.getSerializableExtra("object");
        Boolean isFriend = intent.getBooleanExtra("isFriend",false);
        if(isFriend){
            friend = (Friend)object;
            //如果是好友，则显示对话框
            btn_add_friend.setVisibility(View.GONE);
            btn_send.setVisibility(View.VISIBLE);
            if(friend.getFriendUser().getSex()){
                iv_avator.setImageResource(R.drawable.icon_map_male);
            }else{
                iv_avator.setImageResource(R.drawable.icon_map_female);
            }
            tv_name.setText(friend.getFriendUser().getRemark());

        }else{
            user = (User)object;
            if(user.getSex()){
                iv_avator.setImageResource(R.drawable.icon_map_male);
            }else{
                iv_avator.setImageResource(R.drawable.icon_map_female);
            }
            tv_name.setText(user.getRemark());
            btn_add_friend.setVisibility(View.VISIBLE);
            btn_send.setVisibility(View.GONE);
        }

        btn_add_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BmobQuery<Friend> query = new BmobQuery<Friend>();
                query.addWhereEqualTo("user",BmobUser.getCurrentUser());
                query.addWhereEqualTo("friendUser",user.getObjectId());
                query.include("friendUser");
                query.findObjects(new FindListener<Friend>() {
                    @Override
                    public void done(List<Friend> list, BmobException e) {
                        if(e==null){
                            if(list.size() == 0){
                                BmobIMManager.getInstance().sendAddFriendMessage(getBaseContext(),
                                        user.getObjectId(),user.getUsername());
                            }else{
                                showToast("已添加为好友");
                            }
                        }else{
                            Log.e(TAG,"查询好友失败："+e.getErrorCode()+","+e.getMessage());
                        }
                    }
                });

            }
        });
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = friend.getFriendUser();
                BmobIMUserInfo info = new BmobIMUserInfo(user.getObjectId(), user.getUsername(), null);
                //启动一个会话，实际上就是在本地数据库的会话列表中先创建（如果没有）与该用户的会话信息，且将用户信息存储到本地的用户表中
                BmobIMConversation c = BmobIM.getInstance().startPrivateConversation(info, null);
                Bundle bundle = new Bundle();
                bundle.putSerializable("c", c);

                Intent intent = new Intent(FriendrequestActivity.this,ChatActivity.class);
                intent.putExtra("c",bundle);
                startActivity(intent);
                finish();
            }
        });

    }
}
