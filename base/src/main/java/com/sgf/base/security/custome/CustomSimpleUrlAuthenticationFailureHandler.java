package com.sgf.base.security.custome;

import com.sgf.base.constant.BaseMessageConstant;
import com.sgf.base.constant.LoginConstant;
import com.sgf.base.exception.ImageCodeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

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

    @Value("base.security.login.failnum")
    private String failNum;

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
/*        request.getSession().removeAttribute("session_imgeCode");
        request.getSession().removeAttribute("session_imageTime");*/

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

        String username = request.getParameter(LoginConstant.LOGIN_USERNAME);
        checkFailNum(username,request);

        if (forwardToDestination) {
            request.setAttribute(BaseMessageConstant.ERROR_CODE,errorCode);
            request.setAttribute(BaseMessageConstant.ERROR_MESSAGE,exception.getMessage());
        } else {
            HttpSession session = request.getSession(false);
            if (session != null || allowSessionCreation) {
                request.getSession().setAttribute(WebAttributes.AUTHENTICATION_EXCEPTION, exception);
                request.getSession().setAttribute(BaseMessageConstant.ERROR_CODE,errorCode);
                request.getSession().setAttribute(BaseMessageConstant.ERROR_MESSAGE,exception.getMessage());
            }
        }
    }

    private void checkFailNum(String username, HttpServletRequest request) {
        if(StringUtils.isEmpty(username)){
            return;
        }

        AtomicInteger num = new AtomicInteger(0);
        AtomicInteger oldNum = (AtomicInteger)request.getSession().getAttribute(username + "_" + LoginConstant.LOGIN_FAIL_NUM);
        if(oldNum != null){
            num = oldNum;
        }

        num.addAndGet(1);
        request.getSession().setAttribute(username + "_" + LoginConstant.LOGIN_FAIL_NUM,num);

        int checkNum = new Integer(failNum).intValue();
        if(checkNum >5){
            request.getSession().setAttribute(username + "_" + LoginConstant.LOGIN_FAIL_FLAG,true);
        }

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
