package com.waterfairy.widget.refresh.baseView;

import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;

import com.waterfairy.widget.refresh.inter.BaseExtraView;
import com.waterfairy.widget.refresh.inter.BaseNoDataView;
import com.waterfairy.widget.refresh.inter.OnFreshListener;
import com.waterfairy.widget.refresh.inter.PullRefresh;

/**
 * @author water_fairy
 * @email 995637517@qq.com
 * @date 2018/5/2
 * @Description:
 */
public class TouchHandler implements View.OnClickListener {
    private static final String TAG = "touchHandler";
    //view
    private FreshLayout mFreshLayout;
    private View mFreshView;
    private BaseExtraView mHeadView, mFootView;
    private BaseNoDataView mNoDataView;
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
    // 正在刷新
    public static final int STATE_REFRESHING = 1;
    // 正在加载
    public static final int STATE_LOADING = 2;
    // 刷新失败
    public static final int STATE_REFRESH_FAILED = 3;
    // 加载失败
    public static final int STATE_LOAD_FAILED = 4;
    // 没有数据
    public static final int STATE_NO_DATA = 5;

    //下/上拉距离
    private int mPullDownY, mPullUpY;
    private float radio = 1;//距离指数

    private OnFreshListener onFreshListener;
    private int delayTime = 20;

    // 回滚速度
    public int moveSpeed = 8;
    //回滚开关
    public boolean canAutoScroll = true;
    private boolean canLoadMore;

    public TouchHandler() {
    }

    public void setView(FreshLayout freshLayout, View freshView, BaseExtraView headView, BaseExtraView footView, BaseNoDataView noDataView) {
        this.mFreshLayout = freshLayout;
        this.mFreshView = freshView;
        this.mHeadView = headView;
        this.mFootView = footView;
        this.mNoDataView = noDataView;
    }

    public void dispatchTouchEvent(MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                canAutoScroll = false;
                mEvents = 0;
                mLastY = motionEvent.getY();
                release();
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
            case MotionEvent.ACTION_POINTER_UP:
                mEvents = -1;
                break;
            case MotionEvent.ACTION_MOVE:
                handleMove(motionEvent);
                break;
            case MotionEvent.ACTION_UP:
                handleUp();
                break;

        }
    }

    private void handleUp() {
        // 正在刷新时往下拉（正在加载时往上拉），释放后下拉头（上拉头）不隐藏
        if (mPullDownY > mHeadView.getFreshHeight()) {
            setSate(STATE_REFRESHING);
        } else if (-mPullUpY > mFootView.getFreshHeight()) {
            setSate(STATE_LOADING);
        }
        canAutoScroll = true;
        updateHandler.sendEmptyMessage(0);
    }

    private boolean setSate(int state) {
        if (mState == state) return false;
        switch (state) {
            case STATE_REFRESHING:
                if (mState == STATE_LOADING) return false;
                mState = state;
                if (onFreshListener != null) onFreshListener.onRefresh(mFreshLayout);
                mHeadView.onLoading(BaseExtraView.POS_HEADER);
                if (mNoDataView != null) mNoDataView.onClose();
                break;
            case STATE_LOADING:
                if (mState == STATE_REFRESHING) return false;
                mState = state;
                if (onFreshListener != null) onFreshListener.onLoadMore(mFreshLayout);
                mFootView.onLoading(BaseExtraView.POS_FOOTER);
                if (mNoDataView != null) mNoDataView.onClose();
                break;
            case STATE_LOAD_FAILED:
                mFootView.onLoadingFailed(BaseExtraView.POS_FOOTER);
                reset();
                break;
            case STATE_REFRESH_FAILED:
                mHeadView.onLoadingFailed(BaseExtraView.POS_HEADER);
                if (mNoDataView != null) mNoDataView.onRefreshFailed();
                reset();
                break;
            case STATE_NO_DATA:
                if (mNoDataView != null) mNoDataView.onNoData();
                reset();
                break;
            case STATE_INIT:
                if (mState == STATE_LOADING) mFootView.onLoadingSuccess(BaseExtraView.POS_FOOTER);
                if (mState == STATE_REFRESHING)
                    mHeadView.onLoadingSuccess(BaseExtraView.POS_HEADER);
                reset();
                break;
        }
        return true;
    }

    private void forbidScroll() {
        canPullDown = false;
        canPullUp = false;
    }

    private void release() {
        canPullDown = true;
        canPullUp = true;
    }

    private void reset() {
        updateHandler.removeMessages(0);
        mState = STATE_INIT;
        canAutoScroll = true;
        release();
        if (mPullUpY != 0 || mPullDownY != 0) {
            updateHandler.sendEmptyMessageDelayed(0, 500);
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
                    canPullDown = false;
                } else canPullDown = true;
                if (mPullDownY > mFreshLayout.getMeasuredHeight()) {
                    mPullDownY = mFreshLayout.getMeasuredHeight();
                }
                if (mState != STATE_REFRESHING) {
                    // 正在加载的时候触摸移动
                    mHeadView.onViewMove(BaseExtraView.POS_HEADER, Math.abs(mPullDownY) / (float) mHeadView.getViewHeight());
                }
            } else if (pullRefresh.canPullUp() && mState != STATE_REFRESHING && canPullUp && canLoadMore) {
                mPullUpY += (moveY - mLastY) / radio;
                if (mPullUpY > 0) {
                    mPullUpY = 0;
                    canPullDown = true;
                    canPullUp = false;
                } else canPullUp = true;
                if (mPullUpY < -mFreshLayout.getMeasuredHeight())
                    mPullUpY = -mFreshLayout.getMeasuredHeight();
                if (mState != STATE_LOADING) {
                    // 正在加载的时候触摸移动
                    mFootView.onViewMove(BaseExtraView.POS_FOOTER, Math.abs(mPullUpY) / (float) mFootView.getViewHeight());
                }
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
        int layoutTop = (mPullDownY + mPullUpY);
        int layoutBottom = (mPullDownY + mPullUpY + mFreshLayout.getMeasuredHeight());
        ((View) mHeadView).layout(left, layoutTop - mHeadView.getViewHeight(), right, layoutTop);
        ((View) mFootView).layout(left, layoutBottom, right, layoutBottom + mFootView.getViewHeight());/**/
        if (mNoDataView != null && mNoDataView instanceof View)
            ((View) mNoDataView).layout(left, layoutTop, right, mFreshLayout.getMeasuredHeight());
        mFreshView.layout(left, layoutTop, right, layoutBottom);
    }


    /**
     * 执行自动回滚的handler
     */
    Handler updateHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            removeMessages(0);
            // 回弹速度随下拉距离moveDeltaY增大而增大
