package com.hmxl.yuedemo.db;

import com.hmxl.yuedemo.bean.Friends;

import com.hmxl.yuedemo.tools.exception.MyLog;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobBatch;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BatchResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by Nate on 2017/5/16.
 */

public class FriendManager {
     // 增加好友
    public void addFriend(String user_id,String friend_id){
        Friends friends = new Friends();
        //判断 用户id 是否已经存在并为你的好友
        friends.setUser_id(user_id);
        friends.setFriend_id(friend_id);
        friends.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if(e==null){
                    System.out.println("好友添加成功");

                }else{
                    System.out.println("好友添加失败");
                }
            }
        });
    }
    //删除一个好友
    public void deleteFriend(String user_id,String friend_id){
        Friends friends = new Friends();
        //判断 用户id 是否已经存在并为你的好友
        friends.setUser_id(user_id);
        friends.setFriend_id(friend_id);
        friends.delete(new UpdateListener() {
          @Override
          public void done(BmobException e) {
              if(e==null){
                  System.out.println("好友删除成功");

              }else{
                  System.out.println("好友删除失败");
              }
          }
      });

    }
    // 删除多个好友
    public void deleteFriends(List<BmobObject> templist){
      new BmobBatch().updateBatch(templist).doBatch(new QueryListListener<BatchResult>() {
          @Override
          public void done(List<BatchResult> list, BmobException e) {
                  if(e==null){
                      for(int i=0;i<list.size();i++){
                          BatchResult result = list.get(i);
                          BmobException ex =result.getError();
                          if(ex==null){
                              System.out.println("第"+i+"个数据批量删除成功");
                          }else{
                              System.out.println("第"+i+"个数据批量删除失败："+ex.getMessage()+","+ex.getErrorCode());
                          }
                      }
                  }else{
                      System.out.println("bmob:-->失败："+e.getMessage()+","+e.getErrorCode());
                  }
              }
      });
    }

    //把好友拉入黑名单
    public void addToBlacklist(String user_id,String friend_id){
        // 判断用户是否已经在黑名单了,如果不在则
        Friends friends = new Friends();
        friends.setUser_id(user_id);
        friends.setFriend_id(friend_id);
        friends.setAddToBlacklist(true);
        friends.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    System.out.println("已经添加黑名单了");
                }else{
                    System.out.println("添加黑名单失败");
                }
            }
        });
    }

    // 查找好友(有可能返回一个或者多个好友
    List<Friends> friendlist = new ArrayList<>();
    public List<Friends> searchFriendsByRemark(String remark){
        friendlist.clear();
        BmobQuery<Friends> query = new BmobQuery<Friends>();
        //查询playerName叫“remark”的数据
        query.addWhereEqualTo("friend_remark", remark);
        //返回50条数据，如果不加上这条语句，默认返回10条数据
       // query.setLimit(50);
      //执行查询方法
        query.findObjects(new FindListener<Friends>() {
            @Override
            public void done(List<Friends> object, BmobException e) {
                if(e==null){
                    System.out.println("查询成功：共"+object.size()+"条数据。");
                    for (Friends friend : object) {
                        friend.getObjectId();
                        //获得数据的objectId信息

                        //获得createdAt数据创建时间（注意是：createdAt，不是createAt）
                        friend.getCreatedAt();
                        friendlist.addAll(object);
                    }
                }else{
                    //Log.e("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                    MyLog.e("bmob","request fault",e);
                }
            }
        });
        return friendlist;
    }
    public List<Friends> searchFriendsByName(String name){
        friendlist.clear();
        BmobQuery<Friends> query = new BmobQuery<Friends>();
        //查询playerName叫“remark”的数据
        query.addWhereEqualTo("friend_name", name);
        //返回50条数据，如果不加上这条语句，默认返回10条数据
        // query.setLimit(50);
        //执行查询方法
        query.findObjects(new FindListener<Friends>() {
            @Override
            public void done(List<Friends> object, BmobException e) {
                if(e==null){
                    System.out.println("Success："+object.size()+"tiaoshuju");
                    for (Friends friend : object) {
                        friend.getObjectId();
                        //获得数据的objectId信息

                        //获得createdAt数据创建时间（注意是：createdAt，不是createAt）
                        friend.getCreatedAt();
                        friendlist.addAll(object);
                    }
                }else{
                    MyLog.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
        return friendlist;
    }
}
