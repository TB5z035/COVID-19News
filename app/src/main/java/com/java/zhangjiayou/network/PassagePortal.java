package com.java.zhangjiayou.network;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.List;

/**
 * This class provides multiple methods to acquire news from Web.
 *
 * @author 田倍闻
 * @version 1.0
 */
public class PassagePortal extends Portal {

    private static final String baseURL = "https://covid-dashboard.aminer.cn/api/events/list";

    public List<Passage> getNewsFromType(String type) {
        return getNewsFromType(type, null, null);
    }

    public List<Passage> getNewsFromType(String type, Integer index, Integer size) {
        HashMap<String, String> params = new HashMap<>();
        if (type != null) params.put("type", type);
        if (index != null) params.put("page", index.toString());
        if (size != null) params.put("size", size.toString());
        String response = getRawData(baseURL, params);

        List<Passage> data = null;

        try {
            data = new ObjectMapper().readValue(response, new TypeReference<_tempMapForData>() {
            }).data;
            System.out.println(data.get(0).getClass());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return data;
    }

    public Passage getNewsFromID(String id){
        throw new UnsupportedOperationException();
    }
}