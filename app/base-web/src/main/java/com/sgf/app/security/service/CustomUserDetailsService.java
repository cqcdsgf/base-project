package com.sgf.app.security.service;

import com.sgf.app.security.dao.SysUserRepository;
import com.sgf.app.security.domain.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Created by sgf on 2017\12\26 0026.
 */
public class CustomUserDetailsService implements UserDetailsService{

    @Autowired
    SysUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username)  {
        SysUser user = userRepository.findByUsername(username);
        if(user == null){
            throw new UsernameNotFoundException(username + " 用户名不存在");
        }
        return user;
    }
}
