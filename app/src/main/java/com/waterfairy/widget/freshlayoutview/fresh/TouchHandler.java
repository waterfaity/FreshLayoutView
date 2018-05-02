package com.waterfairy.widget.freshlayoutview.fresh;

import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;

/**
 * @author water_fairy
 * @email 995637517@qq.com
 * @date 2018/5/2
 * @Description:
 */
public class TouchHandler {
    //view
    private FreshLayout mFreshLayout;
    private View mFreshView;
    private ExtraView mHeadView, mFootView;
    //data
    // 过滤多点触碰
    private int mEvents;//正常==0

    //触摸点
    private float mLastY;

    // 这两个变量用来控制pull的方向，如果不加控制，当情况满足可上拉又可下拉时没法下拉
    private boolean canPullDown = true;
    private boolean canPullUp = true;

    //状态体
    private int mState;
    // 初始状态
    public static final int STATE_INIT = 0;
    // 释放刷新
//    public static final int STATE_RELEASE_TO_REFRESH = 1;
    // 正在刷新
    public static final int STATE_REFRESHING = 2;
    // 释放加载
//    public static final int STATE_RELEASE_TO_LOAD = 3;
    // 正在加载
    public static final int STATE_LOADING = 4;
    // 操作完毕
    public static final int STATE_DONE = 5;

    //下/上拉距离
    private float mPullDownY, mPullUpY;
    private float radio = 1;//距离指数

    // 释放刷新的距离
    private float mRefreshDist = 200;
    // 释放加载的距离
    private float mLoadMoreDist = 200;
    private OnFreshListener onFreshListener;

    // 回滚速度
    public float moveSpeed = 8;


    public TouchHandler(FreshLayout freshLayout, View freshView, ExtraView headView, ExtraView footView) {
        this.mFreshLayout = freshLayout;
        this.mFreshView = freshView;
        this.mHeadView = headView;
        this.mFootView = footView;
        mLoadMoreDist = mFootView.getViewHeight();
        mRefreshDist = mHeadView.getViewHeight();

    }

