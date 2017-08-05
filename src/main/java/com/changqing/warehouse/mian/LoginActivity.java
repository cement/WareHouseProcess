package com.changqing.warehouse.mian;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.changqing.warehouse.R;
import com.changqing.warehouse.Utils.Utils;
import com.changqing.warehouse.app.App;
import com.changqing.warehouse.bean.Table;
import com.changqing.warehouse.bean.UserBean;
import com.changqing.warehouse.db.DBCQ170Connection;
import com.changqing.warehouse.db.dao.DBVisitor2;
import com.changqing.warehouse.db.dto.LoginDto;
import com.google.gson.Gson;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity  {


    public static final int LOGIN_TYPE_pckokperson = 1;
    public static final int LOGIN_TYPE_checkperson =2;



    private UserLoginTask mUserLoginTask = null;


    private ImageView mTitleImageView;
    // UI references.
    private AutoCompleteTextView mUsernameView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private int mLoginType = 0;
    private SimpleCursorAdapter mAutoCompleteAdapter;
    private AsyncHintQueryHelper mQueryHelper;
    private Cursor oldCursor;
    private SQLiteDatabase WhDatabase;
    private SimpleCursorAdapter mCursorAdapter;
    private LoaderCallback mLoaderCallback;
   // private SharedPreferences userInfoPreferences;
    private CheckBox mRememberCheckBox;
    private CheckBox mAutoLoginCheckBox;
    private Toast backToast;
    //private MyQueryHandler mQuhandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity_layout);

        backToast = Toast.makeText(this, "再按一次您将离开仓库管理平台,为保证安全，默认登录信息也将清除", Toast.LENGTH_SHORT);


        //userInfoPreferences = getSharedPreferences("userInfo",Context.MODE_PRIVATE);
//        String userInfoStr =  userInfoPreferences.getString("user",null);
//        if(!TextUtils.isEmpty(userInfoStr)){
//            UserBean userBean = App.getGson().fromJson(userInfoStr,UserBean.class);
//            if(userBean!=null){
//                long logintime = userBean.getLoginTime();
//                long currentime = System.currentTimeMillis();
//                Log.d("---1-", "attemptLogin() called with: logintime = [" + logintime + "]");
//                Log.d("---2-", "attemptLogin() called with: currentime = [" + currentime + "]");
//                if(currentime-logintime<1000*60*60*24*7){
//                    App.setUserBean(userBean);
//                    if( userBean.getLogintype() == LOGIN_TYPE_pckokperson){
//                        Intent intent = new Intent(LoginActivity.this,PickupActivity.class);
//                        startActivity(intent);
//                        finish();
//                    }else if(userBean.getLogintype() == LOGIN_TYPE_checkperson){
//                        Intent intent = new Intent(LoginActivity.this,CheckActivity.class);
//                        startActivity(intent);
//                        finish();
//                    }
//                }
//            }
//        }



        // Set up the login form.
        mTitleImageView = (ImageView) findViewById(R.id.login_image);
        mTitleImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Connection connection = DBCQ170Connection.getConnectionWithTimeOut();
                        String sql = "SELECT * FROM v_print_storeout WHERE ThisDate>\'2017-07-11\' AND pckbillno=\'PA201707110028\'";
                        Table table =null;
                        try {
                            table = DBVisitor2.query(connection,sql,new String[]{});
                            //Log.d("--test--", "table"+table+"\r\n");

                            Gson gson = new Gson();
                            String tableStr = gson.toJson(table);
                            Log.d("--tableStr--", "table"+tableStr+"\r\n");
                            Table t = gson.fromJson(tableStr,Table.class);
                            Log.d("--table--", "table"+t+"\r\n");
                            /* Log.d("--test--", "table.getCell(1,\"operator\")"+table.getCell(1,"DocPerson")+"\r\n");
                            Log.d("--test--", "table.getHeader()"+table.getHeader()+"\r\n");
                            Log.d("--test--", "table.getAllRowCount()"+table.getAllRowCount()+"\r\n");
                            Log.d("--test--", "table.getColum(\"beginTime\")"+table.getColum("COMNAME")+"\r\n");
                            Log.d("--test--", "table.getColum(\"operator\")"+table.getColum("custName")+"\r\n");*/
                        } catch (SQLException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }).start();




            }
        });

        //mQuhandler = new MyQueryHandler(getContentResolver());
        mUsernameView = (AutoCompleteTextView) findViewById(R.id.login_username);
       // Drawable userDrawable = getResources().getDrawable(R.drawable.login_user_selector));
       // userDrawable.setBounds(0, 0, mUsernameView.getHeight(), mUsernameView.getHeight());


