package com.hmxl.yuedemo.activities;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RadioGroup;

import com.hmxl.yuedemo.R;

public class All_fragmment_Activity extends AppCompatActivity {
    private Fragment_A fragment_a;
    private Fragment_B fragment_b;
    private Fragment_C fragment_c;
    private Fragment_D fragment_d;
    private RadioGroup radioGroup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_fragmment);

        radioGroup = (RadioGroup) findViewById(R.id.radiogruop);
        fragment_a = new Fragment_A();
        fragment_b = new Fragment_B();
        fragment_c = new Fragment_C();
        fragment_d = new Fragment_D();

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft= fm.beginTransaction();
                if (checkedId==R.id.rd_date){
                    ft.replace(R.id.fg_container,fragment_a);
                }else if (checkedId==R.id.rd_message){
                    ft.replace(R.id.fg_container,fragment_b);
                } else if (checkedId==R.id.rd_find){
                    ft.replace(R.id.fg_container,fragment_c);
                }else{
                    ft.replace(R.id.fg_container,fragment_d);
                }
                ft.commit();
            }
        });



    }
}
