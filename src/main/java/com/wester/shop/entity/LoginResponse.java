package com.wester.shop.entity;

import com.wester.shop.generate.User;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class LoginResponse {
    private boolean login;
    private User user;

    public static LoginResponse alreadyLogin(User user) {
        return new LoginResponse(true, user);
    }
    public static LoginResponse notLogin() {
        return new LoginResponse(false, null);
    }

    public LoginResponse() {

    }

    public LoginResponse(boolean login, User user) {
        this.login = login;
        this.user = user;
    }

    public boolean isLogin() {
        return login;
    }

    public User getUser() {
        return deepClone(user);
    }

    public static <T> T deepClone(T object) {
        if (object == null) {
            return null;
        }
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            oos.close();
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            T clone = (T) ois.readObject();
            ois.close();
            return clone;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
