package com.sgf.app.shiro.dao;

import com.sgf.app.shiro.domain.UserInfo;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Administrator on 2017\11\30 0030.
 */
public interface UserInfoDao  extends CrudRepository<UserInfo,Long> {
    /**通过username查找用户信息;*/
    public UserInfo findByUsername(String username);
}
