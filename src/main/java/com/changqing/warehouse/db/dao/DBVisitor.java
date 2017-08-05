package com.changqing.warehouse.db.dao;

import android.text.TextUtils;
import android.util.Log;

import com.changqing.warehouse.db.DBCQConnection;

import java.net.ConnectException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/6/30 0030.
 */

public class DBVisitor{

    public static List<Map<String,String>> select(String sql, String... params) throws SQLException, ConnectException, ClassNotFoundException {
        return  select(sql, Arrays.asList(params));
    }
    public static List<Map<String,String>> select(String sql, List<String> params) throws ConnectException, SQLException, ClassNotFoundException {
        Connection connection = null;
        PreparedStatement preStatement = null;
        ResultSet resultSet = null;

        List<Map<String,String>> listMap = new ArrayList<>();

        try {
            connection = DBCQConnection.getConnectionWithTimeOut();
            if(connection==null){
                throw new ConnectException("无法连接到服务器");
            }
            preStatement = connection.prepareStatement(sql);
            if(params!=null){
                for(int i = 0; i<params.size();i++){
                    preStatement.setString(i+1,params.get(i));
                }
            }
            resultSet  = preStatement.executeQuery();
            ResultSetMetaData metadata = resultSet.getMetaData();
            while (resultSet.next()){
                Map<String,String> map = new HashMap<>();
                for(int i=1;i<=metadata.getColumnCount();i++){
                    String key = metadata.getColumnLabel(i);
                    String values = resultSet.getString(i);
                    map.put(key,values);
                    Log.d("--->", "select() called with: sql = [" + key + "], params = [" + values + "]");
                }
                listMap.add(map);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }finally {
            try {
                if (connection != null) connection.close();
                if (preStatement != null)  preStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }


        return listMap;
    }



    public static int change(String sql, String... params) throws SQLException, ClassNotFoundException {
        return change(sql,Arrays.asList(params));
    }
    public static int change(String sql, List<String> params) throws SQLException, ClassNotFoundException {
        Connection connection = null;
        PreparedStatement preStatement = null;
        ResultSet resultSet = null;
        int result = 0;
        //List<Map<String,String>> listMap = new ArrayList<>();
        try {
            connection = DBCQConnection.getConnection();
            preStatement = connection.prepareStatement(sql);
            if(params!=null){
                for(int i = 0; i<params.size();i++){
                    preStatement.setString(i+1,params.get(i));
                }
            }


            result  = preStatement.executeUpdate();


        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }finally {
            try {
                if (connection != null) connection.close();
                if (preStatement != null)  preStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        return result;
    }

//    public static int updata(String sql, List<String> params) throws SQLException {
//        Connection connection = null;
//        PreparedStatement preStatement = null;
//        int result = 0;
//        try {
//            connection = DBCQConnection.getConnection();
//            preStatement = connection.prepareStatement(sql);
//            if(params!=null){
//                for(int i = 0; i<params.size();i++){
//                    preStatement.setString(i+1,params.get(i));
//                }
//            }
//            result  = preStatement.executeUpdate();
//
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//            throw e;
//        }finally {
//            try {
//                if (connection != null) connection.close();
//                if (preStatement != null)  preStatement.close();
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//
//        }
//        return result;
//    }
//    public static int delete(String sql, List<String> params) throws SQLException {
//        Connection connection = null;
//        PreparedStatement preStatement = null;
//        int result = 0;
//        try {
//            connection = DBCQConnection.getConnection();
//            preStatement = connection.prepareStatement(sql);
//            if(params!=null){
//                for(int i = 0; i<params.size();i++){
//                    preStatement.setString(i+1,params.get(i));
//                }
//            }
//            result  = preStatement.executeUpdate();
//        } catch (SQLException e) {
//            e.printStackTrace();
//            throw e;
//        }finally {
//            try {
//                if (connection != null) connection.close();
//                if (preStatement != null)  preStatement.close();
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//
//        }
//
//        return result;
//    }

}
