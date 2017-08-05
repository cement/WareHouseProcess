package com.changqing.warehouse.db.dto;

import com.changqing.warehouse.db.dao.BindChehaoDao;

import java.sql.SQLException;

/**
 * Created by Administrator on 2017/6/29 0029.
 */

public class BindChehaoDto {

    public static BindChehaoDao bindChehaoDao = new BindChehaoDao();

    public static int  bindChehao(String chehao,String pckBillno,String type) {
        return bindChehaoDao.bindChehao(chehao,pckBillno,type);
    }
    public static int  unBindChehao(String chehao,String pckBillno){
        return bindChehaoDao.unBindChehao(chehao,pckBillno);
    }

    public static String getChehaoByPckBillno(String pckbillno) {
        return  bindChehaoDao.getChehaoByPckBillno(pckbillno);
    }
    public static String getPckBillnoByChehao(String chehao) {
        return  bindChehaoDao.getPckBillnoByChehao(chehao);
    }
}
