package com.waterfairy.widget.refresh.inter;

/**
 * @author water_fairy
 * @email 995637517@qq.com
 * @date 2018/6/11 10:27
 * @info:
 */
public interface BaseNoDataView {
    /**
     * 展示空view
     */
    void onNoData();

    /**
     * 刷新失败
     */
    void onRefreshFailed();

    /**
     * 关闭view
     */
    void onClose();

}
