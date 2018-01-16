package com.sgf.app.security.service;

import com.sgf.app.security.dao.SysUserRepository;
import com.sgf.app.security.domain.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by sgf on 2018/1/16.
 */
@Service
public class UserService {
    @Autowired
    SysUserRepository userRepository;

    public void registerUser(SysUser sysUser){

        userRepository.save(sysUser);

    }

    public SysUser findByUsername(String username){

        return userRepository.findByUsername(username);
    }

}
