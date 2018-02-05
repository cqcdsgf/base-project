package com.sgf.base.security.dao;

import com.sgf.base.common.BaseDao;
import com.sgf.base.security.domain.AuthPermission;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by sgf on 2018\2\5 0005.
 */
public interface AuthPermissionDao extends BaseDao<AuthPermission,Long> {


}
