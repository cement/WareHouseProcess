package com.changqing.warehouse.mian;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.changqing.warehouse.R;
import com.changqing.warehouse.Utils.CompareUtils;
import com.changqing.warehouse.Utils.Utils;
import com.changqing.warehouse.app.App;
import com.changqing.warehouse.bean.CheckInfo;
import com.changqing.warehouse.bean.DataBaseResult;
import com.changqing.warehouse.bean.UserBean;
import com.changqing.warehouse.db.dto.CheckDto;
import com.changqing.warehouse.helper.BottomNavigationViewHelper;
import com.changqing.warehouse.mian.inteface.IHolderMemberClickListener;
import com.changqing.warehouse.mian.inteface.IHolderMemberLongClickListener;
import com.xys.libzxing.zxing.activity.CaptureActivity;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


public class CheckActivity extends BaseActivity implements IHolderMemberClickListener, IHolderMemberLongClickListener,
        SearchView.OnQueryTextListener,
        BottomNavigationView.OnNavigationItemSelectedListener,View.OnClickListener {
    public static final String TAG = CheckActivity.class.getSimpleName();
    //pckBillno  LocName  InspSeqNo quantity   shippingArea   remark
    //调号         货位     验号      数量         集号          区号

    public static final int SORT_BY_LocName = 0;
    public static final int SORT_BY_InspSeqNo = 1;
    public static final int SORT_BY_quantity = 2;
    public static final int SORT_BY_shippingArea = 3;


    public static final int LOADER_OBTAIN_FLAG = 1000;
    public static final int LOADER_SUBMIT_FLAG = 1001;

    private int sortby = SORT_BY_LocName;

    private SearchView mSearchView;
    private SearchView.SearchAutoComplete mSearchAutoComplete;
    private Button mActionButton;
    private TextView packBillnoView;
    private CheckAdapter mCheckAdapter;
    private FrameLayout mContent;

    private RecyclerView mRecyclerView;

    private TextView titleQuantity;
    private TextView titleLocName;
    private TextView titleshippingArea;
    private TextView amountHintView;
    private BottomNavigationView bottomNaviView;


    private CheckObtainAsyncTask mCheckObtainTask;
    private ProgressBar pregressBar;
    private CheckSubmitAsyncTask mCheckSubmitTask;


    private String currentPckBillno;
    private ImageView mQrcodeIcon;
    private ProgressDialog progerssDialog;
    private TextView checkPersonView;
    private TextView chehaoView;
    private String currentChehao;
    //private TextView packPersonView;
    private TextView mPromptSuccessView;
    private TextView mPromptErrorView;
    private TextView titleInspSeqNo;
    private PopupWindow checkWriteRemarkPopup;
    private UserBean mUserBean;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 隐藏标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 隐藏状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.check_activity_layout);


        mSearchView = (SearchView) findViewById(R.id.check_input_searchview);
        mSearchAutoComplete = (SearchView.SearchAutoComplete) mSearchView.findViewById(R.id.search_src_text);
        mQrcodeIcon = (ImageView) findViewById(R.id.check_zxing_icon);
        mQrcodeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(CheckActivity.this, CaptureActivity.class), 0);
            }
        });
        //hideSoftInputMethod(mSearchAutoComplete);

        mSearchView.onActionViewExpanded();
        mSearchView.setOnQueryTextListener(this);
        mSearchAutoComplete.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
//                Log.d(TAG, "onFocusChange() called with: v = [" + v + "], hasFocus = [" + hasFocus + "]");
//                if(hasFocus){
//                    Utils.hideSoftInputBord(mSearchAutoComplete);
//                }
                //Utils.hideSoftInputBord(mSearchAutoComplete);
                //Utils.hideSoftInputMethod(mSearchAutoComplete);
            }
        });

        mSearchView.clearFocus();

        mActionButton = (Button) findViewById(R.id.check_input_obtainbutton);
        mActionButton.setOnClickListener(this);
        //mObtainLoaderCallback = new PckBillObtainLoaderCallback();
