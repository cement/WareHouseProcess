package com.changqing.warehouse.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/6/17 0017.
 */
//pckBillno  LocName  InspSeqNo quantity   shippingArea   remark
//调号         货位     验号      数量         集号        区号

public class CheckInfo implements Serializable{

//    public static int STATE_pckbill = 0;
//    public static int STATE_uncheck = 0;
//    public static int STATE_checked = 1;
//
//    private int state = STATE_pckbill;

    public static int COLOR_pckbill = 0x88ffffff;
    public static int COLOR_uncheck = 0x88ffffff;
    public static int COLOR_checked = 0x8800ff00;



    private int colorState = COLOR_pckbill;
    //private boolean current;

    //public static int[] BGcolors = new int[]{0x88ffffff,0x8800ff00};

    private String LocName;
    private String InspSeqNo;
    private String quantity;
    private String shippingArea;
    private String remark;
    private String pickupRemark;
    private String pckBillno;
    private String vehicleNo;

    private String comid;
    private String XHbillno;
    private String specification;
    private String unit;
    private String warehouseCode;
    //private String diff ="";


    public String getComid() {
        return comid;
    }

    public void setComid(String conid) {
        this.comid = conid;
    }

    public String getXHBillno() {
        return XHbillno;
    }

    public void setXHBillno(String XHbillno) {
        this.XHbillno = XHbillno;
    }





    private String comname;

//    public int getState() {
//        return state;
//    }
//
//    public void setState(int state) {
//        this.state = state;
//    }




//    public boolean isCurrent() {
//        return current;
//    }
//
//    public void setCurrent(boolean current) {
//        this.current = current;
//    }




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
        return pckBillno;
    }

    public void setPckBillno(String pckBillno) {
        this.pckBillno = pckBillno;
    }

    public String getComname() {
        return comname;
    }

    public void setComname(String comname) {
        this.comname = comname;
    }

    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }



    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getPickupRemark() {
        return pickupRemark;
    }

    public void setPickupRemark(String pickupRemark) {
        this.pickupRemark = pickupRemark;
    }

    public String getWarehouseCode() {
        return warehouseCode;
    }

    public void setWarehouseCode(String warehouseCode) {
        this.warehouseCode = warehouseCode;
    }

    public int getColorState() {
        return colorState;
    }

    public void setColorState(int colorState) {
        this.colorState = colorState;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CheckInfo)) return false;

        CheckInfo checkInfo = (CheckInfo) o;

        if (colorState != checkInfo.colorState) return false;
        if (LocName != null ? !LocName.equals(checkInfo.LocName) : checkInfo.LocName != null)
            return false;
        if (InspSeqNo != null ? !InspSeqNo.equals(checkInfo.InspSeqNo) : checkInfo.InspSeqNo != null)
            return false;
        if (quantity != null ? !quantity.equals(checkInfo.quantity) : checkInfo.quantity != null)
            return false;
        if (shippingArea != null ? !shippingArea.equals(checkInfo.shippingArea) : checkInfo.shippingArea != null)
            return false;
        if (remark != null ? !remark.equals(checkInfo.remark) : checkInfo.remark != null)
            return false;
        if (pickupRemark != null ? !pickupRemark.equals(checkInfo.pickupRemark) : checkInfo.pickupRemark != null)
            return false;
        if (pckBillno != null ? !pckBillno.equals(checkInfo.pckBillno) : checkInfo.pckBillno != null)
            return false;
        if (vehicleNo != null ? !vehicleNo.equals(checkInfo.vehicleNo) : checkInfo.vehicleNo != null)
            return false;
        if (comid != null ? !comid.equals(checkInfo.comid) : checkInfo.comid != null) return false;
        if (XHbillno != null ? !XHbillno.equals(checkInfo.XHbillno) : checkInfo.XHbillno != null)
            return false;
        if (specification != null ? !specification.equals(checkInfo.specification) : checkInfo.specification != null)
            return false;
        if (unit != null ? !unit.equals(checkInfo.unit) : checkInfo.unit != null) return false;
        if (warehouseCode != null ? !warehouseCode.equals(checkInfo.warehouseCode) : checkInfo.warehouseCode != null)
            return false;
        return comname != null ? comname.equals(checkInfo.comname) : checkInfo.comname == null;

    }

    @Override
    public int hashCode() {
        int result = colorState;
        result = 31 * result + (LocName != null ? LocName.hashCode() : 0);
        result = 31 * result + (InspSeqNo != null ? InspSeqNo.hashCode() : 0);
        result = 31 * result + (quantity != null ? quantity.hashCode() : 0);
        result = 31 * result + (shippingArea != null ? shippingArea.hashCode() : 0);
        result = 31 * result + (remark != null ? remark.hashCode() : 0);
        result = 31 * result + (pickupRemark != null ? pickupRemark.hashCode() : 0);
        result = 31 * result + (pckBillno != null ? pckBillno.hashCode() : 0);
        result = 31 * result + (vehicleNo != null ? vehicleNo.hashCode() : 0);
        result = 31 * result + (comid != null ? comid.hashCode() : 0);
        result = 31 * result + (XHbillno != null ? XHbillno.hashCode() : 0);
        result = 31 * result + (specification != null ? specification.hashCode() : 0);
        result = 31 * result + (unit != null ? unit.hashCode() : 0);
        result = 31 * result + (warehouseCode != null ? warehouseCode.hashCode() : 0);
        result = 31 * result + (comname != null ? comname.hashCode() : 0);
        return result;
    }
}
