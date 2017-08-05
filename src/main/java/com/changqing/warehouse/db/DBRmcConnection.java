package com.changqing.warehouse.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBRmcConnection {
    public static Connection getConnection() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            Connection con = DriverManager.getConnection("jdbc:sqlserver://192.168.0.123:1433;DatabaseName=Rmc", "sa", "storeoutdetailcq");

            return con;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("数据库连接失败！");
        }
        return null;
    }

    public static void closeConnection(Connection con) {
        try {
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}