//        mObtainButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Editable editable = mSearchAutoComplete.getText();
//                if(!TextUtils.isEmpty(editable)){
//                    if(editable.toString().matches("^[cC][0-9]{2}")){
//                        mSearchAutoComplete.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
//                        mSearchAutoComplete.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_ENTER));
//                    }else{
//                        Toast.makeText(CheckActivity.this,"输入车号格式错误",Toast.LENGTH_SHORT).show();
//                    }
//                }else{
//                    Toast.makeText(CheckActivity.this,"请输入车号",Toast.LENGTH_SHORT).show();
//                }
//
//                mObtainButton.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        Utils.hideSoftInputBord(mObtainButton);
//                    }
//                },100);
//                //Utils.hideSoftInputBord(mObtainButton);
////                mCheckObtainTask = new CheckObtainAsyncTask();
////                mCheckObtainTask.execute("");
//            }
//        });

        packBillnoView = (TextView) findViewById(R.id.check_content_packbillno);
        amountHintView = (TextView) findViewById(R.id.check_content_amount_hint);
        checkPersonView = (TextView) findViewById(R.id.check_checkperson);
       // packPersonView = (TextView) findViewById(R.id.check_packperson);
        if(savedInstanceState==null){
            mUserBean = (UserBean) getIntent().getSerializableExtra("userBean");
            checkPersonView.setText(mUserBean.getUserName());
        }else{
            mUserBean = (UserBean) savedInstanceState.getSerializable("userbean");
            checkPersonView.setText(mUserBean.getUserName());
        }
        chehaoView = (TextView) findViewById(R.id.check_chehao);
        mContent = (FrameLayout) findViewById(R.id.check_content);

        mRecyclerView = (RecyclerView) getLayoutInflater().inflate(R.layout.content_layout, mContent, false);


        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mCheckAdapter = new CheckAdapter();
        mCheckAdapter.setHolderMemberLongClicklistener(this);
        mCheckAdapter.setHolderMemberListener(this);
        mRecyclerView.setAdapter(mCheckAdapter);

        mContent.addView(mRecyclerView);


        mPromptSuccessView = (TextView) findViewById(R.id.check_prompt_success);
        mPromptErrorView = (TextView) findViewById(R.id.check_prompt_error);
        //mSubmitLoaderCallback = new PckBillSubmitLoaderCallback();
 //       FloatingActionButton submitButton = (FloatingActionButton) findViewById(R.id.check_footer_floatbutton);
//        submitButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.d(TAG, "floatingActionButton ---------- onClick() called with: view = [" + view + "]");
//
//                if(TextUtils.isEmpty(currentChehao) || TextUtils.isEmpty(currentPckBillno) ){
//                    Toast.makeText(App.getAppContext(),"你还没有绑定车号",Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                if(!mCheckAdapter.isAllBasketed()){
//                    //TODO ..................................
//                    Toast.makeText(CheckActivity.this,"此处有药品尚未复核完，这里用提示演示",Toast.LENGTH_SHORT).show();
////                    List<CheckInfo> infos = mCheckAdapter.getNonChecked();
////                    StringBuffer sbuf= new StringBuffer();
////                    for (CheckInfo info:infos){
////                        if(TextUtils.isEmpty(info.getRemark())){
////                            sbuf.append(info.getShippingArea()+"/"+info.getComname()+"/"+info.getQuantity()+"\r\n");
////                        }
////                    }
////                    if(!TextUtils.isEmpty(sbuf)){
////                        Toast.makeText(App.getAppContext(),"还有药品没有复核:\r\n集号/名称/数量\r\n"+sbuf,Toast.LENGTH_SHORT).show();
////                    }
////                    return;
//                }
//
//                showSubmitAlertDialog();
//            }
//        });

//        FloatingActionButton printButton = (FloatingActionButton) findViewById(R.id.print_footer_floatbutton);
//        printButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //Todo
//                Log.d(TAG, "onClick() called with: view = [" + view + "]");
//                String toastStr = "当前车号："+currentChehao+"\r\n"
//                                  +"当前调度单号："+currentPckBillno;
//                Toast.makeText(CheckActivity.this,toastStr,Toast.LENGTH_SHORT).show();
//
//            }
//        });

        bottomNaviView = (BottomNavigationView) findViewById(R.id.check_bottom_navigationview);
        bottomNaviView.setOnNavigationItemSelectedListener(this);
        BottomNavigationViewHelper.disableShiftMode(bottomNaviView);


        //View.OnClickListener listener = new sortClickListener();
        titleQuantity = (TextView) findViewById(R.id.check_item_title_quantity);
        //titleQuantity.setOnClickListener(this);

