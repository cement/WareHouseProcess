package com.changqing.warehouse.db.dao;

import com.changqing.warehouse.bean.DataBaseResult;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2017/7/10 0010.
 */

public class DataBaseVisitor {

    public static <T> DataBaseResult<T> query(Connection connection,String sql, Class<T> tClass,String... params) {
        return query(connection,sql,tClass,Arrays.asList(params));
    }
    public static <T> DataBaseResult<T> updata(Connection connection,String sql,String... params){
        return updata(connection,sql,Arrays.asList(params));
    }



    public static <T> DataBaseResult<T> updata(Connection connection,String sql, List<String> params){


        PreparedStatement preStatement = null;
        DataBaseResult<T> dbresult = new DataBaseResult();

        if(connection==null){
            dbresult.setResultMessage(DataBaseResult.NON_CONNECTION);
            return dbresult;
        }
        try {
            preStatement = connection.prepareStatement(sql);
            if(params!=null && params.size()>0){
                for(int i = 0; i<params.size();i++){
                    preStatement.setString(i+1,params.get(i));
                }
            }
            int result  = preStatement.executeUpdate();
            dbresult.setResultInteger(result);
        } catch (SQLException e) {
            e.printStackTrace();
            dbresult.setResultError(e.getMessage());
        }finally {
            try {
                if (connection != null) connection.close();
                if (preStatement != null)  preStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return dbresult;
    }
    public static <T> DataBaseResult<T> query(Connection connection, String sql, Class<T> tClass, List<String> params) {
        //Connection connectionTemp = null;
        PreparedStatement preStatement = null;
        ResultSet resultSet = null;
        DataBaseResult<T> dbresult = new DataBaseResult();
        try {

            if(connection == null){
                dbresult.setResultMessage(dbresult.NON_CONNECTION);
                return dbresult;
            }
            preStatement = connection.prepareStatement(sql);
            if (params != null && params.size()>0) {
                for (int i = 0; i < params.size(); i++) {
                    preStatement.setString(i + 1, params.get(i));
                }
            }
            resultSet = preStatement.executeQuery();
            ResultSetMetaData metadata = resultSet.getMetaData();


            Field[] fields = tClass.getDeclaredFields();
            T tTitle = tClass.newInstance();
            for (int j = 0; j < fields.length; j++) {
                fields[j].set(tTitle, fields[j].getName());
            }
            dbresult.getResultList().add(tTitle);

            while (resultSet.next()) {
                T tRow = tClass.newInstance();
                for (int i = 1; i <= metadata.getColumnCount(); i++) {
                    for (int j = 0; j < fields.length; j++) {
                        if (metadata.getColumnClassName(i).equals(fields[j])) {
                            fields[j].set(tRow, resultSet.getObject(i));
                        }
                    }
                }
               dbresult.getResultList().add(tRow);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            dbresult.setResultError(e.getMessage());
        } catch (InstantiationException e) {
            e.printStackTrace();
            dbresult.setResultError(e.getMessage());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            dbresult.setResultError(e.getMessage());
        } finally {
            try {
                if (connection != null) connection.close();
                if (preStatement != null) preStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
                dbresult.setResultError(e.getMessage());
            }
        }
        return dbresult;
    }


}
