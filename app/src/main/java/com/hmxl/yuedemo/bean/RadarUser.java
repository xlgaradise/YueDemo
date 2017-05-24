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
        name = "nullname";
        sex = User.Sex.female;
        requsetOption = new RadarRequsetOption();
    }

    public  String toString(){
        String str = "nullstr";
        str = "id:"+id+",name:"+name+",sex:"+sex.toString()
                +",searchType:"+requsetOption.searchType.toString()
                +",searchSex:"+requsetOption.searchSex.toString()
                +",message:"+requsetOption.message
                +",date:"+requsetOption.date;
        return  str;
    }
}
