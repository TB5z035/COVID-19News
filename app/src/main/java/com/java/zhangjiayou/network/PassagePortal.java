package com.java.zhangjiayou.network;

import android.util.Log;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.zhangjiayou.util.Passage;
import com.java.zhangjiayou.util.PassageWithNoContent;

import org.ansj.splitWord.analysis.ToAnalysis;

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

    public Passage getNewsFromId(String id) {
        try {
            String response = getRawData("https://covid-dashboard.aminer.cn/api/event/" + id, null);

            Object temp = new ObjectMapper().readValue(response, new TypeReference<Map<String, Object>>() {
            }).get("data");
            OutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            new ObjectMapper().writeValue(byteArrayOutputStream, temp);
            String JSON = byteArrayOutputStream.toString();
            Passage retVal = new ObjectMapper().readValue(JSON, new TypeReference<Passage>() {
            });
            retVal.rawJSON = JSON;
            return retVal;
        } catch (NoResponseError noResponseError) {
            noResponseError.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Passage getNewsFromRawJSON(String rawJSON) {
        Passage parsed = null;
        try {
            Object data = new ObjectMapper().readValue(rawJSON, new TypeReference<Map<String, Object>>() {
            });
            System.out.println(data.getClass());
            System.out.println(data.toString());
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            new ObjectMapper().writeValue(byteArrayOutputStream, data);
            String output = byteArrayOutputStream.toString();
            parsed = new ObjectMapper().readValue(output, new TypeReference<Passage>() {
            });
            parsed.rawJSON = output;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return parsed;
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

    public String getNewsJSONFromId(String id) throws NoResponseError {
        String response = getRawData("https://covid-dashboard.aminer.cn/api/event/" + id, null);
        try {
            Object data = new ObjectMapper().readValue(response, new TypeReference<Map<String, Object>>() {
            }).get("data");
            OutputStream outputStream = new ByteArrayOutputStream();
            new ObjectMapper().writeValue(outputStream, data);
            return outputStream.toString();
        } catch (JsonProcessingException e) {
            Log.e("parse error", "getNewsJSONFromId: ");
            return null;
        } catch (IOException e) {
            Log.e("parse error", "getNewsJSONFromId: ");
            return null;
        }
    }

    public List<Map<String, String>> getAllPassageIdTitle(BiFunction<String, PassageWithNoContent, Boolean> function) {
        String response = "";
        try {
            response = getRawData("https://covid-dashboard.aminer.cn/api/dist/events.json", null);
            List<PassageWithNoContent> list = new ObjectMapper().readValue(response, new TypeReference<_tempMapForTitle>() {
            }).datas;
            for (PassageWithNoContent p :
                    list) {
                ToAnalysis.parse(p.getTitle()).forEach((v) -> {
                    function.apply(v.getName(), p);
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