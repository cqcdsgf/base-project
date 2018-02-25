package com.sgf.app.home.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;

/**
 * Created by sgf on 2018/2/25.
 */
public class GrantedAuthorityImpl implements GrantedAuthority {
    private static final Logger logger = LoggerFactory.getLogger(GrantedAuthorityImpl.class);

    private String authority;

    public GrantedAuthorityImpl(String authority) {
        this.authority = authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    @Override
    public String getAuthority() {
        return this.authority;
    }
}
