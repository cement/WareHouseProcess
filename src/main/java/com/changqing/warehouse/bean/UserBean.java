package com.changqing.warehouse.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/6/22 0022.
 */

public class UserBean implements Serializable {

    public static final String LOGIN_SUCCESS = "登录成功";
    public static final String LOGIN_FAIL = "登录失败";


    String userId;
    String userName;
    String passWord;

    public long getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(long loginTime) {
        this.loginTime = loginTime;
    }

    public int getLogintype() {
        return logintype;
    }

    public void setLogintype(int logintype) {
        this.logintype = logintype;
    }

    long loginTime;
    int logintype;
    String bs;



    String loginLog;
    public String getLoginLog() {
        return loginLog;
    }

    public void setLoginLog(String loginLog) {
        this.loginLog = loginLog;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }



    public String getBs() {
        return bs;
    }

    public void setBs(String bs) {
        this.bs = bs;
    }



}
