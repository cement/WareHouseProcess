package com.changqing.warehouse.bean;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/28 0028.
 */

public class Table {

    private List<List<String>> list = new LinkedList<>();

    public void addHeader(List<String> titles){
        list.add(titles);
    }
    public void addRow(List<String> row){
        list.add(row);
    }


    public List<String> getHeader(){
        return list.get(0);
    }
    public String getColumName(int colum) throws Exception {
        if(colum < 0){
            throw new Exception("colum index must >=0");
        }
        List<String> columNames = getHeader();
        return columNames.get(colum);
    }

    public List<String> getRow(int rownum) throws Exception {
        if(rownum<=0){
            throw new Exception("row number must >0");
        }
        List<String> row = list.get(rownum);
        return row;
    }
    public String getCell(int rowNum,int colum) throws Exception {
        if(colum<0){
            throw new Exception("colum index must >=0");
        }
        String cell = getRow(rowNum).get(colum);
        return cell;
    }
    public String getCell(int rowNum,String colum) throws Exception {
        int index = getColumIndex(colum);
        return getCell(rowNum,index);
    }

    public List<String> getColum(int colum) throws Exception {
        if(colum<0){
            throw new Exception("row number must >0");
        }
        List<String> columList = new LinkedList<>();
        for (int i = 1; i <list.size() ; i++) {
            String value = getRow(i).get(colum);
            columList.add(value);
        }
        return columList;
    }

    public List<String> getColum(String colum) throws Exception {
        int index = getHeader().indexOf(colum);
        return getColum(index);
    }

    public int getRowCount(){
        return  list.size()-1;
    }
    public int getAllRowCount(){
        return  list.size();
    }

    public int getColumIndex(String columName){
        return getHeader().indexOf(columName);
    }
    @Override
    public String toString() {
        return "Table{" +
                "list=" + list +
                '}';
    }
}
