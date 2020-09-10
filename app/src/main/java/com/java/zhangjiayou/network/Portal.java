package com.java.zhangjiayou.network;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.Normalizer;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

abstract class Portal {
    protected Date updateTime;
    protected String rawData;

    protected Integer getExpirationDuration() {
        return 3600000;
    }

    String getRawData(String url, Map<String, String> params) throws NoResponseError {
        if (updateTime == null || Calendar.getInstance().getTime().getTime() - updateTime.getTime() > getExpirationDuration()) {
            StringBuilder urlBuilder = new StringBuilder(url);
            if (params != null) {
                boolean started = false;
                for (Map.Entry<String, String> entry :
                        params.entrySet()) {
                    if (!started) {
                        urlBuilder.append("?").append(entry.getKey()).append("=").append(entry.getValue());
                        started = true;
                    } else {
                        urlBuilder.append("&").append(entry.getKey()).append("=").append(entry.getValue());
                    }
                }
            }
            
            StringBuilder output = new StringBuilder();
            try {
                URL host = new URL(urlBuilder.toString());
                BufferedReader in = new BufferedReader(new InputStreamReader(host.openStream()));
                String input;
                while ((input = in.readLine()) != null) {
                    output.append(input);
                }
                in.close();
            } catch (Exception e) {
                System.out.println(e.getStackTrace());
            }
            rawData = output.toString();
            updateTime = Calendar.getInstance().getTime();
        }
        if (rawData == null) throw new NoResponseError();
        return rawData;
    }
}
