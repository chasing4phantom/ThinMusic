package com.example.zhang.thinmusic;

import android.content.Context;
import android.util.AttributeSet;

import android.widget.TextView;

/**
 * Created by zhang on 2018/1/31.
 */

public class MarqueeTextView extends TextView {
    public  MarqueeTextView(Context context){
        super(context);
    }
    public  MarqueeTextView(Context context, AttributeSet attrs){
        super(context, attrs);
    }
    public  MarqueeTextView(Context context, AttributeSet attrs, int defStyleAttr){
        super(context, attrs, defStyleAttr);
    }
    //返回textview是否处于选中的状态，只有选中的textview才能实现跑马灯效果
    @Override
    public  boolean isFocused(){
        return true;
    }
}
