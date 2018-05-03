package com.waterfairy.widget.refresh.inter;

/**
 * @author water_fairy
 * @email 995637517@qq.com
 * @date 2018/5/2
 * @Description:
 */
public interface OnExtraViewStateChangeListener {
    /**
     * @param pos
     * @param radio 0-1 继续下/上拉刷新  >1  松开刷新
     */
    void onViewMove(int pos, float radio);

    /**
     * 刷新中
     *
     * @param pos
     */
    void onLoading(int pos);

    /**
     * 成功
     *
     * @param pos
     */
    void onLoadingSuccess(int pos);

    /**
     * 失败
     *
     * @param pos
     */
    void onLoadingFailed(int pos);

}
