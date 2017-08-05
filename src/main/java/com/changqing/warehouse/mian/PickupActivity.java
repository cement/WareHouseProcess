package com.changqing.warehouse.mian;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.changqing.warehouse.R;
import com.changqing.warehouse.Utils.CompareUtils;
import com.changqing.warehouse.Utils.Utils;
import com.changqing.warehouse.app.App;
import com.changqing.warehouse.bean.DataBaseResult;
import com.changqing.warehouse.bean.PickupInfo;
import com.changqing.warehouse.bean.UserBean;
import com.changqing.warehouse.db.dto.PickupDto;
import com.changqing.warehouse.helper.BottomNavigationViewHelper;
import com.changqing.warehouse.mian.inteface.IHolderMemberClickListener;
import com.changqing.warehouse.mian.inteface.IHolderMemberLongClickListener;
import com.xys.libzxing.zxing.activity.CaptureActivity;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


public class PickupActivity extends BaseActivity implements IHolderMemberClickListener, IHolderMemberLongClickListener,
        SearchView.OnQueryTextListener,
        BottomNavigationView.OnNavigationItemSelectedListener {
    public static final String TAG = PickupActivity.class.getSimpleName();
    //pckBillno  LocName  InspSeqNo quantity   shippingArea   remark
    //调号         货位     验号      数量         集号          区号

    public static final int SORT_BY_LocName = 0;
    public static final int SORT_BY_InspSeqNo = 1;
    public static final int SORT_BY_quantity = 2;
    public static final int SORT_BY_shippingArea = 3;

    //流程状态
    public int mProState = PickupInfo.PRO_normal;


//    public static final int LOADER_OBTAIN_FLAG = 1000;
//    public static final int LOADER_SUBMIT_FLAG = 1001;

    private int sortby = SORT_BY_LocName;

    private SearchView mSearchView;
    private SearchView.SearchAutoComplete mSearchAutoComplete;
    private Button mActionButton;

    private TextView packBillnoView;
    private PickupAdapter mPickupAdapter;
    private FrameLayout mContent;
    private RecyclerView mRecyclerView;

    private TextView titleQuantity;
    private TextView titleInspSeqNo;
    private TextView titleLocName;
    private TextView titleshippingArea;
    private TextView amountHintView;

    private BottomNavigationView bottomNaviView;


    private PickupSubmitAsyncTask submitAsyncTask;
    private PicupObtainAsyncTask obtainAsyncTask;

    private String mCurrentPckBillno;
    private ImageView mQrcodeIcon;

    private String mCurrentChehao;
    private ProgressDialog progerssDialog;
    private TextView chehaoView;
    private TextView pckokPersonView;
    //private TextView mPromptView;

    private LinearLayout mPopupView;
    private CoordinatorLayout mRootLayout;
    private PopupWindow remarkPopupwindow;
    private SearchView otherQuestionSearchView;
    //private TextView checkPersonView;
    private String mCheckPerson;
    private PopupWindow writeRemarkPopup;
    private TextView mPromptSuccessView;
    private TextView mPromptErrorView;

    private UserBean mUserBean;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 隐藏标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 隐藏状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.pickup_activity_layout);




        mRootLayout = (CoordinatorLayout) findViewById(R.id.main_root_layout);

        mSearchView = (SearchView) findViewById(R.id.pickup_input_searchview);
        mSearchAutoComplete = (SearchView.SearchAutoComplete) mSearchView.findViewById(R.id.search_src_text);

        //mSearchView.setSubmitButtonEnabled(true);
        String digists = "0123456789abcdefghigklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        mSearchAutoComplete.setKeyListener(DigitsKeyListener.getInstance(digists));
        mSearchAutoComplete.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

            }
        });

        mQrcodeIcon = (ImageView) findViewById(R.id.pickup_zxing_icon);
        mQrcodeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(PickupActivity.this, CaptureActivity.class), 0);
            }
        });

        mSearchView.onActionViewExpanded();

        mSearchView.setOnQueryTextListener(this);

        mActionButton = (Button) findViewById(R.id.pickup_input_obtainbutton);
        mActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 取单处理 领单前清除单号和车号以及数据
           if(mActionButton.getText().equals("领单")){

               doObtainPckbill();
           }else if(mActionButton.getText().equals("提交")){
//               Toast.makeText(PickupActivity.this,"提交",Toast.LENGTH_SHORT).show();
               doSubmitPckbillno();
           }



            }
        });

       // currentInfoView = findViewById(R.id.pickup_current_info);
        packBillnoView = (TextView) findViewById(R.id.pickup_content_packbillno);
        amountHintView = (TextView) findViewById(R.id.pickup_content_amount_hint);


        mContent = (FrameLayout) findViewById(R.id.pickup_content);
        mPromptSuccessView = (TextView) findViewById(R.id.pickup_prompt_success);
        mPromptErrorView = (TextView) findViewById(R.id.pickup_prompt_error);

        chehaoView = (TextView) findViewById(R.id.pickup_chehao);
        pckokPersonView = (TextView) findViewById(R.id.pickup_pckokperson);
        //checkPersonView = (TextView) findViewById(R.id.pickup_checkperson);


        if(savedInstanceState==null){
            mUserBean = (UserBean) getIntent().getSerializableExtra("userBean");
            pckokPersonView.setText(mUserBean.getUserName());
        }else{
            mUserBean = (UserBean) savedInstanceState.getSerializable("userbean");
            pckokPersonView.setText(mUserBean.getUserName());
        }
        mRecyclerView = (RecyclerView) getLayoutInflater().inflate(R.layout.content_layout, mContent, false);


        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mPickupAdapter = new PickupAdapter();
        mPickupAdapter.setHolderMemberLongClicklistener(this);
        mPickupAdapter.setHolderMemberListener(this);
        mRecyclerView.setAdapter(mPickupAdapter);

        mContent.addView(mRecyclerView);


 //       FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.pickup_footer_floatbutton);
 //       floatingActionButton.setOnClickListener(new View.OnClickListener() {
 //           @Override
 //           public void onClick(View view) {
