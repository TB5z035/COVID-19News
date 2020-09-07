package com.java.zhangjiayou.network;

import android.util.Pair;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.zhangjiayou.util.Passage;

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

    public List<Passage> getNewsFromType(String type) throws NoResponseError {
        return getNewsFromType(type, null, null);
    }

    //TODO:implement PassagePortal.getNewsTitleAndId
    public List<Pair<String, String>> getNewsTitleAndId(String type, Integer index, Integer size) {
        throw new UnsupportedOperationException();
    }

    public List<Passage> getNewsFromType(String type, Integer index, Integer size) throws NoResponseError {
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

//        for (Passage item :
//                data) {
//            item.rawJSON = getNewsJSONFromId(item.getId());
//        }
        
        return data;
    }

    public Passage getNewsFromId(String id) {
        //TODO:implement PassagePortal.getNewsFromId
        throw new UnsupportedOperationException();
    }

    public String getNewsJSONFromId(String id) throws NoResponseError {
        return getRawData("https://covid-dashboard.aminer.cn/api/event/" + id, null);
    }
}