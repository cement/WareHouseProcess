package com.changqing.warehouse.db.dao;

import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;

import com.changqing.warehouse.app.App;
import com.changqing.warehouse.bean.CheckInfo;
import com.changqing.warehouse.db.DBCQConnection;
import com.changqing.warehouse.db.dto.BindChehaoDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.LinkedList;

/**
 * Created by YSH on 2017/6/7 0007.
 */

public class CheckObtainDao {

    private  final String TAG = "DispatchBillDao";
    private  static final String  currentDate = DateFormat.format("yyyy-MM-dd",new Date()).toString();;
    private  String  prePckBillno="";
    private  String  pckBillno = "";

    public  String getPckBillno() {

        String sql = "SELECT TOP 1 t.pckBillno FROM\n" +
                "(\n" +
                " SELECT distinct pckBillno\n" +
                " from v_print_storeout \n" +
                " where  inuse<>0 and audit=1 and thisdate>Convert(varchar(10),Getdate(),121) and fuheperson=?\n" +
                " ) AS t";


        Connection connection = null;
        PreparedStatement preStatement = null;
        ResultSet resultSet = null;


        try {
            connection = DBCQConnection.getConnection();
            if(connection!=null) {
                preStatement = connection.prepareStatement(sql);
               // preStatement.setString(1,currentDate);
                preStatement.setString(1,App.getUserBean().getUserName());
                resultSet = preStatement.executeQuery();
                if(resultSet.next()){
                    do{
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

    public LinkedList<CheckInfo> queryPckBillDetail(String chehao) {
//        String  packBillno = getPckBillno();
//        if(TextUtils.isEmpty(packBillno)){
//            return new PckBill();
//        }


       String sss =
               " select pckBillno,COMNAME,LocName,InspSeqNo, quantity, shippingArea, remark,packokperson,fuheperson\n" +
               " from v_print_storeout where  thisdate>Convert(varchar(10),Getdate(),23) and pckBillno=? and fuheperson=?";

//        String sql =
//                " select pckBillno,COMNAME,LocName,InspSeqNo, quantity, shippingArea, remark,packokperson,fuheperson\n" +
//                " from v_print_storeout where  pckBillno=?";
//        String sss ="\n" +
//                " select b.vehicleNo,a.pckBillno,a.COMNAME,a.LocName,a.InspSeqNo, a.quantity, a.shippingArea, a.remark,a.packokperson,a.fuheperson\n" +
//                " from v_print_storeout a ,dbo.WhTransferVehicle b\n" +
//                " where  a.thisdate>convert(varchar(10),getdate(),23) and b.bindTime >convert(varchar(10),getdate()) and a.pckBillno=?";
//        String sql = "select pckBillno,LocName,  InspSeqNo, quantity, shippingArea, remark,packokperson\n" +
//                " from v_print_storeout where  pckBillno=?";
        Connection connection = null;
        PreparedStatement preStatement = null;
        ResultSet resultSet = null;


        LinkedList<CheckInfo> bill = new LinkedList<>();

        String  packBillno = "";
        if(TextUtils.isEmpty(chehao)){
            packBillno = getPckBillno();
        }else{
            packBillno= BindChehaoDto.getPckBillnoByChehao(chehao);
            Log.d("--》", "-------- chehao = [" + chehao + "]");
            Log.d("--》", "-------- packBillno = [" + packBillno + "]");
            Log.d("--》", "-------- App.getUserBean().getUserName() = [" + App.getUserBean().getUserName() + "]");
        }
        try {
            connection = DBCQConnection.getConnection();
            if(connection!=null){

                preStatement = connection.prepareStatement(sss);
                preStatement.setString(1,packBillno);
                preStatement.setString(2,App.getUserBean().getUserName());

                resultSet = preStatement.executeQuery();

                while (resultSet.next()){
                    //dispatchBill.setPackBillno(pckBillno);
                    CheckInfo checkInfo = new CheckInfo();

                    String  LocName = resultSet.getString("LocName");
                    checkInfo.setLocName(LocName);
                    String  InspSeqNo = resultSet.getString("InspSeqNo");
                    checkInfo.setInspSeqNo(InspSeqNo);

                    String  quantity = resultSet.getString("quantity");
                    checkInfo.setQuantity(quantity);

                    String shippingArea = resultSet.getString("shippingArea");
                    checkInfo.setShippingArea(shippingArea);

                    String  remark = resultSet.getString("remark");
                    checkInfo.setRemark(remark);

                    String pckBillno = resultSet.getString("pckBillno");
                    checkInfo.setPckBillno(pckBillno);

                    String comname = resultSet.getString("COMNAME");
                    checkInfo.setComname(comname);

//                    String chehao = resultSet.getString("vehicleNo");
//                    checkInfo.setVehicleNo(chehao);

                    bill.add(checkInfo);

                   // Log.d(TAG, "queryDispatchBill() called"+map);
                }
            }else{
                //TODO .................................
                Log.d("--queryPckBillDetail--", "无法连接到数据库  ");
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
        return  bill;
    }
     //pckBillno  LocName  InspSeqNo quantity   vshippingArea   remark
        //调号     货位      验号      数量         集号         区号



}
