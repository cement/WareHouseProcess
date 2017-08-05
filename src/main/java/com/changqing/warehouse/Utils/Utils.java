package com.changqing.warehouse.Utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.changqing.warehouse.app.App;
import com.changqing.warehouse.mian.PickupActivity;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017/6/14 0014.
 */

public class Utils {

    public static void toggleSoftInputBord(){
        InputMethodManager inputMethodManager = (InputMethodManager) App.getAppContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public static void hideSoftInputBord(View view){
        InputMethodManager imm = (InputMethodManager) App.getAppContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        //imm.showSoftInput(view,InputMethodManager.SHOW_FORCED);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0); //强制隐藏键盘


    }

    public static boolean isNumber(String str) {
        String regex = "^\\d+\\.?\\d*$";
        return match(regex, str);
    }

    private static boolean match(String regex, String str) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }



    public void disableInputBoard(EditText editText) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            editText.setShowSoftInputOnFocus(false);
        }else
        if(Build.VERSION.SDK_INT <= 10) {
            editText.setInputType(InputType.TYPE_NULL);
        } else {
            Class<? extends  EditText>  cls = editText.getClass();
            Method method;
            try {
                method = cls.getMethod("setShowSoftInputOnFocus", boolean.class);
                method.setAccessible(true);
                method.invoke(editText,false);
            } catch(Exception e) {//
                e.printStackTrace();
            }
            try {
                method = cls.getMethod("setSoftInputShownOnFocus", boolean.class);
                method.setAccessible(true);
                method.invoke(editText, false);
            } catch(Exception e) {//
                e.printStackTrace();
            }
        }
    }

    // 隐藏系统键盘
    public static void hideSoftInputMethod(EditText ed){
        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        int currentVersion = android.os.Build.VERSION.SDK_INT;
        String methodName = null;
        if(currentVersion >= 16){
            // 4.2
            methodName = "setShowSoftInputOnFocus";
        }
        else if(currentVersion >= 14){
            // 4.0
            methodName = "setSoftInputShownOnFocus";
        }

        if(methodName == null){
            ed.setInputType(InputType.TYPE_NULL);
        }
        else{
            Class<? extends  EditText> cls = ed.getClass();
            Method setShowSoftInputOnFocus;
            try {
                setShowSoftInputOnFocus = cls.getMethod(methodName, boolean.class);
                setShowSoftInputOnFocus.setAccessible(true);
                setShowSoftInputOnFocus.invoke(ed, false);
            } catch (NoSuchMethodException e) {
                ed.setInputType(InputType.TYPE_NULL);
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();

            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }


    public static String getMD5(String sourceStr) {
        String result = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(sourceStr.getBytes());
            byte b[] = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            result = buf.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void showProgressDialog(Context context){

        ProgressDialog progerssDialog = new ProgressDialog(context);
        progerssDialog.setTitle("正在获取数据...");
        progerssDialog.show();
    }

    public List<Map<String,String>> cursorToListmap(Cursor cursor){
        return  cursorToListmap(cursor,true);
    }
    public List<Map<String,String>> cursorToListmap(Cursor cursor,boolean closeCursor){
        List<Map<String,String>> listMap = new LinkedList<>();
        while (cursor.moveToNext()){
            for(int i=1;i<=cursor.getColumnCount();i++){
                Map<String,String> map = new HashMap<>();
                String key = cursor.getColumnName(i);
                String value = cursor.getString(i);
                map.put(key,value);
                listMap.add(map);
            }
        }
        if(closeCursor){
            cursor.close();
        }
        return listMap;
    }

//    private static int getStatusBarHeight(Context context) {
//        try {
//            Class<?> c = Class.forName("com.android.internal.R$dimen");
//            Object obj = c.newInstance();
//            Field field = c.getField("status_bar_height");
//            int x = Integer.parseInt(field.get(obj).toString());
//            return context.getResources().getDimensionPixelSize(x);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return 0;
//    }


    /**
     * 获取屏幕�?
     */
    public static int getScreenWidth(Activity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    /**
     * 获取屏幕�?
     */
    public static int getScreenHeight(Activity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    /**
     * 获取StatusBar 高度
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            return context.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            Log.d("DimensionUtils", "get status bar height fail");
            e1.printStackTrace();
            return 0;
        }
    }

    /**
     * 获取Actionbar 高度
     * @param context
     * @return
     */
    public static int getActionBarHeight(Context context){
        // Calculate ActionBar height
        TypedValue tv = new TypedValue();
        int actionBarHeight = 0;
        if (context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight  = TypedValue.complexToDimensionPixelSize(tv.data, context.getResources().getDisplayMetrics());
        }
        return actionBarHeight;
    }

    public static String doGetRequest(String url) {
        String result = "";
        InputStream inStream = null;
        HttpURLConnection conn = null;
        try {

            URL realUrl = new URL(url);
            // 打开和URL之间的连�?
            conn = (HttpURLConnection) realUrl.openConnection();
            // 设置通用的请求属�?
//            conn.setRequestProperty("accept", "*/*");
//            conn.setRequestProperty("connection", "Keep-Alive");
//            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
            // 建立实际的连�?
            conn.connect();

           if(conn.getResponseCode()==HttpURLConnection.HTTP_OK){
                inStream = conn.getInputStream();
                byte[] bytes = new byte[1024];
                int readed = 0;
                StringBuffer sbuf = new StringBuffer();
                while ((readed = inStream.read(bytes)) != -1) {
                    sbuf.append(new String(bytes, 0, readed, "utf-8"));
                }
                result=sbuf.toString();
            }

        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入�?
        finally {
            try {
                if (inStream != null) inStream.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

    public static String sendPost(String url, String content) {
        OutputStream outStream = null;
        InputStream inStream = null;
        String result = null;
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连�?
            HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
            // 设置通用的请求属�?
            //conn.setRequestProperty("Accept-Charset", "UTF-8");
            //conn.setRequestProperty("ContentType", "text/plain;charset:UTF-8");
            //conn.addRequestProperty("Cookie",cookies.get(0).toString());
            // conn.setRequestProperty("accept", "*/*");
            // conn.setRequestProperty("connection", "Keep-Alive");
            // conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible;
            // MSIE 6.0; Windows NT 5.1; SV1)");
            conn.setConnectTimeout(20000);
            conn.setReadTimeout(10000);
            // 发�?�POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);

            // 获取URLConnection对象对应的输出流
            outStream = conn.getOutputStream();
            // 发�?�请求参�?
            outStream.write(content.getBytes());
            // flush输出流的缓冲
            outStream.flush();

            System.out.printf("编码:%s \n", conn.getContentEncoding());
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                // 定义BufferedReader输入流来读取URL的响�?
                inStream = conn.getInputStream();

                byte[] bytes = new byte[1024];
                int readed = 0;
                StringBuffer sbuf = new StringBuffer();
                while ((readed = inStream.read(bytes)) != -1) {
                    sbuf.append(new String(bytes, 0, readed, "UTF-8"));
                }

                result = sbuf.toString();
                System.out.println(result);
            }

        } catch (Exception e) {
            System.out.println("发送POST请求出现异常" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输出流�?�输入流
        finally {
            try {
                if (outStream != null) {
                    outStream.close();
                }
                if (inStream != null) {
                    inStream.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

}
