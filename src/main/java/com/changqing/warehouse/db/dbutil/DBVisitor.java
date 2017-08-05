package com.changqing.warehouse.db.dbutil;



import com.changqing.warehouse.bean.Table;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;




/**
 * Created by ysh on 2017/6/30 0030.
 */

public class DBVisitor {
    
   

    public static Table queryTable(Connection connection, String sql) {
    	return queryTable(connection,sql,new ArrayList());
        
    }
    public static Table queryTable(Connection connection,String sql,String... params) {
		return queryTable(connection,sql,Arrays.asList(params));
    	
    }
    public static Table queryTable(Connection connection,String sql,List<String> params) {
    	
    	PreparedStatement preStatement = null;
    	ResultSet resultSet = null;
    	ResultSetMetaData rsMetedata = null;
    	Table table = new Table();
    	
    	try {
    		
    		preStatement = connection.prepareStatement(sql);
    		if(params!=null){
    			for(int i = 0,size=params.size(); i<size;i++){
    				preStatement.setString(i+1,params.get(i));
    			}
    		}
    		resultSet  = preStatement.executeQuery();
    		
    		if (resultSet.next()) {
    			rsMetedata = resultSet.getMetaData();
    			List<String> header = new LinkedList<>();
    			
    			for (int i = 1,count = rsMetedata.getColumnCount(); i <=count; i++) {
    				String lavel = rsMetedata.getColumnLabel(i);
    				header.add(i-1,lavel);
    			}
    			table.addHeader(header);
    			do{
    				List rowList = new LinkedList();
    				for(int i=1;i<=rsMetedata.getColumnCount();i++){
    					Object values = resultSet.getObject(i);
    					rowList.add(i-1,values);
    				}
    				table.addRow(rowList);
    			}while(resultSet.next());
    		}
    		
    	} catch (SQLException e) {
    		e.printStackTrace();
    		
    	}finally {
    		try {
    			if (resultSet !=null) resultSet.close();
    			if (preStatement != null)  preStatement.close();
    			if (connection != null) connection.close();
  			
    			
    		} catch (SQLException e) {
    			e.printStackTrace();
    			
    		}
    	}
    	return table;
    }

    
    public static List queryListMap(Connection connection, String sql) {
    	return queryListMap(connection,sql,new ArrayList());
    }
    public static List queryListMap(Connection connection, String sql,String... params) {
		return queryListMap(connection,sql,Arrays.asList(params));
    	
    }
    public static List queryListMap(Connection connection, String sql,List<String> params) {
        PreparedStatement preStatement = null;
        ResultSet resultSet = null;
        ResultSetMetaData rsMetedata = null;
        List listMap = new LinkedList();

		try {
            preStatement = connection.prepareStatement(sql);
			DatabaseMetaData dbMetadata=connection.getMetaData();

            resultSet = preStatement.executeQuery();

    		if(params!=null){
				for(int i = 0,size = params.size(); i<size;i++){
					preStatement.setString(i+1,params.get(i));
				}
		    }
            
           
            if(resultSet.next()){
            	
            	rsMetedata = resultSet.getMetaData();

                int count = rsMetedata.getColumnCount();
                
               
                do{
                	Map map = new HashMap();
                	for(int i=1;i<=count;i++){
                		 String key = rsMetedata.getColumnLabel(i);
                      	 Object value = resultSet.getObject(i);
                      	 map.put(key, value);
                    }
                	listMap.add(map);
                }while (resultSet.next());
                
            }
            
            
        
        } catch (SQLException e) {
            e.printStackTrace();
            
        }finally {
            try {
            	if (resultSet !=null) resultSet.close();
    			if (preStatement != null)  preStatement.close();
    			if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
               
            }
       }
        return listMap;
    }
    public static <Bean> List<Bean> queryListBean(Connection connection, String sql,Class<Bean> beanClss) {
    	return queryListBean(connection,sql,beanClss,new ArrayList<String>());
    	
    }
    public static <Bean> List<Bean> queryListBean(Connection connection, String sql,Class<Bean> beanClss,String... params) {
		return queryListBean(connection,sql,beanClss,Arrays.asList(params));
    	
    }
    public static <Bean> List<Bean> queryListBean(Connection connection, String sql,Class<Bean> beanClss,List<String> params) {
    	PreparedStatement preStatement = null;
    	ResultSet resultSet = null;
    	ResultSetMetaData rsMetedata = null;
    	List listBean = new LinkedList();
    	try {
    		preStatement = connection.prepareStatement(sql);
    		
    		resultSet = preStatement.executeQuery();
    		
    		if(params!=null){
    			for(int i = 0,size = params.size(); i<size;i++){
    				preStatement.setString(i+1,params.get(i));
    			}
    		}
    		Field[] fields = beanClss.getDeclaredFields();
    		
    		 if(resultSet.next()){
             	 rsMetedata = resultSet.getMetaData();
                 int count = rsMetedata.getColumnCount();
                 do{
                	
         			Bean bean = beanClss.newInstance();
     				for(int i=1;i<=fields.length;i++){
     					String lable = rsMetedata.getColumnLabel(i);
     					String field = fields[i].getName();
     					if(lable.equals(field)){
     						fields[i].setAccessible(true);
     						Object value = resultSet.getObject(i);
     						fields[i].set(bean, value);
     					}
     				}
     				listBean.add(bean);
                 }while (resultSet.next());
             }
    		
    	} catch (SQLException e) {
    		e.printStackTrace();
    		
    	} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}finally {
    		try {
    			if (resultSet !=null) resultSet.close();
    			if (preStatement != null)  preStatement.close();
    			if (connection != null) connection.close();
    		} catch (SQLException e) {
    			e.printStackTrace();
    			
    		}
    	}
    	return listBean;
    }


    public static int updata(Connection connection,String sql) throws SQLException {
    	return updata(connection,sql,new ArrayList());
    }
    public static int updata(Connection connection,String sql, String... params) throws SQLException {
        return updata(connection,sql,Arrays.asList(params));
    }
    public static int updata(Connection connection,String  sql, List<String> params) throws SQLException{
        PreparedStatement preStatement = null;
        int result = 0;
        try {
            preStatement = connection.prepareStatement(sql);
            if(params!=null){
                for(int i = 0,size=params.size(); i<size;i++){
                    preStatement.setString(i+1,params.get(i));
                }
            }
            result  = preStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }finally {
            try {
      
    			if (preStatement != null)  preStatement.close();
    			if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
                throw e;
            }

        }
        return result;
    }

}
