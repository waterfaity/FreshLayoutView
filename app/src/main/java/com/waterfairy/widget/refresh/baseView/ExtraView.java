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
    public static final int BLACK = 1;
    public static final int WHITE = 0;
    private int height;

    private RotateAnimation rotateAnimation;
    //位置信息
    private int posTag;
    //view
    private TextView mTVFresh;
    private ImageView mIVFresh;
    private int mLoadingRes;
    //资源id
    private int textColor;
    private int bgColor;
    private int imgLoading;
    private int imgSuccess;
    private int imgFailed;

    public void setViewTheme(int style) {
        if (style == 0) {
            setViewTheme(
                    com.waterfairy.widget.refresh.R.color.refresh_bg_fresh_black,
                    R.color.refresh_white,
                    com.waterfairy.widget.refresh.R.mipmap.refresh_loading_black,
                    com.waterfairy.widget.refresh.R.mipmap.refresh_successed_black,
                    com.waterfairy.widget.refresh.R.mipmap.refresh_failed_black);
        } else {
            setViewTheme(
                    R.color.refresh_white,
                    R.color.refresh_bg_fresh_black,
                    R.mipmap.refresh_loading,
                    R.mipmap.refresh_successed,
                    R.mipmap.refresh_failed);
        }
    }


    public void setViewTheme(int textColor, int bgColor, int imgLoading, int imgSuccess, int imgFailed) {
        this.textColor = textColor;
        this.bgColor = bgColor;
        this.imgLoading = imgLoading;
        this.imgSuccess = imgSuccess;
        this.imgFailed = imgFailed;
        mLoadingRes = imgLoading;

    }

    public ExtraView(Context context) {
        this(context, null);
    }

    public ExtraView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setViewTheme(WHITE);
        initAnim();
        addView();
        initView();
    }

    private void initAnim() {
        rotateAnimation = (RotateAnimation) AnimationUtils.loadAnimation(getContext(), R.anim.refresh_rotate);
    }

    public void setPosTag(int posTag) {
        this.posTag = posTag;
    }

    public void initView() {
        mTVFresh.setTextColor(getResources().getColor(textColor));
        mIVFresh.setImageResource(imgLoading);
        setBackgroundColor(getResources().getColor(bgColor));
    }

    private void addView() {
        View inflate = LayoutInflater.from(getContext()).inflate(R.layout.refresh_layout_foot, this, false);
        addView(inflate);
        mTVFresh = findViewById(R.id.refresh_text);
        mIVFresh = findViewById(R.id.refresh_icon);
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
        setIconRes(imgLoading);
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
        setIconRes(imgSuccess);
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
        setIconRes(imgFailed);
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
