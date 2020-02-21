package com.example.zhang.thinmusic.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.navigation.NavigationView;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.view.GravityCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.drawerlayout.widget.DrawerLayout;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zhang.thinmusic.ControlPanel;
import com.example.zhang.thinmusic.R;
import com.example.zhang.thinmusic.adapter.FragmentAdapter;
import com.example.zhang.thinmusic.constants.Extras;
import com.example.zhang.thinmusic.constants.Keys;
import com.example.zhang.thinmusic.fragments.LocalMusicFragment;
import com.example.zhang.thinmusic.fragments.NeteaseListFragment;
import com.example.zhang.thinmusic.fragments.NetlistFragment;
import com.example.zhang.thinmusic.fragments.PlayFragment;
import com.example.zhang.thinmusic.utils.AudioPlayer;
import com.example.zhang.thinmusic.utils.Bind;
import com.example.zhang.thinmusic.service.QuitTimer;
import com.example.zhang.thinmusic.utils.SystemUtils;
import com.example.zhang.thinmusic.widget.NaviMenuExcuter;

/**
 * Created by zhang on 2018/3/25.
 */

public class HomepageActivity extends BaseActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener,ViewPager.OnPageChangeListener,QuitTimer.OnTimerListener{
    @Bind(R.id.drawer_layout)
    private DrawerLayout drawerLayout;
    @Bind(R.id.navigation_view)
    private NavigationView navigationView;
    @Bind(R.id.localmusic)
    private TextView LocalMusic;
    @Bind(R.id.net_music)
    private TextView NetMusic;
    @Bind(R.id.netease_music)
    private TextView NeteaseMusic;
    @Bind(R.id.iv_menu)
    private ImageView menu;
    @Bind(R.id.viewpager)
    private ViewPager viewPager;
    @Bind(R.id.play_bar)
    private FrameLayout Playbar;

