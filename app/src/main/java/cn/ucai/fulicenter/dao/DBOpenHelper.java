package cn.ucai.fulicenter.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import cn.ucai.fulicenter.I;

/**
 * Created by Administrator on 2016/10/24.
 */
public class DBOpenHelper extends SQLiteOpenHelper{
    private static final int DATABASE_VERSION = 1;
    private static DBOpenHelper instance;
    private static final String CREATE_USER_TABLE = "";

    public static DBOpenHelper onInit(Context context){
        if (instance==null){
            instance = new DBOpenHelper(context);
        }
        return instance;
    }
    public DBOpenHelper(Context context) {
        super(context, getUserDatabaseName(), null, DATABASE_VERSION);
    }

    private static String getUserDatabaseName() {
        return I.User.TABLE_NAME + "_demo.db";
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_USER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void closeDB(){
        if (instance!=null){
            instance.close();
            instance = null;
        }
    }
}
