package com.changqing.warehouse.app;

import android.app.Application;
import android.app.SharedElementCallback;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.SharedPreferencesCompat;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.changqing.warehouse.bean.UserBean;
import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/6/14 0014.
 */

public class App extends Application  implements Serializable{




    public static Context sAppContext;



    public static Gson sGson;





    public static UserBean sUserBean;



    public static RequestQueue sRequestQueue;

    // public static String  sPackOkPerson = "丁艳伟";
    @Override
    public void onCreate() {
        super.onCreate();
        sAppContext = this;
        sRequestQueue = Volley.newRequestQueue(sAppContext);


        sGson = new Gson();
        if(sUserBean==null){
            SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(this);
            String  userInfo = preference.getString("userinfo",null);
            if(userInfo!=null){
                sUserBean = sGson.fromJson(userInfo,UserBean.class);
                Log.d("--application--", "application onCreate() called  sUserBean==null:" +userInfo);
            }
        }
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        Log.d("--application--", "onTrimMemory() called with: level = [" + level + "]");
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if(sUserBean!=null){
            String  userinfo = sGson.toJson(sUserBean);
            SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(this);
            preference.edit().putString("userinfo",userinfo).commit();

            Log.d("----application----", "application onLowMemory() called  sUserBean!=null:" +userinfo);
        }
    }

    public static Context getAppContext() {
        return sAppContext;
    }

    public static UserBean getUserBean() {
        return sUserBean;
    }

    public static void setUserBean(UserBean sUserBean) {
        App.sUserBean = sUserBean;
    }


    public static RequestQueue getRequestQueue() {
        return sRequestQueue;
    }

    public static void setRequestQueue(RequestQueue sRequestQueue) {
        sRequestQueue = sRequestQueue;
    }

    public static Gson getGson() {
        return sGson;
    }
}
