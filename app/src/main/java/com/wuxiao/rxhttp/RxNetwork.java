package com.wuxiao.rxhttp;


public class RxNetwork {

    private static RxNetwork rxHttp;

    private static String mBaseUrl;


    public static RxNetwork getInstance() {
        if (rxHttp == null) {
            rxHttp = new RxNetwork();
        }
        return rxHttp;
    }


    public <K> K createApi(final Class<K> cls, boolean isCommon) {

        return RetrofitHelper.getInstance(isCommon).create(cls);

    }


}
