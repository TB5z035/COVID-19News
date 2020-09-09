package com.java.zhangjiayou.network;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.java.zhangjiayou.util.Passage;
import com.java.zhangjiayou.util.PassageWithNoContent;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class _tempMapForTitle {
    public List<PassageWithNoContent> datas;
    @JsonCreator
    public _tempMapForTitle(
            @JsonProperty("datas") List<PassageWithNoContent> datas
    ) {
        this.datas = datas;
    }
}
