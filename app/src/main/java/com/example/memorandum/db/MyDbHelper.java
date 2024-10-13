package com.example.memorandum.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class MyDbHelper extends SQLiteOpenHelper {

    private static String DBNAME = "yx.db";
    private static int VERSION = 1;

    public MyDbHelper(@Nullable Context context) {
        super(context,DBNAME,null,VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table tb_memory(_id Integer primary key,title String(200),content String(2000),imgpath String(200),mtime String(50))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
