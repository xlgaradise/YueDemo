package com.hmxl.yuedemo.tools.baidumap;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.radar.RadarNearbyInfo;
import com.baidu.mapapi.radar.RadarNearbyResult;
import com.baidu.mapapi.radar.RadarNearbySearchOption;
import com.baidu.mapapi.radar.RadarNearbySearchSortType;
import com.baidu.mapapi.radar.RadarSearchError;
import com.baidu.mapapi.radar.RadarSearchListener;
import com.baidu.mapapi.radar.RadarSearchManager;
import com.baidu.mapapi.radar.RadarUploadInfo;
import com.baidu.mapapi.radar.RadarUploadInfoCallback;
import com.hmxl.yuedemo.bean.RadarRequsetOption;
import com.hmxl.yuedemo.bean.RadarUser;
import com.hmxl.yuedemo.bean.User;
import com.hmxl.yuedemo.bean.UserMarker;

import java.util.ArrayList;
import java.util.Date;

import cn.bmob.v3.BmobUser;

/**
 * Created by HPC on 2017/5/15.
 */

public class RadarManager{
    private static String TAG = "RadarManager";

    //无需设置变量
    private static Context CurrentContext;
    private static RadarManager radarManager = null;
    public static String separator = "~";
    private Handler handler_withUser;
    private Handler handler_withNullUser;
    private MyLocationData locationData;
    private RadarSearchListener radarSearchListener;
    private RadarUploadInfoCallback radarUploadInfoCallback;

    //需设置
    private Handler handler_result;
    private static RadarUser currentRadarUser;
    private boolean isUploadOnce = true;
    private ArrayList<UserMarker> userMarksList;
    private RadarNearbySearchOption searchOption;

    private RadarManager(){
        init();
    }

    public static RadarManager getInstance(Context context){
        if(radarManager == null){
            radarManager = new RadarManager();
        }
        CurrentContext = context;
        return radarManager;
    }

