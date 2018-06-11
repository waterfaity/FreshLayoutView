package com.waterfairy.widget.refresh.baseView;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.View;

import com.waterfairy.widget.refresh.inter.BaseNoDataView;

/**
 * @author water_fairy
 * @email 995637517@qq.com
 * @date 2018/6/11 10:29
 * @info:
 */
public class NoDataView extends AppCompatTextView implements BaseNoDataView {

    public NoDataView(Context context) {
        super(context);
    }

    public NoDataView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 展示空view
     */
    @Override
    public void onNoData() {
        setVisibility(VISIBLE);
        setText("未查询到数据");

    }

    /**
     * 刷新失败
     */
    @Override
    public void onRefreshFailed() {
        setVisibility(VISIBLE);
        setText("刷新失败");
    }

    /**
     * 关闭view
     */
    @Override
    public void onClose() {
        setVisibility(GONE);
    }


}
