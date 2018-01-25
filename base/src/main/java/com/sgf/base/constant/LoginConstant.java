package com.sgf.base.constant;

/**
 * Created by sgf on 2018\1\22 0022.
 */
public class LoginConstant {

    /**登录错误**/
    //密码错误
    public static final String ERROR_CODE_LOGIN_EXCEPTION_BadCredentialsException ="BadCredentialsException";
    //账号错误
    public static final String ERROR_CODE_LOGIN_EXCEPTION_UsernameNotFoundException ="UsernameNotFoundException";
    //图形码错误
    public static final String ERROR_CODE_LOGIN_EXCEPTION_ImageCodeException ="ImageCodeException";
    //其他错误
    public static final String ERROR_CODE_LOGIN_EXCEPTION_OtherException ="OtherException";


    /** 登录 **/
    //用户名
    public static final String LOGIN_USERNAME ="username";
    //密码
    public static final String LOGIN_PASSWORD ="password";
    //图形校验码
    public static final String LOGIN_IMAGECODE ="imageCode";

    //同一登录名，登录失败次数
    public static final String LOGIN_USER_FAIL_NUM ="loginUserFailNum";
    //登录失败标志（为true时，需要进行图形验证码校验）
    public static final String LOGIN_USER_FAIL_FLAG ="loginUserFailFlag";


    public static final String LOGIN_FAIL_FLAG ="loginFailFlag";

}
