package com.java.zhangjiayou.ui.home.utils;

import android.app.Activity;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class AssetsIO {
    public static String getFromAssets(Activity activity, String fileName){
        String Result="";
        try {
            InputStreamReader inputReader = new InputStreamReader(activity.getAssets().open(fileName));
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line="";
            while((line = bufReader.readLine()) != null)
                Result += line+"\n";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result;
    }
}