//            moveSpeed = (int) (8 + 5 * Math.tan(Math.PI / 2 / mFreshLayout.getMeasuredHeight() * (mPullDownY + Math.abs(mPullUpY))));
            moveSpeed = ((Math.abs(mPullDownY) + Math.abs(mPullUpY)) / 6);


            if (moveSpeed < 1) moveSpeed = 1;
            if (mState == STATE_REFRESHING && mPullDownY <= mHeadView.getViewHeight()) {
                //刷新中 ,恢复到一个headView 高度
                mPullDownY = mHeadView.getViewHeight();
                mFreshLayout.requestLayout();
            } else if (mState == STATE_LOADING && -mPullUpY <= mFootView.getViewHeight()) {
                //加载中 ,恢复到一个footView 高度
                mPullUpY = -mFootView.getViewHeight();
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
                if (canAutoScroll && (mPullUpY != 0 || mPullDownY != 0)) {
                    sendEmptyMessageDelayed(0, delayTime);
                }
                mFreshLayout.requestLayout();
            }
        }
    };

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        if (mState == STATE_REFRESH_FAILED) {
            setSate(STATE_REFRESHING);
        } else if (mState == STATE_LOAD_FAILED) {
            setSate(STATE_LOADING);
        }
    }


    /**
     * 外部设置加载失败 前提是加载中/刷新中
     */
    public void setFailed() {
        if (mState == STATE_REFRESHING) setSate(STATE_REFRESH_FAILED);
        else if (mState == STATE_LOADING) setSate(STATE_LOAD_FAILED);
        else setSate(STATE_INIT);
    }

    /**
     * 外部设置加载成功
     */
    public void setSuccess() {
        setSate(STATE_INIT);
    }

    public void setRefreshing() {
        if (mHeadView != null) {
            mPullDownY = mHeadView.getViewHeight();
            setSate(STATE_REFRESHING);
            mFreshLayout.requestLayout();
        }
    }

    public void setLoadingMore() {
        if (mFootView != null) {
            mPullUpY = mFootView.getViewHeight();
            mFreshLayout.requestLayout();
            setSate(STATE_LOADING);
            mFreshLayout.requestLayout();
        }
    }

    public void setNoData() {
        setSate(STATE_NO_DATA);
    }

    public void setCanLoadMore(boolean canLoadMore) {
        this.canLoadMore = canLoadMore;
    }
}
