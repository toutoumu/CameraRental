package com.dsfy.entity.http;

import com.google.gson.annotations.Expose;

public class JsonResponseHeader {
    @Expose
    private String message = "执行成功";
    @Expose
    private boolean isSuccess = true;
    @Expose
    private int code = 0;
    @Expose
    private String location;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    /**
     * 设置响应：是否成功，成功为True ，否则为False
     *
     * @param isSuccess
     */
    public void setSuccess(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

}
