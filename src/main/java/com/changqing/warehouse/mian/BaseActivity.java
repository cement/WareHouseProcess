package com.changqing.warehouse.mian;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.print.PrintHelper;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuBuilder;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.changqing.warehouse.R;
import com.changqing.warehouse.app.App;
import com.changqing.warehouse.bean.VideoResult;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.lang.reflect.Method;
import java.text.MessageFormat;

/**
 * Created by Administrator on 2017/6/21 0021.
 */

public class BaseActivity  extends AppCompatActivity{


    protected static final String START_SUCCESS="设备启动-成功启动";
    protected static final String START_OFF_LINE="设备启动-设备离线";
    protected static final String START_UNKNOWN="设备启动-未知原因";
    protected static final String START_ERROR="设备启动-网络错误";
    protected static final String START_RESULT_NON="设备启动-返回空";
    protected static final String STOP_SUCCESS="设备停止-成功停止";
    protected static final String STOP_OFF_LINE="设备停止-设备离线";
    protected static final String STOP_UNKNOWN="设备停止-未知原因";
    protected static final String STOP_ERROR="设备停止-网络错误";
    protected static final String STOP_RESULT_NON="设备停止-返回空";




    private static final String TAG = BaseActivity.class.getSimpleName();
    protected String videoState = STOP_SUCCESS;
    private Toast backToast;


    protected  static final String INUSE_STATE_PICKUPING="pickuping";
    protected  static final String INUSE_STATE_PICKUPED="pickuped";
    protected  static final String INUSE_STATE_CHECKING="checking";
    protected  static final String INUSE_STATE_CHECKED="checked";

    protected String inuseState = INUSE_STATE_CHECKED;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        backToast = Toast.makeText(this, "再按一次您将离开当前操作", Toast.LENGTH_SHORT);
    }

    @Override
    public void onBackPressed() {
        if (backToast != null && backToast.getView() != null && backToast.getView().isShown()) {
            finish();
        } else {
            if(inuseState.equals(INUSE_STATE_PICKUPING)){
                Toast.makeText(this,"当前调度单正在发货尚未提交，强行退出将丢失数据",Toast.LENGTH_SHORT).show();
            } else if(inuseState.equals(INUSE_STATE_CHECKING)){
                Toast.makeText(this,"当前调度单正在复核尚未提交，强行退出将丢失数据",Toast.LENGTH_SHORT).show();
            }
            backToast.show();
        }
    }


    public void  doStopVideo(String DevId,String DispNo){
        String url = "http://192.168.0.246:8000/StopRec?DevId={0}&DispNo={1}";
        url = MessageFormat.format(url,new Object[]{DevId,DispNo});
        Log.d(TAG, "doStopVideo() called with: url = "+url);


        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("--------停止----------"+response);
                //TODO 停止
                if(!TextUtils.isEmpty(response)){
                   VideoResult result =  App.getGson().fromJson(response, VideoResult.class);
                    if(response.equals("{\"Result\":\"0\"}")){
                        videoState = STOP_SUCCESS;
                        afterStopVideoSuccess();
                        //TODO ......................
                    }else if (response.equals("{\"Result\":\"-1\"}")){
                        videoState = STOP_OFF_LINE;
                    }else if (response.equals("{\"Result\":\"1\"}")){
                        videoState = STOP_UNKNOWN;
                    }

                }else{
                    videoState=STOP_RESULT_NON;
                }

                Log.d(TAG, "onResponse() called with: response = [" + response + "] "+videoState);
                Snackbar.make(getWindow().getDecorView(),videoState,Snackbar.LENGTH_SHORT).show();
                afterStartVideo(videoState);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                videoState=STOP_ERROR;
                requestStopVIdeoError(error.getMessage());
                Log.d(TAG, "onErrorResponse() called with: error = [" + error + "]  "+videoState+"\r\n"+ error.getMessage());
                Snackbar.make(getWindow().getDecorView(),videoState,Snackbar.LENGTH_SHORT).show();
            }
        });

        request.setShouldCache(false);
        request.setTag(getClass().getSimpleName());
        request.setRetryPolicy(new DefaultRetryPolicy(5*1000,0,0f));
        App.getRequestQueue().add(request);
    }
    public void  doStartVideo(String DevId,String DispNo){
        String url = "http://192.168.0.246:8000/StartRec?DevId={0}&DispNo={1}";
        url = MessageFormat.format(url,new Object[]{DevId,DispNo});
        Log.d(TAG, "doStartVideo() called with: url = "+url);
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("--------开始录像----------"+response);
                //TODO 开始获取调度单
                if(!TextUtils.isEmpty(response)){
                    if(response.equals("{\"Result\":\"0\"}")){
                        videoState = START_SUCCESS;
                        //TODO ......................
                        afterStartVideoSuccess();
                    }else if (response.equals("{\"Result\":\"-1\"}")){
                        videoState = START_OFF_LINE;
                    }else if (response.equals("{\"Result\":\"1\"}")){
                        videoState = START_UNKNOWN;
                    }
                }else{
                    videoState = START_RESULT_NON;
                }
                Log.d(TAG, "doStart onResponse() called with: response = [" + response + "] "+videoState);
                Snackbar.make(getWindow().getDecorView(),videoState,Snackbar.LENGTH_SHORT).show();

                afterStopVideo(videoState);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                videoState=START_ERROR;
                requestStartVideoError(error.getMessage());
                Log.d(TAG, "onErrorResponse() called with: error = [" + error + "]  videoState"+videoState+"\r\n"+error.getMessage());
                Snackbar.make(getWindow().getDecorView(),videoState,Snackbar.LENGTH_SHORT).show();
            }
        });

        request.setShouldCache(false);
        request.setTag(getClass().getSimpleName());
        request.setRetryPolicy(new DefaultRetryPolicy(5*1000,0,0f));
        App.getRequestQueue().add(request);
    }

    public void requestStartVideoError(String errorMessage){

    }
    public void requestStopVIdeoError(String errorMessage){

    }
    public void afterStopVideo(String result){

    }
    public void afterStartVideo(String result){

    }
    public void  afterStopVideoSuccess(){
        JsonObjectRequest request;

    }
    public void afterStartVideoSuccess(){


    }


    public void setWindowBackgroundAlpha(float windowAlpha){
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = windowAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.base_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        if(item.getItemId()==R.id.base_print_start){
//
//        }else if(item.getItemId()==R.id.base_cancel_autologin){
//            SharedPreferences userInfoPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
//            userInfoPreferences.edit().putString("userInfo",null).commit();
//            return true;
//        }
        return super.onOptionsItemSelected(item);
    }

    // 显示文字 和 图标
    @Override
    protected boolean onPrepareOptionsPanel(View view, Menu menu) {
        if (menu != null) {
            if (menu.getClass() == MenuBuilder.class) {
                try {
                    Method m = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (Exception e) {
                }
            }
        }
        return super.onPrepareOptionsPanel(view, menu);
    }


}
