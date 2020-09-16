package com.java.zhangjiayou.sharing;

import android.content.Context;

public interface ShareAPI<SharePort> {
    static void initSDK() {
    }

    static Object getSharePort() {
        return null;
    }

    SharePort setText(String text);

    SharePort setImage(Context context, int resId);

    SharePort setWebPage(Context context, int resLogoId, String title, String description, String actionUrl);

    void share();

}
