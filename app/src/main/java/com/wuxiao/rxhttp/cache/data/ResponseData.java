package com.wuxiao.rxhttp.cache.data;

import java.io.Serializable;

/**
 * Created by masai on 2016/10/26.
 */

public class ResponseData implements Serializable{
    public static final String SUCCESS_CODE = "ok";

    private String status;
    private String message;
    private int errno;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getErrno() {
        return errno;
    }

    public void setErrno(int errno) {
        this.errno = errno;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public boolean isSuccess() {
        if (this.status.equals(SUCCESS_CODE)) {
            return true;
        } else {
            return false;
        }
    }
}
