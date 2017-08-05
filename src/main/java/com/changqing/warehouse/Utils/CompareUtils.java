package com.changqing.warehouse.Utils;

import com.changqing.warehouse.bean.CheckInfo;
import com.changqing.warehouse.bean.PickupInfo;

import java.util.Comparator;

/**
 * Created by Administrator on 2017/6/20 0020.
 */

public class CompareUtils {


    public static Comparator<PickupInfo> compareByLocName = new Comparator<PickupInfo>() {
        @Override
        public int compare(PickupInfo o1, PickupInfo o2) {
            return o1.getLocName().compareTo(o2.getLocName());
        }
    };
    public static Comparator<PickupInfo> compareByInspSeqNo = new Comparator<PickupInfo>() {
        @Override
        public int compare(PickupInfo o1, PickupInfo o2) {
            if(Utils.isNumber(o1.getInspSeqNo()) && Utils.isNumber(o2.getInspSeqNo())){
                Double d1 = Double.parseDouble(o1.getInspSeqNo());
                Double d2 = Double.parseDouble(o2.getInspSeqNo());
                return (int)(d1-d2);
            }else{
                return 0;
            }
        }
    };

    public static Comparator<PickupInfo> compareByShippingArea = new Comparator<PickupInfo>() {
        @Override
        public int compare(PickupInfo o1, PickupInfo o2) {
            if(Utils.isNumber(o1.getShippingArea()) && Utils.isNumber(o2.getShippingArea())){
                Double d1 = Double.parseDouble(o1.getShippingArea());
                Double d2 = Double.parseDouble(o2.getShippingArea());
                return (int)(d1-d2);
            }else{
                return 0;
            }
        }
    };

    public static Comparator<PickupInfo> compareByQuantity = new Comparator<PickupInfo>() {
        @Override
        public int compare(PickupInfo o1, PickupInfo o2) {
            if(Utils.isNumber(o1.getQuantity()) && Utils.isNumber(o2.getQuantity())){
                Double d1 = Double.parseDouble(o1.getQuantity());
                Double d2 = Double.parseDouble(o2.getQuantity());
                return (int)(d1-d2);
            }else{
                return 0;
            }
        }
    };



    public static Comparator<CheckInfo> compareByCheckLocName = new Comparator<CheckInfo>() {
        @Override
        public int compare(CheckInfo o1, CheckInfo o2) {
            return o1.getLocName().compareTo(o2.getLocName());
        }
    };
    public static Comparator<CheckInfo> compareByCheckInspSeqNo = new Comparator<CheckInfo>() {
        @Override
        public int compare(CheckInfo o1, CheckInfo o2) {
            if(Utils.isNumber(o1.getInspSeqNo()) && Utils.isNumber(o2.getInspSeqNo())){
                Double d1 = Double.parseDouble(o1.getInspSeqNo());
                Double d2 = Double.parseDouble(o2.getInspSeqNo());
                return (int)(d1-d2);
            }else{
                return 0;
            }
        }
    };

    public static Comparator<CheckInfo> compareByCheckShippingArea = new Comparator<CheckInfo>() {
        @Override
        public int compare(CheckInfo o1, CheckInfo o2) {
            if(Utils.isNumber(o1.getShippingArea()) && Utils.isNumber(o2.getShippingArea())){
                Double d1 = Double.parseDouble(o1.getShippingArea());
                Double d2 = Double.parseDouble(o2.getShippingArea());
                return (int)(d1-d2);
            }else{
                return 0;
            }
        }
    };

    public static Comparator<CheckInfo> compareByCheckQuantity = new Comparator<CheckInfo>() {
        @Override
        public int compare(CheckInfo o1, CheckInfo o2) {
            if(Utils.isNumber(o1.getQuantity()) && Utils.isNumber(o2.getQuantity())){
                Double d1 = Double.parseDouble(o1.getQuantity());
                Double d2 = Double.parseDouble(o2.getQuantity());
                return (int)(d1-d2);
            }else{
                return 0;
            }
        }
    };


    public static Comparator<PickupInfo> comparePickupAsc = new Comparator<PickupInfo>() {
        @Override
        public int compare(PickupInfo o1, PickupInfo o2) {
            if(o1.getLocName().compareTo(o2.getLocName())==0){
                return o1.getInspSeqNo().compareTo(o2.getInspSeqNo());
            }else{
                return o1.getLocName().compareTo(o2.getLocName());
            }
        }
    };
    public static Comparator<PickupInfo> comparePickupDesc = new Comparator<PickupInfo>() {
        @Override
        public int compare(PickupInfo o1, PickupInfo o2) {
            if(o1.getLocName().compareTo(o2.getLocName())==0){
                return -o1.getInspSeqNo().compareTo(o2.getInspSeqNo());
            }else{
                return -o1.getLocName().compareTo(o2.getLocName());
            }
        }
    };

    public static Comparator<CheckInfo> compareCheckAsc = new Comparator<CheckInfo>() {
        @Override
        public int compare(CheckInfo o1, CheckInfo o2) {
            Integer i1 = Integer.parseInt(o1.getShippingArea());
            Integer i2 = Integer.parseInt(o2.getShippingArea());
            if(i1==i2){
                return o1.getInspSeqNo().compareTo(o2.getInspSeqNo());
            }else{
                return i1-i2;
            }
        }
    };

    public static Comparator<CheckInfo> compareCheckDesc = new Comparator<CheckInfo>() {
        @Override
        public int compare(CheckInfo o1, CheckInfo o2) {
            Integer i1 = Integer.parseInt(o1.getShippingArea());
            Integer i2 = Integer.parseInt(o2.getShippingArea());
            if(i1==i2){
                return -o1.getInspSeqNo().compareTo(o2.getInspSeqNo());
            }else{
                return i1-i2;
            }
        }
    };


}
