package com.java.zhangjiayou.ui.explore;


public interface BackPressedHandlerMain {
    public void setBackPressedHandler(BackPressedHandlerSub backPressedHandler);
    public void superOnBackPressed();
    public void onBackPressed();
}

