package com.hmxl.yuedemo.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.hmxl.yuedemo.R;

import java.util.ArrayList;
import java.util.List;

public class RequestInfoActivity extends AppCompatActivity {
    //
    private Spinner spinner ;
    private List<String> data_list;
    private ArrayAdapter<String> arrayAdapter;
    //
    private Spinner spinner2;
    private List<String> data_list2;
    private ArrayAdapter<String> arrayAdapter2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requestinfo);
        spinner  = (Spinner) findViewById(R.id.spinner);
        data_list = new ArrayList<String>();
        data_list.add("全部");
        data_list.add("吃个饭喽");
        data_list.add("爬山吧");
        data_list.add("一起旅游吧");
        data_list.add("一起旅游吧");
        data_list.add("其他");
       //适配器
        arrayAdapter= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data_list);
        //设置样式
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器
        spinner.setAdapter(arrayAdapter);

        //
        spinner2 = (Spinner) findViewById(R.id.spinner2);
        data_list2 = new ArrayList<String>();
        data_list2.add("0");
        data_list2.add("1");
        data_list2.add("2");
        data_list2.add("3");
        data_list2.add("4");
        data_list2.add("5");
        data_list2.add("6");
        data_list2.add("7");
        //适配器
        arrayAdapter2= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data_list2);
        //设置样式
        arrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器
        spinner2.setAdapter(arrayAdapter2);



    }
}
