package com.changqing.warehouse.db.dao;

import android.text.TextUtils;
import android.util.Log;

import com.changqing.warehouse.app.App;
import com.changqing.warehouse.bean.PickupInfo;
import com.changqing.warehouse.db.DBCQConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

/**
 * Created by YSH on 2017/6/7 0007.
 */

public class PickupObtainDao {

    private  final String TAG = "DispatchBillDao";
   // private  static final String  currentDate = DateFormat.format("yyyy-MM-dd",new Date()).toString();;
    //private  String  prePckBillno="";
    private  String  pckBillno = "";

    public  String getPckBillno(){
//        String sql =
//                " select top 1 * from(\n" +
//                " select  distinct pckBillno\n" +
//                " from v_print_storeout where  inuse<>0 and audit=0 and thisdate >Convert(varchar(10),getdate(),121) and packokperson=?) as a\n" +
//                " order by pckBillno\n";
        String sql =
                        "    SELECT  TOP 1  pckBillno,neiwaiwei FROM(\n" +
                        "        SELECT  DISTINCT  pckBillno,neiwaiwei\n" +
                        "        FROM v_print_storeout \n" +
                        "        WHERE   thisdate >Convert(VARCHAR(10),getdate(),23) and inuse<>0 and audit=0 AND packokperson = ? ) AS a\n" +
                        "    ORDER BY CASE WHEN  a. neiwaiwei IS NULL THEN 99 ELSE a.neiwaiwei END  ";
        Connection connection = null;
        PreparedStatement preStatement = null;
        ResultSet resultSet = null;


        try {
            connection = DBCQConnection.getConnection();
            if(connection!=null) {
                preStatement = connection.prepareStatement(sql);
                //preStatement.setString(1,currentDate);
                preStatement.setString(1,App.getUserBean().getUserName());
                resultSet = preStatement.executeQuery();
                if(resultSet.next()){
                    do{
                        //prePckBillno = pckBillno;
                        pckBillno = resultSet.getString("pckbillno");
                    }while(resultSet.next());
                }else{
                    pckBillno = "";
                }


            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                if (connection != null) connection.close();
                if (preStatement != null)  preStatement.close();
                if (resultSet != null) resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        Log.e("TAG","-----------------------：pckBillno     "+pckBillno);

        return pckBillno;
    }

    public LinkedList<PickupInfo> queryPckBill(){


        LinkedList<PickupInfo> pickupInfos = new LinkedList<>();
        String pckbillno = getPckBillno();
        if(TextUtils.isEmpty(pckbillno)){
            return pickupInfos;
        }

//        String sql = " select pckBillno,LocName,InspSeqNo,quantity,shippingArea, remark,packokperson\n" +
//                     " from  v_print_storeout \n" +
//                     " where pckBillno=?\n";
        String sql = " select pckBillno,billno,comid,comname,LocName,InspSeqNo,quantity,shippingArea, remark,packokperson\n" +
                     " from  v_print_storeout \n" +
                     " where thisdate > convert(VARCHAR(10),getdate(),23) and pckBillno=?\n";


        //insert into dbo.WhTransferRecord values('车号','调度单','操作员',getdate(),null,1,'评论');
        //String recordSql = "insert into dbo.WhTransferRecord values(?,?,?,getdate(),null,1,?);";

        Connection connection = null;
        PreparedStatement preStatement = null;
        ResultSet resultSet = null;




        try {
            connection = DBCQConnection.getConnection();
            if(connection!=null){
                preStatement = connection.prepareStatement(sql);
                preStatement.setString(1,pckBillno);
               // preStatement.setString(1,currentDate);
               // preStatement.setString(2,pckBillno);
                resultSet = preStatement.executeQuery();



                while (resultSet.next()){
                    //pckBill.setPackBillno(pckBillno);
                    PickupInfo pckupInfo = new PickupInfo();

                    String  pckBillno = resultSet.getString("pckBillno");
                    pckupInfo.setPckBillno(pckBillno);

                    String xhbilllno = resultSet.getString("billno");
                    pckupInfo.setXHBillno(xhbilllno);

                    String comid = resultSet.getString("comid");
                    pckupInfo.setComid(comid);

                    String comName = resultSet.getString("comname");
                    pckupInfo.setComname(comName);

                    String  LocName = resultSet.getString("LocName");
                    pckupInfo.setLocName(LocName);

                    String  InspSeqNo = resultSet.getString("InspSeqNo");
                    pckupInfo.setInspSeqNo(InspSeqNo);

                    String  quantity = resultSet.getString("quantity");
                    pckupInfo.setQuantity(quantity);

                    String shippingArea = resultSet.getString("shippingArea");
                    pckupInfo.setShippingArea(shippingArea);


                    pickupInfos.add(pckupInfo);

                }
//                if(!pickupInfos.isEmpty()){
//                    preStatement = connection.prepareStatement(recordSql);
//                    preStatement.setString(1,pckBillno);
//                    preStatement.setString(1,pckBillno);
//                    preStatement.setString(1,pckBillno);
//                }

            }else{
                //pckBill.setDbresult("连接数据库失败");
                //TODO .................................
            }

        } catch (SQLException e) {
            e.printStackTrace();
           //pckBill.setDbresult(e.getMessage());
        }finally {
            try {
                if (connection != null) connection.close();
                if (preStatement != null)  preStatement.close();
                if (resultSet != null) resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }

        return  pickupInfos;
    }


    public void saveRecord(String vohicleNo){

    }

   //pckBillno  LocName  InspSeqNo quantity   vshippingArea   remark
    //调号    货位     验号      数量         集号          区号



}
