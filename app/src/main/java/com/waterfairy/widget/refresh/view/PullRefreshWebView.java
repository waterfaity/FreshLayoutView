package com.waterfairy.widget.refresh.view;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

import com.waterfairy.widget.refresh.inter.PullRefresh;


public class PullRefreshWebView extends WebView implements PullRefresh {

    public PullRefreshWebView(Context context) {
        super(context);
    }

    public PullRefreshWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PullRefreshWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean canPullDown() {
        return getScrollY() == 0;
    }

    @Override
    public boolean canPullUp() {
        return getScrollY() >= getContentHeight() * getScale()
                - getMeasuredHeight();
    }
}
