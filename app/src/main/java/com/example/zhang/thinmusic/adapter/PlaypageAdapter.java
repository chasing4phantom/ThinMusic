package com.example.zhang.thinmusic.adapter;

import 	androidx.viewpager.widget.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * 播放页面适配器
 * Created by 26292 on 2018/4/5.
 */

public class PlaypageAdapter extends PagerAdapter{
    private List<View> views;

    public PlaypageAdapter(List<View> Views){
        views = Views;
    }

    @Override
    public int getCount(){
        return views.size();
    }

    @Override
    public boolean isViewFromObject(View view,Object object){

        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container,int position){
        container.addView(views.get(position));
        return views.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container,int position,Object object){
        container.removeView(views.get(position));
    }

}
