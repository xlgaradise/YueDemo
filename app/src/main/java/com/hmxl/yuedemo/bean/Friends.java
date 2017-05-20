package com.hmxl.yuedemo.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by Nate on 2017/5/16.
 */

public class Friends extends BmobObject {
    String user_id;
    String friend_id;
    Boolean addToBlacklist;
    public Boolean getAddToBlacklist() {
        return addToBlacklist;
    }
    public void setAddToBlacklist(Boolean addToBlacklist) {
        this.addToBlacklist = addToBlacklist;
    }
    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getFriend_id() {
        return friend_id;
    }

    public void setFriend_id(String friend_id) {
        this.friend_id = friend_id;
    }


}
