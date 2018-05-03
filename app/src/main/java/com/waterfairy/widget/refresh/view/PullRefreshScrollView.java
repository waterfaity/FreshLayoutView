package com.waterfairy.widget.refresh.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

import com.waterfairy.widget.refresh.inter.PullRefresh;


public class PullRefreshScrollView extends ScrollView implements PullRefresh {

    public PullRefreshScrollView(Context context) {
        super(context);
    }

    public PullRefreshScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PullRefreshScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean canPullDown() {
        if (getScrollY() == 0)
            return true;
        else
            return false;
    }

    @Override
    public boolean canPullUp() {
        if (getScrollY() >= (getChildAt(0).getHeight() - getMeasuredHeight()))
            return true;
        else
            return false;
    }

}
