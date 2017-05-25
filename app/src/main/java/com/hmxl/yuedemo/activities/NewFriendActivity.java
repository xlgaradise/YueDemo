package com.hmxl.yuedemo.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.hmxl.yuedemo.R;
import com.hmxl.yuedemo.adapter.NewFridAdapter;
import com.hmxl.yuedemo.bean.NewFriendManager;

import java.util.List;

public class NewFriendActivity extends BaseActivity {

    NewFridAdapter adapter;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_friend);
        listView= (ListView) findViewById(R.id.listview);
        adapter=new NewFridAdapter(this);
        listView.setAdapter(adapter);
        //批量更新未读未认证的消息为已读状态
        NewFriendManager.getInstance(this).updateBatchStatus();
    }

    @Override
    protected void onResume() {
        super.onResume();
        query();
    }

    /**
     查询本地会话
     */
    public void query(){
        adapter.addData(NewFriendManager.getInstance(this).getAllNewFriend());
        adapter.notifyDataSetChanged();
    }
}
