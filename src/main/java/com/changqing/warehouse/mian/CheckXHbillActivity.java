package com.changqing.warehouse.mian;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.Toast;

import com.changqing.warehouse.R;
import com.changqing.warehouse.db.dto.CheckDto;

import org.w3c.dom.Text;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/7/22 0022.
 */

public class CheckXHbillActivity extends AppCompatActivity{
    private CheckXHbillAdapter xhbilladapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.check_xhbill_layout);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.check_xhbill_recycleview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        xhbilladapter = new CheckXHbillAdapter();
        recyclerView.setAdapter(xhbilladapter);

        Intent intent = getIntent();

        startQueryXHbil(intent);


    }



    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        startQueryXHbil(intent);
    }
    private void startQueryXHbil(Intent intent) {
        String vehicleno = intent.getStringExtra("vehicleno");
        String basketno  = intent.getStringExtra("basketno");
        if(!TextUtils.isEmpty(vehicleno)&& !TextUtils.isEmpty(basketno)){
            CheckXHbillTask xHbillTask = new CheckXHbillTask();
            xHbillTask.execute(vehicleno,basketno);
        }else{
            Toast.makeText(this,"",Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    public class CheckXHbillTask extends AsyncTask<String,Void,List<Map<String,String>>>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected List<Map<String, String>> doInBackground(String... params) {
            try {
                return CheckDto.queryXhbill(params[0],params[1]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Map<String, String>> datas) {
            if(datas!=null){
                xhbilladapter.setDatas(datas);
                xhbilladapter.notifyDataSetChanged();
            }else{
                Toast.makeText(CheckXHbillActivity.this,"返回数据是空",Toast.LENGTH_SHORT).show();
            }


        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }


}
