package com.java.zhangjiayou.ui.explore.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public class COVIDData {
    public static HashMap<String, Integer[]> getLatest(String strjson) throws JSONException {
        HashMap<String, Integer[]> locationMap = new HashMap<>();
        JSONObject obj = new JSONObject(strjson);
        Iterator iterator = obj.keys();
        while(iterator.hasNext()){
            try {
                String key = (String) iterator.next();
                JSONArray vals = obj.getJSONObject(key).getJSONArray("data");
                JSONArray latest = (JSONArray) vals.get(vals.length() - 1);
                Integer confirmCuredDead [] = {latest.getInt(0), latest.getInt(2), latest.getInt(3)};
                locationMap.put(key, confirmCuredDead);
            } catch (java.lang.NullPointerException e){
                System.out.println("Corrupted covid json data.");
                e.printStackTrace();
            }
        }
        return locationMap;
    }

    public static HashMap<String, Integer[][]> getHist(String strjson) throws JSONException {
        HashMap<String, Integer[][]> locationMap = new HashMap<>();
        JSONObject obj = new JSONObject(strjson);
        Iterator iterator = obj.keys();
        while(iterator.hasNext()){
            try {
                String key = (String) iterator.next();
                JSONArray vals = obj.getJSONObject(key).getJSONArray("data");
                Integer confirmCuredDead [][] = new Integer[3][vals.length()];
                for (int i = 0; i < vals.length(); i++){
                    confirmCuredDead[0][i] = ((JSONArray)vals.get(i)).getInt(0);
                    confirmCuredDead[2][i] = ((JSONArray)vals.get(i)).getInt(1);
                    confirmCuredDead[1][i] = ((JSONArray)vals.get(i)).getInt(2);
                }
                locationMap.put(key, confirmCuredDead);
            } catch (java.lang.NullPointerException e){
                System.out.println("Corrupted covid json data.");
                e.printStackTrace();
            }
        }
        return locationMap;
    }

    public static HashMap<String, Integer[]> filtCountryLatest(HashMap<String, Integer[]> locationMap){
        HashMap<String, String> location2Country = new HashMap<>();
        for (String key : locationMap.keySet()){
            String country = key.split("\\|")[0];
            location2Country.put(key, country);
        }
        HashSet<String> countries = new HashSet<>(location2Country.values());
        HashMap<String, Integer[]> filted = new HashMap<>();
        for (String key: countries){
            if (filted.containsKey(key)){
                continue;
            }
            if (location2Country.containsKey(key)){
                filted.put(key, locationMap.get(key));
            } else {
                for (String loc : locationMap.keySet()){
                    if (loc.matches("^"+key+"\\|.+")){
                        if (filted.containsKey(key)){
                            Integer[] val0 = filted.get(key);
                            Integer[] val1 = locationMap.get(loc);
                            Integer[] val2 = {val0[0]+val1[0], val0[1]+val1[1], val0[2]+val1[2]};
                            filted.put(key, val2);
                        }else{
                            Integer[] val1 = locationMap.get(loc);
                            filted.put(key, val1);
                        }
                    }
                }
            }
        }
        // 和地图统一美国的名字
        filted.put("United States", filted.get("United States of America"));
        filted.remove("United States of America");
        return filted;
    }

    public static HashMap<String, Integer[][]> filtCountryHist(HashMap<String, Integer[][]> locationMap) {
        HashMap<String, String> location2Country = new HashMap<>();
        for (String key : locationMap.keySet()) {
            String country = key.split("\\|")[0];
            location2Country.put(key, country);
        }
        HashSet<String> countries = new HashSet<>(location2Country.values());
        HashMap<String, Integer[][]> filted = new HashMap<>();
        for (String key : countries) {
            if (filted.containsKey(key)) {
                continue;
            }
            if (location2Country.containsKey(key)) {
                filted.put(key, locationMap.get(key));
            } else {
                for (String loc : locationMap.keySet()) {
                    if (loc.matches("^" + key + "\\|.+")) {
                        if (filted.containsKey(key)) {
                            Integer[][] val0 = filted.get(key);
                            Integer[][] val1 = locationMap.get(loc);
                            for (int i = 0; i < val0.length; i++){
                                val0[0][i] += val1[0][i];
                                val0[1][i] += val1[1][i];
                                val0[2][i] += val1[2][i];
                            }
                            filted.put(key, val0);
                        } else {
                            Integer[][] val1 = locationMap.get(loc);
                            filted.put(key, val1);
                        }
                    }
                }
            }
        }
        // 和地图统一美国的名字
        filted.put("United States", filted.get("United States of America"));
        filted.remove("United States of America");
        return filted;
    }
}
