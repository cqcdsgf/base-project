package com.sgf.base.security.service;

import com.sgf.base.security.dao.SysUserRepository;
import com.sgf.base.security.domain.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by sgf on 2018/1/16.
 */
@Service
public class UserService {
/*    @Autowired
    SysUserRepository userRepository;*/

    public void registerUser(SysUser sysUser){

/*        userRepository.save(sysUser);*/

    }

    public SysUser findByUsername(String username){
/*
        return userRepository.findByUsername(username);*/
return null;
    }

}
