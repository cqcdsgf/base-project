package com.sgf.base.security.custome;

import com.sgf.base.constant.ImageCodeConstant;
import com.sgf.base.constant.LoginConstant;
import com.sgf.base.constant.SessionConstant;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by sgf on 2018\1\19 0019.
 * 暂时无用
 */
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {

        String imageCodeType=request.getParameter(ImageCodeConstant.IMAGE_CODE_TYPE);
        String username = request.getParameter(LoginConstant.LOGIN_USERNAME);

        HttpSession session = request.getSession(false);
        String sessionId = session.getId();

        response.setStatus(HttpServletResponse.SC_OK);

        //移除验证码
        session.removeAttribute(sessionId + "_" + imageCodeType + "_" + SessionConstant.SESSION_IMAGECODE );
        session.removeAttribute(sessionId + "_" + imageCodeType + "_" + SessionConstant.SESSION_IMAGETIME );

        request.getSession(false).removeAttribute(username + "_" + LoginConstant.LOGIN_FAIL_NUM);
        request.getSession(false).removeAttribute(username + "_" + LoginConstant.LOGIN_FAIL_FLAG);

        handle(request, response, authentication);
        clearAuthenticationAttributes(request);
    }
}
