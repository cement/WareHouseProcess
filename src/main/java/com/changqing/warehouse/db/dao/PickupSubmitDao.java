package com.changqing.warehouse.db.dao;

import android.util.Log;

import com.changqing.warehouse.app.App;
import com.changqing.warehouse.bean.DBResult;
import com.changqing.warehouse.bean.PickupInfo;
import com.changqing.warehouse.db.DBCQConnection;
import com.changqing.warehouse.db.dto.BindChehaoDto;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/6/20 0020.
 */

/**
 * 2017-06-15 10:22:40
 1.	发货 确认   (销售明细表：storeoutdetail)
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
 更新状态字段inuse=0流程字段 Audit=2  （销售流程完毕）
 有不明白的问岳
 */

public class PickupSubmitDao {







    public DBResult pickupSubmit(String chehao, String pckbillno, List<PickupInfo> pickupInfos){

        String spSql ="{?=call dbo.sp_WhConfirmPickup(?,?,?,?)}";

        String sql1 =
                " insert into dbo.WhTransferRamark (pckbillno,xhbillno,comid,comname,InspSeqNo,vshippingArea,remarkContent,remarkType)\n" +
                " values(?,?,?,?,?,?,?,'1');";


        String sql2 =
                " update dbo.WhTransferRecord set endTime=getDate() " +
                " where storeout_pckbillno=? and vehicleNo= ? " ;

        String sql3 =
                "update dbo.WhTransferVehicle \n" +
                "SET usingPerson=null,bindTime=null,usingState=1\n" +
                "where storeout_pckBillno=? and vehicleNo = ? ";

        Connection connection = null;
        CallableStatement callStatement = null;
        PreparedStatement preparedStatement = null;
        DBResult dbresult = new DBResult();

        try {
            connection = DBCQConnection.getConnection();
            if(connection!=null) {

                connection.setAutoCommit(false);
                callStatement = connection.prepareCall(spSql);
                    callStatement.registerOutParameter(1, Types.DECIMAL);
                    callStatement.setString(2,pckbillno);
                    callStatement.setString(3,App.getUserBean().getUserName());
                    callStatement.registerOutParameter(4, Types.VARCHAR);
                    callStatement.registerOutParameter(5, Types.VARCHAR);
                boolean r = callStatement.execute();

                preparedStatement = connection.prepareStatement(sql1);

                for (int i =0;i<pickupInfos.size();i++){
                    preparedStatement.setString(1,pickupInfos.get(i).getPckBillno());
                    preparedStatement.setString(2,pickupInfos.get(i).getXHBillno());
                    preparedStatement.setString(3,pickupInfos.get(i).getComid());
                    preparedStatement.setString(4,pickupInfos.get(i).getComname());
                    preparedStatement.setString(5,pickupInfos.get(i).getInspSeqNo());
                    preparedStatement.setString(6,pickupInfos.get(i).getShippingArea());
                    preparedStatement.setString(7,pickupInfos.get(i).getRemark());
                    Log.d("<======>", "----------------pickinfos.get(i).getRemark()------------ = [" + pickupInfos.get(i).getRemark() + "]");
                    preparedStatement.addBatch();
                }
                int[] r1  = preparedStatement.executeBatch();
                preparedStatement = connection.prepareStatement(sql2);
                preparedStatement.setString(1,pckbillno);
                preparedStatement.setString(2,chehao);
                int r2 = preparedStatement.executeUpdate();

                preparedStatement = connection.prepareStatement(sql3);
                preparedStatement.setString(1,pckbillno);
                preparedStatement.setString(2,chehao);

                int r3 = preparedStatement.executeUpdate();



                connection.commit();
                Log.d("--->>", "-------------------------执行存储过程-------------------pckbillno----------------- [" + pckbillno + "]");
                int returnInt = callStatement.getInt(1);
                String checkPerson = callStatement.getString(4);
                String resultMessage =callStatement.getString(5);
                dbresult.setResultInt(returnInt);
                dbresult.setResultString(checkPerson);
                dbresult.setResultMessage(resultMessage);
                dbresult.setResultInt(returnInt);
                dbresult.setResultBoolean(true);
                //connection.commit();

            }
        } catch (SQLException e) {
            e.printStackTrace();

            dbresult.setResultBoolean(false);
            dbresult.setResultMessage(e.getMessage());
            try {
                connection.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
                dbresult.setResultMessage(dbresult.getResultMessage()+"\r\n"+e1.getMessage());
            }
        }finally {
            try {
                callStatement.clearBatch();
                connection.setAutoCommit(true);
                if (connection != null)     connection.close();
                if (callStatement != null)  callStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return dbresult;
    }


    public DBResult setupFuhePerson(String pckbillno) throws SQLException {

//        String setupFuhePerson =
//                "update a set a.fuheperson=(case when  a.shippingtype='公司自提' or a.deptcode in(6) then null when a.deptcode in(3,13) then '张宁' else ? end)\n" +
//                "from storeout a,storeoutdetail b,customer c \n" +
//                "where a.billno=b.billno and a.custaccount=c.account and pckbillno =  ?  and thisdate>=CONVERT(varchar(100),getdate(), 23)\n";
        String setupFuhePersonSql ="";
        String lokupFuhePerson = lookupFuhePerson();
        Connection connection = null;
        PreparedStatement preStatement = null;
        DBResult dbResult = new DBResult();

        try {
            connection = DBCQConnection.getConnection();
            if(connection!=null) {
                //connection.setAutoCommit(false);
                preStatement = connection.prepareStatement(setupFuhePersonSql);
                preStatement.setString(1,lokupFuhePerson);
                preStatement.setString(2,pckbillno);
                int result  = preStatement.executeUpdate();
                if(result>0){
                    dbResult.setResultInt(result);
                    dbResult.setResultBoolean(true);
                    dbResult.setResultMessage(lokupFuhePerson);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            dbResult.setResultInt(-1);
            dbResult.setResultBoolean(false);
            dbResult.setResultMessage(e.getMessage());
        }finally {
            try {
                connection.setAutoCommit(true);
                if (connection != null)   connection.close();
                if (preStatement != null)  preStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return dbResult;
    }

    public String lookupFuhePerson() throws SQLException {


        String lookupFuheSql =
                "        select fuheperson=(select top 1 name \n" +
                "                   from checkperson bb  left join \n" +
                "                    ( select count(pckbillno) as c,fuheperson \n" +
                "                          from (\n" +
                "                                 select distinct pckbillno,fuheperson \n" +
                "                                  from storeout a with (nolock),storeoutdetail b with (nolock) \n" +
                "                                  where a.billno=b.billno and a.thisdate >=CONVERT(varchar(100),getdate(),23) \n" +
                "                          )a where (1=1) group by fuheperson\n" +
                "                    )aa on(aa.fuheperson=bb.name)\n" +
                "        where (1=1)  and isnull(bs,0)=0\n" +
                "        order by aa.c,bb.id)" ;
        Connection connection = null;
        PreparedStatement preStatement = null;
        String result = "";

        try {
            connection = DBCQConnection.getConnection();
            if(connection!=null) {
                //connection.setAutoCommit(false);
                preStatement = connection.prepareStatement(lookupFuheSql);
                ResultSet resultSet = preStatement.executeQuery();
                while (resultSet.next()){
                    result = resultSet.getString("name");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                connection.setAutoCommit(true);
                if (connection != null)   connection.close();
                if (preStatement != null)  preStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

}
