package com.changqing.warehouse.db.dto;

import com.changqing.warehouse.bean.PickupInfo;
import com.changqing.warehouse.db.dao.PickupRemarkDao;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Administrator on 2017/7/8 0008.
 */

public class PickupRemarkDto {
    private static PickupRemarkDao dao = new PickupRemarkDao();
    public static int[] pickupRemark(List<PickupInfo> pickupInfos) throws SQLException {
        return  dao.pickupRemark(pickupInfos);
    }
}
