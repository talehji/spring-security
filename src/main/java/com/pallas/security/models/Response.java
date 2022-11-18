package com.pallas.security.models;

/**
 * @author : Pallas
 * @project : Security
 * @created on : 16-November-2022
 * @email : talehji@gmail.com
 */

public class Response {

    private int status;
    private String message;
    private Object response;

    public Response() {
        status = 200;
        message = "Success";
    }

    public int getStatus() {
        return status;
    }

    public Response setStatus(int status) {
        this.status = status;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public Response setMessage(String message) {
        this.message = message;
        return this;
    }

    public Object getResponse() {
        return response;
    }

    public Response setResponse(Object response) {
        this.response = response;
        return this;
    }
}
