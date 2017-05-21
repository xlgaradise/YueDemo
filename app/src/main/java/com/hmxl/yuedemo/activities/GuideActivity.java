package com.hmxl.yuedemo.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.hmxl.yuedemo.R;
import com.hmxl.yuedemo.adapter.MyPageTransformer;
import com.hmxl.yuedemo.adapter.MyViewPagerAdapter;

import java.util.ArrayList;

import cn.bmob.v3.BmobUser;

import static com.hmxl.yuedemo.activities.ApplicationDemo.SHAREDPREFERENCE_NAME;

public class GuideActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private int[] mImgIds = new int[] { R.drawable.guide_1,
            R.drawable.guide_2, R.drawable.guide_3 };

    ArrayList<ImageView> pointsList;
    Button btn_register;
    Button btn_login;
    Button btn_tiyan;
    private boolean isFirstIn = true;

    MyViewPagerAdapter myViewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getState()){
            initView();
            saveState();
        }else{
            isFirstIn = false;
            BmobUser user = BmobUser.getCurrentUser();
            if(user == null){
                Intent intent = new Intent(this, WelcomeActivity.class);
                startActivity(intent);
                finish();
            }else{
                Intent intent = new Intent(this, All_fragmment_Activity.class);
                startActivity(intent);
                finish();
            }
        }

    }

    private void initView(){
        setContentView(R.layout.activity_guide);
        btn_register = (Button) findViewById(R.id.btn_register);
        btn_register.setVisibility(View.GONE);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GuideActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btn_login = (Button) findViewById(R.id.btn_login);
        btn_login.setVisibility(View.GONE);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GuideActivity.this, LoginUsActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btn_tiyan = (Button) findViewById(R.id.btn_pass);
        btn_tiyan.setVisibility(View.GONE);
        btn_tiyan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO
//                Intent intent = new Intent(GuideActivity.this, WelcomeActivity.class);
//                startActivity(intent);
//                finish();
                Toast.makeText(GuideActivity.this,"暂未实现",Toast.LENGTH_SHORT).show();
            }
        });

        myViewPagerAdapter = new MyViewPagerAdapter(this);
        myViewPagerAdapter.setData(mImgIds);

        mViewPager = (ViewPager) findViewById(R.id.viewpager_guide);
        mViewPager.setAdapter(myViewPagerAdapter);
        mViewPager.setPageTransformer(true, new MyPageTransformer(MyPageTransformer.PagerTransformerAction.Depth));

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.ll_point);
        ImageView imageView;
        pointsList = new ArrayList<>();
        for(int i = 0;i<mImgIds.length;i++){
            imageView = new ImageView(this);
            imageView.setPadding(16,16,16,16);
            if(i==0){
                imageView.setImageResource(R.drawable.guide_point_select);
            }else{
                imageView.setImageResource(R.drawable.guide_point_normal);
            }
            pointsList.add(imageView);
            linearLayout.addView(imageView);
        }

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                if(position == pointsList.size()-2){
                    if(positionOffset<0.95) {
                        btn_register.setVisibility(View.GONE);
                        btn_login.setVisibility(View.GONE);
                        btn_tiyan.setVisibility(View.GONE);
                    }else{
                        btn_register.setVisibility(View.VISIBLE);
                        btn_login.setVisibility(View.VISIBLE);
                        btn_tiyan.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onPageSelected(int position) {
                for(int i = 0; i< pointsList.size(); i++){
                    if(i==position){
                        pointsList.get(i).setImageResource(R.drawable.guide_point_select);
                    }else{
                        pointsList.get(i).setImageResource(R.drawable.guide_point_normal);
                    }
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }


        });
    }

    private boolean getState(){
        // 读取SharedPreferences中需要的数据
        // 使用SharedPreferences来记录程序的使用次数
        SharedPreferences preferences = getSharedPreferences(
                SHAREDPREFERENCE_NAME, MODE_PRIVATE);
        // 取得相应的值，如果没有该值，说明还未写入，用true作为默认值
        boolean isFirstIn = preferences.getBoolean("isFirstIn", true);
        return isFirstIn;
    }

    private void saveState(){
        SharedPreferences sf = getSharedPreferences(
                SHAREDPREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sf.edit();
        editor.putBoolean("isFirstIn", false);
        editor.commit();
    }

    @Override
    protected void onDestroy() {
        if(isFirstIn){
            myViewPagerAdapter.destroy();
            pointsList.clear();
        }
        super.onDestroy();
    }
}
