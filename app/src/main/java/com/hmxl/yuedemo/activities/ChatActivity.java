package com.hmxl.yuedemo.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hmxl.yuedemo.R;
import com.hmxl.yuedemo.adapter.MyChatAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMTextMessage;
import cn.bmob.newim.core.BmobIMClient;
import cn.bmob.newim.core.ConnectionStatus;
import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.listener.ConnectListener;
import cn.bmob.newim.listener.ConnectStatusChangeListener;
import cn.bmob.newim.listener.MessageSendListener;
import cn.bmob.newim.listener.MessagesQueryListener;
import cn.bmob.newim.listener.ObseverListener;
import cn.bmob.newim.notification.BmobNotificationManager;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;

public class ChatActivity extends BaseActivity implements ObseverListener{

    private static final String TAG = "ChatActivity";
    BmobIMConversation conversation;
    TextView tv_chat_name ;
    ListView lv_content;
    Button btn_add;
    Button btn_send;
    EditText et_msg;
    MyChatAdapter chatAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("c");
        BmobIMConversation c = (BmobIMConversation) bundle.getSerializable("c");
        conversation = BmobIMConversation.obtain(BmobIMClient.getInstance(), c);
        initView();
        queryMessages();
    }

    private void initView(){
        chatAdapter = new MyChatAdapter(this);
        tv_chat_name = (TextView) findViewById(R.id.tv_chat_name);
        tv_chat_name.setText(conversation.getConversationTitle());
        lv_content = (ListView) findViewById(R.id.lv_chat_content);
        btn_add = (Button) findViewById(R.id.btn_chat_add);
        btn_send = (Button) findViewById(R.id.btn_chat_send);
        et_msg = (EditText) findViewById(R.id.edit_msg);
        lv_content.setAdapter(chatAdapter);
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = et_msg.getText().toString().trim();
                if(msg.equals("")){
                    Toast.makeText(ChatActivity.this, "不能发送空消息", Toast.LENGTH_SHORT).show();
                    return;
                }
                BmobIMTextMessage message = new BmobIMTextMessage();
                message.setContent(msg);
                sendMessage(message);
            }
        });
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(ChatActivity.this);
                dialog.setTitle("是否删除与该用户全部会话信息");
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        BmobIM.getInstance().deleteConversation(conversation.getConversationId());
                        Toast.makeText(getBaseContext(),"已全部删除",Toast.LENGTH_SHORT).show();
                        queryMessages();
                    }
                });
                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                dialog.create().show();
            }
        });
    }


    private void sendMessage(BmobIMTextMessage msg){
        conversation.sendMessage(msg, new MessageSendListener() {
            @Override
            public void onStart(BmobIMMessage msg) {
                Log.d(TAG,"已发送");
                super.onStart(msg);
            }

            @Override
            public void done(BmobIMMessage msg, BmobException e) {
                if(e==null){
                    //BmobIM.getInstance().updateConversation(conversation);
                    chatAdapter.addData(msg);
                    chatAdapter.notifyDataSetChanged();
                    et_msg.setText("");
                }else{
                    Log.e(TAG,"加载聊天信息失败,"+e.getErrorCode()+","+e.getMessage());
                    Toast.makeText(ChatActivity.this, "发送信息失败",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void queryMessages(){
        chatAdapter.clearData();
        conversation.queryMessages(null, 100, new MessagesQueryListener() {
            @Override
            public void done(List<BmobIMMessage> list, BmobException e) {
                if (e == null) {
                    Log.d(TAG,"all msg size:"+list.size());
                    if (null != list && list.size() > 0) {
                        for(BmobIMMessage m:list){
                            chatAdapter.addData(m);
                            //Log.d(TAG,"message:"+m.getFromId()+"-"+m.getContent());
                        }
                    }
                    chatAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(ChatActivity.this, "加载聊天信息失败,"+e.getErrorCode()+","+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    /**接收到聊天消息
     * @param event
     */
    @Subscribe
    public void onEventMainThread(MessageEvent event){
        chatAdapter.addData(event.getMessage());
        chatAdapter.notifyDataSetChanged();
    }


}
