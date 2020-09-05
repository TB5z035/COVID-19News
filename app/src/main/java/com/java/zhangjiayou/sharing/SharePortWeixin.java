package com.java.zhangjiayou.sharing;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.io.ByteArrayOutputStream;

public class SharePortWeixin implements ShareAPI<SharePortWeixin> {
    private final static String APP_ID = "wxaee59de562e6ea8c";
    private static IWXAPI mWXAPI;
    private String type;
    private WXMediaMessage message;
    private Integer dest = SendMessageToWX.Req.WXSceneTimeline;

    public static void initSDK(Context context) {
        mWXAPI = WXAPIFactory.createWXAPI(context, APP_ID, true);
        mWXAPI.registerApp(APP_ID);
    }

    public SharePortWeixin() {
        message = new WXMediaMessage();
    }

    public SharePortWeixin(String dest) {
        if (dest.equals("Dialog")) {
            this.dest = SendMessageToWX.Req.WXSceneSession;
        } else if (dest.equals("Moments")) {
            this.dest = SendMessageToWX.Req.WXSceneTimeline;
        }
    }

    public static IWXAPI getSDK() {
        return mWXAPI;
    }

    public SharePortWeixin setText(String text) {
        WXTextObject textObject = new WXTextObject();
        textObject.text = text;
        message.mediaObject = textObject;
        message.description = text;
        type = "text";
        return this;
    }

    @Override
    public SharePortWeixin setImage(Context context, int resId) {

        Bitmap image = BitmapFactory.decodeResource(context.getResources(), resId);
        WXImageObject imageObject = new WXImageObject(image);
        message.mediaObject = imageObject;
        Bitmap thumbImage = Bitmap.createScaledBitmap(image, 150, 150, true);
        image.recycle();

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, output);
        image.recycle();
        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        message.thumbData = result;
        type = "image";
        return this;
    }

    @Override
    public SharePortWeixin setWebPage(Context context, int resLogoId, String title, String description, String actionUrl) {
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = actionUrl;

        message.mediaObject = webpage;
        message.title = title;
        message.description = description;

        Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), resLogoId);
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, 150, 150, true);
        bmp.recycle();

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, output);
        bmp.recycle();
        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        message.thumbData = result;
        type = "webpage";
        return this;
    }


    public void share() {
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
        req.message = message;
        req.scene = dest;
        mWXAPI.sendReq(req);
    }
}