//                Log.d(TAG, "floatingActionButton ---------- onClick() called with: view = [" + view + "]");
//
//                if(TextUtils.isEmpty(mCurrentChehao) || TextUtils.isEmpty(mCurrentPckBillno) ){
//                    Toast.makeText(App.getAppContext(),"你还没有绑定车号",Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                if(!mPickupAdapter.isAllBasketed()){
//                    Toast.makeText(PickupActivity.this,"此处有药品尚未发货完成，这里用提示演示",Toast.LENGTH_SHORT).show();
//                    List<PickupInfo> infos = mPickupAdapter.getNonBasketed();
//                    StringBuffer sbuf= new StringBuffer();
//                    for (PickupInfo info:infos){
//                        if(TextUtils.isEmpty(info.getRemark())){
//                            sbuf.append(info.getLocName()+"/"+info.getInspSeqNo()+"/"+info.getQuantity()+"/"+info.getShippingArea()+"\r\n");
//                        }
//                    }
//                    if(!TextUtils.isEmpty(sbuf)){
//                        Toast.makeText(App.getAppContext(),"还有药品没有入筐或说明:\r\n货位/验号/数量/集号\r\n"+sbuf,Toast.LENGTH_SHORT).show();
//                    }
//                    return;
//                }
 //               showSubmitAlertDialog();
