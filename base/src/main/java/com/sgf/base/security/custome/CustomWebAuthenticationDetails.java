package com.sgf.base.security.custome;

import com.sgf.base.constant.ImageCodeConstant;
import com.sgf.base.constant.LoginConstant;
import com.sgf.base.constant.SessionConstant;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by sgf on 2018\1\19 0019.
 */
public class CustomWebAuthenticationDetails extends WebAuthenticationDetails {

    private String imageCode;

    private String imageCodeType;

    private String session_imageCode;

    private long session_imageTime;



    public CustomWebAuthenticationDetails(HttpServletRequest request) {
        super(request);

        String username = request.getParameter(LoginConstant.LOGIN_USERNAME);
        Object checkFlag = request.getSession().getAttribute(username + "_" + LoginConstant.LOGIN_FAIL_FLAG);

        if(checkFlag!=null && (boolean)checkFlag){
            this.imageCode = request.getParameter(ImageCodeConstant.IMAGE_CODE);
            this.imageCodeType = request.getParameter(ImageCodeConstant.IMAGE_CODE_TYPE);

            String sessionId = request.getSession().getId();
            this.session_imageCode = (String)request.getSession().getAttribute(sessionId + "_" + imageCodeType + "_" + SessionConstant.SESSION_IMAGECODE);
            String session_verifyTime = (String)request.getSession().getAttribute(sessionId + "_" + imageCodeType + "_" + SessionConstant.SESSION_IMAGETIME);
            if(session_verifyTime == null) {
                this.session_imageTime= 0L;
            } else {
                this.session_imageTime= Long.parseLong(session_verifyTime);
            }
        }else{
            request.getSession().removeAttribute("session_verifyObj");
            request.getSession().removeAttribute("session_verifyObjTime");
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
}
