package com.waterfairy.widget.refresh.inter;

/**
 * @author water_fairy
 * @email 995637517@qq.com
 * @date 2018/5/2
 * @Description: 自动义的footView  headView 实现该类
 */
public interface BaseExtraView extends OnExtraViewStateChangeListener {
    int POS_HEADER = 1;
    int POS_FOOTER = 2;


    /**
     * view的高度
     *
     * @return
     */
    int getViewHeight();

    /**
     * 刷新时 位移 高度之后开始刷新
     *
     * @return
     */
    float getFreshHeight();

//    /**
//     * 设置头部还是底部 head/foot
//     *
//     * @param pos
//     */
//    void setPosTag(int pos);
}
