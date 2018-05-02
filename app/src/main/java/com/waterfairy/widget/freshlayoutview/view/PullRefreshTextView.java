package com.waterfairy.widget.freshlayoutview.view;


import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

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
