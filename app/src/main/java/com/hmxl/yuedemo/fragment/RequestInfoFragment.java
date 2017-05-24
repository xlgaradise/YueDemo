package com.hmxl.yuedemo.fragment;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.hmxl.yuedemo.R;
import com.hmxl.yuedemo.activities.BaseActivity;
import com.hmxl.yuedemo.bean.RadarRequsetOption;
import com.hmxl.yuedemo.bean.RadarUser;
import com.hmxl.yuedemo.bean.User;
import com.hmxl.yuedemo.bean.UserMarker;
import com.hmxl.yuedemo.tools.baidumap.MapManager;
import com.hmxl.yuedemo.tools.baidumap.RadarManager;
import com.hmxl.yuedemo.tools.exception.MyLog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;


public class RequestInfoFragment extends Fragment {
    private static final String TAG = "RequestInfoFragment";
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

    private String delay_days;
    private SimpleDateFormat sdf;
    private Date curDate;
    private String  current_time;

    private RadarRequsetOption.SearchType searchType = RadarRequsetOption.SearchType.all;
    private String sex_type = "all"; // 0 为不限定， 1 为男  -1 为女
    private String message = "";
    private String  date_time;

    private RadarUser radarUser = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_requestinfo,container,false);
        initView(view);
        return view;
    }

    private void initView(final View view){
        rd_gp = (RadioGroup) view.findViewById(R.id.radioGroup);
        spinner = (Spinner) view.findViewById(R.id.spinner);
        tv_current_time = (TextView) view.findViewById(R.id.tv_current_time);
        tv_date_time = (TextView) view.findViewById(R.id.tv_date_time);
        et_remark = (EditText) view.findViewById(R.id.et_remark);
        btn_submit = (Button) view.findViewById(R.id.btn_submit);

        sdf = new SimpleDateFormat    ("yyyy-MM-dd");
        curDate = new Date();//获取当前时间
        current_time = sdf.format(new Date(curDate.getTime() ));
        date_time = current_time;
        tv_current_time.setText(current_time);
        tv_date_time.setText(current_time);
        // spinner
        data_list = new ArrayList<String>();
        setDataList();

        //适配器
        arrayAdapter= new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, data_list);
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
                searchType = RadarRequsetOption.SearchType.getSearchType(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //spinner2
        spinner2 = (Spinner) view.findViewById(R.id.spinner2);
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
        arrayAdapter2= new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, data_list2);
        //设置样式
        arrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器
        spinner2.setAdapter(arrayAdapter2);
        spinner2.setSelection(0, true);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                delay_days = (String )spinner2.getItemAtPosition(position);
                date_time = sdf.format(new Date(curDate.getTime()+ Integer.valueOf(delay_days)* 24 * 60 * 60 * 1000) );
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
                    sex_type = "all";
                }else  if (checkedId == R.id.male){
                    sex_type = "male";
                }else {
                    sex_type = "female";
                }

            }

        });
        ((RadioButton)rd_gp.getChildAt(0)).setChecked(true);

        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                ArrayList<UserMarker> list = (ArrayList<UserMarker>)msg.obj;
                MapManager.getInstance().setUserMarksList(list);
                Toast.makeText(getContext(),list.size()+"个结果已搜索到,请在地图页面查看结果",Toast.LENGTH_SHORT).show();
            }
        };

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                message = et_remark.getText().toString().trim();
                if(message.equals("")){
                    message = "无备注信息";
                }else{
                    message = message.replace(RadarManager.separator," ");
                }
                radarUser = new RadarUser();
                final BmobUser bmobUser = BmobUser.getCurrentUser();
                if(bmobUser != null){
                    Log.d(TAG,"bmobuser != null");
                    //通过objectId获取到user的全部信息
                    BmobQuery<User> query = new BmobQuery<>();
                    query.getObject(bmobUser.getObjectId(), new QueryListener<User>() {
                        @Override
                        public void done(User user, BmobException e) {
                            if(e==null){
                                Log.d(TAG,"e == null");
                                radarUser.id = user.getObjectId();
                                radarUser.name = user.getUsername();
                                if(user.getSex()){
                                    radarUser.sex = User.Sex.male;
                                }else{
                                    radarUser.sex = User.Sex.female;
                                }
                                radarUser.requsetOption = new RadarRequsetOption();
                                radarUser.requsetOption.searchType = searchType;
                                radarUser.requsetOption.date = date_time;
                                radarUser.requsetOption.searchSex = sex_type;
                                radarUser.requsetOption.message = message;

                                RadarManager.getInstance(getContext()).uploadOnce(radarUser,handler);
                            }else{
                                Log.d(TAG,"e != null");
                                Toast.makeText(getContext(),"获取当前用户信息出错",Toast.LENGTH_SHORT).show();
                                MyLog.e("RequestInfoFragment","query user error",e);
                                return;
                            }
                        }
                    });
                }else{
                    Log.d(TAG,"bmobuser == null");
                    Toast.makeText(getContext(),"获取当前用户信息出错",Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }


    private void setDataList(){
        data_list.add("全部");
        data_list.add("跑步");
        data_list.add("看电影");
        data_list.add("吃个饭喽");
        data_list.add("爬山吧");
        data_list.add("一起旅游吧");
        data_list.add("其他");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        RadarManager.getInstance(getContext()).close();
    }
}
