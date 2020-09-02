package com.java.zhangjiayou.network;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;


/**
 * This class provide multiple methods to obtain epidemic data from Web.
 * @author 田倍闻
 * @version 1.0
 */
public class NumberPortal extends Portal {

    public enum NumberType {CONFIRMED, SUSPECTED, CURED, DEAD, SEVERE, RISK, inc24}

    protected String getURL() {
        return "https://covid-dashboard.aminer.cn/api/dist/epidemic.json";
    }

    public Integer getData(String region, String date, NumberType type)
            throws DateParseException, DateOutOfRangeException {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return getData(region, dateFormat.parse(date), type);
        } catch (ParseException e) {
            throw new DateParseException() {
            };
        }
    }

    @org.jetbrains.annotations.Nullable
    public Integer getData(String region, Date date, NumberType type)
            throws DateOutOfRangeException {
        //TODO

        ObjectMapper objectMapper = new ObjectMapper();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        Map<String, Map<String, Object>> dataMap;
        Date begin = null;
        String data;

        data = getRawData(getURL(),null);
        try {
            dataMap = objectMapper.readValue(data, new TypeReference<Map<String, Map<String, Object>>>() {
            });
        } catch (JsonProcessingException e) {
            System.out.println("Server Error: Illegal JSON text provided.");
            return null;
        }

        try {
            begin = dateFormat.parse(dataMap.get(region).get("begin").toString());
        } catch (ParseException e) {
            System.out.println("Server Error: Illegal beginning date provided.");
            return null;
        }
        calendar.setTime(begin);
        ArrayList<ArrayList<Integer>> a =
                (ArrayList<ArrayList<Integer>>) dataMap.get(region).get("data");

        if (date.before(begin) || date.compareTo(begin) >= a.size())
            throw new DateOutOfRangeException();
        return a.get(date.compareTo(begin)).get(type.ordinal());
    }
}

