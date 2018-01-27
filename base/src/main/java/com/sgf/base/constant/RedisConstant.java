package com.sgf.base.constant;

/**
 * Created by sgf on 2018\1\25 0025.
 */
public class RedisConstant {

    //锁定用户的 key = %username%_login_fail_lock_user
    public static final String LOGIN_FAIL_LOCK_USER = "_login_fail_lock";
    //锁定session的 key = %sessionId%_login_fail_lock_session
    public static final String LOGIN_FAIL_LOCK_SESSION ="_login_fail_lock";
    //登录失败多少次后，才锁定用户的 key = %username%_login_fail_num_user
    public static final String LOGIN_FAIL_NUM_USER ="_login_fail_num";
    //登录失败多少次后，才锁定session的 key = %username%_login_fail_num_session
    public static final String LOGIN_FAIL_NUM_SESSION ="_login_fail_num";
    //锁定标志
    public static final String LOGIN_FAIL_LOCK_FLAG ="lock";





}
