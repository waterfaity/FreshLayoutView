package com.waterfairy.widget.freshlayoutview.fresh;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.waterfairy.widget.freshlayoutview.R;

/**
 * @author water_fairy
 * @email 995637517@qq.com
 * @date 2018/5/2
 * @Description: footView  headView
 */
public class ExtraView extends LinearLayout implements RefreshViewTool, OnMoveStateChangeListener {

    //位置信息
    private int posTag;
    //view
    private TextView mTVFresh;
    private ImageView mIVFresh;


    public ExtraView(Context context) {
        this(context, null);
    }

    public ExtraView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        addView();
        initView();
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
//        View view = inflate(getContext(), R.layout.refresh_foot, this);
        addView(inflate);
    }


    @Override
    public int getPosTag() {
        return posTag;
    }

    @Override
    public int getViewHeight() {
        return (int) (40 * getContext().getResources().getDisplayMetrics().density);
    }

    /**
     * @param radio <1 继续下/上拉刷新  >1  松开刷新
     */
    @Override
    public void onViewMove(float radio) {
        if (radio < 1) {
            if (posTag == POS_HEADER) {
                mTVFresh.setText(R.string.fresh_pull_to_refresh);
            } else if (posTag == POS_FOOTER) {
                mTVFresh.setText(R.string.fresh_pull_up_to_load);
            }
        } else if (radio > 1) {
            if (posTag == POS_HEADER) {
                mTVFresh.setText(R.string.fresh_release_to_refresh);
            } else if (posTag == POS_FOOTER) {
                mTVFresh.setText(R.string.fresh_release_to_refresh);
            }
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
    }

    /**
     * 成功
     */
    @Override
    public void onLoadingSuccess() {
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
        if (posTag == POS_HEADER) {
            mTVFresh.setText(R.string.fresh_refresh_failed);
        } else if (posTag == POS_FOOTER) {
            mTVFresh.setText(R.string.fresh_load_failed);
        }
    }
}
