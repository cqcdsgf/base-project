package com.sgf.base.security.custome;

import com.sgf.base.constant.ImageCodeConstant;
import com.sgf.base.constant.LoginConstant;
import com.sgf.base.constant.RedisConstant;
import com.sgf.base.constant.SessionConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
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
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {
        String username = request.getParameter(LoginConstant.LOGIN_USERNAME);
        HttpSession session = request.getSession(false);
        String sessionId = session.getId();

        stringRedisTemplate.opsForHash().delete(RedisConstant.LOGIN_FAIL_NUM_HASH,username);
        stringRedisTemplate.opsForHash().delete(RedisConstant.LOGIN_FAIL_NUM_HASH,sessionId);
        stringRedisTemplate.opsForSet().remove(RedisConstant.LOGIN_FAIL_LOCK_SET,username);
        stringRedisTemplate.opsForSet().remove(RedisConstant.LOGIN_FAIL_LOCK_SET,sessionId);

        //移除验证码
        String imageCodeType=request.getParameter(ImageCodeConstant.IMAGE_CODE_TYPE);
        session.removeAttribute(imageCodeType + "_" + SessionConstant.SESSION_IMAGECODE );

        response.setStatus(HttpServletResponse.SC_OK);
        handle(request, response, authentication);
        clearAuthenticationAttributes(request);
    }
}
