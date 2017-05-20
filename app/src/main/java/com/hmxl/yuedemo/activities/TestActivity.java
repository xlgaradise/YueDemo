package com.hmxl.yuedemo.activities;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.hmxl.yuedemo.R;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_fragmment);

        Button btn_show = (Button) findViewById(R.id.btn_menu);

        btn_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupWindow(v);
            }
        });
    }

    public void showPopupWindow(View view) {
        final Context context = view.getContext();

        // 一个自定义的布局，作为显示的内容
        View contentView = LayoutInflater.from(context).inflate(R.layout.popwindow_menu, null);
        // 设置按钮的点击事件
//        Button button = (Button) contentView.findViewById(R.id.btn_show);
//        button.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(context, "button is pressed",
//                        Toast.LENGTH_SHORT).show();
//            }
//        });

//        TextView tv = (TextView) contentView.findViewById(R.id.txt_userID);
//        tv.setText("userID: "+userMarker.getUserID());

        final PopupWindow popupWindow = new PopupWindow(contentView,
                300, ViewGroup.LayoutParams.WRAP_CONTENT, true);


        popupWindow.setTouchable(true);

        popupWindow.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                System.out.println("on touch");
                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });

        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        popupWindow.setBackgroundDrawable(context.getResources()
                .getDrawable(R.drawable.bg_filling));

        // 设置好参数之后再show
        popupWindow.showAtLocation(view, Gravity.TOP,390,200);

    }

}
