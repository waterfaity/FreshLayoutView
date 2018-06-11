package com.waterfairy.widget.refresh.baseView;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.waterfairy.widget.refresh.inter.BaseExtraView;
import com.waterfairy.widget.refresh.inter.BaseNoDataView;
import com.waterfairy.widget.refresh.inter.OnFreshListener;
import com.waterfairy.widget.refresh.inter.PullRefresh;

/**
 * @author water_fairy
 * @email 995637517@qq.com
 * @date 2018/5/2
 * @Description: -
 * 说明:
 * 1.以 FreshLayout 为父类布局
 * 2.添加字布局: 子布局两种 1>直接写滚动布局(默认添加head,foot)2>布局中加上 head和foot 布局
 * 参考:
 * git :https://github.com/jingchenUSTC/PullToRefreshAndLoad
 * csdn:https://blog.csdn.net/zhongkejingwang/article/details/38868463
 */
public class FreshLayout extends RelativeLayout {
    //data
    public static final int STATE_SUCCESS = 1;
    public static final int STATE_FAILED = 2;
    public static final int STATE_REFRESHING = 3;
    public static final int STATE_LOADING_MORE = 4;
    public static final int STATE_NO_DATA = 5;
    //view
    private BaseNoDataView mNoDataView;
    private BaseExtraView mFootView, mHeadView;
    private View mFreshView;
    //touchHandler
    private TouchHandler mTouchHandler;
    //view是否已经创建
    private boolean isViewCreate;
    private OnViewCreateListener onViewCreateListener;
    private boolean canLoadMore = true;//是否可以加载更多

    public FreshLayout(Context context) {
        this(context, null);
    }

    public FreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mTouchHandler = new TouchHandler();
    }


    private void initView() {
    }

    private void findView() {
        //确保有1刷新view  或 1个footView,1个headView,1个刷新view
        int childCount = getChildCount();
        if (childCount == 1 || childCount == 2) {
            //只有一个刷新view  和 空view
            mFreshView = getChildAt(0);
            mFootView = new ExtraView(getContext());
            mHeadView = new ExtraView(getContext());
            addView((View) mHeadView);
            addView((View) mFootView);

            if (childCount == 2) {
                if (getChildAt(1) instanceof BaseNoDataView)
                    mNoDataView = (BaseNoDataView) getChildAt(1);
            }
        } else if (childCount == 3 || childCount == 4) {
            //确保有3个view  和 空view
            for (int i = 0; i < childCount; i++) {
                View childView = getChildAt(i);
                if (childView instanceof BaseExtraView) {
                    if (mHeadView == null) {
                        mHeadView = (BaseExtraView) childView;
                    } else if (childView instanceof BaseNoDataView) {
                        mNoDataView = (BaseNoDataView) childView;
                    } else {
                        mFootView = (BaseExtraView) childView;
                    }
                }
                if (childView instanceof PullRefresh) {
                    mFreshView = childView;
                }
            }
        }
        mTouchHandler.setView(this, mFreshView, mHeadView, mFootView, mNoDataView);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (!isViewCreate) {
            isViewCreate = true;
            findView();
            initView();
            mTouchHandler.setCanLoadMore(canLoadMore);
            new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    if (onViewCreateListener != null)
                        onViewCreateListener.onViewCreate(FreshLayout.this);
                }
            }.sendEmptyMessageDelayed(0, 100);
        }
        mTouchHandler.onLayout(l, r);
    }

    /**
     * 触摸事件分发
     *
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        mTouchHandler.dispatchTouchEvent(ev);
        super.dispatchTouchEvent(ev);
        return true;
    }

    public void setOnFreshListener(OnFreshListener onFreshListener) {
        mTouchHandler.setOnFreshListener(onFreshListener);
    }

    public void setSuccess() {
        mTouchHandler.setSuccess();
    }

    public void setFailed() {
        mTouchHandler.setFailed();
    }

    public void setState(int state) {
        switch (state) {
            case STATE_SUCCESS:
                setSuccess();
                break;
            case STATE_FAILED:
                setFailed();
                break;
            case STATE_REFRESHING:
                setRefreshing();
                break;
            case STATE_LOADING_MORE:
                setLoadingMore();
                break;
            case STATE_NO_DATA:
                mTouchHandler.setNoData();
                break;
        }
    }

    private void setRefreshing() {
        mTouchHandler.setRefreshing();
    }

    private void setLoadingMore() {
        mTouchHandler.setLoadingMore();

    }

    public View getContentView() {
        return mFreshView;
    }

    public View getHeadView() {
        return (View) mHeadView;
    }

    public View getFootView() {
        return (View) mFootView;
    }

    public void setCanLoadMore(boolean canLoadMore) {
        this.canLoadMore = canLoadMore;
        if (mTouchHandler != null) mTouchHandler.setCanLoadMore(canLoadMore);
    }

    public void setOnViewCreateListener(OnViewCreateListener onViewCreateListener) {
        this.onViewCreateListener = onViewCreateListener;
    }


    public interface OnViewCreateListener {
        void onViewCreate(FreshLayout freshLayout);
    }
}