    private View NavigationHeader;
    private LocalMusicFragment localMusicFragment;
    private NetlistFragment netMusicFragment;
    private NeteaseListFragment neteaseListFragment;
    private PlayFragment playFragment;
    private ControlPanel controlPanel;
    private NaviMenuExcuter naviMenuExcuter;
    private MenuItem timerItem;
    private boolean isPlayFragmentShow;

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        if(isGrantExternalRW(HomepageActivity.this) != true){
            return;
        }//判断是否有读取存储权限
        setContentView(R.layout.activity_music);
    }

    @Override
    protected void onServiceBound(){
        setupView();
        controlPanel = new ControlPanel(Playbar);//playbar初始化
        naviMenuExcuter =new NaviMenuExcuter(this);//navigation初始化
        AudioPlayer.get().addOnPlayListener(controlPanel);//playbar监听
        QuitTimer.get().setOnTimerListener(this);//计时器监听
        parseIntent();
    }
    @Override
    protected  void onNewIntent(Intent intent){
        setIntent(intent);
        parseIntent();
    }
    private void setupView(){
        NavigationHeader = LayoutInflater.from(this).inflate(R.layout.navigation_header,navigationView,false);
        navigationView.addHeaderView(NavigationHeader);


        //初始化碎片，并默认显示localmusic
        localMusicFragment = new LocalMusicFragment();
        netMusicFragment = new NetlistFragment();
        neteaseListFragment = new NeteaseListFragment();
        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager());
        adapter.addFragment(localMusicFragment);
        adapter.addFragment(netMusicFragment);
        adapter.addFragment(neteaseListFragment);
        viewPager.setAdapter(adapter);
        LocalMusic.setSelected(true);

        //绑定按键监听
        LocalMusic.setOnClickListener(this);
        NetMusic.setOnClickListener(this);
        NeteaseMusic.setOnClickListener(this);
        menu.setOnClickListener(this);
        Playbar.setOnClickListener(this);
        viewPager.addOnPageChangeListener(this);
        navigationView.setNavigationItemSelectedListener(this);
    }
    /*由通知进入playfragment*/
    private void parseIntent(){
        Intent intent = getIntent();
        if(intent.hasExtra(Extras.EXTRA_NOTIFICATION)){
            showPlayingFragment();
            setIntent(new Intent());
        }else{
        String action = intent.getAction();
        if(intent.ACTION_VIEW.equals(action)){
        if(intent.getType().equals("audio/*")){
            showPlayingFragment();
            Uri data=intent.getData();
            String path = Uri.decode(data.getEncodedPath());
            Bundle bundle = new Bundle();
            bundle.putString("path",path);
            playFragment.setArguments(bundle);
            AudioPlayer.get().searchMusicandPlay(path);
            setIntent(new Intent());}
        }
        }

    }
   /* 定时器显示*/
    @Override
    public void onTimer(long remain){
        if(timerItem == null){
            timerItem = navigationView.getMenu().findItem(R.id.nav_timer);
        }
    String title = "定时停止播放";
        timerItem.setTitle(remain == 0? title : SystemUtils.formatTime(title +"(mm:ss)",remain));

    }

    /*按键响应处理逻辑*/
    @Override
    public  void onClick(View v) {
        switch (v.getId()) {
            case R.id.localmusic:
                viewPager.setCurrentItem(0);
                break;
            case R.id.net_music:
                viewPager.setCurrentItem(1);
                break;
            case R.id.netease_music:
                viewPager.setCurrentItem(2);
                break;
            case R.id.play_bar:
                showPlayingFragment();
                break;
            case R.id.iv_menu:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item){
        drawerLayout.closeDrawers();
        handler.postDelayed(()-> item.setChecked(false), 500);
        return naviMenuExcuter.onNavigationItemSelected(item);

    }
        @Override
                public void onPageScrolled(int position,float positionOffset,int positionOffsetPixels){

        }

        @Override
                public void onPageSelected(int position){
           if(position == 0){
               LocalMusic.setSelected(true);
               NetMusic.setSelected(false);
               NeteaseMusic.setSelected(false);
           }
           else if(position ==1){
               LocalMusic.setSelected(false);
               NetMusic.setSelected(true);
               NeteaseMusic.setSelected(false);
           }
           else if(position ==2){
               LocalMusic.setSelected(false);
               NetMusic.setSelected(false);
               NeteaseMusic.setSelected(true);
           }
        }//布局切换

        @Override
                public  void onPageScrollStateChanged(int state){

        }

        private void showPlayingFragment(){
                    if(isPlayFragmentShow){
                        return;
                    }
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.setCustomAnimations(R.anim.fragment_slide_up,0);//切换时animation动画过度
            if(playFragment == null){
                playFragment = new PlayFragment();
                ft.replace(android.R.id.content,playFragment);
            }else{
                ft.show(playFragment);
            }
            ft.commitAllowingStateLoss();
            isPlayFragmentShow = true;
    }

    private void hidePlayingFragment(){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(0,R.anim.fragment_slide_down);
        ft.hide(playFragment);
        ft.commitAllowingStateLoss();
        isPlayFragmentShow = false;
    }

    /*按下返回键的逻辑*/
    @Override
    public  void onBackPressed(){
            if(playFragment !=null && isPlayFragmentShow){
                hidePlayingFragment();
                return;
            }
            if(drawerLayout.isDrawerOpen(GravityCompat.START)){
                drawerLayout.closeDrawers();
                return;
            }
        super.onBackPressed();
    }

    /*数据保存*/
    @SuppressLint("MissingSuperCall")
    @Override
    protected void onSaveInstanceState(Bundle outState){
        outState.putInt(Keys.VIEW_PAGER_INDEX,viewPager.getCurrentItem());
        localMusicFragment.onSaveInstanceState(outState);
        netMusicFragment.onSaveInstanceState(outState);
        neteaseListFragment.onSaveInstanceState(outState);

    }
   /* 数据恢复*/
    @Override
    protected void onRestoreInstanceState(final Bundle saveInstanceState){
        viewPager.post(()->{
            viewPager.setCurrentItem(saveInstanceState.getInt(Keys.VIEW_PAGER_INDEX),false);
            localMusicFragment.onRestoreInstanceState(saveInstanceState);
            netMusicFragment.onRestoreInstanceState(saveInstanceState);
            neteaseListFragment.onRestoreInstanceState(saveInstanceState);
        });
    }

    @Override
    protected void onDestroy(){
        AudioPlayer.get().removeOnPlayListener(controlPanel);
        QuitTimer.get().setOnTimerListener(null);
        super.onDestroy();
    }
    /*获得读取权限*/
    public  static  boolean isGrantExternalRW(Activity activity){
        if(activity.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            activity.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
            return  false;
        }
        return  true;
    }

}
