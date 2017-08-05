package com.changqing.warehouse.db.dto;

import android.util.Log;

import com.changqing.warehouse.bean.CheckInfo;
import com.changqing.warehouse.db.dao.CheckObtainDao;

import java.util.LinkedList;

/**
 * Created by Administrator on 2017/6/13 0013.
 */

public class CheckObtainDto {
    public static CheckObtainDao dao = new CheckObtainDao();
    public static LinkedList<CheckInfo> getPckBill(String param){
        Log.d("----------------->", "getDispatchBill() called  getDispatchBill");
        LinkedList<CheckInfo> bill = dao.queryPckBillDetail(param);
        return  bill;
    }
}
