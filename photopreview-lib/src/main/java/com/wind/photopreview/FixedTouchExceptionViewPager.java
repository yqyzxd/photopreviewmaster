package com.wind.photopreview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by wind on 2018/11/10.
 */

public class FixedTouchExceptionViewPager extends ViewPager {
    public FixedTouchExceptionViewPager(@NonNull Context context) {
        super(context);
    }

    public FixedTouchExceptionViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            return super.onInterceptTouchEvent(ev);
        }catch (Exception e){
            e.printStackTrace();
        }
        return true;

    }
}
