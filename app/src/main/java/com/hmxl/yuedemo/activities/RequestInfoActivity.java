package com.hmxl.yuedemo.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.hmxl.yuedemo.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;



public class RequestInfoActivity extends AppCompatActivity {
    //第一个spinner
    private Spinner spinner ;
    private Spinner spinner2;
    private List<String> data_list;
    private List<String> data_list2;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayAdapter<String> arrayAdapter2;
    private RadioGroup rd_gp;
    private TextView tv_current_time;
    private TextView tv_date_time;
    private EditText et_remark;
    private Button btn_submit;
    String data_type ;
    int sex_type; // 0 为不限定， 1 为男  -1 为女
    String remark;
    String delay_days;
    SimpleDateFormat sdf;
    Date curDate;
    String  current_time;
    String    date_time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requestinfo);
        rd_gp = (RadioGroup) findViewById(R.id.radioGroup);
        spinner = (Spinner)findViewById(R.id.spinner);
        tv_current_time = (TextView) findViewById(R.id.tv_current_time);
        tv_date_time = (TextView) findViewById(R.id.tv_date_time);
        et_remark = (EditText) findViewById(R.id.et_remark);
        btn_submit = (Button) findViewById(R.id.btn_submit);
        remark = et_remark.getText().toString().trim();
        sdf    =   new    SimpleDateFormat    ("yyyy年MM月dd日");
        curDate    =   new    Date();//获取当前时间
        current_time    =   sdf.format(new Date(curDate.getTime() ));
        tv_current_time.setText(current_time);
        tv_date_time.setText(current_time);
        // spinner
        data_list = new ArrayList<String>();
        data_list.add("全部");
        data_list.add("吃个饭喽");
        data_list.add("爬山吧");
        data_list.add("一起旅游吧");
        data_list.add("其他");


       //适配器
        arrayAdapter= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data_list);
        //设置样式
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器
        spinner.setAdapter(arrayAdapter);
        //让第一个数据项已经被选中
        spinner.setSelection(0, true);
        //给第一个spinner添加点击事件
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                data_type = (String) spinner.getItemAtPosition(position);
                Toast.makeText(RequestInfoActivity.this, data_type.toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //spinner2
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
        spinner2.setSelection(0, true);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                delay_days = (String )spinner2.getItemAtPosition(position);
                Toast.makeText(RequestInfoActivity.this, delay_days+"", Toast.LENGTH_SHORT).show();
                date_time    =    sdf.format(new Date(curDate.getTime()+ Integer.valueOf(delay_days)* 24 * 60 * 60 * 1000) );
                tv_date_time.setText(date_time);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        // 给radioGroup添加点击事件
        rd_gp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.nosex){
                    sex_type = 0;
                    Toast.makeText(RequestInfoActivity.this, sex_type+"", Toast.LENGTH_SHORT).show();
                }else  if (checkedId == R.id.male){
                    sex_type = 1;
                    Toast.makeText(RequestInfoActivity.this, sex_type+"", Toast.LENGTH_SHORT).show();
                }else {
                    sex_type = -1;
                    Toast.makeText(RequestInfoActivity.this, sex_type+"", Toast.LENGTH_SHORT).show();
                }

            }

        });
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // 把信息上传到输入库中
                // updateRequest();
                // 跳转页面
//                Intent intent = new Intent(RequestInfoActivity.this,All_fragmment_Activity.class);
//
//                startActivity(intent);
            }
        });








    }

}
