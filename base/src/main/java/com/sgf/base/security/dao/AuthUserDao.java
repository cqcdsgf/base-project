package com.sgf.base.security.dao;

import com.sgf.base.common.BaseDao;
import com.sgf.base.security.domain.AuthUser;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by sgf on 2018\2\5 0005.
 */
public interface AuthUserDao extends BaseDao<AuthUser,Long> {


}
