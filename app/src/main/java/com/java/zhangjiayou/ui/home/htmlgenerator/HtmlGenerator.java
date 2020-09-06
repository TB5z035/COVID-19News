package com.java.zhangjiayou.ui.home.htmlgenerator;

import android.text.Html;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HtmlGenerator {
    final static String template = "<!DOCTYPE html>\n" +
            "<html>\n" +
            "<head>\n" +
            "<meta charset=\\utf-8\\>\n" +
            "<title>{{title}}</title>\n" +
            "</head>\n" +
            "<body>\n" +
            "<p>来源:{{source}}</p>\n" +
            "<p>时间:{{date}}</p>\n" +
            "{{text}}\n" +
            "</body>\n" +
            "</html>";

    public static String getSymbol(final String symbol){
        return "\\{\\s*\\{\\s*" + symbol + "\\s*\\}\\s*\\}";
    }

    public static String generatePlainText(final String title, final String text,
                                           final String abstra, final String source,
                                           final String date, final String template){
        String targetTemplate = template == null ? HtmlGenerator.template:template;
        String safeTitle = Html.escapeHtml(title);
        String safeText = Html.escapeHtml(text);
        String safeAbstract = Html.escapeHtml(abstra);
        String safeSource = Html.escapeHtml(source);
        String safeDate = Html.escapeHtml(date);
        return (targetTemplate.replaceAll(getSymbol("title"), safeTitle)
                .replaceAll(getSymbol("text"), safeText)
                .replaceAll(getSymbol("abstract"), safeAbstract)
                .replaceAll(getSymbol("source"), safeSource)
                .replaceAll(getSymbol("date"), safeDate)
        );
    }

    public static String generateWorldMap(final String[] countries, final int[] numCOVID, final String template){
        String data = "";
        for (int i = 0; i< countries.length; i++){
            data += "[\""+countries[i]+"\", " +numCOVID[i]+ "],";
        }
        data = "[" + data + "]";
        List<Integer> numCOVIDArray = new ArrayList<>();
        for (int i = 0; i < numCOVID.length; i++) {
            numCOVIDArray.add(numCOVID[i]);
        }
        return (template.replaceAll(getSymbol("country_COVID_num"), data)
                .replaceAll(getSymbol("vmin"), "0")
                .replaceAll(getSymbol("vmax"), Collections.max(numCOVIDArray).toString()));
    }

    public static String generateWithJson(final String json, final String template){
        return (template.replaceAll(getSymbol("jsonString"), json));
    }

    public static String toString(final int [] x){
        String data = "";
        for (int i = 0; i< x.length; i++){
            data += x[i] +",";
        }
        data = "[" + data + "]";
        return data;
    }
}
