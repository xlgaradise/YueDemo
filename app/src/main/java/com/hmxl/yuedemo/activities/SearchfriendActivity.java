package com.hmxl.yuedemo.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.hmxl.yuedemo.R;
import com.hmxl.yuedemo.adapter.MyUnknowFriendAdapter;
import com.hmxl.yuedemo.bean.Friend;
import com.hmxl.yuedemo.bean.User;
import com.hmxl.yuedemo.bmobMessage.AddFriendMessage;
import com.hmxl.yuedemo.tools.exception.MyLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.core.BmobIMClient;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;

public class SearchfriendActivity extends BaseActivity {

    private static String TAG = "SearchfriendActivity";
    String username ;
    private EditText et_find_name;
    private Button btn_search;
    private ListView lv_searchfriend;
    MyUnknowFriendAdapter adapter ;
    private ArrayList<BmobObject> objects = new ArrayList<>();
    private ArrayList<Boolean> isFriendList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchfriend);
        et_find_name = (EditText) findViewById(R.id.et_find_name);

        btn_search = (Button) findViewById(R.id.btn_search);
        lv_searchfriend = (ListView) findViewById(R.id.lv_searchfriend);
        adapter = new MyUnknowFriendAdapter(getBaseContext());
        lv_searchfriend.setAdapter(adapter);
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFriendList.clear();
                objects.clear();
                adapter.addAllData(objects,isFriendList);
                adapter.notifyDataSetChanged();
                //查询用户
                //System.out.println("aaa");
                username = et_find_name.getText().toString().trim();
                // 判断username是否已经是你的好友，如果不是则添加
                if(username.equals(BmobUser.getCurrentUser().getUsername())){
                    Toast.makeText(getBaseContext(),"您不能搜索自己",Toast.LENGTH_SHORT).show();
                    return;
                }
                BmobQuery<Friend> query = new BmobQuery<>();
                query.addWhereEqualTo("user",BmobUser.getCurrentUser());
                query.include("friendUser");
                query.findObjects(new FindListener<Friend>() {
                    @Override
                    public void done(List<Friend> list, BmobException e) {
                        if(e==null){
                            for(Friend f:list){
                                //System.out.println("id:"+f.getFriendUser().getObjectId());
                                if (f.getFriendUser().getUsername().equals(username)) {
                                    // 假如是你的好友，则按钮不可点击
                                    isFriendList.add(true);
                                    objects.add(f);
                                    adapter.addAllData(objects, isFriendList);
                                    adapter.notifyDataSetChanged();
                                    return;
                                }
                            }
                            BmobQuery<User> bquery = new BmobQuery<User>();
                            bquery.addWhereEqualTo("username",username);
                            bquery.setLimit(50);
                            bquery.findObjects(new FindListener<User>() {
                                @Override
                                public void done(List<User> list, BmobException e) {
                                    // 查询成功,
                                    System.out.println("userList size:"+list.size());
                                    if(list.size()>0){
                                        if (list.get(0).getUsername().equals(username)) {
                                            // 不是你的好友，则按钮可点击
                                            isFriendList.add(false);
                                            objects.add(list.get(0));
                                            adapter.addAllData(objects, isFriendList);
                                            adapter.notifyDataSetChanged();
                                        }
                                    }else{
                                        Toast.makeText(getBaseContext(),
                                                "该用户不存在",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }else{
                            Log.e(TAG,"查询失败："+e.getMessage()+","+e.getErrorCode());
                            MyLog.e(TAG,"查询失败：",e);
                        }
                    }
                });

            }
        });


        lv_searchfriend.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BmobObject object = (BmobObject) adapter.getItem(position);
                Boolean isFriend = adapter.getItemBoolean(position);
                Intent intent = new Intent(SearchfriendActivity.this,FriendrequestActivity.class);
                intent.putExtra("object",object);
                intent.putExtra("isFriend",isFriend);
                startActivity(intent);
            }
        });
    }

}
