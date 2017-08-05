package com.changqing.warehouse.db.dto;

import com.changqing.warehouse.db.dao.TestDao;

/**
 * Created by Administrator on 2017/7/12 0012.
 */

public class TestDto {

    public static TestDao dao = new TestDao();

    public static String daoTest() {
      return dao.test1();
    }
}
