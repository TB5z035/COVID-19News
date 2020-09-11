package com.java.zhangjiayou.ui.adapter;

public interface MyViewAdapter {
    int TYPE_ITEM = 1;
    int TYPE_FOOTER = 2;

    int LOADING = 1;
    int LOADING_COMPLETE = 2;
    int LOADING_END = 3;

    void getData(boolean mode, String type, int size);

    void setLoadState(int loadState);
}
