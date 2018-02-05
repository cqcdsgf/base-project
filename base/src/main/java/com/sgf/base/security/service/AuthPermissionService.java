package com.sgf.base.security.service;

import com.sgf.base.common.BaseService;
import com.sgf.base.security.domain.AuthPermission;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Created by sgf on 2018\2\5 0005.
 */
@Service
public class AuthPermissionService extends BaseService<AuthPermission,Long>{
    private static final Logger logger = LoggerFactory.getLogger(AuthPermissionService.class);

}