//
 //           }
 //       });
        bottomNaviView = (BottomNavigationView) findViewById(R.id.pickup_bottom_navigationview);
        bottomNaviView.setOnNavigationItemSelectedListener(this);


        BottomNavigationViewHelper.disableShiftMode(bottomNaviView);


        //View.OnClickListener listener = new sortClickListener();
        titleQuantity = (TextView) findViewById(R.id.item_title_quantity);
        //titleQuantity.setOnClickListener(listener);
        titleInspSeqNo = (TextView) findViewById(R.id.item_title_InspSeqNo);
        //titleInspSeqNo.setOnClickListener(listener);
        titleLocName = (TextView) findViewById(R.id.item_title_LocName);
        titleLocName.setTag(true);
        //titleLocName.setOnClickListener(listener);

        titleshippingArea = (TextView) findViewById(R.id.item_title_shippingArea);
        //titleshippingArea.setOnClickListener(listener);


    }



    public class sortClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {

            LinkedList<PickupInfo> datas = mPickupAdapter.getCurrentData();

            if (view.equals(titleLocName) && view.getTag()!=null) {

                   if((boolean) view.getTag()){
                        view.setTag(false);
                        Collections.sort(datas, CompareUtils.comparePickupDesc);
                       Log.d(TAG, "onClick() called with: view = [" + view.getTag() + "]");
                    }else{
                        view.setTag(true);
                        Collections.sort(datas, CompareUtils.comparePickupAsc);
                       Log.d(TAG, "onClick() called with: view = [" + view.getTag() + "]");
                    }
                    sortby = SORT_BY_LocName;
                    mPickupAdapter.notifyDataSetChanged();

            } else if (view.equals(titleInspSeqNo) && sortby != SORT_BY_InspSeqNo) {
                Collections.sort(datas, CompareUtils.compareByInspSeqNo);
                sortby = SORT_BY_InspSeqNo;
                mPickupAdapter.notifyDataSetChanged();

            } else if (view.equals(titleQuantity) && sortby != SORT_BY_quantity) {
                Collections.sort(datas, CompareUtils.compareByQuantity);
                sortby = SORT_BY_quantity;
                mPickupAdapter.notifyDataSetChanged();
            } else if (view.equals(titleshippingArea) && sortby != SORT_BY_shippingArea) {
                Collections.sort(datas, CompareUtils.compareByShippingArea);
                sortby = SORT_BY_shippingArea;
                mPickupAdapter.notifyDataSetChanged();

            }
        }
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
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.d("....>;", "onConfigurationChanged() called with: newConfig = [" + newConfig + "]");
    }

    @Override
    protected void onResume() {
        super.onResume();
    }



    @Override
    public boolean onQueryTextChange(String newText) {
        //mSearchAutoComplete.setText(newText);
        // disableInputBoard(mSearchAutoComplete);
        //Utils.hideSoftInputBord(mSearchAutoComplete);
        //mSearchAutoComplete.setSelection(newText.length());
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        String word = query.trim().toUpperCase();

            if (word.matches("^[Cc][0-9]{2}$")){

                if(TextUtils.isEmpty(mCurrentChehao) || TextUtils.isEmpty(mCurrentPckBillno)){
                    String username = mUserBean.getUserName();
                    showAlertConfirmBindChehao(username,word);
                }else {
                    Snackbar.make(getWindow().getDecorView(),"车号"+word+"已经绑定",Snackbar.LENGTH_SHORT).show();
                }
//              Snackbar.make(getWindow().getDecorView(),"已经绑定车号，不能重复绑定"+mCurrentChehao,Snackbar.LENGTH_SHORT).show();
                mSearchView.onActionViewExpanded();
                mSearchAutoComplete.setText("");
                return true;
            }else
            if(word.matches("^[Kk][0-9]{4}$")) {

                boolean find = false;
                String kuanghao = query.trim().replaceAll("[Kk0]","");

                LinkedList<PickupInfo> datas = mPickupAdapter.getPickupData();
                for (int i = 0; i < datas.size(); i++) {
                    PickupInfo data = datas.get(i);
                    if (data.getShippingArea().equals(kuanghao)) {
                        data.setColorState(PickupInfo.COLOR_CURRENT_basket);
                        mPickupAdapter.getPickupData().remove(data);
                        mPickupAdapter.getBasketData().addFirst(data);
                        mPickupAdapter.notifyDataSetChanged();
                        setQuantityHint();
                        mSearchView.onActionViewExpanded();
                        mSearchAutoComplete.setText("");
                        find = true;
                       return true;
                    }
                }
                if(!find){
                    Snackbar.make(getWindow().getDecorView(),"未发现相匹配的集号--"+word,Snackbar.LENGTH_SHORT).show();
                    mSearchView.onActionViewExpanded();
                    mSearchAutoComplete.setText("");
                }
                return true;
            }else
            if(word.matches("[0-9]+")){
                boolean find = false;
                LinkedList<PickupInfo> datas = mPickupAdapter.getUnpickData();
                for (int i = 0; i < datas.size(); i++) {
                    PickupInfo data = datas.get(i);
                    if (data.getInspSeqNo().equals(word) ) {

                        data.setColorState(PickupInfo.COLOR_CURRENT_pickup);

                        mPickupAdapter.getUnpickData().remove(data);
                        mPickupAdapter.getPickupData().addFirst(data);
                        mPickupAdapter.notifyDataSetChanged();
                        setQuantityHint();
                        mSearchView.onActionViewExpanded();
                        mSearchAutoComplete.setText("");
                        find = true;
                        return true;
                    }
                }

                if(!find){
                    Snackbar.make(getWindow().getDecorView(),"未发现相匹配的验号--"+word,Snackbar.LENGTH_SHORT).show();
                    mSearchView.onActionViewExpanded();
                    mSearchAutoComplete.setText("");
                }
                return  true;


            }else{
                Snackbar.make(getWindow().getDecorView(),"未使用的扫描码--"+word,Snackbar.LENGTH_SHORT).show();
                mSearchView.onActionViewExpanded();
                mSearchAutoComplete.setText("");
                return true;

           }


//        if(word.matches("^C[0-9]{2}$")){
//            if(TextUtils.isEmpty(mCurrentChehao) && TextUtils.isEmpty(mCurrentPckBillno)){
//                doAlertConfirmBindChehao(word);
//            }else {
//                Snackbar.make(getWindow().getDecorView(),"车号已经绑定",Snackbar.LENGTH_SHORT).show();
//            }
//
//        }else if(word.matches("^K[0-9]{4}$")){
//
//
//
//        }else if(word.matches("[0-9]+")){
//
//        }else{
//
//        }


//        String word = query.trim();
//        if(TextUtils.isEmpty(mCurrentChehao) && TextUtils.isEmpty(mCurrentPckBillno)) {
//            Snackbar.make(getWindow().getDecorView(),"请先领单",Snackbar.LENGTH_SHORT).show();
//            mSearchView.onActionViewExpanded();
//            mSearchAutoComplete.setText("");
//            return true;
//        }else if(TextUtils.isEmpty(mCurrentChehao) && !TextUtils.isEmpty(mCurrentPckBillno)){
//            if (word.matches("^C[0-9]{2}$")){
//                doAlertConfirmBindChehao(word,mCurrentPckBillno);
//                mSearchView.onActionViewExpanded();
//                mSearchAutoComplete.setText("");
//                return true;
//            }else{
//                mSearchView.onActionViewExpanded();
//                mSearchAutoComplete.setText("");
//                Snackbar.make(getWindow().getDecorView(),"车号尚未绑定,"+word+"不是车号，请扫描车号进行绑定",Snackbar.LENGTH_SHORT).show();
//                return true;
//            }
//        }else if(!TextUtils.isEmpty(mCurrentChehao) && !TextUtils.isEmpty(mCurrentPckBillno)){
//            if (word.matches("^C[0-9]{2}$")){
//                Snackbar.make(getWindow().getDecorView(),"已经绑定车号，不能重复绑定"+mCurrentChehao,Snackbar.LENGTH_SHORT).show();
//                mSearchView.onActionViewExpanded();
//                mSearchAutoComplete.setText("");
//                return true;
//            }else
//            if(word.matches("[0-9]+")){
//                LinkedList<PickupInfo> datas = mPickupAdapter.getUnpickData();
//                for (int i = 0; i < datas.size(); i++) {
//                    PickupInfo data = datas.get(i);
//                    if (data.getInspSeqNo().equals(word) ) {
//                        data.setCurrent(true);
//                        data.setState(PickupInfo.STATE_pickup);
//
//                        mPickupAdapter.getUnpickData().remove(data);
//                        mPickupAdapter.getPickupData().addFirst(data);
//                        mPickupAdapter.notifyDataSetChanged();
//                        setQuantityHint();
//                        mSearchView.onActionViewExpanded();
//                        mSearchAutoComplete.setText("");
//                        return true;
//                    }
//                }
//
//                Snackbar.make(getWindow().getDecorView(),"未发现相匹配的验号--"+word,Snackbar.LENGTH_SHORT).show();
//                mSearchView.onActionViewExpanded();
//                mSearchAutoComplete.setText("");
//                return  true;
//
//
//            }else
//            if(word.matches("^K[0-9]{4}$")) {
//                //String kuanghao = query.trim().replaceAll("K0","");
//                LinkedList<PickupInfo> datas = mPickupAdapter.getPickupData();
//                for (int i = 0; i < datas.size(); i++) {
//                    PickupInfo data = datas.get(i);
//                    if (data.getShippingArea().equals(kuanghao)) {
//                        data.setState(PickupInfo.STATE_basket);
//                        mPickupAdapter.getPickupData().remove(data);
//                        mPickupAdapter.getBasketData().addFirst(data);
//                        mPickupAdapter.notifyDataSetChanged();
//                        setQuantityHint();
//                        mSearchView.onActionViewExpanded();
//                        mSearchAutoComplete.setText("");
//                        return true;
//                    }
//                }
//                Snackbar.make(getWindow().getDecorView(),"未发现相匹配的集号--"+word,Snackbar.LENGTH_SHORT).show();
//                mSearchView.onActionViewExpanded();
//                mSearchAutoComplete.setText("");
//                return  true;
//            }else {
//                Snackbar.make(getWindow().getDecorView(),"未使用的扫描码--"+word,Snackbar.LENGTH_SHORT).show();
//                mSearchView.onActionViewExpanded();
//                mSearchAutoComplete.setText("");
//                return true;
//
//            }
//
//        }
//
//        return false;


//        LinkedList<PickupInfo> datas = mPickupAdapter.getPckBillData();
//        for (int i = 0; i < datas.size(); i++) {
//            PickupInfo data = datas.get(i);
//            if (data.getInspSeqNo().equals(query.toString()) && (data.getState() == PickupInfo.STATE_unpick)) {
//                data.setCurrent(true);
//                data.setState(PickupInfo.STATE_pickup);
//
//                mPickupAdapter.getUnpickData().remove(data);
//                mPickupAdapter.getPickupData().addFirst(data);
////                if (sortby != SORT_BY_InspSeqNo) {
////                    Collections.sort(datas, CompareUtils.compareByInspSeqNo);
////                    sortby = SORT_BY_InspSeqNo;
////                }
//                mPickupAdapter.notifyDataSetChanged();
//                setQuantityHint();
//                mSearchView.onActionViewExpanded();
//                mSearchAutoComplete.setText("");
//               return true;
//            } else if (data.getShippingArea().equals(query.toString()) && (data.getState() == PickupInfo.STATE_pickup)) {
//                data.setState(PickupInfo.STATE_basket);
//                mPickupAdapter.getPickupData().remove(data);
//                mPickupAdapter.getBasketData().addFirst(data);
////
////                if (sortby != SORT_BY_shippingArea) {
////                    Collections.sort(datas, CompareUtils.compareByShippingArea);
////                    sortby = SORT_BY_shippingArea;
////                }
//                mPickupAdapter.notifyDataSetChanged();
//                setQuantityHint();
//                mSearchView.onActionViewExpanded();
//                mSearchAutoComplete.setText("");
//                return  true;
//            }else{
//                Snackbar.make(getWindow().getDecorView(),"未发现相匹配的验号或集号--"+word,Snackbar.LENGTH_SHORT).show();
//                //Toast.makeText(App.getAppContext(),"未发现相匹配的验号或集号",Toast.LENGTH_SHORT).show();
//                mSearchView.onActionViewExpanded();
//                mSearchAutoComplete.setText("");
//                return  true;
//            }
//        }
//
////        mSearchView.onActionViewExpanded();
////        mSearchAutoComplete.setText("");


    }



