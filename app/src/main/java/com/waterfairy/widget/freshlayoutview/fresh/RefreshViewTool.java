package com.waterfairy.widget.freshlayoutview.fresh;

/**
 * @author water_fairy
 * @email 995637517@qq.com
 * @date 2018/5/2
 * @Description:
 */
public interface RefreshViewTool {
    int POS_HEADER = 1;
    int POS_CONTENT = 2;
    int POS_FOOTER = 3;

    int getPosTag();

    int getViewHeight();
}
