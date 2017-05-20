package com.hmxl.yuedemo.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by Nate on 2017/5/16.
 */

public class Chat extends BmobObject{
    String user_id;
    String friend_id;
    String content;
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFriend_id() {
        return friend_id;
    }

    public void setFriend_id(String friend_id) {
        this.friend_id = friend_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }


}
