package com.hmxl.yuedemo.db;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.hmxl.yuedemo.bean.User;
import java.util.List;
import cn.bmob.v3.BmobBatch;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BatchResult;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
/**
 * Created by Nate on 2017/5/11.
 */
public class UserManager {
    String TAG = "UserManager";
    // 上传用户信息到数据库中

    public void updateUserInfo(String objectId,String remark,boolean sex,int age,String location,String introduction ){
            User user = new User();
            user.setAge(age);
            user.setRemark(remark);
            user.setIntroduction(introduction);
            user.setSex(sex);
            user.setLocation(location);
            user.update(objectId,new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if(e==null){
                        Log.i("bmob","更新成功");
                    }else{
                        Log.i("bmob","更新失败："+e.getMessage()+","+e.getErrorCode());
                    }
                }
            });
    }



    public void delete(String id){
        User user = new User();
        user.setObjectId(id);
        user.delete(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    Log.i(TAG,"delete success");
                }else{
                    Log.e(TAG,"delete error",e);
                }
            }
        });
    }
    /**
     * 批量删除好友
     * @param templist
     */
    public void delete(List<BmobObject> templist){
        new BmobBatch().updateBatch(templist).doBatch(new QueryListListener<BatchResult>() {
            @Override
            public void done(List<BatchResult> list, BmobException e) {
                if(e==null){
                    for(int i=0;i<list.size();i++){
                        BatchResult result = list.get(i);
                        BmobException ex =result.getError();
                        if(ex==null){
                            Log.i(TAG,i+"delete success");
                        }else{
                            Log.e(TAG,i+"delete error",e);
                        }
                    }
                }else{
                    Log.e(TAG,"delete error",e);
                }
            }
        });
    }

    /**
     * 根据用户id查询好友
     * @return user
     * @param objectid
     */
    public void searchUserById(String objectid){
       // User user = new  User();
        BmobQuery<User> bmobQuery = new BmobQuery<User>();
        bmobQuery.getObject(objectid, new QueryListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if(e==null){
                   Log.i(TAG,"search success");
                }else{
                    Log.i(TAG,"search error",e);
                }
            }
        });
     //   return  user;
    }
    /**
     * 根据备注查询好友,需要在个人信息表中添加备注(Remark),暂时在bmob中未添加
     * @param Remark
     * @return
     */
    public void searchAllUserByRemark(String Remark, final Handler handler){
        User user = new  User();
        BmobQuery<User> bmobQuery = new BmobQuery<User>();
        //返回50条数据，如果不加上这条语句，默认返回10条数据
        //bmobQuery.setLimit(50);
        bmobQuery.addWhereEqualTo("remark", Remark);
        bmobQuery.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if(e==null){
                    Log.i(TAG,"search success"+list.size());
                    System.out.println(list.size());
                    if(handler != null){
                        Message message = Message.obtain();
                        message.what = 1;
                        message.obj = list;
                        handler.sendMessage(message);
                    }
                }else{
                    if(handler != null){
                        Message message = Message.obtain();
                        message.what = 0;
                        handler.sendMessage(message);
                    }
                    Log.e("bmob","error："+e.getMessage()+","+e.getErrorCode());

                }
            }
        });
    }

    /**
     * 图片更新
     * @param objectid
     * @param bmobFile
     */
    public void update_thumbnail(final String objectid,  final BmobFile bmobFile){
        // 2.选择图片，根据图片地址创建bmobFile文件，在服务器数据库中保存的是图片地址
        // 如何根据图片获取地址
        bmobFile.upload(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    Log.i("上传图片","上传成功");

                }else{
                    Log.i("上传图片","上传失败"+e.getMessage()+e.getErrorCode());
                }
            }
        });

    }
}





