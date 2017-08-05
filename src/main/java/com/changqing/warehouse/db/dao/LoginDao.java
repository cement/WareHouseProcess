package com.changqing.warehouse.db.dao;

import android.location.SettingInjectorService;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;
import android.util.Pair;

import com.changqing.warehouse.Utils.Utils;
import com.changqing.warehouse.app.App;
import com.changqing.warehouse.bean.UserBean;
import com.changqing.warehouse.db.DBCQ170Connection;
import com.changqing.warehouse.db.DBCQConnection;
import com.changqing.warehouse.db.DBSetting;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.LogManager;

import javax.xml.transform.Result;

/**
 * Created by Administrator on 2017/6/21 0021.
 */

public class LoginDao {

    public UserBean pckokPersonLogin(String username,String password){

        //TODO 发货员登录
//
//        String sql = "SELECT a.USER_ID AS userId,a.USER_NAME AS userName,a.PASSWD AS passWord,a.LoginTime AS loginTime,b.bs\n" +
//                " FROM sys_operator a,dbo.fhperson b \n" +
//                " WHERE a.USER_NAME = b.name AND b.bs=0 and a.USER_NAME=?";

        String sql = "SELECT a.USER_ID AS userId,a.USER_NAME AS userName,a.PASSWD AS passWord,a.LoginTime AS loginTime,b.bs\n" +
                    " FROM sys_operator a,dbo.fhperson b \n" +
                    " WHERE a.USER_NAME = b.name  and a.USER_NAME=?";
        Connection connection = null;
        PreparedStatement preStatement = null;
        ResultSet resultSet = null;
        boolean Result = false;
        UserBean resultUser = new UserBean();

        try {
            connection = DBCQConnection.getConnectionWithTimeOut();
            if(connection!=null) {
                preStatement = connection.prepareStatement(sql);
                preStatement.setQueryTimeout(DBSetting.STATEMENT_TIMEOUT);
                preStatement.setString(1, username);
//                preStatement.setString(2, App.getPackOkPerson());
//                preStatement.setString(3,pckBillno);
                resultSet  = preStatement.executeQuery();

                    ;
                    while (resultSet.next()){
                        resultUser.setUserId(resultSet.getString("userId"));
                        resultUser.setUserName(resultSet.getString("userName"));
                        resultUser.setPassWord(resultSet.getString("passWord"));
                        //resultUser.setLoginTime(resultSet.getString("loginTime"));
                        resultUser.setBs(resultSet.getString("bs"));
                    }
                    if(TextUtils.isEmpty(resultUser.getUserName())){
                        resultUser.setLoginLog("用户名不存在");
                    }else{
                        String md5password = Utils.getMD5(password);
                        Log.d("------", "pckokPersonLogin() called with: username = [" + username + "], password = [" + md5password + "]");
                        if(resultUser.getPassWord().equals(md5password)){
                            resultUser.setLoginLog("登录成功");
                            resultUser.setUserName(username);
                            resultUser.setPassWord(password);
                        }else{
                            resultUser = new UserBean();
                            resultUser.setLoginLog("密码错误");
                        }
                    }

            }else{
               resultUser.setLoginLog("无法连接到服务器");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            resultUser = new UserBean();
            resultUser.setLoginLog(e.getMessage());

        } finally {
            try {
                if (connection != null) connection.close();
                if (preStatement != null)  preStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }


        return resultUser;

    }



    public UserBean checkPersonLogin(String username,String password){

        //TODO 复核员登录

        String sql =
                   " SELECT a.USER_ID AS userId,a.USER_NAME AS userName,a.PASSWD AS passWord,a.LoginTime AS loginTime,b.bs\n" +
                   " FROM sys_operator a,dbo.checkperson b \n" +
                   " WHERE a.USER_NAME = b.name and a.USER_NAME=?";


//        String sql = "SELECT a.USER_ID AS userId,a.USER_NAME AS userName,a.PASSWD AS passWord,a.LoginTime AS loginTime,b.bs\n" +
//                " FROM sys_operator a,dbo.fhperson b \n" +
//                " WHERE a.USER_NAME = b.name AND b.bs=0 and a.USER_NAME=?";
        Connection connection = null;
        PreparedStatement preStatement = null;
        ResultSet resultSet = null;
        boolean Result = false;
        UserBean resultUser = new UserBean();

        try {
            connection = DBCQConnection.getConnectionWithTimeOut();
            if(connection!=null) {
                preStatement = connection.prepareStatement(sql);
                preStatement.setQueryTimeout(DBSetting.STATEMENT_TIMEOUT);
                preStatement.setString(1, username);
//                preStatement.setString(2, App.getPackOkPerson());
//                preStatement.setString(3,pckBillno);
                resultSet  = preStatement.executeQuery();

                while (resultSet.next()){
                    resultUser.setUserId(resultSet.getString("userId"));
                    resultUser.setUserName(resultSet.getString("userName"));
                    resultUser.setPassWord(resultSet.getString("passWord"));
                   // resultUser.setLoginTime(resultSet.getString("loginTime"));
                    resultUser.setBs(resultSet.getString("bs"));
                }

                if(TextUtils.isEmpty(resultUser.getUserName())){
                    resultUser.setLoginLog("用户名不存在");
                }else{
                    String md5password = Utils.getMD5(password);
                    Log.d("------", "pckokPersonLogin() called with: username = [" + username + "], password = [" + md5password + "]");
                    if(resultUser.getPassWord().equals(md5password)){
                        resultUser.setLoginLog("登录成功");
                        resultUser.setUserName(username);
                        resultUser.setPassWord(password);
                    }else{
                        resultUser.setLoginLog("密码错误");
                    }
                }

            }else{
                resultUser.setLoginLog("无法连接到服务器");
            }
        } catch (SQLException e) {
            Log.d("--", "checkPersonLogin() called with: username = [" + e.getMessage() +  "]");
            resultUser = new UserBean();
            resultUser.setLoginLog(e.getMessage());
            e.printStackTrace();

        } finally {
            try {
                if (connection != null) connection.close();
                if (preStatement != null)  preStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        return resultUser;
    }



}
