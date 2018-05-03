package com.waterfairy.widget.refresh.inter;

import com.waterfairy.widget.refresh.baseView.FreshLayout;

/**
 * @author water_fairy
 * @email 995637517@qq.com
 * @date 2018/5/2
 * @Description:
 */
public interface OnFreshListener {

    void onRefresh(FreshLayout freshLayout);

    void onLoadMore(FreshLayout freshLayout);


}
