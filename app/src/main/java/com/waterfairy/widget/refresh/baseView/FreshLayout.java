package com.waterfairy.widget.refresh.baseView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.waterfairy.widget.refresh.inter.OnFreshListener;
import com.waterfairy.widget.refresh.inter.PullRefresh;
import com.waterfairy.widget.refresh.inter.RefreshViewTool;

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
    //view
    private ExtraView mFootView, mHeadView;
    private View mFreshView;
    //touchHandler
    private TouchHandler mTouchHandler;
    //view是否已经创建
    private boolean isViewCreate;
    private OnViewCreateListener onViewCreateListener;

    public FreshLayout(Context context) {
        this(context, null);
    }

    public FreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mTouchHandler = new TouchHandler();
    }


    private void initView() {
        if (mFootView != null)
            mFootView.setPosTag(RefreshViewTool.POS_FOOTER);
        if (mHeadView != null)
            mHeadView.setPosTag(RefreshViewTool.POS_HEADER);
    }

    private void findView() {
        //确保有1刷新view  或 1个footView,1个headView,1个刷新view
        int childCount = getChildCount();
        if (childCount == 1) {
            //只有一个刷新view
            mFreshView = getChildAt(0);
            mFootView = new ExtraView(getContext());
            mHeadView = new ExtraView(getContext());
            addView(mHeadView);
            addView(mFootView);
        } else if (childCount == 3) {
            //确保有3个view
            for (int i = 0; i < 3; i++) {
                View childView = getChildAt(i);
                if (childView instanceof ExtraView) {
                    if (mHeadView == null) {
                        mHeadView = (ExtraView) childView;
                    } else {
                        mFootView = (ExtraView) childView;
                    }
                }
                if (childView instanceof PullRefresh) {
                    mFreshView = childView;
                }
            }
        }
        mTouchHandler.setView(this, mFreshView, mHeadView, mFootView);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (!isViewCreate) {
            isViewCreate = true;
            findView();
            initView();
            if (onViewCreateListener != null) onViewCreateListener.onViewCreate(this);
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


    public View getContentView() {
        return mFreshView;
    }


    public ExtraView getHeadView() {
        return mHeadView;
    }

    public ExtraView getFootView() {
        return mFootView;
    }

    public void setOnViewCreateListener(OnViewCreateListener onViewCreateListener) {
        this.onViewCreateListener = onViewCreateListener;
    }

    public static interface OnViewCreateListener {
        void onViewCreate(FreshLayout freshLayout);
    }
}
