package com.changqing.warehouse.base;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Administrator on 2017/6/14 0014.
 */

public abstract  class BaseViewHolder extends RecyclerView.ViewHolder {
    public BaseViewHolder(View itemView) {
        super(itemView);
        bindView();
    }

    public abstract void bindView();

    public abstract void bindData(Object data);
}
