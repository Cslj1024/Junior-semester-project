package com.aiit.lj;

import java.io.Serializable;

public class Admin extends User implements Serializable {
    private String username;

    public Admin(String loginName, String username, String password) {
        super(loginName, password);
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
