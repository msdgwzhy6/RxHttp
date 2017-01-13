package com.wuxiao.rxhttp.cache.diskconverter;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by wuxiao on 2017/1/10.
 * 通用转换器
 *
 */
public interface IDiskConverter {

    /**
     * 读取
     *
     * @param source
     * @return
     */
   Object load(InputStream source);

    /**
     * 写入
     *
     * @param sink
     * @param data
     * @return
     */
    boolean writer(OutputStream sink, Object data);

}
