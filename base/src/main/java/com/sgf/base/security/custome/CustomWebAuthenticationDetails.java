package com.sgf.base.security.custome;

import com.sgf.base.constant.ImageCodeConstant;
import com.sgf.base.constant.LoginConstant;
import com.sgf.base.constant.SessionConstant;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by sgf on 2018\1\19 0019.
 * 自定义传输数据
 */
public class CustomWebAuthenticationDetails extends WebAuthenticationDetails {

    private String imageCode;

    private String imageCodeType;

    private String session_imageCode;

    private long session_imageTime;

    private boolean checkFlag = true;

    public CustomWebAuthenticationDetails(HttpServletRequest request) {
        super(request);

        HttpSession session = request.getSession();
        String sessionId = session.getId();

        String username = request.getParameter(LoginConstant.LOGIN_USERNAME);
        Object flag  = session.getAttribute(username + "_" + LoginConstant.LOGIN_FAIL_FLAG);

        if(null != flag){
            checkFlag = (Boolean)flag;
        }

        this.imageCode = request.getParameter(ImageCodeConstant.IMAGE_CODE);
        this.imageCodeType = request.getParameter(ImageCodeConstant.IMAGE_CODE_TYPE);

        String session_verifyTime;
        if(StringUtils.isNotEmpty(imageCodeType)) {
            this.session_imageCode = (String) session.getAttribute(sessionId + "_" + imageCodeType + "_" + SessionConstant.SESSION_IMAGECODE);
            session_verifyTime = (String) session.getAttribute(sessionId + "_" + imageCodeType + "_" + SessionConstant.SESSION_IMAGETIME);
        }else{
            //todo 登录时，添加了新的验证码校验类型时，需要更改此部分程序
            this.session_imageCode = (String) session.getAttribute(sessionId + "_" + ImageCodeConstant.IMAGE_CODE_TYPE_PERSONLOGIN + "_" + SessionConstant.SESSION_IMAGECODE);
            session_verifyTime = (String) session.getAttribute(sessionId + "_" + ImageCodeConstant.IMAGE_CODE_TYPE_PERSONLOGIN + "_" + SessionConstant.SESSION_IMAGETIME);
        }

        if(session_verifyTime == null) {
            this.session_imageTime= 0L;
        } else {
            this.session_imageTime= Long.parseLong(session_verifyTime);
        }

    }

    public String getImageCode(){
        return imageCode;
    }

    public String getSession_imageCode() {
        return session_imageCode;
    }

    public long getSession_imageTime() {
        return session_imageTime;
    }

    public String getImageCodeType() {
        return imageCodeType;
    }

    public boolean isCheckFlag() {
        return checkFlag;
    }
}
