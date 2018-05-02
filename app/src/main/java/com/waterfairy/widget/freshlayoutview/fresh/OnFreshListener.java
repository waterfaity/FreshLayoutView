package com.waterfairy.widget.freshlayoutview.fresh;

import com.waterfairy.widget.freshlayoutview.PullToRefreshLayout;

/**
 * @author water_fairy
 * @email 995637517@qq.com
 * @date 2018/5/2
 * @Description:
 */
public interface OnFreshListener {

    void onRefresh(FreshLayout freshLayout);

    void onLoadMore(FreshLayout freshLayout);

    void onReLoadMore(FreshLayout freshLayout);

}