//        titleInspSeqNo = (TextView) findViewById(R.id.check_item_title_InspSeqNo);
//        titleInspSeqNo.setOnClickListener(listener);

        titleLocName = (TextView) findViewById(R.id.check_item_title_comname);
        //titleLocName.setOnClickListener(this);

        titleshippingArea = (TextView) findViewById(R.id.check_item_title_shippingArea);
        titleshippingArea.setTag(true);
       // titleshippingArea.setOnClickListener(this);


        setQuantityHint();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        Log.d(TAG, "onWindowFocusChanged() called with: hasFocus = [" + hasFocus + "]");
        if (hasFocus) {
            mSearchAutoComplete.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Utils.hideSoftInputBord(getWindow().getDecorView());
                }
            }, 0);
        }
        super.onWindowFocusChanged(hasFocus);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            //
            case R.id.check_item_title_comname :
                LinkedList<CheckInfo> datas1 = mCheckAdapter.getCurrentdata();
                Collections.sort(datas1, CompareUtils.compareByCheckLocName);
                sortby = SORT_BY_LocName;
                mCheckAdapter.notifyDataSetChanged();
                break;
            case R.id.check_item_title_shippingArea:
                if(view.getTag()!=null && (boolean)view.getTag()){
                    LinkedList<CheckInfo> datas2 = mCheckAdapter.getCurrentdata();
                    Collections.sort(datas2, CompareUtils.compareCheckDesc);
                    view.setTag(false);

                }else{
                    LinkedList<CheckInfo> datas2 = mCheckAdapter.getCurrentdata();
                    Collections.sort(datas2, CompareUtils.compareCheckAsc);
                    view.setTag(true);
                }
                sortby = SORT_BY_shippingArea;
                mCheckAdapter.notifyDataSetChanged();
                break;
            case R.id.check_write_remmark_submitbutton_add:

                CheckInfo checkInfo1 = (CheckInfo) view.getTag();
                EditText addEditText = (EditText) view.getTag(R.id.check_write_remmark_edittext);
                String addDiff = addEditText.getText().toString();

                if(!TextUtils.isEmpty(addDiff)){
                    checkInfo1.setRemark("[+"+addDiff+"]");
                    mCheckAdapter.getUncheckData().remove(checkInfo1);
                    mCheckAdapter.getCheckedData().addFirst(checkInfo1);
                    mCheckAdapter.getRemarkData().addFirst(checkInfo1);
                    checkInfo1.setColorState(CheckInfo.COLOR_checked);
                    mCheckAdapter.notifyDataSetChanged();
                }else{
                    Toast.makeText(this,"数量不能为空",Toast.LENGTH_SHORT).show();
                }

//                if(!TextUtils.isEmpty(addDiff) && TextUtils.isEmpty(subDiff)){
//                    checkInfo.setRemark("[+"+addDiff+"]");
//                    mCheckAdapter.getRemarkData().addFirst(checkInfo);
//                    checkInfo.setState(CheckInfo.STATE_checked);
//                    mCheckAdapter.notifyDataSetChanged();
//                }else if(TextUtils.isEmpty(addDiff) && !TextUtils.isEmpty(subDiff)){
//                    mCheckAdapter.getRemarkData().addFirst(checkInfo);
//                    checkInfo.setRemark("[-"+subDiff+"]");
//                    checkInfo.setState(CheckInfo.STATE_checked);
//                    mCheckAdapter.notifyDataSetChanged();
//                }else if(TextUtils.isEmpty(addDiff) && TextUtils.isEmpty(subDiff)){
//                    Toast.makeText(this, "不能都为空", Toast.LENGTH_SHORT).show();
//                }else if(!TextUtils.isEmpty(addDiff) && !TextUtils.isEmpty(subDiff)){
//                    Toast.makeText(this, "不能都有数值", Toast.LENGTH_SHORT).show();
//                }

                //Log.d(TAG, "-----------addEditText------- = [" + addEditText.getText() + "]");
               // Log.d(TAG, "-----------subEditText------- = [" + subEditText.getText() + "]");
                //TODO
                checkWriteRemarkPopup.dismiss();
                Utils.hideSoftInputBord(view);

                break;
            case R.id.check_write_remmark_submitbutton_sub:
                CheckInfo checkInfo2 = (CheckInfo) view.getTag();
                EditText subEditText = (EditText) view.getTag(R.id.check_write_remmark_edittext);
                String subDiff = subEditText.getText().toString();


                if(!TextUtils.isEmpty(subDiff)){
                    checkInfo2.setRemark("[-"+subDiff+"]");
                    mCheckAdapter.getUncheckData().remove(checkInfo2);
                    mCheckAdapter.getCheckedData().addFirst(checkInfo2);
                    mCheckAdapter.getRemarkData().addFirst(checkInfo2);

                    checkInfo2.setColorState(CheckInfo.COLOR_checked);
                    mCheckAdapter.notifyDataSetChanged();
                }else{
                    Toast.makeText(this,"数量不能为空",Toast.LENGTH_SHORT).show();
                }
                checkWriteRemarkPopup.dismiss();
                Utils.hideSoftInputBord(view);
                break;
            case  R.id.check_input_obtainbutton:

                if(mActionButton.getText().equals("领单")){
                    doObtainPckbill();
//                    Editable editable = mSearchAutoComplete.getText();
//                    if(!TextUtils.isEmpty(editable)){
//                        if(editable.toString().matches("^[cC][0-9]{2}")){
//                            mSearchAutoComplete.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
//                            mSearchAutoComplete.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_ENTER));
//                        }else{
//                            Toast.makeText(CheckActivity.this,"输入车号格式错误",Toast.LENGTH_SHORT).show();
//                        }
//                    }else{
//                        Toast.makeText(CheckActivity.this,"请输入车号",Toast.LENGTH_SHORT).show();
//                    }
                }else if(mActionButton.getText().equals("提交")){
                    doSubmitPckbill();
                }

                mActionButton.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Utils.hideSoftInputBord(mActionButton);
                    }
                },100);
                break;


        }



    }


