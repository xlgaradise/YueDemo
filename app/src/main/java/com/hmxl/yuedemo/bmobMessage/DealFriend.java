package com.hmxl.yuedemo.bmobMessage;

import com.hmxl.yuedemo.bean.Friend;
import com.hmxl.yuedemo.bean.User;
import java.util.List;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by Nate on 2017/5/22.
 */

public class DealFriend {
    /**
     * 查询好友
     * @param listener
     */
    public void queryFriend(final FindListener<Friend> listener) {
        BmobQuery<Friend> query = new BmobQuery<>();
        User user = BmobUser.getCurrentUser(User.class);
        query.addWhereEqualTo("user", user);
        query.include("friendUser");
        query.order("-updateAt");
        query.findObjects(new FindListener<Friend>() {
            @Override
            public void done(List<Friend> list, BmobException e) {
                if (e == null) {
                    //查询成功
                    if (list != null && list.size() > 0) {
                        listener.done(list, e);
                    } else {
                        listener.done(list, new BmobException(0, "暂无联系人"));
                    }
                } else {
                    //查询失败
                    listener.done(list, e);
                }
            }
        });
    }

    /**
     * 删除好友
     * @param f
     * @param listener
     */
    public void deleteFriend(Friend f, UpdateListener listener) {
        Friend friend = new Friend();
        friend.delete(f.getObjectId(), listener);
    }
    //添加好友,Demo中创建了一个NewFriend的本地数据库类用来存储所有的添加好友请求


}
