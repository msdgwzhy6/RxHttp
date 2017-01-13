package com.wuxiao.rxhttp.cache.data;

/**
 * 数据
 */
public class Result<T> {

    public ResultFrom from;
    public String key;
    public T data;

    public Result() {
    }
    public Result(ResultFrom from, String key, T data) {
        this.from = from;
        this.key = key;
        this.data = data;
    }

    public ResultFrom getFrom() {
        return from;
    }

    public void setFrom(ResultFrom from) {
        this.from = from;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }



}
