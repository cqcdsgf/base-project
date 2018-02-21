package com.sgf.app.security.service;

import com.sgf.app.domain.AuthUser;
import com.sgf.app.user.AuthUserDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Created by sgf on 2018/2/21.
 */
public class ManagementUserDetailService implements UserDetailsService {
    private static final Logger logger = LoggerFactory.getLogger(ManagementUserDetailService.class);

    @Autowired
    AuthUserDao authUserDao;

    @Override
    public UserDetails loadUserByUsername(String username)  {
        AuthUser user = authUserDao.findByUsername(username);

        if(user == null){
            throw new UsernameNotFoundException(username + " 用户名不存在");
        }
        return user;
    }
}
