package com.java.zhangjiayou;

import com.java.zhangjiayou.Portal.DateOutOfRangeException;
import com.java.zhangjiayou.Portal.DateParseException;
import com.java.zhangjiayou.Portal.NumberPortal;

import org.junit.Test;

import java.io.IOException;
import java.text.ParseException;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws DateParseException, DateOutOfRangeException {
        System.out.println("Hello!");
//
//        String data = getRawData();
//        ObjectMapper objectMapper = new ObjectMapper();
//        String json = "{ \"color\" : \"Black\", \"type\" : \"BMW\" }";
//        Map<String, Object> map =
//                objectMapper.readValue(data, new TypeReference<Map<String, Object>>() {
//                });
////        Map<String, Object> map1 = objectMapper.readValue(map.get("China|Hong Kong").toString(), new TypeReference<Map<String, Object>>() {
////        });
////        System.out.println(map1.get("beginning"));
//
//
//        Map<String, Map<String, Object>> output = objectMapper.readValue(data, new TypeReference<Map<String, Map<String, Object>>>() {
//        });
//        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        Date begin = dateFormat.parse(output.get("China|Hong Kong").get("begin").toString());
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(begin);
//        ArrayList<ArrayList<Integer>> a = (ArrayList<ArrayList<Integer>>) output.get("China|Hong Kong").get("data");
//        for (int i = 0; i < a.size(); i++) {
//            System.out.println(dateFormat.format(calendar.getTime()) + ":\t" + a.get(i).get(0));
//            calendar.add(Calendar.DAY_OF_MONTH, 1);
//        }
//
////        JSONObject object = JSONObject.parseObject(data);
////        Number number = JSON.parseObject(data, Number.class);
////        // System.out.println(JSON.parseObject(JSON.parseObject(data).getString("China|Hong
////        // Kong")).getJSONArray("data").get(0));
////        JSONArray arr = JSON.parseObject(JSON.parseObject(data).getString("China|Hong Kong")).getJSONArray("data");
////        System.out.println(arr);
////        ArrayList<String> integers = new ArrayList<>(arr.toJavaList(String.class));
////        System.out.println(integers);

        System.out.println(NumberPortal.getData(
                "China", "2020-01-23", NumberPortal.NumberType.CONFIRMED));
        assertEquals(4, 2 + 2);
    }
}