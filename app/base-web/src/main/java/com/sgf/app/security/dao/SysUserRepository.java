package com.sgf.app.security.dao;

import com.sgf.app.security.domain.SysUser;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by sgf on 2017\12\26 0026.
 */
public interface SysUserRepository extends JpaRepository<SysUser,Long>{

    SysUser findByUsername(String username);

}
