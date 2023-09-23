package com.aiit.lj;

import java.io.Serializable;

public class User implements Serializable {
    private String loginName;
    private String password;

    public User(String loginName, String password) {
        this.loginName = loginName;
        this.password = password;
    }

    public String getLoginName() {
        return loginName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password=password;
    }
}
