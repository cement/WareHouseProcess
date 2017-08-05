package com.changqing.warehouse.db.dao;

import android.util.Log;

import com.changqing.warehouse.bean.CheckInfo;
import com.changqing.warehouse.bean.DataBaseResult;
import com.changqing.warehouse.bean.PickupInfo;
import com.changqing.warehouse.db.dto.CheckDto;
import com.changqing.warehouse.db.dto.PickupDto;
import com.changqing.warehouse.db.dto.PickupObtainDto;
import com.changqing.warehouse.db.dto.TestDto;

import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Administrator on 2017/7/12 0012.
 */
public class PickupObtainDaoTest {
    @Test
    public void  test(){
        String s = TestDto.daoTest();
        System.out.println("-------------------------------------------"+s);
    }
    @Test
    public  void  obtainPickupBill(){
        String chehao = "C01";
        String username = "丁艳伟";
        DataBaseResult<PickupInfo> reaults =  PickupDto.obtainPickupPckBill(chehao,username);
        System.out.println(reaults.getResultMessage());
        System.out.println(reaults.getResultString());
        System.out.println(reaults.getResultError());
        System.out.println(reaults.getResultList());
    }
    @Test
    public  void  submitPickupBill(){
        String packbillno = "PA201707110007";
        String username = "丁艳伟";
        DataBaseResult<PickupInfo> reaults =  PickupDto.submitPickupPckBill(packbillno,username,null);
        System.out.println(reaults.getResultMessage());
        System.out.println(reaults.getResultString());
        System.out.println(reaults.getResultError());
        System.out.println(reaults.getResultList());
    }

    @Test
    public  void  obtaiCheckBill(){
        String chehao = "C01";
        String username = "孙玉杰.";
        DataBaseResult<CheckInfo> reaults =  CheckDto.obtainCheckPckBill(chehao,username);
        System.out.println(reaults.getResultMessage());
        System.out.println(reaults.getResultString());
        System.out.println(reaults.getResultError());
        System.out.println(reaults.getResultList());
    }

    @Test
    public  void  submitCheckBill(){
        String packbillno = "PA201707110007";
        String username = "孙玉杰.";
        DataBaseResult<PickupInfo> reaults =  CheckDto.submitCheckPckBill(packbillno, username, new LinkedList<CheckInfo>());
        System.out.println(reaults.getResultMessage());
        System.out.println(reaults.getResultString());
        System.out.println(reaults.getResultError());
        System.out.println(reaults.getResultList());
    }

    @Test
    public  void  queryShbill(){
        CheckDto.queryXhbill("C09","3");
    }
}