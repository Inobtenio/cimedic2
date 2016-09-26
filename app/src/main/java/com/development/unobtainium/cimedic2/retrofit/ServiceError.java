package com.development.unobtainium.cimedic2.retrofit;

/**
 * Created by unobtainium on 17/09/16.
 */
public class ServiceError {
    private String message;
    private int code;
    private String description;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
