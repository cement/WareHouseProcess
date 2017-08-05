package com.changqing.warehouse.db.dto;

import android.util.Log;

import com.changqing.warehouse.bean.CheckInfo;
import com.changqing.warehouse.bean.DataBaseResult;
import com.changqing.warehouse.bean.PickupInfo;
import com.changqing.warehouse.db.DBCQ170Connection;
import com.changqing.warehouse.db.DBCQConnection;
import com.changqing.warehouse.db.DBSetting;
import com.changqing.warehouse.db.dao.DBVisitor;

import org.junit.Test;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/7/13 0013.
 */

public class CheckDto {

    public static DataBaseResult<CheckInfo> obtainCheckPckBill(String username,String vehicleNo){

        Connection connection=null;
        CallableStatement callableStatement = null;
        ResultSet resultSet = null;

        DataBaseResult<CheckInfo> dataBaseResult = new DataBaseResult<>();

        String spSql = "{?=call dbo.sp_WhObtainCheck(?,?,?)}";
        try {
            connection = DBCQConnection.getConnectionWithTimeOut();
            if(connection==null){
                dataBaseResult.setResultBoolean(false);
                dataBaseResult.setResultError("无法连接到服务器");
                return dataBaseResult;
            }
            connection.setAutoCommit(false);
            callableStatement = connection.prepareCall(spSql);
            callableStatement.setQueryTimeout(DBSetting.STATEMENT_TIMEOUT);
            callableStatement.registerOutParameter(1, Types.INTEGER);
            callableStatement.setString(2, username);
            callableStatement.setString(3, vehicleNo);
            callableStatement.registerOutParameter(4,Types.VARCHAR);
            resultSet = callableStatement.executeQuery();
            connection.commit();
            //callableStatement.getUpdateCount()==-1&&callableStatement.getMoreResults()==true
            while (resultSet.next()){
                CheckInfo checkInfo = new CheckInfo();
                String  pckBillno = resultSet.getString("pckBillno");
                checkInfo.setPckBillno(pckBillno);
                String xhbilllno = resultSet.getString("billno");
                checkInfo.setXHBillno(xhbilllno);
                String comid = resultSet.getString("comid");
                checkInfo.setComid(comid);
                String comName = resultSet.getString("comname");
                checkInfo.setComname(comName);
                String  LocName = resultSet.getString("LocName");
                checkInfo.setLocName(LocName);
                String  InspSeqNo = resultSet.getString("InspSeqNo");
                checkInfo.setInspSeqNo(InspSeqNo);
                String  quantity = resultSet.getString("quantity");
                checkInfo.setQuantity(quantity);
                String shippingArea = resultSet.getString("shippingArea");
                checkInfo.setShippingArea(shippingArea);

                String unit = resultSet.getString("unit");
                checkInfo.setUnit(unit);

                String specification = resultSet.getString("specification");
                checkInfo.setSpecification(specification);

                String warehouseCode = resultSet.getString("warehouse_code");
                checkInfo.setWarehouseCode(warehouseCode);

                dataBaseResult.getResultList().add(checkInfo);
            }
            Integer resultInteger = callableStatement.getInt(1);
            String  resultString  = callableStatement.getString(4);
            dataBaseResult.setResultInteger(resultInteger);
            dataBaseResult.setResultString(resultString);
            dataBaseResult.setResultBoolean(true);
        } catch (SQLException e) {
            e.printStackTrace();
            dataBaseResult.setResultError(e.getMessage());
            dataBaseResult.setResultBoolean(false);
            try {
                if (connection != null) connection.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
                dataBaseResult.setResultError(e.getMessage()+"\r\n"+e1.getMessage());
            }

        } finally {
            try {
                if (connection != null) connection.setAutoCommit(true);
                if (connection != null) connection.close();
                if (callableStatement != null)  callableStatement.close();
                if (resultSet != null) resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return dataBaseResult;
    }

    public static DataBaseResult<PickupInfo> submitCheckPckBill(String username,String pckbillno,List<CheckInfo> checkInfos){

        String spSql = "{?=call dbo.sp_WhSubmitCheck(?,?,?)}";
        String remarkSql =
                          " insert into dbo.WhTransferRamark (pckbillno,xhbillno,comid,comname,InspSeqNo,vshippingArea,remarkContent,remarkPerson,transferType)\n" +
                          " values(?,?,?,?,?,?,?,?,2);";
        Connection connection=null;
        CallableStatement callableStatement = null;
        PreparedStatement preparedStatement = null;
       // ResultSet resultSet = null;
        DataBaseResult<PickupInfo> dataBaseResult = new DataBaseResult<>();

        try {
            connection = DBCQConnection.getConnectionWithTimeOut();
            if(connection==null){
                dataBaseResult.setResultBoolean(false);
                dataBaseResult.setResultError("无法连接到服务器");
                return dataBaseResult;
            }
            connection.setAutoCommit(false);
            callableStatement = connection.prepareCall(spSql);
            callableStatement.setQueryTimeout(DBSetting.STATEMENT_TIMEOUT);
            callableStatement.registerOutParameter(1, Types.INTEGER);
            callableStatement.setString(2, username);
            callableStatement.setString(3, pckbillno);
            callableStatement.registerOutParameter(4,Types.VARCHAR);
            boolean b = callableStatement.execute();

            if(checkInfos!=null && !checkInfos.isEmpty()){
                preparedStatement = connection.prepareStatement(remarkSql);
                preparedStatement.setQueryTimeout(DBSetting.STATEMENT_TIMEOUT);
                for (int i =0;i<checkInfos.size();i++){
                    Log.d("======", "submitPickupPckBill() called with: I = [" + i + "]");
                    preparedStatement.setString(1,checkInfos.get(i).getPckBillno());
                    preparedStatement.setString(2,checkInfos.get(i).getXHBillno());
                    preparedStatement.setString(3,checkInfos.get(i).getComid());
                    preparedStatement.setString(4,checkInfos.get(i).getComname());
                    preparedStatement.setString(5,checkInfos.get(i).getInspSeqNo());
                    preparedStatement.setString(6,checkInfos.get(i).getShippingArea());
                    preparedStatement.setString(7,checkInfos.get(i).getRemark());
                    preparedStatement.setString(8,username);
                    preparedStatement.addBatch();
                }
                int[] r1  = preparedStatement.executeBatch();
            }

            connection.commit();
            //callableStatement.getUpdateCount()==-1&&callableStatement.getMoreResults()==true

            Integer resultInteger = callableStatement.getInt(1);
            String  resultString  = callableStatement.getString(4);
            dataBaseResult.setResultInteger(resultInteger);
            dataBaseResult.setResultString(resultString);
            dataBaseResult.setResultBoolean(true);

        } catch (SQLException e) {
            e.printStackTrace();
            dataBaseResult.setResultError(e.getMessage());
            dataBaseResult.setResultBoolean(false);
            try {
                if (connection != null) connection.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
                dataBaseResult.setResultError(e.getMessage()+"\r\n"+e1.getMessage());
            }

        } finally {
            try {
                if (connection != null) connection.setAutoCommit(true);
                if (connection != null) connection.close();
                if (callableStatement != null)  callableStatement.close();
               // if (resultSet != null) resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return dataBaseResult;
    }

    @Test
    public static List<Map<String,String>> queryXhbill(String vehicleno, String basketno) throws Exception {

         String fnSql = "select * from dbo.fn_whcheck_queryXhbill(?,?)";
         return DBVisitor.select(fnSql,vehicleno,basketno);

    }
}
