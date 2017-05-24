package com.hmxl.yuedemo.bean;

import cn.bmob.v3.BmobObject;

/**
 * @author mlf
 *
 */
public class Friend extends BmobObject {

    private User user,friendUser;
    private String friend_remak;
    //拼音
    private transient String pinyin;

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getFriendUser() {
        return friendUser;
    }

    public void setFriendUser(User friendUser) {
        this.friendUser = friendUser;
    }

    public String getFriend_remak() {
        return friend_remak;
    }

    public void setFriend_remark(String friend_remark) {
        this.friend_remak = friend_remark;
    }
}