//    public void showBindKuangHaoPopupwindow(String kuanghao,List<PickupInfo> piupdata){
//        if(mBindKuangHaoPopupwindow==null){
//            mPopupView = (LinearLayout) getLayoutInflater().inflate(R.layout.poppup_bind_kuanghao_layout, null);
//        }
//        if(mBindKuangHaoPopupwindow==null){
//            mBindKuangHaoPopupwindow = new PopupWindow(mPopupView,WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
//            mBindKuangHaoPopupwindow.setAnimationStyle(R.style.popup_anim_add_cart);
//            mBindKuangHaoPopupwindow.setBackgroundDrawable(new ColorDrawable(0xffffffff));
//            mBindKuangHaoPopupwindow.setFocusable(true);
//            mBindKuangHaoPopupwindow.setOnDismissListener(new PopupDismissListener());
//
//            AdapterView.OnItemClickListener listener = new PopupItemClickListener();
//
//            popupListView = (ListView) mPopupView.findViewById(R.id.popup_bind_kuanghao_list);
//            ImageView cancelView = (ImageView) mPopupView.findViewById(R.id.popup_bind_kuanghao_cancel);
//            cancelView.setOnClickListener(new PopupCancelClickListener());
//            popupListView.setOnItemClickListener(listener);
//
//            popupListView.setAdapter(new PopupListAdapter(piupdata));
//        }
//        popupListView.setTag(kuanghao);
//        setWindowBackgroundAlpha(0.6f);
//        Log.d(TAG, "-----------------------------+ kkkkk----------------------+");
//        mBindKuangHaoPopupwindow.showAtLocation(mRootLayout, Gravity.BOTTOM, 0, 0);
//    }

    public class PopupDismissListener implements PopupWindow.OnDismissListener{

        @Override
        public void onDismiss() {
            setWindowBackgroundAlpha(1.0f);
        }
    }
    public class PopupItemClickListener implements AdapterView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String kuanghao = (String) parent.getTag();
            PickupInfo info = (PickupInfo) parent.getAdapter().getItem(position);

            for (int i = 0; i <mPickupAdapter.getPckBillData().size(); i++) {
                PickupInfo infoTemp = mPickupAdapter.getPckBillData().get(i);
                if(infoTemp.getShippingArea().equals(info.getShippingArea())){
                    infoTemp.setKuangHao(kuanghao);
                    Log.d(TAG, infoTemp.getKuangHao());
                }
            }

            //mBindKuangHaoPopupwindow.dismiss();
        }
    }
