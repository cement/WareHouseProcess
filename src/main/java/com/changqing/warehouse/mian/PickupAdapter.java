package com.changqing.warehouse.mian;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.changqing.warehouse.R;
import com.changqing.warehouse.base.BaseViewHolder;
import com.changqing.warehouse.bean.PickupInfo;
import com.changqing.warehouse.mian.inteface.IHolderMemberClickListener;
import com.changqing.warehouse.mian.inteface.IHolderMemberLongClickListener;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class PickupAdapter extends RecyclerView.Adapter<BaseViewHolder> {




//    private PckBill mPckBillData = new PckBill();
//    private PckBill mUnpickData = new PckBill();
//    private PckBill mPickupData = new PckBill();
//    private PckBill mBasketData = new PckBill();
//
//
//
    private LinkedList<PickupInfo> mRemarkedData = new LinkedList<>();


//    private PckBill data = mPckBillData;]
    private LinkedList<PickupInfo> mPckBillData = new LinkedList<>();
    private LinkedList<PickupInfo> mUnpickData = new LinkedList<>();
    private LinkedList<PickupInfo> mPickupData = new LinkedList<>();
    private LinkedList<PickupInfo> mBasketData = new LinkedList<>();
    private LinkedList<PickupInfo> mCurrentData = mPckBillData;





    private IHolderMemberClickListener mListener;



    private IHolderMemberLongClickListener mLongClicklistener;


    private View.OnContextClickListener mContextListener;


    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view =  inflater.inflate(R.layout.pickup_item_layout, parent, false);
        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        PickupInfo sortingInfo = getCurrentData().get(position);
        holder.bindData(sortingInfo);

    }



    @Override
    public int getItemCount() {
        return  mCurrentData.size();
    }

    public LinkedList<PickupInfo> getRemarkedData() {
        return mRemarkedData;
    }

    public void setRemarkedData(LinkedList<PickupInfo> mRemarkedData) {
        this.mRemarkedData = mRemarkedData;
    }


    private class MyViewHolder extends BaseViewHolder implements View.OnClickListener,View.OnLongClickListener{

        TextView pckBillnoView;
        TextView LocNameView;
        TextView InspSeqNoView;
        TextView quantityView;
        TextView shippingAreaView;
        TextView actionView;
        private TextView comnameView;

        public MyViewHolder(View itemView) {
            super(itemView);

        }

        @Override
        public void bindView() {

            LocNameView = (TextView) itemView.findViewById(R.id.item_LocName);
            InspSeqNoView = (TextView) itemView.findViewById(R.id.item_InspSeqNo);
            quantityView = (TextView) itemView.findViewById(R.id.item_quantity);
            shippingAreaView = (TextView) itemView.findViewById(R.id.item_shippingArea);
            comnameView = (TextView) itemView.findViewById(R.id.item_comnmae);
            //actionView = (TextView) itemView.findViewById(R.id.item_action);
            itemView.setOnLongClickListener(this);
//            itemView.setOnContextClickListener(this);
            //actionView.setOnClickListener(this);
        }

        @Override
        public void bindData(Object data) {
            itemView.setTag(data);
            PickupInfo pickupInfo = (PickupInfo) data;
            //String whcode = pickupInfo.getWarehouseCode();

//                if(pickupInfo.isCurrent()){
//                    int curentColor = PickupInfo.BGcolors[PickupInfo.STATE_curent];
//                    ((CardView)itemView).setCardBackgroundColor(curentColor);
//                    pickupInfo.setCurrent(false);
//                }else{
//                    int stateColor = PickupInfo.BGcolors[pickupInfo.getState()];
//                    ((CardView)itemView).setCardBackgroundColor(stateColor);
//                }

               if(pickupInfo.getColorState()==PickupInfo.COLOR_CURRENT_pickup){
                   ((CardView)itemView).setCardBackgroundColor(pickupInfo.getColorState());
                   pickupInfo.setColorState(PickupInfo.COLOR_pickup);
               }else
               if(pickupInfo.getColorState()==PickupInfo.COLOR_CURRENT_basket){
                   ((CardView)itemView).setCardBackgroundColor(pickupInfo.getColorState());
                   pickupInfo.setColorState(PickupInfo.COLOR_basket);
               }
               else{
                   ((CardView)itemView).setCardBackgroundColor(pickupInfo.getColorState());
               }

                LocNameView.setText(pickupInfo.getLocName());
                InspSeqNoView.setText(pickupInfo.getInspSeqNo());
                comnameView.setText(pickupInfo.getComname());
                quantityView.setText(pickupInfo.getQuantity());
                shippingAreaView.setText(pickupInfo.getShippingArea());

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

    public LinkedList<PickupInfo> getPckBillData() {
        return mPckBillData;
    }

    public void setPckBillData(LinkedList<PickupInfo> mPckBillData) {
        this.mPckBillData = mPckBillData;
    }


    public LinkedList<PickupInfo> getUnpickData() {
        return mUnpickData;
    }

    public void setUnpickData(LinkedList<PickupInfo> mUnpickData) {
        this.mUnpickData = mUnpickData;
    }

    public LinkedList<PickupInfo> getPickupData() {
        return mPickupData;
    }

    public void setPickupData(LinkedList<PickupInfo> mPickupData) {
        this.mPickupData = mPickupData;
    }

    public LinkedList<PickupInfo> getBasketData() {
        return mBasketData;
    }

    public void setBasketData(LinkedList<PickupInfo> mBasketData) {
        this.mBasketData = mBasketData;
    }

    public LinkedList<PickupInfo> getCurrentData() {
        return mCurrentData;
    }

    public void setCurrentData(LinkedList<PickupInfo> data) {
        this.mCurrentData  =  data;
    }

    public void swarpData(LinkedList<PickupInfo> data){
        List<PickupInfo> oldData = this.getCurrentData();
        this.setCurrentData(data);
        oldData = null;
    }

    public void clearAllDatas(){
        mPckBillData.clear();
        mBasketData.clear();
        mPickupData.clear();
        mUnpickData.clear();
    }


    public boolean isAllBasketed() {
        return  mBasketData.containsAll(mPckBillData);

    }

    public List<PickupInfo> getNonBasketed() {
        List<PickupInfo> infos = new ArrayList<>();
        for(PickupInfo info:mPckBillData){
            if(info.getColorState()!=PickupInfo.COLOR_basket){
                infos.add(info);
            }
        }
        return infos;
    }


}