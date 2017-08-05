package com.changqing.warehouse.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.changqing.warehouse.db.LocalDataBaseHelper;

/**
 * Created by Administrator on 2017/6/27 0027.
 */

public class LoginPromptDao  {

    private LocalDataBaseHelper dataBaseHelper;
    private String tableName;
    public LoginPromptDao(Context context,String tableName) {
        dataBaseHelper = new LocalDataBaseHelper(context);
        this.tableName = tableName;
    }

    public Cursor query(String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        SQLiteDatabase database =dataBaseHelper.getReadableDatabase();
        Cursor cursor = database.query(tableName,projection,selection,selectionArgs,null,null,sortOrder,"10");
        //database.close();
        return cursor;
    }


    public long insert(ContentValues values) {
        SQLiteDatabase database =dataBaseHelper.getReadableDatabase();
        long result = database.insert(tableName,null,values);
        database.close();
        return  result;

    }
    public int update(ContentValues values, String selection,String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public int delete(String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }



}
