package com.java.zhangjiayou.Portal;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * Transit
 */
public class NumberPortal {

    public enum NumberType {CONFIRMED, SUSPECTED, CURED, DEAD, SEVERE, RISK, inc24}

    private static String getRawData() {
        StringBuilder output = new StringBuilder();
        try {
            URL host = new URL("https://covid-dashboard.aminer.cn/api/dist/epidemic.json");
            BufferedReader in = new BufferedReader(new InputStreamReader(host.openStream()));
            String input;
            while ((input = in.readLine()) != null) {
                output.append(input);
            }
            in.close();
        } catch (Exception e) {
            System.out.println(e.getStackTrace().toString());
        }
        return output.toString();
    }

    public static Integer getData(String region, String date, NumberType type)
            throws DateParseException, DateOutOfRangeException {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return getData(region, dateFormat.parse(date), type);
        } catch (ParseException e) {
            throw new DateParseException() {
            };
        }
    }

    public static Integer getData(String region, Date date, NumberType type)
            throws DateParseException, DateOutOfRangeException {
        //TODO

        ObjectMapper objectMapper = new ObjectMapper();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        Map<String, Map<String, Object>> dataMap;
        Date begin = null;
        String data;

        data = getRawData();
        try {
            dataMap = objectMapper.readValue(data, new TypeReference<Map<String, Map<String, Object>>>() {
            });
        } catch (JsonProcessingException e) {
            System.out.println("Server Error: Illegal JSON text provided.");
            return null;
        }

        try {
            begin = dateFormat.parse(dataMap.get("China|Hong Kong").get("begin").toString());
        } catch (ParseException e) {
            throw new DateParseException();
        }
        calendar.setTime(begin);
        ArrayList<ArrayList<Integer>> a =
                (ArrayList<ArrayList<Integer>>) dataMap.get(region).get("data");

        if (date.before(begin) || date.compareTo(begin) >= a.size())
            throw new DateOutOfRangeException();
        return a.get(date.compareTo(begin)).get(type.ordinal());
    }
}

