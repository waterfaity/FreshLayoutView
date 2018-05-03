package com.waterfairy.widget.refresh.view;


import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.waterfairy.widget.refresh.inter.PullRefresh;


public class PullRefreshTextView extends TextView implements PullRefresh {

    public PullRefreshTextView(Context context) {
        super(context);
    }

    public PullRefreshTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PullRefreshTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean canPullDown() {
        return true;
    }

    @Override
    public boolean canPullUp() {
        return true;
    }

}
