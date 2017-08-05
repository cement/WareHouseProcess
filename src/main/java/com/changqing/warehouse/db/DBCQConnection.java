package com.changqing.warehouse.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBCQConnection {
    public static Connection getConnection() throws SQLException {
       if(DBSetting.APPDEBUG){
           return DBCQ170Connection.getConnectionWithTimeOut();
       }else{
           return DBCQ123Connection.getConnectionWithTimeOut();
       }

    }
    public static Connection getConnectionWithTimeOut() throws SQLException {
       if(DBSetting.APPDEBUG){
           return DBCQ170Connection.getConnectionWithTimeOut();

       }else{
           return DBCQ123Connection.getConnectionWithTimeOut();
       }
    }



    //pckBillno  LocName  InspSeqNo quantity   shippingArea   remark
    //调号         货位     验号      数量         集号          区号



}