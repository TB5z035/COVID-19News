package com.java.zhangjiayou.sharing;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WbAuthListener;
import com.sina.weibo.sdk.common.UiError;
import com.sina.weibo.sdk.openapi.IWBAPI;
import com.sina.weibo.sdk.openapi.WBAPIFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

public class SharePortWeibo implements ShareAPI<SharePortWeibo> {
    private static final String APP_KY = "1287314582";
    private static final String REDIRECT_URL = "https://api.weibo.com/oauth2/default.html";
    private static final String SCOPE = "";
    private static IWBAPI mWBAPI;
    private WeiboMultiMessage message;

    public static final IWBAPI getAPI() {
        return mWBAPI;
    }

    public static void initSDK(Context context) {
        AuthInfo authInfo = new AuthInfo(context, APP_KY, REDIRECT_URL, SCOPE);
        mWBAPI = WBAPIFactory.createWBAPI(context);
        mWBAPI.registerApp(context, authInfo);
    }

    public SharePortWeibo() {
        message = new WeiboMultiMessage();
        TextObject textObject = new TextObject();
        textObject.text = "我正在使用微博进行分享!";
        message.textObject = textObject;
    }

    public SharePortWeibo appendText(String text) {
        message.textObject.text = message.textObject.text + text;
        return this;
    }

    public SharePortWeibo setText(String text) {
        message.textObject.text = text;
        return this;
    }

    public SharePortWeibo setImage(Context context, int resId) {
        ImageObject imageObject = new ImageObject();
        imageObject.setImageData(BitmapFactory.decodeResource(context.getResources(), resId));
        message.imageObject = imageObject;
        return this;
    }

    public SharePortWeibo setWebPage(Context context, int resLogoId, String title, String description, String actionUrl) {
        WebpageObject webObject = new WebpageObject();
        webObject.identify = UUID.randomUUID().toString();
        webObject.title = title;
        webObject.description = description;
        webObject.actionUrl = actionUrl;
        webObject.defaultText = "分享网页";
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resLogoId);
        ByteArrayOutputStream os = null;
        try {
            os = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 85, os);
            webObject.thumbData = os.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        message.mediaObject = webObject;
        return this;
    }

    public void share() {
        if (mWBAPI.isWBAppInstalled()) {
            mWBAPI.shareMessage(message, true);
        } else {
            mWBAPI.authorize(new WbAuthListener() {
                @Override
                public void onComplete(Oauth2AccessToken oauth2AccessToken) {
                    mWBAPI.shareMessage(message, true);
                }
                @Override
                public void onError(UiError uiError) {
                }
                @Override
                public void onCancel() {
                }
            });
        }
    }
}
