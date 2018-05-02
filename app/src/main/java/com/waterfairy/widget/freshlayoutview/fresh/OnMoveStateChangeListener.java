package com.waterfairy.widget.freshlayoutview.fresh;

/**
 * @author water_fairy
 * @email 995637517@qq.com
 * @date 2018/5/2
 * @Description:
 */
public interface OnMoveStateChangeListener {
    /**
     * @param radio 0-1 继续下/上拉刷新  >1  松开刷新
     */
    void onViewMove(float radio);

    /**
     * 刷新中
     */

    void onLoading();

    /**
     * 成功
     */
    void onLoadingSuccess();

    /**
     * 失败
     */

    void onLoadingFailed();

}
