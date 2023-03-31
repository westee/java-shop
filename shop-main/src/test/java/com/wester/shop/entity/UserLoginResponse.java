package com.wester.shop.entity;

import com.wester.shop.generate.User;

public class UserLoginResponse {
    String cookie;
    User user;

    public UserLoginResponse() {}

    public UserLoginResponse(String cookie, User user) {
        this.cookie = cookie;
        this.user = user;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
