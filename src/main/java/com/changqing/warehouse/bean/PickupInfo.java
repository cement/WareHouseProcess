package com.changqing.warehouse.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/6/17 0017.
 */
//pckBillno  LocName  InspSeqNo quantity   shippingArea   remark
//调号         货位     验号      数量         集号        区号

public class PickupInfo implements Serializable{

    public static int PRO_normal = 0;
    public static int PRO_useing = 1;




//    public static int STATE_unpick = 0;
//    public static int STATE_curent = 1;
//    public static int STATE_pickup = 2;
//    public static int STATE_basket = 3;

    public static int COLOR_unpick = 0x88ffffff;
    public static int COLOR_pickup = 0x8800ff00;
    public static int COLOR_basket = 0x880000ff;
    public static int COLOR_CURRENT_pickup = 0x88ff0000;
    public static int COLOR_CURRENT_basket = 0x884E5FF4;

    //private int state = STATE_unpick;
    //private boolean current;

   // public static int[] BGcolors = new int[]{0x88ffffff,0x88ff0000,0x8800ff00,0x880000ff};

    private String LocName;
    private String InspSeqNo;
    private String quantity;
    private String shippingArea;
    private String remark;
    private String PckBillno;
    private String XHBillno;
    private String comid;
    private String comname;
    private String warehouseCode;


    private String KuangHao;




    private int colorstate = COLOR_unpick;



    public int getColorState() {
        return colorstate;
    }

    public void setColorState(int colorstate) {
        this.colorstate = colorstate;
    }

    public String getLocName() {
        return LocName;
    }

    public void setLocName(String locName) {
        LocName = locName;
    }

    public String getInspSeqNo() {
        return InspSeqNo;
    }

    public void setInspSeqNo(String inspSeqNo) {
        InspSeqNo = inspSeqNo;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getShippingArea() {
        return shippingArea;
    }

    public void setShippingArea(String shippingArea) {
        this.shippingArea = shippingArea;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }




    public String getPckBillno() {
        return PckBillno;
    }

    public void setPckBillno(String pckBillno) {
        PckBillno = pckBillno;
    }

    public String getKuangHao() {
        return KuangHao;
    }

    public void setKuangHao(String kuangHao) {
        KuangHao = kuangHao;
    }

    public String getComid() {
        return comid;
    }

    public void setComid(String comid) {
        this.comid = comid;
    }

    public String getXHBillno() {
        return XHBillno;
    }

    public void setXHBillno(String XHBillno) {
        this.XHBillno = XHBillno;
    }

    public String getComname() {
        return comname;
    }

    public void setComname(String comname) {
        this.comname = comname;
    }

    @Override
    public String toString() {
        return "PickupInfo{" +

                ", LocName='" + LocName + '\'' +
                ", InspSeqNo='" + InspSeqNo + '\'' +
                ", quantity='" + quantity + '\'' +
                ", shippingArea='" + shippingArea + '\'' +
                ", remark='" + remark + '\'' +
                ", PckBillno='" + PckBillno + '\'' +
                ", XHBillno='" + XHBillno + '\'' +
                ", comid='" + comid + '\'' +
                ", comname='" + comname + '\'' +
                ", KuangHao='" + KuangHao + '\'' +
                '}';
    }


    public String getWarehouseCode() {
        return warehouseCode;
    }

    public void setWarehouseCode(String warehouseCode) {
        this.warehouseCode = warehouseCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PickupInfo)) return false;

        PickupInfo that = (PickupInfo) o;

        if (colorstate != that.colorstate) return false;
        if (LocName != null ? !LocName.equals(that.LocName) : that.LocName != null) return false;
        if (InspSeqNo != null ? !InspSeqNo.equals(that.InspSeqNo) : that.InspSeqNo != null)
            return false;
        if (quantity != null ? !quantity.equals(that.quantity) : that.quantity != null)
            return false;
        if (shippingArea != null ? !shippingArea.equals(that.shippingArea) : that.shippingArea != null)
            return false;
        if (remark != null ? !remark.equals(that.remark) : that.remark != null) return false;
        if (PckBillno != null ? !PckBillno.equals(that.PckBillno) : that.PckBillno != null)
            return false;
        if (XHBillno != null ? !XHBillno.equals(that.XHBillno) : that.XHBillno != null)
            return false;
        if (comid != null ? !comid.equals(that.comid) : that.comid != null) return false;
        if (comname != null ? !comname.equals(that.comname) : that.comname != null) return false;
        if (warehouseCode != null ? !warehouseCode.equals(that.warehouseCode) : that.warehouseCode != null)
            return false;
        return KuangHao != null ? KuangHao.equals(that.KuangHao) : that.KuangHao == null;

    }

    @Override
    public int hashCode() {
        int result = LocName != null ? LocName.hashCode() : 0;
        result = 31 * result + (InspSeqNo != null ? InspSeqNo.hashCode() : 0);
        result = 31 * result + (quantity != null ? quantity.hashCode() : 0);
        result = 31 * result + (shippingArea != null ? shippingArea.hashCode() : 0);
        result = 31 * result + (remark != null ? remark.hashCode() : 0);
        result = 31 * result + (PckBillno != null ? PckBillno.hashCode() : 0);
        result = 31 * result + (XHBillno != null ? XHBillno.hashCode() : 0);
        result = 31 * result + (comid != null ? comid.hashCode() : 0);
        result = 31 * result + (comname != null ? comname.hashCode() : 0);
        result = 31 * result + (warehouseCode != null ? warehouseCode.hashCode() : 0);
        result = 31 * result + (KuangHao != null ? KuangHao.hashCode() : 0);
        result = 31 * result + colorstate;
        return result;
    }


}
