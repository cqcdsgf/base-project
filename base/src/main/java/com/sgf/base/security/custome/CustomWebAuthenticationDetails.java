package com.sgf.base.security.custome;

import com.sgf.base.constant.ImageCodeConstant;
import com.sgf.base.constant.LoginConstant;
import com.sgf.base.constant.SessionConstant;
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

    public CustomWebAuthenticationDetails(HttpServletRequest request) {
        super(request);

        String username = request.getParameter(LoginConstant.LOGIN_USERNAME);
        Object checkFlag = request.getSession(false).getAttribute(username + "_" + LoginConstant.LOGIN_FAIL_FLAG);

        HttpSession session = request.getSession(false);
        String sessionId = session.getId();
        //当需要校验图形验证码时，获取页面传递的图验证码，及session中保存的对应数据。
        //if(checkFlag!=null && (boolean)checkFlag){
            this.imageCode = request.getParameter(ImageCodeConstant.IMAGE_CODE);
            this.imageCodeType = request.getParameter(ImageCodeConstant.IMAGE_CODE_TYPE);

            this.session_imageCode = (String)session.getAttribute(sessionId + "_" + imageCodeType + "_" + SessionConstant.SESSION_IMAGECODE);
            String session_verifyTime = (String)session.getAttribute(sessionId + "_" + imageCodeType + "_" + SessionConstant.SESSION_IMAGETIME);
            if(session_verifyTime == null) {
                this.session_imageTime= 0L;
            } else {
                this.session_imageTime= Long.parseLong(session_verifyTime);
            }
        //}
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
}
