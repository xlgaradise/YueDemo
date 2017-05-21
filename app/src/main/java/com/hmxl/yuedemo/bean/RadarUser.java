package com.hmxl.yuedemo.bean;

/**
 * Created by HPC on 2017/5/15.
 */

public class RadarUser {


    public String id;
    public String name;
    public User.Sex sex;
    public RadarRequsetOption requsetOption;
    public RadarUser(){
        id = "nullUser";
        name = "";
        sex = User.Sex.female;
        requsetOption = new RadarRequsetOption();
    }
}
