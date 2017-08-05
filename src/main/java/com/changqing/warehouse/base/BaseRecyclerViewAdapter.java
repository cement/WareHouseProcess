package com.changqing.warehouse.base;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * Created by Administrator on 2017/6/14 0014.
 */

public abstract class BaseRecyclerViewAdapter<D> extends RecyclerView.Adapter<BaseViewHolder>{




    public D data ;



    @Override
    public abstract BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) ;


    @Override
    public abstract void onBindViewHolder(BaseViewHolder holder, int position) ;

    public abstract D getItemData(int position);
    public D getAdapterData() {
        return data;
    }

    public void setAdapterData(D data) {
        this.data = data;
    }


}
