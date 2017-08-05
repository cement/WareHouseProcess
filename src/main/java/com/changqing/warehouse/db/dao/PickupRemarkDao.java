package com.changqing.warehouse.db.dao;

import android.util.Log;

import com.changqing.warehouse.bean.PickupInfo;
import com.changqing.warehouse.db.DBCQConnection;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

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

public class PickupRemarkDao {


    public int[] pickupRemark(List<PickupInfo> pickinfos) throws SQLException {



        // String s = "UPDATE 表名称 SET 列名称 = 新值 WHERE 列名称 = 某值";
        String sql1 =
                " insert into dbo.WhTransferRamark (pckbillno,xhbillno,comid,comname,InspSeqNo,vshippingArea,remarkContent,remarkType)\n" +
                " values(?,?,?,?,?,?,?,'1');";
        String sql2 = "";
        Connection connection = null;
        PreparedStatement preStatement = null;
        int[] results = null;


        try {
            connection = DBCQConnection.getConnection();
            if(connection!=null) {
                boolean b  = supportBatch(connection);
                Log.d("<======>", "---------------------------- = [" + b + "]");
                connection.setAutoCommit(false);
                preStatement = connection.prepareStatement(sql1);
                for (int i =0;i<pickinfos.size();i++){
                        preStatement.setString(1,pickinfos.get(i).getPckBillno());
                        preStatement.setString(2,pickinfos.get(i).getXHBillno());
                        preStatement.setString(3,pickinfos.get(i).getComid());
                        preStatement.setString(4,pickinfos.get(i).getComname());
                        preStatement.setString(5,pickinfos.get(i).getInspSeqNo());
                        preStatement.setString(6,pickinfos.get(i).getShippingArea());
                        preStatement.setString(7,pickinfos.get(i).getRemark());
                    Log.d("<======>", "----------------pickinfos.get(i).getRemark()------------ = [" + pickinfos.get(i).getRemark() + "]");
                    preStatement.addBatch();
                }
                results  = preStatement.executeBatch();
                connection.commit();
            }

        } catch (SQLException e) {
            connection.rollback();
            e.printStackTrace();
        }finally {
            try {
                connection.setAutoCommit(true);
                preStatement.clearBatch();
                if (connection != null) connection.close();
                if (preStatement != null)  preStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }


        return results;
    }

    /** 判断数据库是否支持批处理 */

    public static boolean supportBatch(Connection con) {

        try {

            // 得到数据库的元数据

            DatabaseMetaData md = con.getMetaData();

            return md.supportsBatchUpdates();

        } catch (SQLException e) {

            e.printStackTrace();

        }

        return false;

    }
//pckBillno  LocName  InspSeqNo quantity   vshippingArea   remark
    //调号    货位     验号      数量         集号          区号



}
