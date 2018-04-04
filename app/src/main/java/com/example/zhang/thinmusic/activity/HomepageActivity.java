package com.example.zhang.thinmusic.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.zhang.thinmusic.ControlPanel;
import com.example.zhang.thinmusic.R;
import com.example.zhang.thinmusic.adapter.FragmentAdapter;
import com.example.zhang.thinmusic.constants.Extras;
import com.example.zhang.thinmusic.constants.Keys;
import com.example.zhang.thinmusic.fragments.LocalMusicFragment;
import com.example.zhang.thinmusic.fragments.PlayFragment;
import com.example.zhang.thinmusic.utils.AudioPlayer;
import com.example.zhang.thinmusic.utils.Bind;

/**
 * Created by zhang on 2018/3/25.
 */

public class HomepageActivity extends BaseActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener,ViewPager.OnPageChangeListener{
    @Bind(R.id.drawer_layout)
    private DrawerLayout drawerLayout;
    @Bind(R.id.navigation_view)
    private NavigationView navigationView;
    @Bind(R.id.localmusic)
    private TextView LocalMusic;
    @Bind(R.id.favourite_music)
    private TextView FavourMusic;
    @Bind(R.id.viewpager)
    private ViewPager viewPager;
    @Bind(R.id.play_bar)
    private FrameLayout Playbar;

    private View NavigationHeader;
    private LocalMusicFragment localMusicFragment;
    private LocalMusicFragment favourMusicFragment;
    private PlayFragment playFragment;
    private ControlPanel controlPanel;
    private boolean isPlayFragmentShow;

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_music);
    }

    @Override
    protected void onServiceBound(){
        setupView();
        controlPanel = new ControlPanel(Playbar);
        AudioPlayer.get().addOnPlayListener(controlPanel);
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


        localMusicFragment = new LocalMusicFragment();
        favourMusicFragment = new LocalMusicFragment();
        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager());
        adapter.addFragment(localMusicFragment);
        adapter.addFragment(favourMusicFragment);
        viewPager.setAdapter(adapter);
        LocalMusic.setSelected(true);

        LocalMusic.setOnClickListener(this);
        FavourMusic.setOnClickListener(this);
        Playbar.setOnClickListener(this);
        viewPager.addOnPageChangeListener(this);
    }
    private void parseIntent(){
        Intent intent = getIntent();
        if(intent.hasExtra(Extras.EXTRA_NOTIFICATION)){
            showPlayingFragment();
            setIntent(new Intent());
        }
    }

    @Override
    public  void onClick(View v) {
        switch (v.getId()) {
            case R.id.localmusic:
                viewPager.setCurrentItem(0);
                break;
            case R.id.favourite_music:
                viewPager.setCurrentItem(1);
            case R.id.play_bar:
                showPlayingFragment();
                break;
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item){
        drawerLayout.closeDrawers();
        handler.postDelayed(()-> item.setChecked(false), 500);
        return false;

    }
        @Override
       public void onPageScrolled(int position,float positionOffset,int positionOffsetPixels){

        }

        @Override
                public void onPageSelected(int position){
           if(position == 0){
               LocalMusic.setSelected(true);
               FavourMusic.setSelected(false);
           }
           else{
               LocalMusic.setSelected(false);
               FavourMusic.setSelected(true);
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
            ft.setCustomAnimations(R.anim.fragment_slide_up,0);
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
        android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(0,R.anim.fragment_slide_up);
        ft.hide(playFragment);
        ft.commitAllowingStateLoss();
        isPlayFragmentShow = false;
    }
    @Override
    public  void onBackPressed(){
            if(playFragment !=null && isPlayFragmentShow){
                hidePlayingFragment();
                return;
            }
        super.onBackPressed();
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onSaveInstanceState(Bundle outState){
        outState.putInt(Keys.VIEW_PAGER_INDEX,viewPager.getCurrentItem());
        localMusicFragment.onSaveInstanceState(outState);
        favourMusicFragment.onSaveInstanceState(outState);

    }

    @Override
    protected void onRestoreInstanceState(final Bundle saveInstanceState){
        viewPager.post(()->{
            viewPager.setCurrentItem(saveInstanceState.getInt(Keys.VIEW_PAGER_INDEX),false);
            localMusicFragment.onRestoreInstanceState(saveInstanceState);
        });
    }

    @Override
    protected void onDestroy(){
        AudioPlayer.get().removeOnPlayListener(controlPanel);
        super.onDestroy();
    }
    /*获得读取权限*/
    public  static  boolean isGrantExternalRW(Activity activity){
        if(activity.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            activity.requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
            return  false;
        }
        return  true;
    }

}
