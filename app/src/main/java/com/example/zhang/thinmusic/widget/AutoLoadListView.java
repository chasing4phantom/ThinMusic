package com.example.zhang.thinmusic.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import com.example.zhang.thinmusic.R;

/**
 * Created by zhang on 2018/4/19.
 */

public class AutoLoadListView extends ListView implements AbsListView.OnScrollListener{
     private static final String TAG = AutoLoadListView.class.getSimpleName();
     private View Footer;
     private OnLoadListener mListener;
     private int mFirstVisibleItem = 0;
     private boolean mEnableLoad = true;
     private boolean mIsLoading =false;

     public AutoLoadListView(Context context){
         super(context);
         init();
     }

     public AutoLoadListView(Context context, AttributeSet attributeSet){
         super(context,attributeSet);
         init();
     }

     public AutoLoadListView(Context context,AttributeSet attributeSet,int defStyleAttr){
         super(context,attributeSet,defStyleAttr);
         init();
     }

     public void init(){
         Footer = LayoutInflater.from(getContext()).inflate(R.layout.auto_load_list_view_footer,null);
         addFooterView(Footer,null,false);
         setOnScrollListener(this);
         onLoadComplete();
     }

     public void setOnLoadListener(OnLoadListener listener){mListener = listener;}

     public void onLoadComplete(){
         Log.d(TAG, "onLoadComplete ");
         mIsLoading = false;
         removeFooterView(Footer);
     }

     public void setEnable(boolean enable){mEnableLoad = enable;}

     @Override
    public void onScroll(AbsListView view,int firstVisibleItem,int visibleItemCount,int totalItemCount){
        boolean isPullDown = firstVisibleItem >mFirstVisibleItem;
        if(mEnableLoad && !mIsLoading && isPullDown){
            int lastVisibleItem = firstVisibleItem + visibleItemCount;
            if(lastVisibleItem >= totalItemCount -1){
                onLoad();
            }
        }
        mFirstVisibleItem = firstVisibleItem;
     }

     @Override
    public void onScrollStateChanged(AbsListView view,int scrollState){

     }

     private void onLoad(){
         Log.d(TAG, "onLoad: onLoad");
         mIsLoading = true;
         addFooterView(Footer,null,false);
         if(mListener !=null){
             mListener.onLoad();
         }
     }

     public interface OnLoadListener{
         void onLoad();
     }
}
