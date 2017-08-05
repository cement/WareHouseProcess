package com.changqing.warehouse.db.dto;

import com.changqing.warehouse.bean.DBResult;
import com.changqing.warehouse.bean.PickupInfo;
import com.changqing.warehouse.db.dao.PickupSubmitDao;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Administrator on 2017/6/20 0020.
 */

public class PickupSubmitDto {

    public  static PickupSubmitDao submitDao = new PickupSubmitDao();
    public static DBResult submitPckbill(String chehao, String pckbillno, List<PickupInfo> pickupInfos) throws SQLException {
        return submitDao.pickupSubmit(chehao,pckbillno,pickupInfos);
    }
    public static DBResult setupFuhePerson(String pckbillno) throws SQLException {
        return submitDao.setupFuhePerson(pckbillno);
    }
}
