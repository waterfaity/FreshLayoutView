package com.waterfairy.widget.refresh.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.waterfairy.widget.refresh.inter.PullRefresh;


public class PullRefreshImageView extends ImageView implements PullRefresh {

    public PullRefreshImageView(Context context) {
        super(context);
    }

    public PullRefreshImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PullRefreshImageView(Context context, AttributeSet attrs, int defStyle) {
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
