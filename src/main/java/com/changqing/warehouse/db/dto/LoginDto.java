package com.changqing.warehouse.db.dto;

import com.changqing.warehouse.bean.UserBean;
import com.changqing.warehouse.db.dao.LoginDao;

/**
 * Created by Administrator on 2017/6/21 0021.
 */

public class LoginDto {
    private static LoginDao loginDao = new LoginDao();
    public static UserBean pckokPersonLogin(String username, String password){
        return loginDao.pckokPersonLogin(username,password);
    }
    public static UserBean checkPersonLogin(String username,String password){
        return loginDao.checkPersonLogin(username,password);
    }
}
