package com.sgf.base.security.custome;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by sgf on 2018\1\19 0019.
 */
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {

        //移除验证码
        request.getSession().removeAttribute("session_verifyObj");
        request.getSession().removeAttribute("session_verifyObjTime");
        response.setStatus(HttpServletResponse.SC_OK);

        handle(request, response, authentication);
        clearAuthenticationAttributes(request);
    }
}