    private void init(){
        userMarksList = new ArrayList<>();

        handler_withUser = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what == 1){//success
                    locationData = (MyLocationData) msg.obj;
                    //System.out.println(locationData.latitude+","+locationData.longitude);
                    if(currentRadarUser != null){
                        // 周边雷达设置用户，id为空默认是设备标识
                        RadarSearchManager.getInstance().setUserID(currentRadarUser.id);
                    }else{
                        RadarSearchManager.getInstance().setUserID("nullUser");
                    }
                    if(isUploadOnce){//单次上传一次
                        RadarUploadInfo info = new RadarUploadInfo();
                        info.comments = parseRadarUserToComment(currentRadarUser);
                        info.pt = new LatLng(locationData.latitude,locationData.longitude);
                        RadarSearchManager.getInstance().uploadInfoRequest(info);
                    }else{//持续上传
                        RadarSearchManager.getInstance().startUploadAuto(radarUploadInfoCallback,5000);
                    }
                }else{
                    Toast.makeText(CurrentContext,"获取定位失败！",Toast.LENGTH_SHORT).show();
                }
            }
        };

        handler_withNullUser = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what == 1){//success
                    locationData = (MyLocationData) msg.obj;
                    RadarSearchManager.getInstance().setUserID("nullUser");
                    //System.out.println(locationData.latitude+","+locationData.longitude);
                    searchOption.centerPt(new LatLng(locationData.latitude,locationData.longitude));
                    RadarSearchManager.getInstance().nearbyInfoRequest(searchOption);
                }else{
                    Toast.makeText(CurrentContext,"获取定位失败！",Toast.LENGTH_SHORT).show();
                }
            }
        };

        /*-上传位置监听函数-*/
        radarSearchListener = new RadarSearchListener() {
            @Override
            public void onGetNearbyInfoList(RadarNearbyResult radarNearbyResult, RadarSearchError radarSearchError) {
                if (radarSearchError == RadarSearchError.RADAR_NO_ERROR) {
                    //获取成功，处理数据
                    parseSearchResult(radarNearbyResult);
                } else {
                    //获取失败
                    Toast.makeText(CurrentContext, "查询周边信息没有结果", Toast.LENGTH_SHORT)
                            .show();
                }
            }

            @Override
            public void onGetUploadState(RadarSearchError radarSearchError) {
                if (radarSearchError == RadarSearchError.RADAR_NO_ERROR) {
                    //上传成功
                    if(isUploadOnce){
                        Toast.makeText(CurrentContext, "上传个人信息成功", Toast.LENGTH_LONG)
                                .show();
                    }
                    requestUserInfo();
                } else {
                    Log.d(TAG,radarSearchError.toString());
                    //上传失败
                    if(isUploadOnce){
                        Toast.makeText(CurrentContext, "上传个人信息失败", Toast.LENGTH_LONG)
                                .show();
                    }
                }
            }

            @Override
            public void onGetClearInfoState(RadarSearchError radarSearchError) {
                if (radarSearchError == RadarSearchError.RADAR_NO_ERROR) {
                    // 清除成功
                    Toast.makeText(CurrentContext,"信息已清除",Toast.LENGTH_SHORT).show();
                } else {
                    // 清除失败
                    Toast.makeText(CurrentContext, "清除个人位置信息失败", Toast.LENGTH_SHORT)
                            .show();
                }
            }
        };

        //自动上传信息回调函数
        radarUploadInfoCallback = new RadarUploadInfoCallback() {
            @Override
            public RadarUploadInfo onUploadInfoCallback() {
                RadarUploadInfo info = new RadarUploadInfo();
                info.comments = parseRadarUserToComment(currentRadarUser);
                info.pt = new LatLng(locationData.latitude,locationData.longitude);
                return info;
            }
        };

        // 周边雷达设置监听
        RadarSearchManager.getInstance().addNearbyInfoListener(radarSearchListener);
    }

    /**
     * 上传一次位置并自动请求周边信息
     * @param currentUser 可为null
     */
    public void uploadOnce(RadarUser currentUser,Handler handler) {
        if(currentUser == null){
            requestUserInfoWithNullUser(handler);
        }else{
            Log.d(TAG,"upload info:--"+currentUser.toString());
            //clear();
            isUploadOnce = true;
            this.currentRadarUser = currentUser;
            userMarksList.clear();
            searchOption = getDefaultOption();
            handler_result = handler;
            LocationManager.getInstance().requestLocation(handler_withUser);
        }
    }

    /**
     * 不带用户的默认请求
     * @param handler
     */
    public void requestUserInfoWithNullUser(Handler handler){
        //clear();
        userMarksList.clear();
        searchOption = getDefaultOption();
        handler_result = handler;
        currentRadarUser = null;
        LocationManager.getInstance().requestLocation(handler_withNullUser);
    }

    /**
     * 请求周边信息
     */
    private void requestUserInfo(){
        searchOption.centerPt(new LatLng(locationData.latitude,locationData.longitude));
        RadarSearchManager.getInstance().nearbyInfoRequest(searchOption);
    }

    public RadarUser getRadaruser(){
        return this.currentRadarUser;
    }

    /**
     * 清除所有信息
     */
    public void clear() {
        this.userMarksList.clear();
        this.isUploadOnce = true;
        this.locationData = null;
        this.searchOption = null;
        this.handler_result = null;
    }

    /**
     * 清除用户信息
     */
    public void clearUserInfo(String id){
        clear();
        RadarSearchManager.getInstance().setUserID(id);
        RadarSearchManager.getInstance().clearUserInfo();
        currentRadarUser = null;

    }


    /**
     * 关闭周边雷达服务
     */
    public void close(){
        CurrentContext = null;
        radarManager = null;
        clear();
        //移除监听
        RadarSearchManager.getInstance().removeNearbyInfoListener(radarSearchListener);
        //释放资源
        RadarSearchManager.getInstance().destroy();
    }

    private String parseRadarUserToComment(RadarUser user){
        String string;
        if(user != null){
            string = "name:" + user.name.trim()
                    +separator+ "sex:"+user.sex.toString().trim()
                    +separator+ "searchType:"+user.requsetOption.searchType.toString()
                    +separator+ "searchSex:"+user.requsetOption.searchSex
                    +separator+ "message:" + user.requsetOption.message.trim()
                    +separator+ "date:"+user.requsetOption.date;
        }else{
            string = "name:无"
                    +separator+ "sex:female"
                    +separator+ "searchType:all"
                    +separator+ "searchSex:all"
                    +separator+ "message:无备注信息"
                    +separator+ "date:无";
        }
        return string;
    }

    private RadarUser parserCommentToRadarUser(String id,String comment){
        RadarUser user = new RadarUser();
        user.id = id;
        if(id.equals("nullUser")){
            return new RadarUser();
        }
        String strs[] = comment.split(separator);

        user.name = strs[0].substring(strs[0].indexOf(":")+1);
        user.sex = User.Sex.valueOf(strs[1].substring(strs[1].indexOf(":")+1));
        user.requsetOption.searchType = RadarRequsetOption.SearchType.valueOf(
                strs[2].substring(strs[2].indexOf(":")+1));
        user.requsetOption.searchSex = strs[3].substring(strs[3].indexOf(":")+1);
        user.requsetOption.message = strs[4].substring(strs[4].indexOf(":")+1);
        user.requsetOption.date = strs[5].substring(strs[5].indexOf(":")+1);
        return user;
    }

    /**
     * 获取默认查询参数
     * @return
     */
    private RadarNearbySearchOption getDefaultOption(){
        Date end = new Date();
        Date start = new Date();
        start.setTime(end.getTime()-3600000/6);
        RadarNearbySearchOption option = new RadarNearbySearchOption();
        option
                .sortType(RadarNearbySearchSortType.time_from_recent_to_past)
                .radius(1000)
                .pageNum(0)
               // .timeRange(start,end)
        ;
        return  option;
    }

    /**
     * 在查询结果中筛选
     * @param result
     */
    private void parseSearchResult(RadarNearbyResult result){
        ArrayList<RadarNearbyInfo> resultList = (ArrayList<RadarNearbyInfo>) result.infoList;
        RadarUser radarUser;UserMarker userMarker;
        int i = 10 - userMarksList.size();
        for(RadarNearbyInfo info:resultList){//遍历所有结果
            if(i < 1) break;
            radarUser = parserCommentToRadarUser(info.userID,info.comments);
            if(isFit(radarUser)){
                userMarker = new UserMarker(radarUser,info.pt);
                userMarksList.add(userMarker);
                i--;
            }
        }
        if(i > 0){//结果数未达到要求数量
            if(result.pageIndex < result.pageNum-1){//当前页不是最后一页
                searchOption.pageNum(result.pageIndex+1);
                RadarSearchManager.getInstance().nearbyInfoRequest(searchOption);
            }else{
                Message message = Message.obtain();
                message.obj = userMarksList;
                handler_result.sendMessage(message);
            }
        }else{
            Message message = Message.obtain();
            message.obj = userMarksList;
            handler_result.sendMessage(message);
        }
    }

    private boolean isFit(RadarUser radarUser){
        boolean b = false;
        if(radarUser.id.equals("nullUser")) {//无效用户信息
            return false;
        }
        if(currentRadarUser != null){//有当前用户
            if(currentRadarUser.requsetOption.searchType == RadarRequsetOption.SearchType.all){//所有类型
                if(!currentRadarUser.requsetOption.searchSex.equals("all")){//有性别筛选
                    if(currentRadarUser.requsetOption.searchSex.equals("male")
                            && radarUser.sex == User.Sex.male){
                        if(currentRadarUser.requsetOption.date.equals(radarUser.requsetOption.date)){
                            //约会日期匹配
                            b = true;
                        }
                    }else if(currentRadarUser.requsetOption.searchSex.equals("female")
                            && radarUser.sex == User.Sex.female){
                        if(currentRadarUser.requsetOption.date.equals(radarUser.requsetOption.date)){
                            //约会日期匹配
                            b = true;
                        }
                    }
                }else{//无性别筛选
                    if(currentRadarUser.requsetOption.date.equals(radarUser.requsetOption.date)){
                        //约会日期匹配
                        b = true;
                    }
                }
            }else{//有类型筛选
                if(currentRadarUser.requsetOption.searchType == radarUser.requsetOption.searchType){//有类型筛选
                    if(!currentRadarUser.requsetOption.searchSex.equals("all")){//有性别筛选
                        if(currentRadarUser.requsetOption.searchSex.equals("male")
                                && radarUser.sex == User.Sex.male){
                            if(currentRadarUser.requsetOption.date.equals(radarUser.requsetOption.date)){
                                //约会日期匹配
                                b = true;
                            }
                        }else if(currentRadarUser.requsetOption.searchSex.equals("female")
                                && radarUser.sex == User.Sex.female){
                            if(currentRadarUser.requsetOption.date.equals(radarUser.requsetOption.date)){
                                //约会日期匹配
                                b = true;
                            }
                        }
                    }else{//无性别筛选
                        if(currentRadarUser.requsetOption.date.equals(radarUser.requsetOption.date)){
                            //约会日期匹配
                            b = true;
                        }
                    }
                }
            }
        }else{//当前用户为nullUser
            b = true;
        }
        return b;
    }


    /**
     * 开始自动上传
     * @param currentRadarUser 可为null
     */
//    public void uploadAuto(RadarUser currentRadarUser) {
//        isUploadOnce = false;
//        this.currentRadarUser = currentRadarUser;
//        LocationManager.getInstance().requestLocation(handler_withUser);
//    }

    /**
     * 停止自动上传
     *
     */
//    public void stopUploadAuto() {
//        isUploadOnce = true;
//        this.currentRadarUser = null;
//        RadarSearchManager.getInstance().stopUploadAuto();
//    }
}
