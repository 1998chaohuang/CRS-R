package com.example.www44.memorandum;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;

/**
 数据库
 数据库的类
 */

public class NoteDB extends SQLiteOpenHelper {

    public static final String TABLE_NAME = "score";
    public static final String TABLE_USER = "user";
    public static final String tb_userinfo = "tb_userinfo";//后加的
    public static final String SCORE = "total_score";
    public static final String NAME= "name";
    public static final String RESULT = "result";
    public static final String REASON = "reason";
    public static final String ROOM ="room";
    public static final String USERID ="userid";
    public static final String HAPPEN_DAY = "happen";
    public static final String HOSPITAL_DAY = "hospital";
    public static final String ID = "_id";
    public static final String TIME = "time";
    public static final String NUM="num";
    public static final String mDbName =  "jfk.db";

    public static final String name = "name";//后加的
    public static final String pwd = "pwd";//后加的
    public static final String email = "email";//后加的
    public static final String phone = "phone";//后加的
    /*
     *  context 上下文
     *  notes 数据库文件名
     *  null 为游标，null表示使用默认创建游标得方式
     *  1 表示数据库版本号（只可以提高不允许降低）
     *
     */
    public NoteDB(Context context) {
        super(context, mDbName, null, 1);
    }


    //第一次进入程序先创建数据库和数据表
    @Override
    public void onCreate(SQLiteDatabase db) {

        //创建用户表
        db.execSQL("CREATE TABLE " + tb_userinfo + "(" +ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + name +" TEXT NOT NULL,"
                + pwd +" TEXT NOT NULL,"
                + email +" TEXT NOT NULL,"
                + phone + " TEXT NOT NULL)");

        db.execSQL("CREATE TABLE " + TABLE_NAME + "(" +ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + SCORE +" TEXT NOT NULL,"
                + NAME +" TEXT NOT NULL,"
                + USERID +" TEXT NOT NULL,"

                +TIME + " TEXT NOT NULL)");
        //创建病人表
        db.execSQL("CREATE TABLE " + TABLE_USER + "(" +ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + NAME +" TEXT NOT NULL,"
                + NUM +" TEXT NOT NULL,"
                + ROOM +" TEXT NOT NULL,"
                + HAPPEN_DAY +" TEXT NOT NULL,"
                + HOSPITAL_DAY +" TEXT NOT NULL,"
                + RESULT +" TEXT NOT NULL,"
                + REASON +" TEXT NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
