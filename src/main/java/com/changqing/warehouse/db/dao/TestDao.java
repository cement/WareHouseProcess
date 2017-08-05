package com.changqing.warehouse.db.dao;

import android.util.Log;

import com.changqing.warehouse.app.App;
import com.changqing.warehouse.bean.DBResult;
import com.changqing.warehouse.bean.DataBaseResult;
import com.changqing.warehouse.bean.PickupInfo;
import com.changqing.warehouse.db.DBCQ170Connection;
import com.changqing.warehouse.db.DBCQConnection;


import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

/**
 * Created by Administrator on 2017/7/12 0012.
 */

public class TestDao {


    public DataBaseResult test() {
        Connection connection = DBCQ170Connection.getConnection();
        String sql = " SELECT   pckBillno,neiwaiwei,billno,comid,comname,LocName,InspSeqNo,quantity,shippingArea, remark,packokperson\n" +
                "    FROM    v_print_storeout   \n" +
                "    WHERE   thisdate >convert(VARCHAR(50),dateadd(dd,datediff(dd,getdate(),'2017-07-09'),getdate()),121)";
        DataBaseResult<PickupInfo> dataBaseResult = DataBaseVisitor.query(connection, sql, PickupInfo.class);
        return dataBaseResult;
    }

    @org.junit.Test
    public String test1() {

        String sql = "{?=call dbo.sp_WhConfirmPickup(?,?,?,?)}";
        Connection connection = null;
        CallableStatement callStatement = null;
        PreparedStatement preparedStatement = null;
        DBResult dbresult = new DBResult();
        String s = null;


        try {
            connection = DBCQ170Connection.getConnection();
            if (connection != null) {

                connection.setAutoCommit(false);
                callStatement = connection.prepareCall(sql);
                callStatement.registerOutParameter(1, Types.INTEGER);
                callStatement.setString(2, "C01");
                callStatement.setString(3, "发货员");
                callStatement.registerOutParameter(4, Types.VARCHAR);
                callStatement.registerOutParameter(5, Types.VARCHAR);

                boolean r = callStatement.execute();

                s=callStatement.getString(4);

            }
        } catch (SQLException e) {
            e.printStackTrace();
            Log.d("====", "test1() called"+e.getMessage());
        }
        return s;
    }
}
