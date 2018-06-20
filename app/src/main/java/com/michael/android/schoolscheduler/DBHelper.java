package com.michael.android.schoolscheduler;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;

    public DBHelper(Context context) {
        super(context, "SubjectDB", null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String memoSQL= "create table tb_data " +
                "(_id integer primary key autoincrement,"
                + "subject text,"
                + "name text,"
                + "location text,"
                + "email text,"
                + "number text)";

        db.execSQL(memoSQL);

//        db.execSQL("insert into tb_data (category, location) values ('0', '서울특별시')");
//        db.execSQL("insert into tb_data (category, location) values ('0', '경기도')");
//
//        db.execSQL("insert into tb_data (category, location) values ('서울특별시', '종로구')");
//        db.execSQL("insert into tb_data (category, location) values ('서울특별시', '강남구')");
//        db.execSQL("insert into tb_data (category, location) values ('서울특별시', '송파구')");
//        db.execSQL("insert into tb_data (category, location) values ('서울특별시', '용산구')");
//        db.execSQL("insert into tb_data (category, location) values ('서울특별시', '동작구')");
//        db.execSQL("insert into tb_data (category, location) values ('서울특별시', '영등포구')");
//
//        db.execSQL("insert into tb_data (category, location) values ('경기도', '성남시')");
//        db.execSQL("insert into tb_data (category, location) values ('경기도', '수원시')");
//        db.execSQL("insert into tb_data (category, location) values ('경기도', '용인시')");
//        db.execSQL("insert into tb_data (category, location) values ('경기도', '의정부시')");
//        db.execSQL("insert into tb_data (category, location) values ('경기도', '과천시')");
//        db.execSQL("insert into tb_data (category, location) values ('경기도', '구리시')");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(newVersion==DATABASE_VERSION){
            db.execSQL("drop table tb_data");
            onCreate(db);
        }

    }

}