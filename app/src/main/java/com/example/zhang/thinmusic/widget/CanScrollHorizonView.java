package com.example.zhang.thinmusic.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.example.zhang.thinmusic.adapter.CanScrollHorizonViewAdapter;

import java.util.HashMap;
import java.util.Map;


public  class CanScrollHorizonView extends HorizontalScrollView implements View.OnClickListener {

    public CanScrollHorizonView(Context context){
        super(context);
    }
    public CanScrollHorizonView(Context context,AttributeSet attributeSet,int defstyle){
        super(context,attributeSet,defstyle);
    }

/*    图片滚动时的回调*/
    public interface CurrentCardChangeListener{
        void onCurrentCardChangeListener(int position,View viewIndicater);
    }
/*        条目点击的回调*/
    public interface OnItemClickListener{
        void onClick(View view, int position);
}
    private CurrentCardChangeListener mListener;

    private OnItemClickListener onItemClickListener;

    private static final String TAG="CanScrollHorizonView";


/*    HorizontalScrollView中的Lineralayout*/
    private LinearLayout mContainier;

/*    子元素card的宽度和高度*/
    private int childWidth;
    private int chidlHeigth;

    private int mFirstIndex;//当前界面第一个Card的下标
    private int mCurrentIndex;//当前界面最后一个Card的下标
    private int mCountScreen;//横向显示crad的个数
    private CanScrollHorizonViewAdapter adapter;//View适配器

    private int ScreenWidth;//屏幕的宽度

    private Map<View,Integer> mViewPosition = new HashMap<>();

    public CanScrollHorizonView(Context context, AttributeSet attributes){
        super(context,attributes);
        //获得屏幕宽度
        WindowManager windowManager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(outMetrics);
        ScreenWidth = outMetrics.widthPixels;
    }
    public void onMeasure(int widthMeasureSpec,int heightMeasureSpec){
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mContainier = (LinearLayout)getChildAt(0);
    }

    //加载后一个cardview
    protected void loadnextCard(){
        //边界值计算
        if(mCurrentIndex == adapter.getCount()-1){
            return;
        }
        scrollTo(0,0);//移除第一个card，并将滚动重置
        mViewPosition.remove(mContainier.getChildAt(0));
        mContainier.removeViewAt(0);

        //获取下一个card，并加入容器中，设置onclick事件
        View view = adapter.getView(++mCurrentIndex,null,mContainier);
        view.setOnClickListener(this);
        mContainier.addView(view);
        mViewPosition.put(view,mCurrentIndex);

        mFirstIndex++;//当前第一个card向后移动一位

        if(mListener!=null){
            notifyCurrentCardChanged();
        }
    }

    //加载前一个card
    protected void loadpreCard(){
        //边界值计算,如果当前已经是第一张，返回
        if(mFirstIndex ==0){
            return;
        }
        int index = mCurrentIndex - mCountScreen;//计算当前应该第一个显示的card的index
        if(index>0){
            int oldViewPosition = mContainier.getChildCount()-1;//将最右的view放入containier中
            mViewPosition.remove(mContainier,getChildAt(oldViewPosition));//移除最右的一个card
            mContainier.removeViewAt(oldViewPosition);//移除最右的card布局

/*            将第一个应该显示的card的view放入*/
            View view = adapter.getView(index,null,mContainier);
            mViewPosition.put(view,index);
            mContainier.addView(view,0);
            view.setOnClickListener(this);
            scrollTo(childWidth,0);//水平向左一个card的宽度
            mCurrentIndex--;//当前位置减一
            mFirstIndex--;//当前第一个显示的cardindex减一

            if(mListener!=null){
                notifyCurrentCardChanged();//滑动回调
            }
        }

    }
 /*       滑动回调*/

    public void notifyCurrentCardChanged(){
        mListener.onCurrentCardChangeListener(mFirstIndex,mContainier.getChildAt(0));
    }
        /*初始化数据，设置adapter*/
    public void initDatas(CanScrollHorizonViewAdapter madapter,int mCountScreen){
        this.adapter = madapter;
        this.mCountScreen = mCountScreen;
        mContainier = (LinearLayout)getChildAt(0);
        final View view = adapter.getView(0,null,mContainier);//获取适配器中的第一个view
        mContainier.addView(view);

        //计算当前view的宽和高
        if(childWidth==0 && chidlHeigth==0){
            int w = View.MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
            int h = View.MeasureSpec.makeMeasureSpec(0,MeasureSpec.UNSPECIFIED);
            view.measure(w,h);
            childWidth = view.getMeasuredWidth();
            chidlHeigth = view.getMeasuredHeight();
            Log.e(TAG, "initDatas:w&h "+view.getMeasuredWidth()+","+view.getMeasuredHeight() );
            //mCountScreen = ScreenWidth/childWidth;
            Log.e(TAG, "initDatas:mCountScreen "+mCountScreen+"childwidth"+childWidth );
        }
        initFirstScreenChildren(mCountScreen);//初始化未滑动时的数据
    }

/*    加载初始未滑动时的view*/
    public void initFirstScreenChildren(int mCountScreen){
        mContainier = (LinearLayout)getChildAt(0);
        mContainier.removeAllViews();
        mViewPosition.clear();

        for(int i=0;i<mCountScreen;i++){
            View view = adapter.getView(i,null,mContainier);
            view.setOnClickListener(this);
            mContainier.addView(view);
            mViewPosition.put(view,i);
            mCurrentIndex = i;
        }
        if(mListener!=null){
            notifyCurrentCardChanged();
        }
    }
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent){
        switch (motionEvent.getAction()){
            case MotionEvent.ACTION_MOVE:
                Log.e(TAG, "onTouchEVENT: getScrollx():"+getScrollX() );

                int scrollx = getScrollX();
                if(scrollx>=childWidth)//滑动的距离大于item的宽度，加载下一个card，移除第一个card
                {
                    loadnextCard();
                }
                if(scrollx<=0){
                    loadpreCard();//scrollx<=0,加载前一个card，移除最后一个card
                }
                break;
        }
        return super.onTouchEvent(motionEvent);
    }
/*    getScrollX()及scrollx含义参考https://blog.csdn.net/linmiansheng/article/details/17767795*/


    @Override
    public void onClick( View view){
        if(onItemClickListener!=null){
            onItemClickListener.onClick(view,mViewPosition.get(view));
        }
    }

    public void setOnItemClickListener(OnItemClickListener mOnClickListener){
        this.onItemClickListener = mOnClickListener;
    }
    public void setmCurrentCardChangeListener(CurrentCardChangeListener listener){
        this.mListener = listener;
    }
}
