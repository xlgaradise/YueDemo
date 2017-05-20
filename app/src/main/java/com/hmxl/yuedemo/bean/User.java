package com.hmxl.yuedemo.bean;

import java.io.File;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by Nate on 2017/5/15.
 */

public class User extends BmobObject{
    private String sex;
    private int  age;
    private BmobFile thumbnail;
    private String remark;
    //用户当前的经度
    private String point_X;
    //用户当前的纬度
    private String point_Y;
    //由于提示Integer number too large 所以改为Long
    public  int      getAge() {
        return age;
    }public void     setAge(int age) {
        this.age = age;
    }public String   getRemark() {
        return remark;
    }public void     setRemark(String remark) {
        this.remark = remark;
    }public String   getSex() {
        return sex;
    }public void     setSex(String sex) {
        this.sex = sex;
    }public BmobFile getThumbnail() {
        return thumbnail;
    }public void     setThumbnail(BmobFile thumbnail) {
        this.thumbnail = thumbnail;
    }

}
