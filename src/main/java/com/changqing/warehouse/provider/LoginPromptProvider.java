package com.changqing.warehouse.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.changqing.warehouse.db.dao.LoginPromptDao;

import java.util.Arrays;

/**
 * Created by Administrator on 2017/6/27 0027.
 */

public class LoginPromptProvider extends ContentProvider{

    public static  UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);


    static {
        matcher.addURI("com.changqing.warehouse.provider.LoginPromptProvider","logprompt",1000);

    }

    private LoginPromptDao promptDao;

    @Override
    public boolean onCreate() {





        promptDao = new LoginPromptDao(getContext(),"WareHouse_login");
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Log.d("---TAG---", "query() called with: uri = [" + uri + "], projection = [" + Arrays.asList(projection) + "], selection = [" + selection + "], selectionArgs = [" + selectionArgs + "], sortOrder = [" + sortOrder + "]");
        Cursor cursor = promptDao.query(projection,selection,selectionArgs,sortOrder);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
//        SQLiteDatabase db =  SQLiteDatabase.openOrCreateDatabase(getContext().getFilesDir()+"WareHouse",null);
//        long rowId  = db.insert("WareHouse",null,values);
//        db.close();
        long rowId = promptDao.insert(values);
        return ContentUris.withAppendedId(uri,rowId);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
