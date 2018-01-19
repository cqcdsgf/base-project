package com.sgf.base.security.custome;

import org.springframework.security.web.authentication.WebAuthenticationDetails;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by sgf on 2018\1\19 0019.
 */
public class CustomWebAuthenticationDetails extends WebAuthenticationDetails {

    private String imageCode;

    private String session_imageCode;

    private long session_imageTime;

    public CustomWebAuthenticationDetails(HttpServletRequest request) {
        super(request);
        this.imageCode = request.getParameter("imageCode");
        this.session_imageCode = (String)request.getSession().getAttribute("session_imageCode");
        String session_verifyTime = (String)request.getSession().getAttribute("session_imageTime");
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
}
