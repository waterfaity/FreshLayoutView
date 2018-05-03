package com.waterfairy.widget.refresh.baseView;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.waterfairy.widget.refresh.R;
import com.waterfairy.widget.refresh.inter.OnMoveStateChangeListener;
import com.waterfairy.widget.refresh.inter.RefreshViewTool;

/**
 * @author water_fairy
 * @email 995637517@qq.com
 * @date 2018/5/2
 * @Description: footView  headView
 */
public class ExtraView extends LinearLayout implements RefreshViewTool, OnMoveStateChangeListener {
    private int height;

    private RotateAnimation rotateAnimation;
    //位置信息
    private int posTag;
    //view
    private TextView mTVFresh;
    private ImageView mIVFresh;
    private int mLoadingRes;


    public ExtraView(Context context) {
        this(context, null);
    }

    public ExtraView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mLoadingRes = R.mipmap.refresh_loading;
        initAnim();
        addView();
        initView();
    }

    private void initAnim() {
        rotateAnimation = (RotateAnimation) AnimationUtils.loadAnimation(getContext(), R.anim.rotate);
    }

    public void setPosTag(int posTag) {
        this.posTag = posTag;
    }

    private void initView() {
        mTVFresh = findViewById(R.id.refresh_text);
        mIVFresh = findViewById(R.id.refresh_icon);
    }

    private void addView() {
        View inflate = LayoutInflater.from(getContext()).inflate(R.layout.refresh_foot, this, false);
        addView(inflate);
    }


    @Override
    public int getPosTag() {
        return posTag;
    }

    @Override
    public int getViewHeight() {
        if (height == 0)
            height = (int) (40 * getContext().getResources().getDisplayMetrics().density);
        return height;
    }

    /**
     * @param radio <1 继续下/上拉刷新  >1  松开刷新
     */
    @Override
    public void onViewMove(float radio) {
        setIconRes(R.mipmap.refresh_loading);
        if (radio < 2) {
            if (posTag == POS_HEADER) {
                mTVFresh.setText(R.string.fresh_pull_to_refresh);
            } else if (posTag == POS_FOOTER) {
                mTVFresh.setText(R.string.fresh_pull_up_to_load);
            }
        } else if (radio > 2) {
            if (posTag == POS_HEADER) {
                mTVFresh.setText(R.string.fresh_release_to_refresh);
            } else if (posTag == POS_FOOTER) {
                mTVFresh.setText(R.string.fresh_release_to_refresh);
            }
        }
        mIVFresh.setRotation(radio * 360 / 4);
    }

    private void setIconRes(int loadingRes) {
        if (mLoadingRes != loadingRes) {
            this.mLoadingRes = loadingRes;
            mIVFresh.setImageResource(loadingRes);
        }
    }

    /**
     * 刷新中
     */
    @Override
    public void onLoading() {
        if (posTag == POS_HEADER) {
            mTVFresh.setText(R.string.fresh_refreshing);
        } else if (posTag == POS_FOOTER) {
            mTVFresh.setText(R.string.fresh_loading);
        }
        mIVFresh.startAnimation(rotateAnimation);
    }

    /**
     * 成功
     */
    @Override
    public void onLoadingSuccess() {
        mIVFresh.clearAnimation();
        setIconRes(R.mipmap.refresh_succeed);
        mIVFresh.setRotation(0);
        if (posTag == POS_HEADER) {
            mTVFresh.setText(R.string.fresh_refresh_succeed);
        } else if (posTag == POS_FOOTER) {
            mTVFresh.setText(R.string.fresh_load_succeed);
        }
    }

    /**
     * 失败
     */
    @Override
    public void onLoadingFailed() {
        mIVFresh.clearAnimation();
        setIconRes(R.mipmap.refresh_failed);
        mIVFresh.setRotation(0);
        if (posTag == POS_HEADER) {
            mTVFresh.setText(R.string.fresh_refresh_failed);
        } else if (posTag == POS_FOOTER) {
            mTVFresh.setText(R.string.fresh_load_failed);
        }
    }

    @Override
    public float getFreshHeight() {
        return getViewHeight() * 2;
    }
}
