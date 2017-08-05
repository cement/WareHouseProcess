package com.changqing.warehouse.mian;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

/**
 * Created by Administrator on 2017/6/7 0007.
 */

public class PickupSubmitLoader extends AsyncTaskLoader<Integer> {
    private static final String TAG = "DispachBillLoader";

    private Bundle bundle;
    public PickupSubmitLoader(Context context, Bundle bundle) {
           super(context);
           this.bundle = bundle;
        }
        @Override
        public Integer loadInBackground() {

            return  100;
        }

        @Override
        public void cancelLoadInBackground() {
            Log.d(TAG, "cancelLoadInBackground() called");
            super.cancelLoadInBackground();
        }

        @Override
        public void deliverResult(Integer data) {
            super.deliverResult(data);
        }

        @Override
        protected void onStartLoading() {
            Log.d(TAG, "onStartLoading() called");
           forceLoad();

        }

        @Override
        protected void onStopLoading() {
            cancelLoad();
        }


        @Override
        protected void onReset() {
            super.onReset();
        }

    }