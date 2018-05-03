package com.waterfairy.widget.refresh.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.waterfairy.widget.refresh.inter.PullRefresh;

/**
 * @author water_fairy
 * @email 995637517@qq.com
 * @date 2018/5/3
 * @Description:
 */
public class PullRefreshRecyclerView extends RecyclerView implements PullRefresh {
    public PullRefreshRecyclerView(Context context) {
        super(context);
    }

    public PullRefreshRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean canPullDown() {
        LayoutManager layoutManager = getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager) {
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
            //getLayoutManager().getItemCount() == 0  没有item的时候也可以下拉刷新
            return getLayoutManager().getItemCount() == 0
                    || linearLayoutManager.findFirstVisibleItemPosition() == 0
                    && getChildAt(0).getTop() >= 0;
        }
        return false;
    }

    @Override
    public boolean canPullUp() {
        LayoutManager layoutManager = getLayoutManager();
        int itemCount = getLayoutManager().getItemCount();
        if (itemCount == 0) {
            //没有item的时候也可以下拉刷新
            return true;
        } else {
            if (layoutManager instanceof LinearLayoutManager) {
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
                if (linearLayoutManager.findLastVisibleItemPosition() == itemCount - 1) {
                    int index = linearLayoutManager.findLastVisibleItemPosition() - linearLayoutManager.findFirstVisibleItemPosition();
                    return getChildAt(index) != null && getChildAt(index).getBottom() <= getMeasuredHeight();
                }
            }
        }
        return false;
    }
}
