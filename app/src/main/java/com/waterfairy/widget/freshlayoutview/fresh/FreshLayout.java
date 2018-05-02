package com.waterfairy.widget.freshlayoutview.fresh;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * @author water_fairy
 * @email 995637517@qq.com
 * @date 2018/5/2
 * @Description:
 */
public class FreshLayout extends RelativeLayout {
    //资源id
    //view
    private ExtraView mFootView, mHeadView;
    private View mFreshView;
    //touchHandler
    private TouchHandler mTouchHandler;

    public FreshLayout(Context context) {
        super(context);
    }

    public FreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    private void initData() {
        if (mFreshView != null) {
            mTouchHandler = new TouchHandler(this, mFreshView, mHeadView, mFootView);
        }
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
                        mHeadView.setPosTag(RefreshViewTool.POS_HEADER);
                    } else {
                        mFootView = (ExtraView) childView;
                        mFootView.setPosTag(RefreshViewTool.POS_FOOTER);
                    }
                }
                if (childView instanceof PullRefresh) {
                    mFreshView = childView;
                }
            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (mTouchHandler == null) {
            findView();
            initView();
            initData();
        }
        if (mTouchHandler != null)
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
        if (mTouchHandler != null) mTouchHandler.dispatchTouchEvent(ev);
        super.dispatchTouchEvent(ev);
        return true;
    }

    public void setOnFreshListener(OnFreshListener onFreshListener) {
        if (mTouchHandler != null) mTouchHandler.setOnFreshListener(onFreshListener);
    }



}
