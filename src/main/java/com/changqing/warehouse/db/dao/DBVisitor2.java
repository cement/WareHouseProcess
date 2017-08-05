package com.changqing.warehouse.db.dao;

import android.support.annotation.Nullable;
import android.util.Log;

import com.changqing.warehouse.bean.Table;
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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/6/30 0030.
 */

public class DBVisitor2 {



    public static Table query(Connection connection, String sql, @Nullable String... params) throws SQLException {
        List<String> p = (params==null) ? null:Arrays.asList(params);
        return query(connection,sql,p);
    }

    public static Table query(Connection connection, String sql, @Nullable List<String> params) throws SQLException {
        PreparedStatement preStatement = null;
        ResultSet resultSet = null;
        ResultSetMetaData rsMetedata = null;
        Table table = new Table();

        try {
            preStatement = connection.prepareStatement(sql);
            if(params!=null){
                for(int i = 0; i<params.size();i++){
                    preStatement.setString(i+1,params.get(i));
                }
            }
            resultSet  = preStatement.executeQuery();

            if (resultSet.next()) {
                rsMetedata = resultSet.getMetaData();
                List<String> header = new LinkedList<>();

                for (int i = 1; i <=rsMetedata.getColumnCount(); i++) {
                    String lavel = rsMetedata.getColumnLabel(i);
                    header.add(i-1,lavel);
                }
                table.addHeader(header);
                do{
                    List<String> rowList = new LinkedList<>();
                    for(int i=1;i<=rsMetedata.getColumnCount();i++){
                        String values = resultSet.getString(i);
                        rowList.add(i-1,values);
                    }
                    table.addRow(rowList);
                }while(resultSet.next());
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }finally {
            try {
                if (connection != null) connection.close();
                if (preStatement != null)  preStatement.close();
                if (resultSet !=null) resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
                throw e;
            }
       }
        return table;
    }


    public static int updata(Connection connection,String sql, String... params) throws SQLException {
        return updata(connection,sql,Arrays.asList(params));
    }
    public static int updata(Connection connection,String  sql, List<String> params) throws SQLException{
        PreparedStatement preStatement = null;
        int result = 0;
        try {
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
                throw e;
            }

        }
        return result;
    }

}
