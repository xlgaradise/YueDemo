package com.hmxl.yuedemo.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.hmxl.yuedemo.R;
import com.hmxl.yuedemo.adapter.NewFridAdapter;
import com.hmxl.yuedemo.bean.Friend;
import com.hmxl.yuedemo.bean.NewFriend;
import com.hmxl.yuedemo.bean.NewFriendManager;
import com.hmxl.yuedemo.bmobMessage.Config;

import java.util.ArrayList;
import java.util.List;

public class NewFriendActivity extends BaseActivity {

    private static String TAG = "NewFriendActivity";
    NewFridAdapter adapter;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_friend);

        Button btn = (Button) findViewById(R.id.btn_friendsearch);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NewFriendActivity.this, SearchfriendActivity.class);
                startActivity(intent);
            }
        });

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
     查询本地有效会话
     */
    public void query(){
        List<NewFriend> friendArrayList_1 = NewFriendManager.getInstance(this).getAllNewFriend();
        List<NewFriend> friendArrayList = new ArrayList<>();
        for(NewFriend friend:friendArrayList_1){
            if(friend.getStatus() == Config.STATUS_VERIFY_READED){
                friendArrayList.add(friend);
                Log.d(TAG,friend.getName());
            }
        }

        List<NewFriend> removeList = new ArrayList<>();
        NewFriend newFriend_1,newFriend_2;

        int i = 0;
        while(true){
            if(friendArrayList.size() == 0) break;
            if(i >= friendArrayList.size()) break;

            newFriend_1 = friendArrayList.get(i);
            for (int j = i + 1; j < friendArrayList.size(); j++) {
                newFriend_2 = friendArrayList.get(j);
                if (newFriend_1.getName().equals(newFriend_2.getName())) {
                    removeList.add(newFriend_2);
                    NewFriendManager.getInstance(this).deleteNewFriend(newFriend_2);
                }
            }
            friendArrayList.removeAll(removeList);
            i++;
        }

        adapter.addData(friendArrayList);
        adapter.notifyDataSetChanged();
    }
}
