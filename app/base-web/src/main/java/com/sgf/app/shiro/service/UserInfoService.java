package com.sgf.app.shiro.service;

import com.sgf.app.shiro.dao.UserInfoDao;
import com.sgf.app.shiro.domain.UserInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by Administrator on 2017\11\30 0030.
 */
@Service
public class UserInfoService {
    //@Resource
    private UserInfoDao userInfoDao;

    public UserInfo findByUsername(String username) {
        System.out.println("UserInfoServiceImpl.findByUsername()");
        return userInfoDao.findByUsername(username);
    }
}
