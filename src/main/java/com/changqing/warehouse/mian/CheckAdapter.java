package com.changqing.warehouse.mian;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.changqing.warehouse.R;
import com.changqing.warehouse.base.BaseViewHolder;
import com.changqing.warehouse.bean.CheckInfo;
import com.changqing.warehouse.bean.PickupInfo;
import com.changqing.warehouse.mian.inteface.IHolderMemberClickListener;
import com.changqing.warehouse.mian.inteface.IHolderMemberLongClickListener;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class CheckAdapter extends RecyclerView.Adapter<BaseViewHolder> {




    private LinkedList<CheckInfo> mPckBillData = new LinkedList<>();
    private LinkedList<CheckInfo> mUncheckData = new LinkedList<>();
    private LinkedList<CheckInfo> mCheckedData = new LinkedList<>();
    private LinkedList<CheckInfo> mRemarkData = new LinkedList<>();
    private LinkedList<CheckInfo> mCurrentdata = mPckBillData;






    private IHolderMemberClickListener mListener;



    private IHolderMemberLongClickListener mLongClicklistener;


    private View.OnContextClickListener mContextListener;



    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view =  inflater.inflate(R.layout.check_item_layout, parent, false);
        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        CheckInfo ckeckInfo = getCurrentdata().get(position);
        holder.bindData(ckeckInfo);

    }



    @Override
    public int getItemCount() {
        return  mCurrentdata.size();
    }

    public LinkedList<CheckInfo> getRemarkData() {
        return mRemarkData;
    }

    public void setRemarkData(LinkedList<CheckInfo> mRemarkData) {
        this.mRemarkData = mRemarkData;
    }


    private class MyViewHolder extends BaseViewHolder implements View.OnClickListener,View.OnLongClickListener{

        TextView pckBillnoView;
        TextView LocNameView;
        //TextView InspSeqNoView;
        TextView quantityView;
        TextView shippingAreaView;
        TextView actionView;
        TextView ComName;
        TextView diffQuantityView;
        private TextView specificationView;
        private TextView pickupRemarkView;

        public MyViewHolder(View itemView) {
            super(itemView);

        }

        @Override
        public void bindView() {

            //LocNameView = (TextView) itemView.findViewById(R.id.check_item_LocName);
            //InspSeqNoView = (TextView) itemView.findViewById(R.id.item_InspSeqNo);
            ComName = (TextView) itemView.findViewById(R.id.check_item_comname);
            quantityView = (TextView) itemView.findViewById(R.id.check_item_quantity);
            shippingAreaView = (TextView) itemView.findViewById(R.id.check_item_shippingArea);
            specificationView = (TextView) itemView.findViewById(R.id.check_item_spec);
            pickupRemarkView = (TextView) itemView.findViewById(R.id.check_item_pickup_remark);
            diffQuantityView = (TextView) itemView.findViewById(R.id.check_item_diff);
            actionView = (TextView) itemView.findViewById(R.id.check_item_action);
            itemView.setOnLongClickListener(this);
//          itemView.setOnContextClickListener(this);
            actionView.setOnClickListener(this);
        }

        @Override
        public void bindData(Object data) {
            itemView.setTag(data);
            CheckInfo checkInfo = (CheckInfo) data;
            ComName.setText("");
            quantityView.setText("");
            shippingAreaView.setText("");
            specificationView.setText("");
            pickupRemarkView.setText("");

            //diffView.setText("");
            actionView.setVisibility(View.VISIBLE);
            diffQuantityView.setVisibility(View.INVISIBLE);
            if(checkInfo.getColorState() == CheckInfo.COLOR_checked){
                actionView.setVisibility(View.INVISIBLE);
                diffQuantityView.setVisibility(View.VISIBLE);
                diffQuantityView.setText(checkInfo.getRemark());
            }
            //int stateColor = CheckInfo.BGcolors[checkInfo.getState()];
            ((CardView)itemView).setCardBackgroundColor(checkInfo.getColorState());
            //LocNameView.setText(checkInfo.getLocName());
            //InspSeqNoView.setText(checkInfo.getInspSeqNo());
            ComName.setText(checkInfo.getComname());
            specificationView.setText("【"+checkInfo.getSpecification()+"/"+checkInfo.getUnit()+"】");
            pickupRemarkView.setText(checkInfo.getPickupRemark());
            quantityView.setText(checkInfo.getQuantity());
            shippingAreaView.setText(checkInfo.getShippingArea());

            //diffView.setText(checkInfo.getDiff());
            Log.d("--------", "checkInfo.getDiff()= [" + checkInfo.getRemark() + "]");
        }

        @Override
        public void onClick(View view) {

            if (mListener != null) {
                mListener.onHolderMemberClicked(itemView,view,getAdapterPosition());
                //dismiss();
            }
        }



        @Override
        public boolean onLongClick(View view) {
//
            if(mLongClicklistener != null){
               return mLongClicklistener.onHolderMemberLongClicked(itemView,view,getAdapterPosition());
            }
            return  true;
        }


    }






    public IHolderMemberLongClickListener getHolderMemberLongClicklistener() {
        return mLongClicklistener;
    }

    public void setHolderMemberLongClicklistener(IHolderMemberLongClickListener mLongClicklistener) {
        this.mLongClicklistener = mLongClicklistener;
    }
    public IHolderMemberClickListener getHolderMemberListener() {
        return mListener;
    }

    public void setHolderMemberListener(IHolderMemberClickListener mListener) {
        this.mListener = mListener;
    }




    public LinkedList<CheckInfo> getCurrentdata() {
        return mCurrentdata;
    }

    public void setCurrentdata(LinkedList<CheckInfo> mCurrentdata) {
        this.mCurrentdata = mCurrentdata;
    }

    public LinkedList<CheckInfo> getCheckedData() {
        return mCheckedData;
    }

    public void setCheckedData(LinkedList<CheckInfo> mCheckedData) {
        this.mCheckedData = mCheckedData;
    }

    public LinkedList<CheckInfo> getUncheckData() {
        return mUncheckData;
    }

    public void setUncheckData(LinkedList<CheckInfo> mUncheckData) {
        this.mUncheckData = mUncheckData;
    }
    public LinkedList<CheckInfo> getPckBillData() {
        return mPckBillData;
    }

    public void setPckBillData(LinkedList<CheckInfo> mPckBillData) {
        this.mPckBillData = mPckBillData;
    }


    public boolean isAllChecked() {
        return  mCheckedData.containsAll(mPckBillData);

    }


    public List<CheckInfo> getNonChecked() {
        List<CheckInfo> infos = new ArrayList<>();
        for(CheckInfo info:mPckBillData){
            if(info.getColorState()!=CheckInfo.COLOR_checked){
                infos.add(info);
            }
        }
        return infos;
    }



    public void clearAllDatas() {
        mPckBillData.clear();
        mUncheckData.clear();
        mCheckedData.clear();
    }


}