package cn.ucai.fulicenter.dao;

import android.content.Context;

import cn.ucai.fulicenter.bean.User;

/**
 * Created by Administrator on 2016/10/24.
 */
public class UserDao {
    public static final String TABLE_USER_NAME = "t_superwechat_user";
    public static final String TABLE_COLUMN_NAME = "m_user_name";
    public static final String TABLE_COLUMN_NICK = "m_user_nick";
    public static final String TABLE_COLUMN_AVATAR_ID = "m_user_avatar_id";
    public static final String TABLE_COLUMN_AVATAR_TYPE = "m_user__avatar_type";
    public static final String TABLE_COLUMN_AVATAR_PATH = "m_user_avatar_path";
    public static final String TABLE_COLUMN_AVATAR_SUFFIX = "m_user_avatar_suffix";
    public static final String TABLE_COLUMN_AVATAR_LASTUPDATE_TIME = "m_user_avatar_lastupdate_time";
    DBManager dbManager;
    public UserDao(Context context) {
        dbManager = DBManager.onInit(context);
    }
    public boolean saveUser(User user){
        return dbManager.saveUser(user);
    }
    public User getUser(String username){
        return dbManager.getUser(username);
    }
    public boolean updateUser(User user){
        return dbManager.updateUser(user);
    }
}

