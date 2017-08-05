package com.changqing.warehouse.bean;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/10 0010.
 */

public class DataBaseResult<T> {

    public static final String NON_CONNECTION="无数据库连接";
    public static final String SUCCESS_QUERY="查询成功";

    private Boolean resultBoolean;
    private Integer resultInteger;
    private String  resultMessage;
    private String  resultString;
    private String  resultError;


    private LinkedList<T> resultList = new LinkedList<>();

    private LinkedList<String> resultColumns = new LinkedList<>();




    public LinkedList<String> getResultColumns() {
        return resultColumns;
    }

    public void setResultColumns(LinkedList<String> resultColumns) {
        this.resultColumns = resultColumns;
    }

    public LinkedList<T> getResultList() {
        return resultList;
    }

    public void setResultList(LinkedList<T> resultList) {
        this.resultList = resultList;
    }

    public String getResultMessage() {
        return resultMessage;
    }

    public void setResultMessage(String resultMessage) {
        this.resultMessage = resultMessage;
    }

    public String getResultError() {
        return resultError;
    }

    public void setResultError(String resultError) {
        this.resultError = resultError;
    }

    public Boolean getResultBoolean() {
        return resultBoolean;
    }

    public void setResultBoolean(Boolean resultBoolean) {
        this.resultBoolean = resultBoolean;
    }

    public Integer getResultInteger() {
        return resultInteger;
    }

    public void setResultInteger(Integer resultInteger) {
        this.resultInteger = resultInteger;
    }


    public String getResultString() {
        return resultString;
    }

    public void setResultString(String resultString) {
        this.resultString = resultString;
    }
}
