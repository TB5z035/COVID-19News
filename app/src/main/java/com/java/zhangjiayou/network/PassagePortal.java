package com.java.zhangjiayou.network;

import android.util.Pair;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.zhangjiayou.util.Passage;
import com.java.zhangjiayou.util.PassageWithNoContent;

import org.ansj.splitWord.analysis.BaseAnalysis;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

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
        ArrayList<Object> rawList;

        try {
            data = new ObjectMapper().readValue(response, new TypeReference<_tempMapForData>() {
            }).data;
            rawList = (ArrayList<Object>) new ObjectMapper().readValue(response, new TypeReference<Map<String, Object>>() {
            }).get("data");

            for (int i = 0; i < data.size(); i++) {
                OutputStream outputStream = new ByteArrayOutputStream();
                new ObjectMapper().writeValue(outputStream,rawList.get(i));
                data.get(i).rawJSON = outputStream.toString();
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;
    }

    public Passage getNewsFromId(String id) {
        //TODO:implement PassagePortal.getNewsFromId
        throw new UnsupportedOperationException();
    }

    public String getNewsJSONFromId(String id) throws NoResponseError {
        return getRawData("https://covid-dashboard.aminer.cn/api/event/" + id, null);
    }

    public List<Map<String, String>> getAllPassageIdTitle(BiFunction<String, String, Boolean> function) {
        String response = "";
        try {
            response = getRawData("https://covid-dashboard.aminer.cn/api/dist/events.json", null);
            List<PassageWithNoContent> list = new ObjectMapper().readValue(response, new TypeReference<_tempMapForTitle>() {
            }).datas;
            for (PassageWithNoContent p :
                    list) {
                BaseAnalysis.parse(p.getTitle()).forEach((v) -> {
//                    System.out.println(v.getName());
                    function.apply(v.getName(), p.getId());
                });
            }
        } catch (NoResponseError noResponseError) {
            noResponseError.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}