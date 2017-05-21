package com.hmxl.yuedemo.activities;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.hmxl.yuedemo.R;
import com.hmxl.yuedemo.fragment.CommunityFragment;
import com.hmxl.yuedemo.fragment.FindFragment;
import com.hmxl.yuedemo.fragment.MessageFragment;
import com.hmxl.yuedemo.fragment.MineFragment;

public class All_fragmment_Activity extends AppCompatActivity {
    private CommunityFragment communityFragment;
    private MessageFragment messageFragment;
    private FindFragment findFragment_;
    private MineFragment mineFragment;
    private RadioGroup radioGroup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_fragmment);

        radioGroup = (RadioGroup) findViewById(R.id.radiogruop);
        communityFragment = new CommunityFragment();
        messageFragment = new MessageFragment();
        findFragment_ = new FindFragment();
        mineFragment = new MineFragment();

        Intent intent = getIntent();
        int index = intent.getIntExtra("index",1);
        showIndex(index);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft= fm.beginTransaction();
                if (checkedId==R.id.rd_community){
                    ft.replace(R.id.fg_container, communityFragment);
                }else if (checkedId==R.id.rd_message){
                    ft.replace(R.id.fg_container, messageFragment);
                } else if (checkedId==R.id.rd_find){
                    ft.replace(R.id.fg_container, findFragment_);
                }else{
                    ft.replace(R.id.fg_container, mineFragment);
                }
                ft.commit();
            }
        });

    }

    private void showIndex(int index){
        if (index == 1){
            ((RadioButton)radioGroup.getChildAt(0)).setChecked(true);
        }else if (index == 2){
            ((RadioButton)radioGroup.getChildAt(1)).setChecked(true);
        } else if (index == 3){
            ((RadioButton)radioGroup.getChildAt(2)).setChecked(true);
        }else{
            ((RadioButton)radioGroup.getChildAt(3)).setChecked(true);
        }
    }
}
