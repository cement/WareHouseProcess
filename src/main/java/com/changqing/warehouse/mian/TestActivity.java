package com.changqing.warehouse.mian;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.print.PrintHelper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.changqing.warehouse.R;

/**
 * Created by Administrator on 2017/6/30 0030.
 */

public class TestActivity extends AppCompatActivity{
    private static final String TAG = TestActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.layout);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.base_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        if(item.getItemId()== R.id.base_print_start){
//            Log.d(TAG, "onOptionsItemSelected() called with: item = [" + item + "]");
//            doPhotoPrint();
//            return true;
//        }


        return false;
    }
    private void doPhotoPrint() {
        PrintHelper photoPrinter = new PrintHelper(this);
        photoPrinter.setScaleMode(PrintHelper.SCALE_MODE_FIT);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_round);
        Log.d(TAG, "doPhotoPrint() called  bitmap="+bitmap);
        Log.d(TAG, "doPhotoPrint() called  photoPrinter="+photoPrinter);
        photoPrinter.printBitmap("droids.png", bitmap);
    }

}
