package com.hmxl.yuedemo.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by HPC on 2017/5/11.
 */

public class User extends BmobObject {
    public enum Sex{
        male,
        female
    }

    private String sex;
    private int  age;
    private BmobFile thumbnail;
    private String remark;
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
