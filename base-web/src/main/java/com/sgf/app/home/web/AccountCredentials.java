package com.sgf.app.home.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by sgf on 2018/2/25.
 */
public class AccountCredentials {
    private static final Logger logger = LoggerFactory.getLogger(AccountCredentials.class);

    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
