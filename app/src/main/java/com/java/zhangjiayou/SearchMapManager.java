package com.java.zhangjiayou;

import android.app.Activity;
import android.app.Application;

import java.util.HashMap;
import java.util.HashSet;

public class SearchMapManager extends Application {
    private static HashMap<String, HashSet<String>> searchMap = new HashMap<>();

    synchronized static HashMap<String, HashSet<String>> getMap() {
        return searchMap;
    }
}
