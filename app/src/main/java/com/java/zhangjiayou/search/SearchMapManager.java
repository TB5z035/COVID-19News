package com.java.zhangjiayou.search;

import android.app.Application;

import com.java.zhangjiayou.util.PassageWithNoContent;

import java.util.HashMap;
import java.util.HashSet;

public class SearchMapManager extends Application {
    private static HashMap<String, HashSet<PassageWithNoContent>> searchMap = new HashMap<>();

    synchronized public static HashMap<String, HashSet<PassageWithNoContent>> getMap() {
        return searchMap;
    }
}
