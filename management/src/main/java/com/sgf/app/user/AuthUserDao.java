package com.sgf.app.user;

import com.sgf.app.domain.AuthUser;
import com.sgf.base.common.BaseDao;

/**
 * Created by sgf on 2018\2\5 0005.
 */
public interface AuthUserDao extends BaseDao<AuthUser,Long> {

    AuthUser findByUsername(String username);
}
