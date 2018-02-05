package com.sgf.base.security.service;

import com.sgf.base.common.BaseService;
import com.sgf.base.security.domain.AuthRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Created by sgf on 2018\2\5 0005.
 */
@Service
public class AuthRoleService extends BaseService<AuthRole,Long>{
    private static final Logger logger = LoggerFactory.getLogger(AuthRoleService.class);

}
