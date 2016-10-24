package cn.ucai.fulicenter.dao;

import android.content.Context;

import cn.ucai.fulicenter.bean.User;

/**
 * Created by Administrator on 2016/10/24.
 */
public class DBManager {
    private static DBManager dbMgr = new DBManager();
    private static DBOpenHelper mHelper;

    public DBManager() {

    }
    public static DBManager onInit(Context context){
        if (mHelper==null){
            mHelper = DBOpenHelper.onInit(context);
        }
        return dbMgr;
    }
    public static void closeDB(){
        if (mHelper!=null){
            mHelper.closeDB();
        }
    }
    public boolean saveUser(User user){

        return false;
    }

    public User getUser(String username){
        return null;
    }

    public boolean updateUser(User user){
        return false;
    }
}
