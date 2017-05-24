package com.hmxl.yuedemo.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by HPC on 2017/5/11.
 */

public class User extends BmobUser {
    public enum Sex{
        male,
        female
    }
    // 新添加的第一处
//    public User(NewFriend friend){
//        setObjectId(friend.getUid());
//        setUsername(friend.getName());
//        setAvatar(friend.getAvatar());
//
//    }
    // 新添加的第二处
    private String avatar;
    private BmobFile thumbnail;
    private String remark;
    private Boolean sex;
    private int  age;
    private String location;
    private String introduction;
    private boolean isFriend = false;

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }


    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public BmobFile getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(BmobFile thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Boolean getSex() {
        return sex;
    }

    public void setSex(Boolean sex) {
        this.sex = sex;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public boolean isFriend() {
        return isFriend;
    }

    public void setFriend(boolean friend) {
        isFriend = friend;
    }
}
