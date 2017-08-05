package com.changqing.warehouse.bean;

/**
 * Created by Administrator on 2017/7/7 0007.
 */

public class DBResult {

    private String  resultMessage;
    private boolean resultBoolean ;
    private int     resultInt ;
    private String  resultString;
    private String  resultError;
    private Object  resultObkect;

    public String getResultMessage() {
        return resultMessage;
    }

    public void setResultMessage(String resultMessage) {
        this.resultMessage = resultMessage;
    }

    public boolean isResultBoolean() {
        return resultBoolean;
    }

    public void setResultBoolean(boolean resultBoolean) {
        this.resultBoolean = resultBoolean;
    }

    public int getResultInt() {
        return resultInt;
    }

    public void setResultInt(int resultInt) {
        this.resultInt = resultInt;
    }

    public String getResultString() {
        return resultString;
    }

    public void setResultString(String resultString) {
        this.resultString = resultString;
    }

    public String getResultError() {
        return resultError;
    }

    public void setResultError(String resultError) {
        this.resultError = resultError;
    }

    public Object getResultObkect() {
        return resultObkect;
    }

    public void setResultObkect(Object resultObkect) {
        this.resultObkect = resultObkect;
    }
}
