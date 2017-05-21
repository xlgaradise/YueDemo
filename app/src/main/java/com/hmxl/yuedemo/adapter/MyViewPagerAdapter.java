package com.hmxl.yuedemo.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/5/8.
 */

public class MyViewPagerAdapter extends PagerAdapter {

    private Context context;
    private ArrayList<View> imageViewList = new ArrayList<>();

    public MyViewPagerAdapter(Context context){
        this.context = context;

    }

    public void setData(int[] imgsIds){
        for (int imgId : imgsIds)
        {
            ImageView imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setImageResource(imgId);
            imageViewList.add(imageView);
        }
    }

    public void destroy(){
        imageViewList.clear();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(imageViewList.get(position));
        return imageViewList.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position,
                            Object object) {
        container.removeView(imageViewList.get(position));
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public int getCount() {
        return imageViewList.size();
    }
}
