package com.example.zhang.thinmusic.activity;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.SimpleAdapter;
import android.widget.Toolbar;

import com.example.zhang.thinmusic.R;
import com.example.zhang.thinmusic.service.PlayService;
import com.example.zhang.thinmusic.utils.PermissionReq;
import com.example.zhang.thinmusic.utils.ViewBinder;


/**activity基类
 * Created by zhang on 2018/3/20.
 */

public abstract class BaseActivity extends AppCompatActivity {
    protected Handler handler;
    protected PlayService playService;
    private ServiceConnection serviceConnection;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setSystemBarTransparent();
        handler = new Handler(Looper.getMainLooper());
        bindService();
    }
    @Override
    public void setContentView(int layoutResID){
        super.setContentView(layoutResID);
        initView();
    }

    @Override
    public void setContentView(View view){
        super.setContentView(view);
        initView();
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params ){
        super.setContentView(view, params);
        initView();
    }

    private void initView(){
        ViewBinder.bind(this);

        androidx.appcompat.widget.Toolbar mToolbar = findViewById(R.id.toolbar);
        if(mToolbar == null){
            throw new IllegalStateException("Layout is required to include a toolbar");
        }
        setSupportActionBar(mToolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);//给左上角图标的左边加上一个返回的图标
        }
    }
    //绑定播放服务
    private  void bindService(){
        Intent intent = new Intent();
        intent.setClass(this,PlayService.class);
        serviceConnection = new PlayServiceConnection();
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
       /* @param intent
        @param serviceconnection 监听服务当前状态
        @param BIND_AUTO_CREATE 服务自动创建然后绑定*/
    }
    //重写Serviceconnection
    private  class PlayServiceConnection implements ServiceConnection{
        @Override
        public  void onServiceConnected(ComponentName name, IBinder service){
            playService = ((PlayService.PlayBinder)service).getService();
            onServiceBound();
        }

        @Override
        public void onServiceDisconnected(ComponentName name){
            Log.e(getClass().getSimpleName(), "onServiceDisconnected: " );
        }
    }
    protected void onServiceBound(){

    }

    //沉浸式任务栏
    private void setSystemBarTransparent(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            //Lolipop以上版本
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void showProgress(){showProgress(getString(R.string.loading));}

    public void showProgress(String message){
        if(progressDialog == null){
            progressDialog = new ProgressDialog(this);
            progressDialog.setCancelable(false);
        }
        progressDialog.setMessage(message);
        if(!progressDialog.isShowing()){
            progressDialog.show();
        }
    }

    public void cancelProgress(){
        if(progressDialog !=null && progressDialog.isShowing()){
            progressDialog.cancel();
        }
    }

    //权限请求
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        PermissionReq.onRequsetPermissionsResult(requestCode,permissions,grantResults);
    }
    @Override
    protected void onDestroy(){
        if(serviceConnection != null){
            unbindService(serviceConnection);
        }
        super.onDestroy();
    }
}
