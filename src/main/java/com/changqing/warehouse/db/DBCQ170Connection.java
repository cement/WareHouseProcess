package com.changqing.warehouse.db;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.changqing.warehouse.Utils.Utils;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;

public class DBCQ170Connection {


    public static Map map ;
    public static String sa ="sa";
    public static String pa = "storeoutdetailcq";
    public static String sqlUrl ="jdbc:sqlserver://192.168.0.170:1433;DatabaseName=udoyuzhengbo";
    public static String serverUrl="http://www.cqyypt.cn/SqlServerkodeord";
    static{
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
//            String str = Utils.doGetRequest(serverUrl);
//            Log.d("===123===", "static initializer() called");
//            Log.d("===123===str", str);
//            map = (Map) JSON.parse(str);
//            Log.d("===123===map", map.toString());
//            sa = (String) map.get("brugernavn");
//            pa = (String) map.get("kodeord");
//            sqlUrl= (String)map.get("url");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }



    }

    public static Connection getConnection() {

        Connection con = null;
        try {
//            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            con = DriverManager.getConnection(sqlUrl, sa, pa);
            //Connection con = DriverManager.getConnection("jdbc:sqlserver://221.1.104.252:1433;DatabaseName=UdoDBCQ", "sa", "storeoutdetailcq");
            DriverManager.setLoginTimeout(10);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return con;
    }
    public static Connection getConnectionWithTimeOut() {
        return getConnectionWithTimeOut(DBSetting.MANAGER_LOGIN_TIMEOUT);
    }

    public static Connection getConnectionWithTimeOut(int seconds) {

        Connection con = null;
        try {
           // Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            DriverManager.setLoginTimeout(seconds);

            con = DriverManager.getConnection(sqlUrl, sa, pa);
            //Connection con = DriverManager.getConnection("jdbc:sqlserver://192.168.0.170:1433;DatabaseName=udoyuzhengbo", "sa", "storeoutdetailcq");
            //Connection con = DriverManager.getConnection("jdbc:sqlserver://221.1.104.252:1433;DatabaseName=UdoDBCQ", "sa", "storeoutdetailcq");


        } catch (SQLException e) {
            e.printStackTrace();

        }

       return con;
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
    public static void beginTransaction(Connection cnn) {
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
    public static void resetTransaction(Connection cnn)  {
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
    public static void commitTransaction(Connection cnn) {
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
    public static void rollBackTransaction(Connection cnn) {
        if(cnn!=null){
            try {
                if(!cnn.getAutoCommit()){
                    cnn.rollback();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    //pckBillno  LocName  InspSeqNo quantity   vshippingArea   remark
    //调号         货位     验号      数量         集号          区号



}