//        if (WhDatabase == null) {
//            WhDatabase = openOrCreateDatabase("AppWareHouse.db",MODE_PRIVATE, null);
//            String createSql = "create table AppWareHouse_login.table (_id integer primary key autoincrement,username varchar(100))";
//            WhDatabase.execSQL(createSql);
//            WhDatabase.rawQuery();
//        }
        mUsernameView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

//                if(LoginActivity.this.getLoaderManager().getLoader(0)==null){
//                    mLoaderCallback = new LoaderCallback();
//                    getSupportLoaderManager().initLoader(0,null,mLoaderCallback);
//                }else{
//                    getSupportLoaderManager().restartLoader(0,null,mLoaderCallback);
//                }
                //mQuhandler.startQuery(0,);

            }
        });

 //       mUsernameView.setAdapter(new SimpleCursorAdapter(this,));
 //       populateAutoComplete();

        mPasswordView = (EditText) findViewById(R.id.login_password);
//        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
//                if (id == R.id.login || id == EditorInfo.IME_NULL) {
//                    attemptLogin();
//                    return true;
//                }
//                return false;
//            }
//        });


        mRememberCheckBox = (CheckBox) findViewById(R.id.login_remember_password);
        mAutoLoginCheckBox = (CheckBox) findViewById(R.id.login_auto_login);
        mAutoLoginCheckBox.setChecked(false);
        mRememberCheckBox.setChecked(false);

        Button mPckokPersonLoginButton = (Button) findViewById(R.id.pckokperson_login_button);
        mPckokPersonLoginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                 mLoginType = LOGIN_TYPE_pckokperson;
                //if(TextUtils.isEmpty(mUsernameView.getText())&&TextUtils.isEmpty(mPasswordView.getText())){
                if(mAutoLoginCheckBox.isChecked()){
                     autoLogin(mLoginType);
                }else{
                     attemptLogin(mLoginType);
                }

                Utils.hideSoftInputBord(view);

            }
        });
        Button mCheckpersonButton = (Button) findViewById(R.id.checkperson_login_button);
        mCheckpersonButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                 mLoginType = LOGIN_TYPE_checkperson;
                if(TextUtils.isEmpty(mUsernameView.getText())&&TextUtils.isEmpty(mPasswordView.getText())){
                    autoLogin(mLoginType);
                }else{
                    attemptLogin(mLoginType);
                }

                Utils.hideSoftInputBord(view);

            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