    public void dispatchTouchEvent(MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mEvents = 0;
                mLastY = motionEvent.getY();
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
            case MotionEvent.ACTION_POINTER_UP:
                mEvents = -1;
                break;
            case MotionEvent.ACTION_MOVE:
                handleMove(motionEvent);
                break;
            case MotionEvent.ACTION_UP:
                handleUp(motionEvent);
                break;

        }
    }

    private void handleUp(MotionEvent motionEvent) {
        if (mPullDownY > mRefreshDist || -mPullDownY > mPullUpY) {
            // 正在刷新时往下拉（正在加载时往上拉），释放后下拉头（上拉头）不隐藏
            if (mPullDownY > mRefreshDist) {
                setSate(STATE_REFRESHING);
            } else if (-mPullDownY > mPullUpY) {
                setSate(STATE_LOADING);
            }
        }
        updateHandler.removeMessages(0);
        updateHandler.sendEmptyMessageDelayed(0, 10);
    }


    private void setSate(int state) {
        switch (state) {
            case STATE_REFRESHING:
                mState = state;
                if (onFreshListener != null) onFreshListener.onRefresh(mFreshLayout);
                mHeadView.onLoading();
                break;
            case STATE_LOADING:
                mState = state;
                if (onFreshListener != null) onFreshListener.onLoadMore(mFreshLayout);
                mFootView.onLoading();
                break;
        }
    }


    /**
     * 移动处理
     *
     * @param motionEvent
     */
    private void handleMove(MotionEvent motionEvent) {
        float moveY = motionEvent.getY();
        PullRefresh pullRefresh = (PullRefresh) mFreshView;
        if (mEvents == 0) {
            if (pullRefresh.canPullDown() && mState != STATE_LOADING && canPullDown) {
                //下拉
                mPullDownY += (moveY - mLastY) / radio;
                if (mPullDownY < 0) {
                    mPullDownY = 0;
                    canPullUp = true;
                } else {
                    canPullUp = false;
                }
                if (mPullDownY > mFreshLayout.getMeasuredHeight()) {
                    mPullDownY = mFreshLayout.getMeasuredHeight();
                }
                if (mState == STATE_REFRESHING) {
                    // 正在加载的时候触摸移动

                } else {
                    mHeadView.onViewMove(Math.abs(mPullDownY) / (float) mHeadView.getViewHeight());
                }
            } else if (pullRefresh.canPullUp() && mState != STATE_REFRESHING && canPullUp) {
                mPullUpY += (moveY - mLastY) / radio;
                if (mPullUpY > 0) {
                    mPullUpY = 0;
                    canPullDown = true;
                } else {
                    canPullDown = false;
                }
                if (mPullUpY < -mFreshLayout.getMeasuredHeight())
                    mPullUpY = -mFreshLayout.getMeasuredHeight();
                if (mState == STATE_LOADING) {
                    // 正在加载的时候触摸移动
                } else {
                    mFootView.onViewMove(Math.abs(mPullUpY) / (float) mFootView.getViewHeight());
                }
            } else {
                release();
            }

        } else mEvents = 0;
        //计算位置改变指数
        mLastY = moveY;
        radio = (float) (2 + 2 * Math.tan(Math.PI / 2 / mFreshLayout.getMeasuredHeight()
                * (mPullDownY + Math.abs(mPullUpY))));
        //刷新布局
        mFreshLayout.requestLayout();
        // 因为刷新和加载操作不能同时进行，所以mPullDownY和mPullUpY不会同时不为0，因此这里用(mPullDownY +
        // Math.abs(mPullUpY))就可以不对当前状态作区分了
        if ((mPullDownY + Math.abs(mPullUpY)) > 8) {
            // 防止下拉过程中误触发长按事件和点击事件
            motionEvent.setAction(MotionEvent.ACTION_CANCEL);
        }
    }

    private void changeState(int state) {
        switch (state) {
            case STATE_INIT:
                //初始状态
                break;
        }
    }

    private void release() {
        canPullDown = false;
        canPullUp = false;
    }

    public void setOnFreshListener(OnFreshListener onFreshListener) {
        this.onFreshListener = onFreshListener;
    }

    /**
     * 刷新布局
     *
     * @param left
     * @param right
     */
    public void onLayout(int left, int right) {
        int layoutTop = (int) (mPullDownY + mPullUpY);
        int layoutBottom = (int) (mPullDownY + mPullUpY + mFreshLayout.getMeasuredHeight());
        mFreshView.layout(left, layoutTop, right, layoutBottom);
        mFootView.layout(left, layoutBottom, right, layoutBottom + mFootView.getViewHeight());
        mHeadView.layout(left, layoutTop - mHeadView.getViewHeight(), right, layoutTop);
    }


    /**
     * 执行自动回滚的handler
     */
    Handler updateHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            // 回弹速度随下拉距离moveDeltaY增大而增大
            moveSpeed = (float) (16 + 5 * Math.tan(Math.PI / 2 / mFreshLayout.getMeasuredHeight() * (mPullDownY + Math.abs(mPullUpY))));
            if (mState == STATE_REFRESHING && mPullDownY <= mRefreshDist) {
                //刷新中 ,恢复到一个headView 高度
                mPullDownY = mRefreshDist;
                mFreshLayout.requestLayout();
            } else if (mState == STATE_LOADING && -mPullUpY <= mLoadMoreDist) {
                //加载中 ,恢复到一个footView 高度
                mPullUpY = -mLoadMoreDist;
                mFreshLayout.requestLayout();
            } else {
                if (mPullDownY > 0) {
                    mPullDownY -= moveSpeed;
                } else {
                    mPullDownY = 0;
                }
                if (mPullUpY < 0) {
                    mPullUpY += moveSpeed;
                } else {
                    mPullUpY = 0;
                }
                if (mPullUpY != 0 || mPullDownY != 0) {
                    sendEmptyMessageDelayed(0, 10);
                }
                mFreshLayout.requestLayout();
            }
        }
    };
}
