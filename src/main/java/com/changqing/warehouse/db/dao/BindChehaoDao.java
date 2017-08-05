package com.changqing.warehouse.db.dao;

import android.util.Log;

import com.changqing.warehouse.app.App;
import com.changqing.warehouse.db.DBCQConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/6/29 0029.
 */

public class BindChehaoDao {




    public  int bindChehao(String chehao,String pckbillno,String type){
        Log.d(".---》：", "bindChehao() called with: chehao = [" + chehao + "], pckbillno = [" + pckbillno + "]");

        String sql =
                "update dbo.WhTransferVehicle \n" +
                "SET storeout_pckBillno=?,usingPerson=?,usingState=1,bindTime=getdate() \n" +
                "where 1=1 and vehicleNo =?";

        String recordSql =
                " insert into dbo.WhTransferRecord (vehicleNo,storeout_pckbillno,operator,beginTime,transferType) " +
                " values(?,?,?,getdate(),?)";

//        String sss = "insert into dbo.WhTransferRecord(vehicleNo,storeout_pckbillno,operator,beginTime,transferType)" +
//                    " values(?,?,?,getdate(),1)";

        Connection connection = null;
        PreparedStatement preStatement = null;
        int  result = -1;


        try {
            connection = DBCQConnection.getConnection();
            if(connection!=null) {

                connection.setAutoCommit(false);
                preStatement = connection.prepareStatement(sql);
                preStatement.setString(1,pckbillno);
                preStatement.setString(2,App.getUserBean().getUserName());
                preStatement.setString(3, chehao);
                result = preStatement.executeUpdate();
                if(result>0){
                    preStatement = connection.prepareStatement(recordSql);
                    preStatement.setString(1,chehao);
                    preStatement.setString(2,pckbillno);
                    preStatement.setString(3,App.getUserBean().getUserName());
                    preStatement.setString(4,type);
                    result = preStatement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }finally {
            try {
                connection.setAutoCommit(true);
                if (connection != null) connection.close();
                if (preStatement != null)  preStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        Log.d("--->>>>", "bindChehao: "+result);
        return result;
    }


    public  int unBindChehao(String chehao,String pckbillno) {

        String sql =
                "update dbo.WhTransferVehicle \n" +
                "SET storeout_pckBillno=null,usingPerson=null,bindTime=null,usingState=0\n" +
                "where storeout_pckBillno=? and vehicleNo =?";
        String recordSql =
                " update dbo.WhTransferRecord set endTime=getDate() " +
                "where vehicleNo=? and storeout_pckbillno=?" ;


        Connection connection = null;
        PreparedStatement preStatement = null;
        int  result = -1;


        try {
            connection = DBCQConnection.getConnection();
            if(connection!=null) {
                preStatement = connection.prepareStatement(sql);
                preStatement.setString(1,pckbillno);
                preStatement.setString(2, chehao);
                result = preStatement.executeUpdate();
                if(result>0){
                    preStatement = connection.prepareStatement(recordSql);
                    preStatement.setString(1,chehao);
                    preStatement.setString(2,pckbillno);
                   // preStatement.setString(3,App.getUserBean().getUserName());
                    result = preStatement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
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

   public String getChehaoByPckBillno(String param) {
        String sql = "select vehicleNo from dbo.WhTransferVehicle where storeout_pckbillno= ? ";
        Connection connection = null;
        PreparedStatement preStatement = null;
        String result = null;

       Log.d("--===----", "----------------- param = [" + param + "]");
        try {
            connection = DBCQConnection.getConnection();
            if(connection!=null) {
                preStatement = connection.prepareStatement(sql);
                preStatement.setString(1,param);

                ResultSet  resultSet = preStatement.executeQuery();
                if(resultSet.next()){
                    result = resultSet.getString("vehicleNo");
                }
                Log.d("--===----", "----------------- result = [" + result + "]");
            }
        } catch (SQLException e) {
            e.printStackTrace();
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

    public String getPckBillnoByChehao(String param) {
        String sql = "select storeout_pckbillno from dbo.WhTransferVehicle where vehicleNo= ? ";
        Connection connection = null;
        PreparedStatement preStatement = null;
        String result = null;

        try {
            connection = DBCQConnection.getConnection();
            if(connection!=null) {
                preStatement = connection.prepareStatement(sql);
                preStatement.setString(1,param);

                ResultSet  resultSet = preStatement.executeQuery();
                if(resultSet.next()){
                    result = resultSet.getString("storeout_pckbillno");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
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
}
