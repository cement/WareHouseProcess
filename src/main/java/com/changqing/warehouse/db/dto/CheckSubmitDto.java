package com.changqing.warehouse.db.dto;

import android.util.Log;

import com.changqing.warehouse.bean.CheckInfo;
import com.changqing.warehouse.bean.DBResult;
import com.changqing.warehouse.db.dao.CheckObtainDao;
import com.changqing.warehouse.db.dao.CheckSubmitDao;

import java.util.LinkedList;

/**
 * Created by Administrator on 2017/6/13 0013.
 */

public class CheckSubmitDto {
    public static CheckSubmitDao submitdao = new CheckSubmitDao();
    public static DBResult checkSubmit(String pckBillno,String chehao){
        Log.d("----------------->", "getDispatchBill() called  getDispatchBill");
        return submitdao.checkSubmit(pckBillno,chehao);
    }
}
