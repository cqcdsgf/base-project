package com.sgf.app.role;

import com.sgf.app.domain.AuthRole;
import com.sgf.app.permission.AuthPermissionDao;
import com.sgf.base.common.BaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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


}
