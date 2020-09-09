package com.java.zhangjiayou;

import android.app.Activity;
import android.app.Application;

import com.java.zhangjiayou.util.PassageWithNoContent;

import java.util.HashMap;
import java.util.HashSet;

public class SearchMapManager extends Application {
    private static HashMap<String, HashSet<PassageWithNoContent>> searchMap = new HashMap<>();

    synchronized static HashMap<String, HashSet<PassageWithNoContent>> getMap() {
        return searchMap;
    }
}
