package com.changqing.warehouse.mian;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.changqing.warehouse.R;
import com.changqing.warehouse.bean.PickupInfo;

import java.util.List;

/**
 * Created by Administrator on 2017/7/5 0005.
 */

public class PopupListAdapter extends BaseAdapter {
    private List<PickupInfo> pickupInfos;

    public PopupListAdapter(List<PickupInfo> pickupInfos) {
        this.pickupInfos = pickupInfos;

    }

    @Override
    public int getCount() {

        return pickupInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return pickupInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        PopupItemViewHolder viewHolder = null;
        if(convertView==null){
            viewHolder = new PopupItemViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.pickup_item_layout,parent,false);
            viewHolder.quantityView= (TextView) convertView.findViewById(R.id.item_quantity);
            viewHolder.shippingAreaView= (TextView) convertView.findViewById(R.id.item_shippingArea);
            viewHolder.InspSeqNoView= (TextView) convertView.findViewById(R.id.item_InspSeqNo);
            viewHolder.LocNameView= (TextView) convertView.findViewById(R.id.item_LocName);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (PopupItemViewHolder) convertView.getTag();
        }

        PickupInfo info = pickupInfos.get(position);
        viewHolder.LocNameView.setText(info.getLocName());
        viewHolder.InspSeqNoView.setText(info.getInspSeqNo());
        viewHolder.quantityView.setText(info.getQuantity());
        viewHolder.shippingAreaView.setText(info.getShippingArea());
        return convertView;
    }
    public class PopupItemViewHolder{
        TextView quantityView;
        TextView shippingAreaView;
        TextView InspSeqNoView;
        TextView LocNameView;
    }
}
