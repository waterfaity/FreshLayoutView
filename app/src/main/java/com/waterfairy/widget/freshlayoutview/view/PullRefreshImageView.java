package com.waterfairy.widget.freshlayoutview.view;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;


public class PullRefreshImageView extends AppCompatImageView implements PullRefresh {

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
