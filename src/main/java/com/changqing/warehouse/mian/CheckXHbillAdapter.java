package com.changqing.warehouse.mian;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.changqing.warehouse.R;
import com.changqing.warehouse.base.BaseViewHolder;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/7/22 0022.
 */

public class CheckXHbillAdapter<T> extends  RecyclerView.Adapter<BaseViewHolder> {


    private List<Map<String,String>> datas = new LinkedList<>();
    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.check_xhbill_item_layout,parent,false);
        return new XHbillViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
          holder.bindData(datas.get(position));
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }



    public class XHbillViewHolder extends BaseViewHolder{


        private TextView shippingArea;
        private TextView comname;
        private TextView quantity;
        private TextView InvSeqNo;
        private TextView locname;


        public XHbillViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void bindView() {
            shippingArea = (TextView) itemView.findViewById(R.id.check_xhbill_shippingArea);
            comname = (TextView) itemView.findViewById(R.id.check_xhbill_comname);
            quantity = (TextView) itemView.findViewById(R.id.check_xhbill_quantity);
            InvSeqNo = (TextView) itemView.findViewById(R.id.check_xhbill_InvSeqNo);
            locname = (TextView) itemView.findViewById(R.id.check_xhbill_locname);
            //action =  itemView.findViewById(R.id.check_xhbill_action);
        }


        //pckBillno,billno,shippingArea,comname,quantity
        @Override
        public void bindData(Object data) {
            Map<String,String> map = (Map<String,String>)data;
            shippingArea.setText(map.get("shippingArea"));
            comname.setText(map.get("comname"));
            quantity.setText(map.get("quantity"));
            InvSeqNo.setText(map.get("InvSeqNo"));
            locname.setText(map.get("LocName"));
        }
    }



    public List<Map<String, String>> getDatas() {
        return datas;
    }

    public void setDatas(List<Map<String, String>> datas) {
        this.datas = datas;
    }

}
