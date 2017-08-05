package com.changqing.warehouse.db.dto;

import android.util.Log;

import com.changqing.warehouse.bean.DataBaseResult;
import com.changqing.warehouse.bean.PickupInfo;
import com.changqing.warehouse.db.DBCQ170Connection;
import com.changqing.warehouse.db.DBCQConnection;
import com.changqing.warehouse.db.DBSetting;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

/**
 * Created by Administrator on 2017/7/13 0013.
 */

public class PickupDto {

    public static DataBaseResult<PickupInfo> obtainPickupPckBill(String username, String vehicleNo){

        Connection connection=null;
        CallableStatement callableStatement = null;
        ResultSet resultSet = null;


        DataBaseResult<PickupInfo> dataBaseResult = new DataBaseResult<>();

        String spSql = "{?=call dbo.sp_WhObtainPickup(?,?,?)}";
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
                PickupInfo pickupInfo = new PickupInfo();

                String  pckBillno = resultSet.getString("pckBillno");
                pickupInfo.setPckBillno(pckBillno);

                String xhbilllno = resultSet.getString("billno");
                pickupInfo.setXHBillno(xhbilllno);

                String comid = resultSet.getString("comid");
                pickupInfo.setComid(comid);

                String comName = resultSet.getString("comname");
                pickupInfo.setComname(comName);

                String  LocName = resultSet.getString("LocName");
                pickupInfo.setLocName(LocName);

                String  InspSeqNo = resultSet.getString("InspSeqNo");
                pickupInfo.setInspSeqNo(InspSeqNo);

                String  quantity = resultSet.getString("quantity");
                pickupInfo.setQuantity(quantity);

                String shippingArea = resultSet.getString("shippingArea");
                pickupInfo.setShippingArea(shippingArea);

                String warehouseCode = resultSet.getString("warehouse_code");
                pickupInfo.setWarehouseCode(warehouseCode);

                dataBaseResult.getResultList().add(pickupInfo);
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

    public static DataBaseResult<PickupInfo> submitPickupPckBill(String username,String pckbillno,List<PickupInfo> pickupInfos){

        Connection connection=null;
        CallableStatement callableStatement = null;
        PreparedStatement preparedStatement = null;
       // ResultSet resultSet = null;


        DataBaseResult<PickupInfo> dataBaseResult = new DataBaseResult<>();

        String spSql = "{?=call dbo.sp_WhSubmitPickup(?,?,?)}";

        String remarkSql =
                " insert into dbo.WhTransferRamark (pckbillno,xhbillno,comid,comname,InspSeqNo,vshippingArea,remarkContent,remarkPerson,transferType)\n" +
                " values(?,?,?,?,?,?,?,?,1);";
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
            Log.d("======", "submitPickupPckBill() called with: pickupInfos = [" + pickupInfos + "]");
            if(!pickupInfos.isEmpty()){
                preparedStatement = connection.prepareStatement(remarkSql);
                preparedStatement.setQueryTimeout(DBSetting.STATEMENT_TIMEOUT);
                for (int i =0;i<pickupInfos.size();i++){
                    Log.d("======", "submitPickupPckBill() called with: I = [" + i + "]");
                    preparedStatement.setString(1,pickupInfos.get(i).getPckBillno());
                    preparedStatement.setString(2,pickupInfos.get(i).getXHBillno());
                    preparedStatement.setString(3,pickupInfos.get(i).getComid());
                    preparedStatement.setString(4,pickupInfos.get(i).getComname());
                    preparedStatement.setString(5,pickupInfos.get(i).getInspSeqNo());
                    preparedStatement.setString(6,pickupInfos.get(i).getShippingArea());
                    preparedStatement.setString(7,pickupInfos.get(i).getRemark());
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
}
