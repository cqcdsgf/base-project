package com.sgf.app.user;

import com.sgf.app.domain.AuthUser;
import com.sgf.base.common.BaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by sgf on 2018\2\5 0005.
 */
@Service
public class AuthUserService extends BaseService<AuthUser,Long>{
    private static final Logger logger = LoggerFactory.getLogger(AuthUserService.class);
    @Autowired
    AuthUserDao authUserDao;

    public AuthUser findByUsername(String username){

        return authUserDao.findByUsername(username);
    }

}
