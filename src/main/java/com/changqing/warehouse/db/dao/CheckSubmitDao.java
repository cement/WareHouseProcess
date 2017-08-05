package com.changqing.warehouse.db.dao;

import android.text.format.DateFormat;
import android.util.Log;

import com.changqing.warehouse.app.App;
import com.changqing.warehouse.bean.CheckInfo;
import com.changqing.warehouse.bean.DBResult;
import com.changqing.warehouse.db.DBCQConnection;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Date;
import java.util.LinkedList;

/**
 * Created by YSH on 2017/6/7 0007.
 */

    /* 1.	发货 确认   (销售明细表：storeoutdetail)
        初始查询条件（状态字段Inuse <>0,流程字段 Audit=0 ）
        更新发货员姓名 WhPerson
        更新发货时间 WhDate
        更新发货意见 WhRemark
        更新流程字段 Audit=1




     2.	复核 确认    (销售明细表：storeoutdetail)
        初始查询条件（状态字段Inuse <>0,流程字段 Audit=1 ）
        更新复核员姓名 CheckPerson
        更新复核时间 CheckDate
        更新复核意见 CheckRemark
        更新状态字段inuse=0流程字段 Audit=2  （销售流程完毕）*/

      /*  update storeoutdetail set bs =NULL  where billno in(select billno from storeout where pckbillno='PA201611300158')

        update storeoutdetail set bs =1 from storeoutdetail a join storeout b on a.billno=b.billno where pckbillno='PA201611300158'


        update storeoutdetail set bs =1 from storeout a,storeoutdetail b where a.billno=b.billno and a.pckbillno='PA201611300158'*/

public class CheckSubmitDao {


    public DBResult checkSubmit(String pckBillno,String chehao) {

//        String updateState =
//                    " update storeoutdetail set audit = 1, WhPerson=?,WhDate=getdate() \n" +
//                    " from storeoutdetail a join storeout b on a.billno=b.billno where pckbillno=?\n";
//        String updatebind ="update dbo.WhTransferVehicle \n" +
//                    " SET storeout_pckBillno=null,usingPerson=null,bindTime=null \n" +
//                    " where storeout_pckBillno=? and number =?";

        String spSql = "{?=call dbo.sp_WhConfirmCheck(?,?,?,?)}";
        String sql2 =
                " update dbo.WhTransferVehicle \n" +
                        "SET storeout_pckBillno=null,usingPerson=null,bindTime=null,usingState=0\n" +
                        "where storeout_pckBillno=? and vehicleNo =?";
        String sql3 =
                " update dbo.WhTransferRecord set endTime=getDate() " +
                        "where storeout_pckBillno=? and vehicleNo=?" ;


        Connection connection = null;
        CallableStatement callStatement = null;
        PreparedStatement preStatement = null;
        DBResult dbResult = new DBResult();


        try {
            connection = DBCQConnection.getConnection();
            connection.setAutoCommit(false);
            if(connection!=null) {
                callStatement = connection.prepareCall(spSql);
                callStatement.registerOutParameter(1, Types.INTEGER);
                callStatement.setString(2, pckBillno);
                callStatement.setString(3, App.getUserBean().getUserName());

                callStatement.registerOutParameter(4, Types.VARCHAR);
                callStatement.registerOutParameter(5, Types.VARCHAR);
                callStatement.execute();

                preStatement = connection.prepareStatement(sql2);
                preStatement.setString(1,pckBillno);
                preStatement.setString(1,chehao);
                preStatement.executeUpdate();


                preStatement = connection.prepareStatement(sql3);
                preStatement.setString(1,pckBillno);
                preStatement.setString(1,chehao);
                preStatement.executeUpdate();

                connection.commit();

                int returnInt = callStatement.getInt(1);
                String packPerson = callStatement.getString(4);
                String resultMessage =callStatement.getString(5);
                dbResult.setResultInt(returnInt);
                dbResult.setResultString(packPerson);
                dbResult.setResultMessage(resultMessage);
                dbResult.setResultBoolean(true);

            }
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                connection.rollback();
                dbResult.setResultMessage(e.getMessage());
            } catch (SQLException e1) {
                e1.printStackTrace();
                dbResult.setResultMessage(e1.getMessage());
            }
        }finally {
            try {
                connection.setAutoCommit(true);
                if (connection != null) connection.close();
                if (callStatement != null)  callStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }


        return dbResult;
    }
//pckBillno  LocName  InspSeqNo quantity   vshippingArea   remark
   //调号    货位     验号      数量         集号          区号



}
