package com.sgf.base.security.custom.filter;

import com.sgf.base.constant.ImageCodeConstant;
import com.sgf.base.constant.LoginConstant;
import com.sgf.base.constant.SessionConstant;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by sgf on 2018\1\19 0019.
 * 自定义传输数据
 */
public class CustomWebAuthenticationDetails extends WebAuthenticationDetails {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private String imageCode;

    private String imageCodeType;

    private String session_imageCode;

    private String sessionId;

    public CustomWebAuthenticationDetails(HttpServletRequest request) {
        super(request);

        String username = request.getParameter(LoginConstant.LOGIN_USERNAME);
        Assert.notNull(username,"用户名不能为空！");
        HttpSession session = request.getSession(false);
        sessionId = session.getId();

        this.imageCode = request.getParameter(ImageCodeConstant.IMAGE_CODE);
        this.imageCodeType = request.getParameter(ImageCodeConstant.IMAGE_CODE_TYPE);

        if(StringUtils.isNotEmpty(imageCodeType)) {
            this.session_imageCode = (String) session.getAttribute(imageCodeType + SessionConstant.SESSION_IMAGECODE);
        }else{
            //todo 登录时，添加了新的验证码校验类型时，需要更改此部分程序
            this.session_imageCode = (String) session.getAttribute(ImageCodeConstant.IMAGE_CODE_TYPE_PERSONLOGIN + SessionConstant.SESSION_IMAGECODE);
        }


    }

    public String getImageCode(){
        return imageCode;
    }

    public String getSession_imageCode() {
        return session_imageCode;
    }

    public String getImageCodeType() {
        return imageCodeType;
    }

    @Override
    public String getSessionId() {
        return sessionId;
    }
}