//    public class sortClickListener implements View.OnClickListener {
//
//        @Override
//        public void onClick(View view) {
//
//            LinkedList<CheckInfo> datas = mCheckAdapter.getCurrentdata();
//
//            if (view.equals(titleLocName) && sortby != SORT_BY_LocName) {
//                Collections.sort(datas, CompareUtils.compareByCheckLocName);
//                sortby = SORT_BY_LocName;
//                mCheckAdapter.notifyDataSetChanged();
//            } else if (view.equals(titleshippingArea) && sortby != SORT_BY_shippingArea) {
//                Collections.sort(datas, CompareUtils.compareByCheckShippingArea);
//                sortby = SORT_BY_shippingArea;
//                mCheckAdapter.notifyDataSetChanged();
//
//            }
//        }
//    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.d("....>;", "onConfigurationChanged() called with: newConfig = [" + newConfig + "]");

        Utils.hideSoftInputBord(getCurrentFocus());
    }




    public boolean onQueryTextSubmit(String query) {
        String queryTemp = query.trim().toUpperCase();
        if(queryTemp.matches("^C[0-9]{2}")){
            if(TextUtils.isEmpty(currentChehao)){
                showPromptObtainPckbillnoDialog(queryTemp);
            }else if(queryTemp.equals(currentChehao)){
                Toast.makeText(this,"你正在复核"+currentChehao+"号车",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this,"你有未完成的复核任务，车号"+currentChehao,Toast.LENGTH_SHORT).show();
            }
            mSearchView.onActionViewExpanded();
            mSearchAutoComplete.setText("");
        }else if(queryTemp.matches("^K[0-9]{4}")){
            //启动一个Activity 展示该筐内容
           String jihao = queryTemp.replaceAll("[Kk0]","");
            Log.d(TAG, "onQueryTextSubmit() called with: query = [" + jihao + "]");
             Intent intent = new Intent(this,CheckXHbillActivity.class);
              intent.putExtra("vehicleno",currentChehao);
              intent.putExtra("basketno",jihao);
             startActivity(intent);
            Toast.makeText(this,"你扫描的是集号："+jihao,Toast.LENGTH_SHORT).show();
        }else if(queryTemp.matches("[0-9]+")){
            Toast.makeText(this,"你扫描的是验号",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this,"未使用的扫描码",Toast.LENGTH_SHORT).show();
        }

        Utils.hideSoftInputBord(mActionButton);
        return true;
    }

    private void showPromptObtainPckbillnoDialog(final String chehao) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("温馨提示");
            builder.setIcon(R.drawable.ic_home_black_24dp);
            builder.setMessage("确定要通过车号领单吗？");
            builder.setNeutralButton("撤销", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // mSearchView.clearFocus();
                }
            });
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //TODO 提交处理task
                    mCheckObtainTask = new CheckObtainAsyncTask();
                    mCheckObtainTask.execute(chehao);
                }
            });
            builder.create();
            builder.show();

    }


    @Override
    public boolean onQueryTextChange(String newText) {
        return true;
    }
    /*
     底部导航监听
     */
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.bottom_navigation_pckbill:
                if (mCheckAdapter.getCurrentdata() != mCheckAdapter.getPckBillData()) {
                    mCheckAdapter.setCurrentdata(mCheckAdapter.getPckBillData());
                    mCheckAdapter.notifyDataSetChanged();
                }
                return true;
            case R.id.bottom_navigation_uncheck:
                if (mCheckAdapter.getCurrentdata() != mCheckAdapter.getUncheckData()) {
                    mCheckAdapter.setCurrentdata(mCheckAdapter.getUncheckData());
                    mCheckAdapter.notifyDataSetChanged();
                }
                return true;
            case R.id.bottom_navigation_checked:
                if (mCheckAdapter.getCurrentdata() != mCheckAdapter.getCheckedData()) {
                    mCheckAdapter.setCurrentdata(mCheckAdapter.getCheckedData());
                    mCheckAdapter.notifyDataSetChanged();
                }
                return true;
        }
        return false;
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("pckBilldata", mCheckAdapter.getPckBillData());
        outState.putSerializable("checkedData", mCheckAdapter.getCheckedData());
        outState.putSerializable("uncheckData", mCheckAdapter.getUncheckData());
        outState.putInt("selectedItemId",bottomNaviView.getSelectedItemId());
        outState.putSerializable("userBean",mUserBean);
        super.onSaveInstanceState(outState);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        LinkedList<CheckInfo> pckBilldata = (LinkedList<CheckInfo>) savedInstanceState.getSerializable("pckBilldata");
        if (pckBilldata != null) {
            mCheckAdapter.setPckBillData(pckBilldata);
        }

        LinkedList<CheckInfo> checkedData = (LinkedList<CheckInfo>) savedInstanceState.getSerializable("checkedData");
        if (checkedData != null) {
            mCheckAdapter.setCheckedData(checkedData);
        }

        LinkedList<CheckInfo> uncheckData = (LinkedList<CheckInfo>) savedInstanceState.getSerializable("uncheckData");
        if (uncheckData != null) {
            mCheckAdapter.setUncheckData(uncheckData);
        }
        Serializable userBean = savedInstanceState.getSerializable("userBean");
        if(userBean!=null && userBean instanceof  UserBean){
            App.setUserBean((UserBean) userBean);
        }

        int selectItemId = savedInstanceState.getInt("selectedItemId");
        bottomNaviView.setSelectedItemId(selectItemId);
        setQuantityHint();
        super.onRestoreInstanceState(savedInstanceState);

    }

    @Override
    public void onHolderMemberClicked(View itemView, View clickView, int position) {

        CheckInfo checkInfo = (CheckInfo) itemView.getTag();
        if (checkInfo.getColorState() == CheckInfo.COLOR_uncheck) {
            checkInfo.setColorState(CheckInfo.COLOR_checked);
            mCheckAdapter.getUncheckData().remove(checkInfo);
            mCheckAdapter.getCheckedData().addFirst(checkInfo);
            mCheckAdapter.notifyDataSetChanged();
            setQuantityHint();
        }
    }


    @Override
    public boolean onHolderMemberLongClicked(View itemView, View clickView, int position) {
        final CheckInfo checkInfo = (CheckInfo) itemView.getTag();
        showRecordAlertDialog(checkInfo);
//        if (itemView.equals(clickView)) {
//            final CheckInfo checkInfo = (CheckInfo) itemView.getTag();
//            if (checkInfo.getState() == CheckInfo.STATE_checked) {
//                String message = "确定要撤销操作吗？\r\n" + " 名称： " + checkInfo.getLocName() + ";   框号：" + checkInfo.getShippingArea();
//                Snackbar.make(getWindow().getDecorView(), message, Snackbar.LENGTH_SHORT).setAction("确 定", new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Log.d(TAG, "onClick() called with: v = [" + v + "]");
//                        checkInfo.setState(CheckInfo.STATE_uncheck);
//
//                        mCheckAdapter.getUncheckData().addFirst(checkInfo);
//                        mCheckAdapter.getCheckedData().remove(checkInfo);
//                        mCheckAdapter.notifyDataSetChanged();
//                        setQuantityHint();
//                    }
//                }).show();
//             }
//            return true;
//        }
        return true;
    }

    public void showRecordAlertDialog(final CheckInfo checkInfo){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("温馨提示");
        builder.setIcon(R.drawable.ic_home_black_24dp);
        builder.setMessage("药名："+checkInfo.getComname());

        final View alertView = getLayoutInflater().inflate(R.layout.alert_layout, null);

        TextView LocNameView = (TextView) alertView.findViewById(R.id.alert_item_LocName);
        TextView InspSeqNoView = (TextView) alertView.findViewById(R.id.alert_item_InspSeqNo);
        TextView quantityView = (TextView) alertView.findViewById(R.id.alert_item_quantity);
        TextView shippingAreaView = (TextView) alertView.findViewById(R.id.alert_item_shippingArea);

        LocNameView.setText(checkInfo.getLocName());
        InspSeqNoView.setText(checkInfo.getInspSeqNo());
        quantityView.setText(checkInfo.getQuantity());
        shippingAreaView.setText(checkInfo.getShippingArea());

        builder.setView(alertView);


        builder.setNeutralButton("撤销复核确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(checkInfo.getColorState()==CheckInfo.COLOR_checked){
                    mCheckAdapter.getCheckedData().remove(checkInfo);
                    mCheckAdapter.getUncheckData().addFirst(checkInfo);
                    setQuantityHint();
                    checkInfo.setColorState(CheckInfo.COLOR_uncheck);
                    mCheckAdapter.notifyDataSetChanged();
                }else{
                    Toast.makeText(CheckActivity.this,"该药品未复核\r\n"+checkInfo.getComname(),Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("复核说明", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //TODO
                showWriteRemarkPopupWindow(checkInfo);
            }
        });
        builder.setPositiveButton("手动确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(checkInfo.getColorState()==CheckInfo.COLOR_uncheck){
                    mCheckAdapter.getUncheckData().remove(checkInfo);
                    mCheckAdapter.getCheckedData().addFirst(checkInfo);
                    setQuantityHint();
                    checkInfo.setColorState(CheckInfo.COLOR_checked);
                    mCheckAdapter.notifyDataSetChanged();
                }else{
                    Toast.makeText(CheckActivity.this,"该药品已复核\r\n"+checkInfo.getComname(),Toast.LENGTH_SHORT).show();
                }

            }
        });
        builder.create();
        builder.show();

    }

    private void showWriteRemarkPopupWindow(CheckInfo sortingInfo) {
        View writeRemarkView = LayoutInflater.from(this).inflate(R.layout.popup_check_write_remark_layout,null);
        checkWriteRemarkPopup = new PopupWindow(writeRemarkView,WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        checkWriteRemarkPopup.setAnimationStyle(R.style.popup_anim_add_cart);
        checkWriteRemarkPopup.setBackgroundDrawable(new ColorDrawable(0xffdbdbdb));
        checkWriteRemarkPopup.setFocusable(true);
        //以下两行让软键盘把popupwindow顶上去
        checkWriteRemarkPopup.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        checkWriteRemarkPopup.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        checkWriteRemarkPopup.setOnDismissListener(new CheckActivity.PopupDismissListener());

        //View.OnClickListener listener = new PickupActivity.PopupRemarkClickListener();
        ImageView cancelImage = (ImageView) writeRemarkView.findViewById(R.id.check_write_remmark_cancel);
        cancelImage.setTag(checkWriteRemarkPopup);
        cancelImage.setOnClickListener(this);
//        EditText addEditText = (EditText) writeRemarkView.findViewById(R.id.check_write_remmark_edittext_add);
//        addEditText.setTag(sortingInfo);
//        addEditText.setOnClickListener(this);

        EditText subEditText = (EditText) writeRemarkView.findViewById(R.id.check_write_remmark_edittext);
        subEditText.setTag(sortingInfo);
        subEditText.setOnClickListener(this);
        Button   submitAddButton  = (Button) writeRemarkView.findViewById(R.id.check_write_remmark_submitbutton_add);
        submitAddButton.setTag(sortingInfo);
        submitAddButton.setTag(R.id.check_write_remmark_edittext,subEditText);
        submitAddButton.setOnClickListener(this);

        Button   submitSubButton  = (Button) writeRemarkView.findViewById(R.id.check_write_remmark_submitbutton_sub);
        submitSubButton.setTag(sortingInfo);
        submitSubButton.setTag(R.id.check_write_remmark_edittext,subEditText);
        submitSubButton.setOnClickListener(this);
        //submitButton.setTag(writEditText);
        //submitButton.setTag(R.id.check_write_remmark_edittext_add,addEditText);

        setWindowBackgroundAlpha(0.6f);
        checkWriteRemarkPopup.showAtLocation(mContent, Gravity.BOTTOM,0,0);


    }
    public void showSubmitAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("温馨提示");
        builder.setIcon(R.drawable.ic_home_black_24dp);
        builder.setMessage("确定提交本次复核？");
        builder.setNeutralButton("撤销", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
              mSearchView.clearFocus();
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //TODO
                mCheckSubmitTask = new CheckSubmitAsyncTask();
                mCheckSubmitTask.execute(currentPckBillno);
                mSearchView.clearFocus();
            }
        });
        builder.create();
        builder.show();

    }

    public class PopupDismissListener implements PopupWindow.OnDismissListener{
        @Override
        public void onDismiss() {
            setWindowBackgroundAlpha(1.0f);
        }
    }

    //pckBillno  LocName  InspSeqNo quantity   shippingArea   remark
    //调号         货位     验号      数量         集号          区号


    private class CheckObtainAsyncTask extends AsyncTask<String,Void,DataBaseResult<CheckInfo>> {
        private String chehao;
        private String username;

        @Override
        protected void onPreExecute() {
            setupProgressBar(true);
            currentPckBillno = "";
        }

        @Override
        protected DataBaseResult<CheckInfo> doInBackground(String... params) {

            chehao = params[0];
            username = mUserBean.getUserName();
            DataBaseResult<CheckInfo> dataBaseResult = CheckDto.obtainCheckPckBill(username,chehao);

            return dataBaseResult;
        }

        @Override
        protected void onPostExecute(DataBaseResult<CheckInfo> data) {
            setupProgressBar(false);
            mPromptErrorView.setVisibility(View.INVISIBLE);
            mPromptSuccessView.setVisibility(View.INVISIBLE);
            if(data .getResultBoolean()){

                inuseState=INUSE_STATE_CHECKING;
                LinkedList<CheckInfo> infos = data.getResultList();
                mCheckAdapter.setPckBillData(infos);
                mCheckAdapter.setCurrentdata(infos);
                mCheckAdapter.getUncheckData().clear();
                mCheckAdapter.getUncheckData().addAll(infos);
                mCheckAdapter.getCheckedData().clear();
                mCheckAdapter.notifyDataSetChanged();
                setQuantityHint();
                bottomNaviView.setSelectedItemId(R.id.bottom_navigation_pckbill);

                Log.d(TAG, "onPostExecute() called with: data = [" + data + "]");
                currentPckBillno = data.getResultList().get(0).getPckBillno();
                currentChehao = chehao;
                chehaoView.setText(currentChehao);
                packBillnoView.setText(currentPckBillno);

                Log.d(TAG, "onPostExecute() called with: billno = [" + currentPckBillno + "]");


                mActionButton.setText("提交");
                //mObtainButton.setClickable(false);
//                if (sortby != SORT_BY_shippingArea) {
//                    Collections.sort(infos,CompareUtils.compareCheckDesc);
//                    sortby = SORT_BY_shippingArea;
//                }
                //mSearchView.setFocusable(false);
            }else{
                mPromptErrorView.setVisibility(View.VISIBLE);
                String errorInfo = data.getResultError();
                mPromptErrorView.setText(errorInfo);
                Snackbar.make(getWindow().getDecorView(),errorInfo,Snackbar.LENGTH_SHORT).show();

            }
            Utils.hideSoftInputBord(mSearchView);
        }

    }


    public class CheckSubmitAsyncTask extends AsyncTask<String,Void,DataBaseResult>{

        @Override
        protected void onPreExecute() {
            setupProgressBar(true);
        }
        @Override
        protected DataBaseResult doInBackground(String... params) {
            String username = mUserBean.getUserName();
            List<CheckInfo> checkInfos = mCheckAdapter.getRemarkData();
            DataBaseResult dbresult = CheckDto.submitCheckPckBill(username,currentPckBillno,checkInfos);
            return dbresult;

        }

        @Override
        protected void onPostExecute(DataBaseResult dbresult) {
            setupProgressBar(false);
            //TODO 此处提交返回后的处理

            mPromptErrorView.setVisibility(View.INVISIBLE);
            mPromptSuccessView.setVisibility(View.INVISIBLE);
            Log.d(TAG, "onPostExecute() called with: currentPckBillno = [" + currentPckBillno + "]");
            if(dbresult.getResultBoolean()){

                inuseState=INUSE_STATE_CHECKED;
                currentPckBillno = "";
                packBillnoView.setText(currentPckBillno);
                currentChehao="";
                chehaoView.setText(currentChehao);
                mCheckAdapter.clearAllDatas();
                mCheckAdapter.notifyDataSetChanged();
                mActionButton.setText("领单");
                String packPerson = dbresult.getResultString();
                //packPersonView.setText(packPerson);
                setQuantityHint();
                String packperson = dbresult.getResultString();
                String promptInfo = "复核提交成功,装箱员是："+packperson;
                mPromptSuccessView.setText(promptInfo);
                mPromptSuccessView.setVisibility(View.VISIBLE);
                Snackbar.make(getWindow().getDecorView(),promptInfo,Snackbar.LENGTH_SHORT).show();

            }else{
                String errorMessage = dbresult.getResultError();
                String errorInfo = errorMessage+"请重新提交";
                mPromptErrorView.setVisibility(View.VISIBLE);
                mPromptErrorView.setText(errorInfo);
                Toast.makeText(CheckActivity.this,errorInfo,Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void setQuantityHint() {
        int pckbillCount = mCheckAdapter.getPckBillData().size();
        int uncheckCount = mCheckAdapter.getUncheckData().size();
        int checkedCount = mCheckAdapter.getCheckedData().size();

        Spanned pckbillSpan = Html.fromHtml("<font color=\"#880000\">" + pckbillCount + "</font>");
        Spanned pickupSpan = Html.fromHtml("<font color=\"#008800\">" + uncheckCount + "</font>");
        Spanned basketSpan = Html.fromHtml("<font color=\"#000088\">" + checkedCount + "</font>");
        amountHintView.setText(pckbillCount+"/"+pickupSpan + "/" + basketSpan);
    }



    private void setupProgressBar(boolean show) {
        if (pregressBar == null) {
            pregressBar = (ProgressBar) findViewById(R.id.check_content_pressbar);
        }
        if(show){
            pregressBar.setVisibility(View.VISIBLE);
        }else{
            pregressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void afterStartVideoSuccess(){

    }

    @Override
    public void afterStopVideoSuccess() {
        super.afterStopVideoSuccess();
    }

    @Override
    protected void onPause() {
        if(mCheckObtainTask != null && !mCheckObtainTask.isCancelled()){
            mCheckObtainTask.cancel(true);
        }
        App.getRequestQueue().cancelAll(getClass().getSimpleName());
        super.onPause();
    }

    @Override
    protected void onStop() {
        //doStopVideo("80001707",currentPckBillno);
        super.onStop();
    }

    public void showProgressDialog(String title,String messgage,boolean showOrHide){
        if(progerssDialog==null){
            progerssDialog = new ProgressDialog(this);
        }
        progerssDialog.setTitle(title);//"正在获取数据..."
        progerssDialog.setMessage(messgage);
        if(showOrHide){
            progerssDialog.show();
        }else{
            progerssDialog.dismiss();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult() called with: requestCode = [" + requestCode + "], resultCode = [" + resultCode + "], data = [" + data + "]");
        if (resultCode == RESULT_OK && requestCode == 0) {
            String result = data.getExtras().getString("result");
            mSearchAutoComplete.setText(result);
            mSearchAutoComplete.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
            mSearchAutoComplete.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_ENTER));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case  R.id.pckbill_obtain:
                doObtainPckbill();
                break;
            case  R.id.pckbill_submit:

                doSubmitPckbill();

                break;
            case  R.id.check_print:

                //TODO
                Log.d(TAG, "onClick() called with: view = [" + item + "]");
                String toastStr = "当前车号："+currentChehao+"\r\n"
                                  +"当前调度单号："+currentPckBillno;
                Toast.makeText(CheckActivity.this,toastStr,Toast.LENGTH_SHORT).show();


                break;

            default:
                break;


        }
        return super.onOptionsItemSelected(item);
    }

    private void doObtainPckbill() {
        if(!TextUtils.isEmpty(currentPckBillno)){
            Toast.makeText(CheckActivity.this,"当前调度单尚未提交",Toast.LENGTH_SHORT).show();
            return;
        }
        Editable editable = mSearchAutoComplete.getText();
        if(!TextUtils.isEmpty(editable)){
            if(editable.toString().matches("^[cC][0-9]{2}")){
                mSearchAutoComplete.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
                mSearchAutoComplete.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_ENTER));
            }else{
                Toast.makeText(CheckActivity.this,"输入车号格式错误",Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(CheckActivity.this,"请输入车号",Toast.LENGTH_SHORT).show();
        }
        mActionButton.postDelayed(new Runnable() {
            @Override
            public void run() {
                Utils.hideSoftInputBord(mActionButton);
            }
        },100);
    }

    private void doSubmitPckbill() {
        if(TextUtils.isEmpty(currentChehao) || TextUtils.isEmpty(currentPckBillno) ){
            Toast.makeText(App.getAppContext(),"你还没有绑定车号",Toast.LENGTH_SHORT).show();
            return;
        }
        if(!mCheckAdapter.isAllChecked()){
            //TODO ..................................
            //Toast.makeText(CheckActivity.this,"此处有药品尚未复核完，这里用提示演示",Toast.LENGTH_SHORT).show();
                    List<CheckInfo> infos = mCheckAdapter.getNonChecked();
                    StringBuffer sbuf= new StringBuffer();
                    for (CheckInfo info:infos){
                        if(TextUtils.isEmpty(info.getRemark())){
                            sbuf.append(info.getShippingArea()+"/"+info.getComname()+"/"+info.getQuantity()+"\r\n");
                        }
                    }
                    if(!TextUtils.isEmpty(sbuf)){
                        Toast.makeText(App.getAppContext(),"还有药品没有复核:\r\n集号/名称/数量\r\n"+sbuf,Toast.LENGTH_SHORT).show();
                    }
                    return;
        }

        showSubmitAlertDialog();

    }
}