//    private void doAlertObtainPckbillno() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setIcon(R.drawable.ic_home_black_24dp);
//        builder.setTitle("提示");
//        builder.setMessage("请先领单");
//        builder.setPositiveButton("领单", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                Log.e(TAG, "onClick() called with: dialog = [" + dialog + "], which = [" + which + "]");
//                //dialog.dismiss();
//
//                mCurrentChehao = "";
//                mCurrentPckBillno = "";
//
//                packBillnoView.setText("");
//                amountHintView.setText("");
//                chehaoView.setText("");
//
//                mPickupAdapter.clearAllDatas();
//                mPickupAdapter.notifyDataSetChanged();
//                startObtainTask();
//            }
//        });
//        builder.setNeutralButton("取消", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                //DO nothing .......................
//                //dialog.dismiss();
//            }
//        });
//        builder.create().show();
//
//    }

    private void doAlertPromptBindChehao() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.ic_home_black_24dp);
        builder.setTitle("提示");
        builder.setMessage("请先绑定流转车");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.e(TAG, "onClick() called with: dialog = [" + dialog + "], which = [" + which + "]");
            }
        });
        builder.create().show();
    }

    public void showAlertConfirmBindChehao(final  String  packokperson,final String chehao) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.popup_icon_bind_cart_24dp);
        builder.setTitle("绑定流转车");
        builder.setMessage("确定绑定车号" + chehao + " 吗？");
        builder.setPositiveButton("确认绑定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            //TODO 车号与调度单号关联到数据库
            startAsyncObtainTask(packokperson,chehao);
            }
        });
        builder.setNeutralButton("取消绑定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //DO nothing................
            }
        });
        AlertDialog alertDialog = builder.create();
        //alertDialog.setOnDismissListener();
        alertDialog.show();
    }

    /*
     底部导航监听
     */
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.bottom_navigation_normal:
                if (mPickupAdapter.getCurrentData() != mPickupAdapter.getPckBillData()) {
                    mPickupAdapter.swarpData(mPickupAdapter.getPckBillData());
                    mPickupAdapter.notifyDataSetChanged();
                }
                return true;
            case R.id.bottom_navigation_unpick:
                if (mPickupAdapter.getCurrentData() != mPickupAdapter.getUnpickData()) {
                    mPickupAdapter.swarpData(mPickupAdapter.getUnpickData());
                    mPickupAdapter.notifyDataSetChanged();
                }
                return true;
            case R.id.bottom_navigation_pickup:
                if (mPickupAdapter.getCurrentData() != mPickupAdapter.getPickupData()) {
                    mPickupAdapter.swarpData(mPickupAdapter.getPickupData());
                    mPickupAdapter.notifyDataSetChanged();
                }
                return true;
            case R.id.bottom_navigation_basket:
                if (mPickupAdapter.getCurrentData() != mPickupAdapter.getBasketData()) {
                    mPickupAdapter.swarpData(mPickupAdapter.getBasketData());
                    mPickupAdapter.notifyDataSetChanged();
                }
                return true;
        }
        return false;
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("PckBillData", mPickupAdapter.getPckBillData());
        outState.putSerializable("UnpickData", mPickupAdapter.getUnpickData());
        outState.putSerializable("PickupData", mPickupAdapter.getPickupData());
        outState.putSerializable("BasketData", mPickupAdapter.getBasketData());
        outState.putSerializable("RemarkedData", mPickupAdapter.getRemarkedData());
        outState.putSerializable("chehao", mCurrentChehao);
        outState.putSerializable("pckbillno", mCurrentPckBillno);
        outState.putSerializable("checkperson", mCheckPerson);
        outState.putSerializable("userbean", mUserBean);
        //outState.putString("pckbillno", mCurrentPckBillno);

        outState.putInt("SelectedItemId", bottomNaviView.getSelectedItemId());
        super.onSaveInstanceState(outState);
        //Log.d("-----save------", "onSaveInstanceState() called with: outState = [" + outState + "]");

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        LinkedList<PickupInfo> PckBillData = (LinkedList<PickupInfo>) savedInstanceState.get("PckBillData");
        if (PckBillData != null) {
            mPickupAdapter.setPckBillData(PckBillData);
        }
        LinkedList<PickupInfo> UnpickData = (LinkedList<PickupInfo>) savedInstanceState.get("UnpickData");
        if (UnpickData != null) {
            mPickupAdapter.setUnpickData(UnpickData);
        }
        LinkedList<PickupInfo> PickupData = (LinkedList<PickupInfo>) savedInstanceState.get("PickupData");
        if (PickupData != null) {
            mPickupAdapter.setPickupData(PickupData);
        }
        LinkedList<PickupInfo> BasketData = (LinkedList<PickupInfo>) savedInstanceState.get("BasketData");
        if (BasketData != null) {
            mPickupAdapter.setBasketData(BasketData);
        }
        LinkedList<PickupInfo> RemarkedData = (LinkedList<PickupInfo>) savedInstanceState.get("RemarkedData");
        if (RemarkedData != null) {
            mPickupAdapter.setRemarkedData(RemarkedData);
        }

        int SelectedItemId = savedInstanceState.getInt("SelectedItemId");
        bottomNaviView.setSelectedItemId(SelectedItemId);

        String chehaoStr = savedInstanceState.getString("chehao");

        if (chehaoStr != null) {
            chehaoView.setText(mCurrentChehao=chehaoStr);
        }
        String pckbillnoStr = savedInstanceState.getString("pckbillno");
        if (pckbillnoStr != null) {
            packBillnoView.setText(mCurrentPckBillno = pckbillnoStr);
        }
        String checkpersonStr = savedInstanceState.getString("pckbillno");
        if (checkpersonStr != null) {
            packBillnoView.setText(mCheckPerson = checkpersonStr);
        }
        UserBean userBean = (UserBean) savedInstanceState.get("userbean");
        if (userBean != null) {
            App.setUserBean(userBean);
        }

        mPickupAdapter.notifyDataSetChanged();
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onHolderMemberClicked(View itemView, View clickView, int position) {

        Log.d(TAG, "onHolderMemberClicked() called with: itemView = [" + itemView + "], clickView = [" + clickView + "], position = [" + position + "]");
        Snackbar.make(getWindow().getDecorView(), "nHolderMemberClicked", Snackbar.LENGTH_SHORT).setAction("????", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick() called with: v = [" + v + "]");
            }
        }).show();
    }


    @Override
    public boolean onHolderMemberLongClicked(View itemView, View clickView, int position) {
        if (itemView.equals(clickView)) {
            if (TextUtils.isEmpty(mCurrentChehao)) {
                //如果车号没有绑定，提示绑定车号
                //doAlertPromptBindChehao();
                final Snackbar snackbar = Snackbar.make(getWindow().getDecorView(),"车号尚未绑定，请先绑定车号",Snackbar.LENGTH_SHORT);
                Snackbar snackbar1 = snackbar.setAction("知道了", new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        snackbar.dismiss();
                    }
                });
                snackbar.show();
                return true;
            }
            PickupInfo pickupInfo = (PickupInfo) itemView.getTag();
            //showPopupWindow(itemView);
            showActionAlertDialog(clickView, pickupInfo);
            return true;
        }

        return false;
    }


    public void showSubmitAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("温馨提示");
        builder.setIcon(R.drawable.popup_icon_submit_24dp);
        builder.setMessage("确定提交本次发货？");
        builder.setNeutralButton("撤销提交", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // mSearchView.clearFocus();
            }
        });
        builder.setPositiveButton("确认提交", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //TODO 提交处理task
                startAsyncSubmitTask();

            }
        });
        builder.create();
        builder.show();

    }


    public void showActionAlertDialog(final View auchView,  PickupInfo sortingInfo) {
        AlertDialog dialog = null;
        final PickupInfo infoTemp = sortingInfo;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("温馨提示");
        builder.setMessage("请确认操作");
        builder.setIcon(R.drawable.ic_home_black_24dp);


        final View alertView = getLayoutInflater().inflate(R.layout.alert_layout, null);

        TextView LocNameView = (TextView) alertView.findViewById(R.id.alert_item_LocName);
        TextView InspSeqNoView = (TextView) alertView.findViewById(R.id.alert_item_InspSeqNo);
        TextView quantityView = (TextView) alertView.findViewById(R.id.alert_item_quantity);
        TextView shippingAreaView = (TextView) alertView.findViewById(R.id.alert_item_shippingArea);

        LocNameView.setText(sortingInfo.getLocName());
        InspSeqNoView.setText(sortingInfo.getInspSeqNo());
        quantityView.setText(sortingInfo.getQuantity());
        shippingAreaView.setText(sortingInfo.getShippingArea());

        builder.setView(alertView);

        builder.setNeutralButton("撤销确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (infoTemp.getColorState() == PickupInfo.COLOR_basket) {
                    infoTemp.setColorState(PickupInfo.COLOR_pickup);
                    mPickupAdapter.getBasketData().remove(infoTemp);
                    mPickupAdapter.getPickupData().addFirst(infoTemp);
                    mPickupAdapter.notifyDataSetChanged();
                    //mSearchView.clearFocus();
                    setQuantityHint();
                } else if (infoTemp.getColorState() == PickupInfo.COLOR_pickup) {
                    infoTemp.setColorState(PickupInfo.COLOR_unpick);
                    mPickupAdapter.getPickupData().remove(infoTemp);
                    mPickupAdapter.getUnpickData().addFirst(infoTemp);
                    mPickupAdapter.notifyDataSetChanged();
                    // mSearchView.clearFocus();
                    setQuantityHint();
                }


            }
        });
        builder.setNegativeButton("原因说明", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d(TAG, "setNegativeButton(取消)----------- = [" + dialog + "], which = [" + which + "]");
                 showRemarkPopupWindow(auchView,infoTemp);
                //mSearchView.clearFocus();


            }
        });
        builder.setPositiveButton("手动确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (infoTemp.getColorState() == PickupInfo.COLOR_unpick) {
                    infoTemp.setColorState(PickupInfo.COLOR_pickup);
                    mPickupAdapter.getUnpickData().remove(infoTemp);
                    mPickupAdapter.getPickupData().addFirst(infoTemp);
                    mPickupAdapter.notifyDataSetChanged();
                    // mSearchView.clearFocus();
                    setQuantityHint();
                } else if (infoTemp.getColorState() == PickupInfo.COLOR_pickup) {
                    infoTemp.setColorState(PickupInfo.COLOR_basket);
                    mPickupAdapter.getPickupData().remove(infoTemp);
                    mPickupAdapter.getBasketData().addFirst(infoTemp);
                    mPickupAdapter.notifyDataSetChanged();
                    // mSearchView.clearFocus();
                    setQuantityHint();
                }

            }
        });
        builder.setMessage("温馨提示：");
        dialog = builder.create();
        //dialog.setOnDismissListener(new DismissListener());
        builder.show();
    }


    private void showRemarkPopupWindow(View view,PickupInfo sortingInfo) {
        if(remarkPopupwindow==null){
            mPopupView = (LinearLayout) getLayoutInflater().inflate(R.layout.popup_remark_layout, null);
        }
        //if(remarkPopupwindow==null){
            //remarkPopupwindow = new PopupWindow(mPopupView,WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
            remarkPopupwindow = new PopupWindow(mPopupView,Utils.getScreenWidth(this)/3*2,WindowManager.LayoutParams.WRAP_CONTENT);

            remarkPopupwindow.setAnimationStyle(R.style.popup_anim_add_cart);
            //remarkPopupwindow.setBackgroundDrawable(new ColorDrawable(0x00ffffff));
            remarkPopupwindow.setBackgroundDrawable(ContextCompat.getDrawable(this,R.drawable.popup_arrow_background));
            remarkPopupwindow.setFocusable(true);
            //以下两行让软键盘把popupwindow顶上去
            remarkPopupwindow.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
            remarkPopupwindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            remarkPopupwindow.setOnDismissListener(new PopupDismissListener());
            View.OnClickListener listener = new PopupRemarkClickListener();
            TextView custReturnsTextView = (TextView) mPopupView.findViewById(R.id.remark_cust_returns);
            custReturnsTextView.setTag(sortingInfo);
            custReturnsTextView.setOnClickListener(listener);
            TextView nonInventoryTextView = (TextView) mPopupView.findViewById(R.id.remark_non_inventory);
            nonInventoryTextView.setOnClickListener(listener);
            nonInventoryTextView.setTag(sortingInfo);
            TextView packDamageTextView = (TextView) mPopupView.findViewById(R.id.remark_pack_damage);
            packDamageTextView.setOnClickListener(listener);
            packDamageTextView.setTag(sortingInfo);
            TextView otherTextView = (TextView) mPopupView.findViewById(R.id.remark_other_reason);
            otherTextView.setTag(sortingInfo);
            otherTextView.setOnClickListener(listener);

//            otherQuestionSearchView = (SearchView) mPopupView.findViewById(R.id.remark_other_question);
//            otherQuestionSearchView.setOnQueryTextListener(new PopupQueryTextListener(sortingInfo));
//            otherQuestionSearchView.setTag(sortingInfo);
        //setWindowBackgroundAlpha(0.6f);
        Log.d(TAG, "----------------------------- ++ kkkkk ++ ---------------------- ++");
        remarkPopupwindow.showAsDropDown(view);


    }

    private void showWriteRemarkPopupWindow(PickupInfo sortingInfo) {
        View writeRemarkView = LayoutInflater.from(this).inflate(R.layout.popup_pickup_write_remark_layout,null);
        writeRemarkPopup = new PopupWindow(writeRemarkView,WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        writeRemarkPopup.setAnimationStyle(R.style.popup_anim_add_cart);
        writeRemarkPopup.setBackgroundDrawable(new ColorDrawable(0xffdbdbdb));
        writeRemarkPopup.setFocusable(true);
        //以下两行让软键盘把popupwindow顶上去
        writeRemarkPopup.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        writeRemarkPopup.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        writeRemarkPopup.setOnDismissListener(new PopupDismissListener());

        View.OnClickListener listener = new PopupRemarkClickListener();
        ImageView cancelImage = (ImageView) writeRemarkView.findViewById(R.id.pickup_write_remmark_cancel);
        cancelImage.setTag(writeRemarkPopup);
        cancelImage.setOnClickListener(listener);
        EditText writEditText = (EditText) writeRemarkView.findViewById(R.id.pickup_write_remmark_edittext);
        writEditText.setTag(sortingInfo);
        writEditText.setOnClickListener(listener);
        Button   submitButton  = (Button) writeRemarkView.findViewById(R.id.pickup_write_remmark_submitbutton);
        submitButton.setTag(writEditText);
        submitButton.setOnClickListener(listener);

        setWindowBackgroundAlpha(0.6f);
        writeRemarkPopup.showAtLocation(mRootLayout,Gravity.BOTTOM,0,0);


    }
    public class PopupQueryTextListener implements SearchView.OnQueryTextListener{


        PickupInfo pickupInfo;
        public PopupQueryTextListener(PickupInfo pickupInfo) {
            this.pickupInfo = pickupInfo;
        }

        @Override
        public boolean onQueryTextSubmit(String query) {
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            List<PickupInfo> listInfos = mPickupAdapter.getRemarkedData();
            if(!listInfos.contains(pickupInfo)){
                pickupInfo.setRemark(newText);
                listInfos.add(pickupInfo);

                mPickupAdapter.getBasketData().addFirst(pickupInfo);
                pickupInfo.setColorState(PickupInfo.COLOR_basket);
                mPickupAdapter.notifyDataSetChanged();
            }else{
                Toast.makeText(PickupActivity.this,"你已经书写备注",Toast.LENGTH_SHORT).show();
            }

            remarkPopupwindow.dismiss();
            return true;
        }
    }

    public class PopupRemarkClickListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            //TODO


            if(view.getId()==R.id.remark_other_reason){
                //// TODO: 2017/7/14 0014
                PickupInfo info = (PickupInfo) view.getTag();
                showWriteRemarkPopupWindow(info);
            }
            else if(view.getId()==R.id.pickup_write_remmark_edittext){

            }
            else if(view.getId()==R.id.pickup_write_remmark_submitbutton){
                 EditText edit = (EditText) view.getTag();
                 PickupInfo info = (PickupInfo) edit.getTag();
                 String remark = edit.getText().toString();
                List<PickupInfo> listInfos = mPickupAdapter.getRemarkedData();
                if(!listInfos.contains(info)){
                    info.setRemark(remark);
                    mPickupAdapter.getRemarkedData().addFirst(info);

                    info.setColorState(PickupInfo.COLOR_basket);
                    mPickupAdapter.getBasketData().addFirst(info);
                    mPickupAdapter.notifyDataSetChanged();
                }
                writeRemarkPopup.dismiss();

            }
            else if(view.getId()==R.id.pickup_write_remmark_cancel){
                writeRemarkPopup.dismiss();
            }
            else{

                PickupInfo pickupInfo = (PickupInfo) view.getTag();
                String remarkStr = ((TextView)view).getText().toString();
                List<PickupInfo> listInfos = mPickupAdapter.getRemarkedData();
                if(!listInfos.contains(pickupInfo)){
                    pickupInfo.setRemark(remarkStr);
                    mPickupAdapter.getRemarkedData().addFirst(pickupInfo);

                    pickupInfo.setColorState(PickupInfo.COLOR_basket);
                    mPickupAdapter.getBasketData().addFirst(pickupInfo);
                    mPickupAdapter.notifyDataSetChanged();
                }
            }


            remarkPopupwindow.dismiss();
            //RemarkAsyncTask remarkTask = new RemarkAsyncTask(pickupInfo);
            //remarkTask.execute();
        }
    }



    //pckBillno  LocName  InspSeqNo quantity   shippingArea   remark
    //调号         货位     验号      数量         集号        区号


    private class PicupObtainAsyncTask extends AsyncTask<String, Void, DataBaseResult<PickupInfo>> {


        String username ;
        String chehao   ;

        //ProgressDialog progerssDialog;
        @Override
        protected void onPreExecute() {
            showProgressDialog("获取调度单", "正在获取调度单。。。", true);
        }

        @Override
        protected DataBaseResult<PickupInfo> doInBackground(String... params) {
            username = params[0];
            chehao   = params[1];
            return PickupDto.obtainPickupPckBill(username,chehao);
        }

        @Override
        protected void onPostExecute(DataBaseResult<PickupInfo> data) {

            showProgressDialog("", "", false);
            //用于测试返回空数据，也就是没有分配的情况
            //data = new PckBill();
            //mPromptEmptyView.setVisibility(View.INVISIBLE);
            mPromptErrorView.setVisibility(View.INVISIBLE);
            mPromptSuccessView.setVisibility(View.INVISIBLE);
            if (data.getResultBoolean()) {
                inuseState=INUSE_STATE_PICKUPING;
                mCurrentChehao = chehao;
                mCurrentPckBillno = data.getResultList().get(0).getPckBillno();
                Log.d(TAG, "mCurrentPckBillno: 返回单号 = [" + mCurrentPckBillno + "]");
                packBillnoView.setText(mCurrentPckBillno);
                chehaoView.setText(mCurrentChehao);

                mActionButton.setText("提交");
                //mObtainButton.setClickable(false);


                mPickupAdapter.setPckBillData(data.getResultList());
                mPickupAdapter.setCurrentData(mPickupAdapter.getPckBillData());
                mPickupAdapter.getUnpickData().clear();
                mPickupAdapter.getUnpickData().addAll(data.getResultList());
                mPickupAdapter.getPickupData().clear();
                mPickupAdapter.getBasketData().clear();


                bottomNaviView.setSelectedItemId(R.id.bottom_navigation_normal);
                mPickupAdapter.notifyDataSetChanged();


//                if (sortby != SORT_BY_LocName) {
//                    Collections.sort(data.getResultList(), CompareUtils.compareByInspSeqNo);
//                    sortby = SORT_BY_LocName;
//                }

                setQuantityHint();

                mProState = PickupInfo.PRO_useing;
//                mPromptSuccessView.setText("执行成功");
//                mPromptSuccessView.setVisibility(View.VISIBLE);
                Snackbar.make(getWindow().getDecorView(), "调度单领取成功，当前调度单是："+mCurrentPckBillno, Snackbar.LENGTH_SHORT).show();


            } else {
                String resultErrorMeaasge = data.getResultError();
                mPromptErrorView.setText(resultErrorMeaasge);
                mPromptErrorView.setVisibility(View.VISIBLE);

                Snackbar.make(getWindow().getDecorView(), resultErrorMeaasge, Snackbar.LENGTH_SHORT).show();
            }
        }
    }

    public class PickupSubmitAsyncTask extends AsyncTask<String, Void, DataBaseResult> {

        @Override
        protected void onPreExecute() {
            showProgressDialog("提交调度单", "正在提交。。。", true);
        }

        @Override
        protected DataBaseResult doInBackground(String... params) {

            String username = mUserBean.getUserName();
            List<PickupInfo> pickinfos = mPickupAdapter.getRemarkedData();
            Log.d(TAG, "---------------->>>>   = [" + pickinfos + "]");
            DataBaseResult dbResult = PickupDto.submitPickupPckBill(username,mCurrentPckBillno,pickinfos);
            return dbResult;
        }

        @Override
        protected void onPostExecute(DataBaseResult dbResult) {
            showProgressDialog("", "", false);
            mPromptErrorView.setVisibility(View.INVISIBLE);
            mPromptSuccessView.setVisibility(View.INVISIBLE);
            Log.d(TAG, "onPostExecute() called with: integer = [" + dbResult + "]");
            if (dbResult.getResultBoolean()) {
                //doStopVideo("80001707", mCurrentPckBillno);
                //TODO 成功提交后应取消当前单号
                inuseState=INUSE_STATE_PICKUPED;
                mCurrentChehao = "";
                mCurrentPckBillno = "";
                packBillnoView.setText("");
                amountHintView.setText("");
                chehaoView.setText("");

                mActionButton.setText("领单");

                mPickupAdapter.clearAllDatas();
                mPickupAdapter.notifyDataSetChanged();
                //mObtainButton.setClickable(true);
                mProState = PickupInfo.PRO_normal;

                mCheckPerson = dbResult.getResultString();
                //checkPersonView.setText(mCheckPerson);
                String resultMessage = dbResult.getResultMessage();
                String promptInfo = "提交成功，本调度单复核员："+mCheckPerson;
                //String checkPerson = dbResult.getResultString();
                Log.d(TAG, "-----------------> + dbResult ::checkPerson："+mCheckPerson);
                Log.d(TAG, "-----------------> + dbResult ::resultMessage："+resultMessage);
                mPromptSuccessView.setText( promptInfo);
                mPromptSuccessView.setVisibility(View.VISIBLE);
                Toast.makeText(App.getAppContext(), promptInfo, Toast.LENGTH_SHORT).show();

            } else {
                String errorInfo = dbResult.getResultError();
                String promptInfo = "提交失败\r\n "+errorInfo+"\r\n"+"请稍后重新提交！";
                mPromptErrorView.setText(promptInfo);
                Toast.makeText(App.getAppContext(), promptInfo, Toast.LENGTH_SHORT).show();
            }

        }
    }


    public void startAsyncSubmitTask(String... params) {
        submitAsyncTask = new PickupSubmitAsyncTask();
        submitAsyncTask.execute(params);
    }

    public void startAsyncObtainTask(String... params) {
        obtainAsyncTask = new PicupObtainAsyncTask();
        obtainAsyncTask.execute(params);
    }



    private void setQuantityHint() {
        if (mPickupAdapter != null && mPickupAdapter != null && mPickupAdapter != null) {
            int unpickAmount = mPickupAdapter.getUnpickData().size();
            int pickupAmount = mPickupAdapter.getPickupData().size();
            int basketAmount = mPickupAdapter.getBasketData().size();
            amountHintView.setText(unpickAmount + "/" + pickupAmount + "/" + basketAmount);
        }
    }


    @Override
    protected void onPause() {
        if (obtainAsyncTask != null && !obtainAsyncTask.isCancelled()) {
            obtainAsyncTask.cancel(true);
        }
        if (submitAsyncTask != null && !submitAsyncTask.isCancelled()) {
            submitAsyncTask.cancel(true);
        }
        super.onPause();
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
            case R.id.pckbill_obtain:
                doObtainPckbill();
                break;
            case R.id.pckbill_submit:
                doSubmitPckbillno();
                break;

        }
        return super.onOptionsItemSelected(item);
    }


    private void doObtainPckbill() {
        if(!TextUtils.isEmpty(mCurrentPckBillno)){
            Toast.makeText(PickupActivity.this,"当前调度单尚未提交",Toast.LENGTH_SHORT).show();
            return;
        }
        Editable editable = mSearchAutoComplete.getText();
        if(!TextUtils.isEmpty(editable)){
            if(editable.toString().matches("^[cC][0-9]{2}")){
                mSearchAutoComplete.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
                mSearchAutoComplete.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_ENTER));
            }else{
                Toast.makeText(PickupActivity.this,"输入车号格式错误",Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(PickupActivity.this,"请输入车号",Toast.LENGTH_SHORT).show();
        }
        mActionButton.postDelayed(new Runnable() {
            @Override
            public void run() {
                Utils.hideSoftInputBord(mActionButton);
            }
        },100);
    }
    private void doSubmitPckbillno() {
        if(TextUtils.isEmpty(mCurrentChehao) || TextUtils.isEmpty(mCurrentPckBillno) ){
            Toast.makeText(App.getAppContext(),"你还没有绑定车号",Toast.LENGTH_SHORT).show();
            return ;
        }
        if(!mPickupAdapter.isAllBasketed()){
          //  Toast.makeText(PickupActivity.this,"此处有药品尚未发货完成，这里用提示演示",Toast.LENGTH_SHORT).show();
                    List<PickupInfo> infos = mPickupAdapter.getNonBasketed();
                    StringBuffer sbuf= new StringBuffer();
                    for (PickupInfo info:infos){
                        if(TextUtils.isEmpty(info.getRemark())){
                            sbuf.append(info.getLocName()+"/"+info.getInspSeqNo()+"/"+info.getQuantity()+"/"+info.getShippingArea()+"\r\n");
                        }
                    }
                    if(!TextUtils.isEmpty(sbuf)){
                        Toast.makeText(App.getAppContext(),"还有药品没有入筐或说明:\r\n货位/验号/数量/集号\r\n"+sbuf,Toast.LENGTH_SHORT).show();
                    }
            return ;
        }
        showSubmitAlertDialog();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        App.setUserBean(null);
        mCurrentPckBillno="";
        mCurrentChehao="";
        mProState=PickupInfo.PRO_normal;
    }


}
