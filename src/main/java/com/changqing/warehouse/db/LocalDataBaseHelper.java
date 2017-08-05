package com.changqing.warehouse.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2017/6/27 0027.
 */

public class LocalDataBaseHelper extends SQLiteOpenHelper {

    private static final String dbname = "WareHouse";

    public LocalDataBaseHelper(Context context){
        this(context,dbname,null,1);
    }

    public LocalDataBaseHelper(Context context, String dbname){
        this(context,dbname,null,1);
    }
    public LocalDataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createSql = "create table WareHouse_login ('_id' integer primary key autoincrement,'username' varchar(100),'createTime' TimeStamp NOT NULL DEFAULT (datetime('now','localtime')) )";
        db.execSQL(createSql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
