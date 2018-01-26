package com.sgf.base.security.custome;

import com.sgf.base.constant.*;
import com.sgf.base.exception.ImageCodeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.util.Assert;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by sgf on 2018\1\19 0019.
 */
public class CustomSimpleUrlAuthenticationFailureHandler implements
        AuthenticationFailureHandler {
    private static final Logger logger = LoggerFactory.getLogger(CustomSimpleUrlAuthenticationFailureHandler.class);

    @Value("${security.login.checkImageCode}")
    private boolean checkImageCode;

    @Value("${security.login.user.failnum}")
    private int userFailNum;

    @Value("${security.login.session.failnum}")
    private int sessionFailNum;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private String defaultFailureUrl;
    private boolean forwardToDestination = false;
    private boolean allowSessionCreation = true;
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    public CustomSimpleUrlAuthenticationFailureHandler() {
    }

    public CustomSimpleUrlAuthenticationFailureHandler(String defaultFailureUrl) {
        setDefaultFailureUrl(defaultFailureUrl);
    }

    /**
     * Performs the redirect or forward to the {@code defaultFailureUrl} if set, otherwise
     * returns a 401 error code.
     * <p>
     * If redirecting or forwarding, {@code saveException} will be called to cache the
     * exception for use in the target view.
     */
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response, AuthenticationException exception)
            throws IOException, ServletException {

        //移除验证码
        HttpSession session = request.getSession(false);
        String imageCodeType=request.getParameter(ImageCodeConstant.IMAGE_CODE_TYPE);
        session.removeAttribute(imageCodeType + "_" + SessionConstant.SESSION_IMAGECODE );

        if (defaultFailureUrl == null) {
            logger.debug("No failure URL set, sending 401 Unauthorized error");

            response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
                    "Authentication Failed: " + exception);
        } else {
            saveException(request, exception);

            if (forwardToDestination) {
                logger.debug("Forwarding to " + defaultFailureUrl);

                request.getRequestDispatcher(defaultFailureUrl)
                        .forward(request, response);
            } else {
                logger.debug("Redirecting to " + defaultFailureUrl);
                redirectStrategy.sendRedirect(request, response, defaultFailureUrl);
            }
        }
    }

    /**
     * Caches the {@code AuthenticationException} for use in view rendering.
     * <p>
     * If {@code forwardToDestination} is set to true, request scope will be used,
     * otherwise it will attempt to store the exception in the session. If there is no
     * session and {@code allowSessionCreation} is {@code true} a session will be created.
     * Otherwise the exception will not be stored.
     */
    protected final void saveException(HttpServletRequest request,
                                       AuthenticationException exception) {
        String errorCode = getErrorCode(exception);

        if(checkImageCode) {
            String username = request.getParameter(LoginConstant.LOGIN_USERNAME);
            checkFailNum(username, request);
        }

        if (forwardToDestination) {
            request.setAttribute(BaseMessageConstant.ERROR_CODE,errorCode);
            request.setAttribute(BaseMessageConstant.ERROR_MESSAGE,exception.getMessage());
        } else {
            HttpSession session = request.getSession(false);
            if (session != null || allowSessionCreation) {
                request.getSession(false).setAttribute(WebAttributes.AUTHENTICATION_EXCEPTION, exception);
                request.getSession(false).setAttribute(BaseMessageConstant.ERROR_CODE,errorCode);
                request.getSession(false).setAttribute(BaseMessageConstant.ERROR_MESSAGE,exception.getMessage());
            }
        }
    }

    private void checkFailNum(String username, HttpServletRequest request) {
        Assert.notNull(username,"用户名不能为空!");

        HttpSession session = request.getSession(false);
        String sessionId = session.getId();

        AtomicInteger userFailnum = new AtomicInteger(0);
        AtomicInteger sessionFailnum = new AtomicInteger(0);
        Object oldUserFailNum = stringRedisTemplate.opsForHash().get(RedisConstant.LOGIN_FAIL_NUM_HASH,username);
        Object oldSessionFailNum =stringRedisTemplate.opsForHash().get(RedisConstant.LOGIN_FAIL_NUM_HASH,sessionId);

        if(null != oldUserFailNum){
            userFailnum =   new AtomicInteger(new Integer((String)(oldUserFailNum)));
        }
        if(null != oldSessionFailNum){
            sessionFailnum = new AtomicInteger(new Integer((String)(oldSessionFailNum)));
        }
        userFailnum.addAndGet(1);
        sessionFailnum.addAndGet(1);

        stringRedisTemplate.opsForHash().put(RedisConstant.LOGIN_FAIL_NUM_HASH,username,userFailnum.toString());
        stringRedisTemplate.opsForHash().put(RedisConstant.LOGIN_FAIL_NUM_HASH,sessionId,sessionFailnum.toString());

        if(userFailnum.intValue() > userFailNum){
            stringRedisTemplate.opsForSet().add(RedisConstant.LOGIN_FAIL_LOCK_SET,username);
        }
        if(sessionFailnum.intValue() > sessionFailNum){
            stringRedisTemplate.opsForSet().add(RedisConstant.LOGIN_FAIL_LOCK_SET,sessionId);
        }

        //移除验证码
        String imageCodeType=request.getParameter(ImageCodeConstant.IMAGE_CODE_TYPE);
        session.removeAttribute(imageCodeType + "_" + SessionConstant.SESSION_IMAGECODE );
    }

    private String getErrorCode(AuthenticationException exception) {
        String result;
        if(exception instanceof BadCredentialsException){
            result = LoginConstant.ERROR_CODE_LOGIN_EXCEPTION_BadCredentialsException;
        }else if(exception instanceof UsernameNotFoundException){
            result = LoginConstant.ERROR_CODE_LOGIN_EXCEPTION_UsernameNotFoundException;
        }else if(exception instanceof ImageCodeException){
            result = LoginConstant.ERROR_CODE_LOGIN_EXCEPTION_ImageCodeException;
        }else{
            result = LoginConstant.ERROR_CODE_LOGIN_EXCEPTION_OtherException;
        }
        return result;
    }

    /**
     * The URL which will be used as the failure destination.
     *
     * @param defaultFailureUrl the failure URL, for example "/loginFailed.jsp".
     */
    public void setDefaultFailureUrl(String defaultFailureUrl) {
        Assert.isTrue(UrlUtils.isValidRedirectUrl(defaultFailureUrl), "'"
                + defaultFailureUrl + "' is not a valid redirect URL");
        this.defaultFailureUrl = defaultFailureUrl;
    }

    protected boolean isUseForward() {
        return forwardToDestination;
    }

    /**
     * If set to <tt>true</tt>, performs a forward to the failure destination URL instead
     * of a redirect. Defaults to <tt>false</tt>.
     */
    public void setUseForward(boolean forwardToDestination) {
        this.forwardToDestination = forwardToDestination;
    }

    /**
     * Allows overriding of the behaviour when redirecting to a target URL.
     */
    public void setRedirectStrategy(RedirectStrategy redirectStrategy) {
        this.redirectStrategy = redirectStrategy;
    }

    protected RedirectStrategy getRedirectStrategy() {
        return redirectStrategy;
    }

    protected boolean isAllowSessionCreation() {
        return allowSessionCreation;
    }

    public void setAllowSessionCreation(boolean allowSessionCreation) {
        this.allowSessionCreation = allowSessionCreation;
    }
}
