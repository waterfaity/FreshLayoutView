# FreshLayoutView 

## 简介:
 上拉越界加载,下拉越界刷新
##  使用说明:
 xml布局(默认)
```xml
 <com.waterfairy.widget.refresh.baseView.FreshLayout
        android:id="@+id/fresh_layout"
        android:layout_width="match_parent"
        android:layout_height="match_parent">

        <com.waterfairy.widget.refresh.view.PullRefreshRecyclerView
            android:id="@+id/recycler_view"
            android:layout_width="match_parent"
            android:layout_height="match_parent"/>

</com.waterfairy.widget.refresh.baseView.FreshLayout>
```

```java
freshLayout = findViewById(R.id.fresh_layout);
freshLayout.setOnFreshListener(this);
freshLayout.setOnViewCreateListener(new FreshLayout.OnViewCreateListener() {
    @Override
    public void onViewCreate(FreshLayout freshLayout) {
        //设置样式
        ExtraView footView = (ExtraView) freshLayout.getFootView();
        ExtraView headView = (ExtraView) freshLayout.getHeadView();
        headView.setViewTheme(ExtraView.BLACK).freshView();
        footView.setViewTheme(ExtraView.WHITE).freshView();
    }
});
```
### 自动义- 头view/底view:
继承 BaseExtraView
```java
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
}
```
```java
public interface OnExtraViewStateChangeListener {
    /**
     * @param pos
     * @param radio  默认:0-2 继续下/上拉刷新  >2  松开刷新
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
```
xml布局(自定义head/foot view)
```xml
 <com.waterfairy.widget.refresh.baseView.FreshLayout
        android:id="@+id/fresh_layout"
        android:layout_width="match_parent"
        android:layout_height="match_parent">

        <com.waterfairy.test.MyHeadView
            android:layout_width="match_parent"
            android:layout_height="40dp"/>

        <com.waterfairy.test.MyHeadView
            android:layout_width="match_parent"
            android:layout_height="40dp"/>

        <com.waterfairy.widget.refresh.view.PullRefreshRecyclerView
            android:id="@+id/recycler_view"
            android:layout_width="match_parent"
            android:layout_height="match_parent"/>

    </com.waterfairy.widget.refresh.baseView.FreshLayout>
```
### 关于默认HeadView/FootView样式:
ExtraView 中 <br><br>
public static final int BLACK = 1;//黑色<br>
public static final int WHITE = 0;//白色<br>

支持修改背景颜色颜色 ,刷新icon<br>
```java
 public ExtraView setViewTheme(int textColor, int bgColor, int imgLoading, int imgSuccess, int imgFailed) {
        this.textColor = textColor;
        this.bgColor = bgColor;
        this.imgLoading = imgLoading;
        this.imgSuccess = imgSuccess;
        this.imgFailed = imgFailed;
        mLoadingRes = imgLoading;
        return this;
    }
```
### 参考
[https://github.com/jingchenUSTC/PullToRefreshAndLoad]





