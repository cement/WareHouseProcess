package com.changqing.warehouse.db;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.android.volley.toolbox.JsonObjectRequest;
import com.changqing.warehouse.Utils.Utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;

public class DBCQ123Connection {

    public static String sa ;
    public static String pa ;
    public static String sqlUrl;
    public static String serverUrl="http://192.168.0.123/SqlServerkodeord";
    static{
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String str = Utils.doGetRequest(serverUrl);
             Map map = (Map)JSON.parse(str);
             sa = (String) map.get("brugernavn");
             pa = (String) map.get("kodeord");
             sqlUrl= (String)map.get("url");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public static Connection getConnection() {
        Connection con = null;
        try {
            con = DriverManager.getConnection(sqlUrl,sa,pa);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("数据库连接失败！");
        }
        return con;
    }
    public static Connection getConnectionWithTimeOut(){
        return  getConnectionWithTimeOut(DBSetting.MANAGER_LOGIN_TIMEOUT);
    }

    public static Connection getConnectionWithTimeOut(int seconds){

        Connection conn = null;
        try {

            DriverManager.setLoginTimeout(seconds);

            conn = DriverManager.getConnection(sqlUrl,sa,pa);

        } catch (SQLException e) {
            e.printStackTrace();

        }
        return conn;

    }

    public static void closeConnection(Connection con) {
        try {
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 开始事务
     * @param cnn
     */
    public static void beginTransaction(Connection cnn){
        if(cnn!=null){
            try {
                if(cnn.getAutoCommit()){
                    cnn.setAutoCommit(false);
                }
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    /**
     * 开始事务
     * @param cnn
     */
    public static void resetTransaction(Connection cnn){
        if(cnn!=null){
            try {
                if(!cnn.getAutoCommit()){
                    cnn.setAutoCommit(true);
                }
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    /**
     * 提交事务
     * @param cnn
     */
    public static void commitTransaction(Connection cnn){
        if(cnn!=null){
            try {
                if(!cnn.getAutoCommit()){
                    cnn.commit();
                }
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    /**
     * 回滚事务
     * @param cnn
     */
    public static void rollBackTransaction(Connection cnn){
        if(cnn!=null){
            try {
                if(!cnn.getAutoCommit()){
                    cnn.rollback();
                }
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }


    //pckBillno  LocName  InspSeqNo quantity   vshippingArea   remark
    //调号         货位     验号      数量         集号          区号



}