//        mQueryHelper = new AsyncHintQueryHelper(getContentResolver());
//        mQueryHelper.startQuery(0,null, Uri.parse(""),new String[]{},"keyword=?",new String[]{""},null);
//        openOrCreateDatabase("", Context.MODE_PRIVATE,null);


    }

    private void attemptLogin(int longinType,String username,String password) {
        // Reset errors.
        mUsernameView.setError(null);
        mPasswordView.setError(null);



        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
//        if (TextUtils.isEmpty(password) && !isPasswordValid(password)) {
//            mPasswordView.setError("密码必须大于6位");
//            focusView = mPasswordView;
//            cancel = true;
//            return;
//        }

        // Check for a valid username address.
        if (TextUtils.isEmpty(username) && !isUsernameValid(username)) {
            mUsernameView.setError("用户名必须大于1位");
            focusView = mUsernameView;
            cancel = true;
            return;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.

            showProgress(true);

            mUserLoginTask = new UserLoginTask(username, password);
            mUserLoginTask.execute(longinType);
        }
    }
    private void attemptLogin(int longinType) {

        // Store values at the time of the login attempt.
        String username = mUsernameView.getText().toString();
        String password = mPasswordView.getText().toString();

        attemptLogin(longinType,username,password);
    }

    private boolean isUsernameValid(String email) {
        //TODO: Replace this with your own logic
        return email.length()>1;
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 6;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }



    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mUsernameView.setAdapter(adapter);
    }



    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Integer, Void, UserBean> {

        private final String mUsername;
        private final String mPassword;

        UserLoginTask(String username, String password) {
            mUsername = username;
            mPassword = password;
            Log.d("=====", "UserLoginTask() called with: username = [" + username + "], password = [" + password + "]");
        }

        @Override
        protected UserBean doInBackground(Integer... params) {
            int loginType = params[0];
            boolean reault = false;
            Log.d("----", "doInBackground() called with: mUsername = [" + mUsername + "]");
            Log.d("----", "doInBackground() called with: mPassword = [" + mPassword + "]");
            if(loginType == LOGIN_TYPE_pckokperson){
                return LoginDto.pckokPersonLogin(mUsername,mPassword);
            }else if(loginType == LOGIN_TYPE_checkperson){
                return LoginDto.checkPersonLogin(mUsername,mPassword);
            }

            return new UserBean();
        }

        @Override
        protected void onPostExecute(final UserBean resultUser) {
            mUserLoginTask = null;
            showProgress(false);

            if (!TextUtils.isEmpty(resultUser.getUserName())
                    && !TextUtils.isEmpty(resultUser.getLoginLog())
                    && resultUser.getLoginLog().equals(UserBean.LOGIN_SUCCESS)){
                resultUser.setLoginTime(System.currentTimeMillis());
                resultUser.setLogintype(mLoginType);
                //App.setUserBean(resultUser);

                if(mRememberCheckBox.isChecked()){
                    SharedPreferences userInfoPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

                    if(mLoginType== LOGIN_TYPE_pckokperson){
                        String userInfo = App.getGson().toJson(resultUser);
                        userInfoPreferences.edit().putString("pckokperson",userInfo).commit();

                    }else if(mLoginType == LOGIN_TYPE_checkperson){
                        String userInfo = App.getGson().toJson(resultUser);
                        userInfoPreferences.edit().putString("checkperson",userInfo).commit();

                    }

                }
                if( mLoginType == LOGIN_TYPE_pckokperson){
                    Intent intent = new Intent(LoginActivity.this,PickupActivity.class);
                    intent.putExtra("userBean",resultUser);
                    startActivity(intent);
                    //Utils.hideSoftInputBord(mPasswordView);
                    //Utils.hideSoftInputMethod(mPasswordView);
                    //finish();
                }else if(mLoginType == LOGIN_TYPE_checkperson){
                    Intent intent = new Intent(LoginActivity.this,CheckActivity.class);
                    intent.putExtra("userBean",resultUser);
                    startActivity(intent);

                    //finish();
                }
            } else {
                if(!TextUtils.isEmpty(resultUser.getLoginLog())){
                    mPasswordView.setError(resultUser.getLoginLog());
                }else{
                    mPasswordView.setError(UserBean.LOGIN_FAIL);
                }
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mUserLoginTask = null;
            showProgress(false);
        }
    }


    public class AsyncHintQueryHelper extends AsyncQueryHandler{

        public AsyncHintQueryHelper(ContentResolver cr) {
            super(cr);
        }

        @Override
        protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
            super.onQueryComplete(token, cookie, cursor);
        }
    }
