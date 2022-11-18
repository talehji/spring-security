package com.pallas.security.models;

/**
 * @author : Pallas
 * @project : Security
 * @created on : 17-November-2022
 * @email : talehji@gmail.com
 */

public class UserSessionData {

    private String token;
    private long userId;

    public UserSessionData() {
    }

    public String getToken() {
        return token;
    }

    public UserSessionData setToken(String token) {
        this.token = token;
        return this;
    }

    public long getUserId() {
        return userId;
    }

    public UserSessionData setUserId(long userId) {
        this.userId = userId;
        return this;
    }
}
