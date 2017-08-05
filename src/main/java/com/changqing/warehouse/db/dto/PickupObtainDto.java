package com.changqing.warehouse.db.dto;

import android.util.Log;

import com.changqing.warehouse.app.App;
import com.changqing.warehouse.bean.DataBaseResult;
import com.changqing.warehouse.bean.PickupInfo;
import com.changqing.warehouse.db.DBCQ170Connection;
import com.changqing.warehouse.db.dao.PickupObtainDao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.LinkedList;

/**
 * Created by Administrator on 2017/6/13 0013.
 */

public class PickupObtainDto {
     private static PickupObtainDao dao = new PickupObtainDao();
    public static LinkedList<PickupInfo> queryPckBill(){


        return dao.queryPckBill();

    }


}