//
//    public class MyQueryHandler extends AsyncQueryHandler{
//
//        public MyQueryHandler(ContentResolver cr) {
//            super(cr);
//        }
//
//        @Override
//        protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
//
//            if (mAutoCompleteAdapter == null) {
//                mAutoCompleteAdapter = new SimpleCursorAdapter(LoginActivity.this,android.R.layout.simple_list_item_2,cursor,new String[]{},new int[]{}, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER.);
//
//                mUsernameView.setAdapter(mAutoCompleteAdapter);
//            }else{
//                oldCursor = mAutoCompleteAdapter.swapCursor(cursor);
//            }
//
//
//            super.onQueryComplete(token, cookie, cursor);
//        }
//    }

    public class LoaderCallback implements LoaderManager.LoaderCallbacks<Cursor>{

        @Override
        public Loader onCreateLoader(int id, Bundle args) {
            String uriStr = "content://com.changqing.warehouse.provider.LoginPromptProvider/logprompt";

            return new CursorLoader(LoginActivity.this, Uri.parse(uriStr),new String[]{"username"},null,null,null);
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

            if (mUsernameView.getAdapter() == null) {
                mCursorAdapter = new SimpleCursorAdapter(LoginActivity.this,android.R.layout.simple_list_item_1,cursor,new String[]{"username"},new int[]{android.R.id.text1},0);
                mUsernameView.setAdapter(mCursorAdapter);
            }else{
                mCursorAdapter.swapCursor(cursor);
            }
        }


        @Override
        public void onLoaderReset(Loader loader) {
            mCursorAdapter.swapCursor(null);
        }
    }


    public boolean autoLogin(int logintype){
        String userInfoStr = null;
        SharedPreferences userInfoPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        if(logintype == LOGIN_TYPE_pckokperson){
            userInfoStr =  userInfoPreferences.getString("pckokperson",null);
        }else if(logintype == LOGIN_TYPE_checkperson){
            userInfoStr =  userInfoPreferences.getString("checkperson",null);
        }
        Log.d("--------", "autoLogin() called with: userInfoStr = [" + userInfoStr + "]");
        if(!TextUtils.isEmpty(userInfoStr)){
            UserBean userBean = App.getGson().fromJson(userInfoStr,UserBean.class);
            if(userBean!=null){
                long logintime = userBean.getLoginTime();
                long currentime = System.currentTimeMillis();
                Log.d("---1-", "attemptLogin() called with: logintime = [" + logintime + "]");
                Log.d("---2-", "attemptLogin() called with: currentime = [" + currentime + "]");
                if(currentime-logintime<1000*60*60*24){
                  if(userBean.getLogintype()==mLoginType){

                      showProgress(true);

                      mUserLoginTask = new UserLoginTask(userBean.getUserName(), userBean.getPassWord());
                      mUserLoginTask.execute(userBean.getLogintype());
                       //attemptLogin(mLoginType,userBean.getUserName(),userBean.getPassWord());
                       return true;
                  }else{
                       Toast.makeText(App.getAppContext(),"请按岗位登录",Toast.LENGTH_SHORT).show();
                  }

//                    App.setUserBean(userBean);
//                    if( userBean.getLogintype() == LOGIN_TYPE_pckokperson){
//                        Intent intent = new Intent(LoginActivity.this,PickupActivity.class);
//                        startActivity(intent);
//                        finish();
//                    }else if(userBean.getLogintype() == LOGIN_TYPE_checkperson){
//                        Intent intent = new Intent(LoginActivity.this,CheckActivity.class);
//                        startActivity(intent);
//                        finish();
//                    }

                }else{
                   //SharedPreferences userInfoPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    userInfoPreferences.edit().remove("pckokperson").commit();
                    userInfoPreferences.edit().remove("checkperson").commit();
                    Toast.makeText(App.getAppContext(),"默认登录当天有效",Toast.LENGTH_SHORT).show();
                }
            }
        }else{
            Toast.makeText(App.getAppContext(),"上次登录未选择记住密码，无法默认登录",Toast.LENGTH_SHORT).show();
        }
       return false;
    }

    @Override
    protected void onPause() {
        if (oldCursor != null) {
            oldCursor.close();
        }
        super.onPause();

    }


    @Override
    public void onBackPressed() {
        if (backToast != null && backToast.getView() != null && backToast.getView().isShown()) {
            finish();
        }
        backToast.show();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences userInfoPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        userInfoPreferences.edit().remove("pckokperson").commit();
        userInfoPreferences.edit().remove("checkperson").commit();
        Log.d("===login===", "onDestroy() pckokperson"+userInfoPreferences.getString("pckokperson",null));
        Log.d("===login===", "onDestroy() checkperson"+userInfoPreferences.getString("checkperson",null));

    }
}

