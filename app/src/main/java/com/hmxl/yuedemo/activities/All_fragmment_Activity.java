package com.hmxl.yuedemo.activities;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RadioGroup;

import com.hmxl.yuedemo.R;
import com.hmxl.yuedemo.fragment.CommunityFragment;
import com.hmxl.yuedemo.fragment.FindFragment;
import com.hmxl.yuedemo.fragment.MessageFragment;
import com.hmxl.yuedemo.fragment.MineFragment;

public class All_fragmment_Activity extends AppCompatActivity {
    private CommunityFragment _communityFragment;
    private MessageFragment messageFragment_;
    private FindFragment findFragment_;
    private MineFragment mineFragment_;
    private RadioGroup radioGroup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_fragmment);

        radioGroup = (RadioGroup) findViewById(R.id.radiogruop);
        _communityFragment = new CommunityFragment();
        messageFragment_ = new MessageFragment();
        findFragment_ = new FindFragment();
        mineFragment_ = new MineFragment();

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft= fm.beginTransaction();
                if (checkedId==R.id.rd_community){
                    ft.replace(R.id.fg_container, _communityFragment);
                }else if (checkedId==R.id.rd_message){
                    ft.replace(R.id.fg_container, messageFragment_);
                } else if (checkedId==R.id.rd_find){
                    ft.replace(R.id.fg_container, findFragment_);
                }else{
                    ft.replace(R.id.fg_container, mineFragment_);
                }
                ft.commit();
            }
        });



    }
}
