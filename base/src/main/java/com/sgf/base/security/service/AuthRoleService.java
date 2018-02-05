package com.sgf.base.security.service;

import com.sgf.base.common.BaseService;
import com.sgf.base.security.dao.AuthPermissionDao;
import com.sgf.base.security.dao.AuthRoleDao;
import com.sgf.base.security.domain.AuthPermission;
import com.sgf.base.security.domain.AuthRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

/**
 * Created by sgf on 2018\2\5 0005.
 */
@Service
public class AuthRoleService extends BaseService<AuthRole,Long>{
    private static final Logger logger = LoggerFactory.getLogger(AuthRoleService.class);

    @Autowired
    AuthRoleDao authRoleDao;

    @Autowired
    AuthPermissionDao authPermissionDao;

    @Transactional
    public void updateAndRemove(){

        AuthRole authRole = authRoleDao.findOne(2L);
  /*      authRole.setSummary("sgf");

        authRole.getPermissions().clear();

        Long[] ids = new Long[2];
        ids[0] = 1L;
        ids[1] = 2L;
        List<AuthPermission> authPermissions = authPermissionDao.findAll(Arrays.asList(ids));

        authRole.getPermissions().addAll(authPermissions);*/

        Long[] ids = new Long[2];
        ids[0] = 4L;
        ids[1] = 5L;
        List<AuthPermission> authPermissions = authPermissionDao.findAll(Arrays.asList(ids));


       authRole.getPermissions().removeAll(authPermissions);


        authRoleDao.save(authRole);

    }

}
