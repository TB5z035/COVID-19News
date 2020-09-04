package com.java.zhangjiayou.network;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.java.zhangjiayou.util.Passage;

import java.util.List;

/**
 * This is a helper class intended to extract "data" field
 * from JSON text given after passage list query.
 * @author 田倍闻
 * @version 1.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
class _tempMapForData {
    public List<Passage> data;

    @JsonCreator
    public _tempMapForData(
            @JsonProperty("data") List<Passage> data
    ) {
        this.data = data;
    }
}