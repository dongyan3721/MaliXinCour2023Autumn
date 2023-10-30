package com.example.genshinimpactcookbook.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

/**
 * 工具类 单例模式 （1. 构造函数私有化 2. 对外提供函数）
 */
public class SQLiteOperator extends SQLiteOpenHelper {
    /**
     *
     * @param context
     * @param name 数据库名称
     * @param factory
     * @param version
     * @param errorHandler
     */
    private static SQLiteOpenHelper mInstance;

    public static synchronized SQLiteOpenHelper getInstance(Context context){
        if(mInstance==null){
            mInstance = new SQLiteOperator(context, "xxsStimulate", null, 1);
        }
        return mInstance;
    }

    public SQLiteOperator(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    // 创建表，数据库第一次创建的时候嗲用，只触发一次
    @Override
    public void onCreate(SQLiteDatabase db) {
//        String createXXSHappyCalc = "create table tasks(_id integer primary key autoincrement, num1 text, num2 text, operator text, result text, exceed text)";
        String createRecRight = "create table right_record(_id integer primary key autoincrement, record text)";
//        db.execSQL(createXXSHappyCalc);
        db.execSQL(createRecRight);
        db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
