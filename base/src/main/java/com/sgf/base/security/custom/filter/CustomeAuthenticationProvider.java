package com.sgf.base.security.custom.filter;

import com.sgf.base.constant.RedisConstant;
import com.sgf.base.exception.ImageCodeException;
import com.sgf.base.utils.SpringContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.authentication.dao.SaltSource;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.authentication.encoding.PlaintextPasswordEncoder;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.Assert;

/**
 * Created by sgf on 2018\1\18 0018.
 */
public class CustomeAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {
    private static final Logger logger = LoggerFactory.getLogger(CustomeAuthenticationProvider.class);

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    // ~ Static fields/initializers
    // =====================================================================================

    /**
     * The plaintext password used to perform
     * {@link PasswordEncoder#isPasswordValid(String, String, Object)} on when the user is
     * not found to avoid SEC-2056.
     */
    private static final String USER_NOT_FOUND_PASSWORD = "userNotFoundPassword";

    // ~ Instance fields
    // ================================================================================================

    private PasswordEncoder passwordEncoder;

    /**
     * The password used to perform
     * {@link PasswordEncoder#isPasswordValid(String, String, Object)} on when the user is
     * not found to avoid SEC-2056. This is necessary, because some
     * {@link PasswordEncoder} implementations will short circuit if the password is not
     * in a valid format.
     */
    private String userNotFoundEncodedPassword;

    private SaltSource saltSource;

    private UserDetailsService userDetailsService;

    public CustomeAuthenticationProvider() {
        setPasswordEncoder(new PlaintextPasswordEncoder());
    }

    /**
     * 自定义登录匹配规则
     * @param userDetails
     * @param authentication
     * @throws AuthenticationException
     */
    protected void additionalAuthenticationChecks(UserDetails userDetails,UsernamePasswordAuthenticationToken authentication)
            throws AuthenticationException {
        Object salt = null;

        if (this.saltSource != null) {
            salt = this.saltSource.getSalt(userDetails);
        }

        if (authentication.getCredentials() == null || authentication.getCredentials() == "") {
            String tip = userDetails.getUsername() +  " 凭证为空！";
            logger.debug(tip);

            throw new BadCredentialsException(messages.getMessage(
                    "AbstractUserDetailsAuthenticationProvider.badCredentials",tip));
        }

        String presentedPassword = authentication.getCredentials().toString();

        if (!passwordEncoder.isPasswordValid(userDetails.getPassword(),
                presentedPassword, salt)) {
            String tip = userDetails.getUsername() + " 凭证出错！";
            logger.debug(tip);

            throw new BadCredentialsException(messages.getMessage(
                    "AbstractUserDetailsAuthenticationProvider.badCredentials",tip));
        }

        CustomWebAuthenticationDetails details = (CustomWebAuthenticationDetails) authentication.getDetails();
        String username = userDetails.getUsername();
        String sessionId = details.getSessionId();

        boolean checkFlag = getCheckFlag(username, sessionId);
        if(!checkFlag){
            return;
        }

        String session_imageCode = details.getSession_imageCode();
        String imageCode = details.getImageCode();

        if(imageCode == null || session_imageCode == null) {
            String tip = userDetails.getUsername() +  " 验证码错误！";
            logger.debug(tip);
            throw new ImageCodeException(tip);
        }
        if(!imageCode.equalsIgnoreCase(session_imageCode)) {
            String tip = userDetails.getUsername() +  " 验证码错误！";
            logger.debug(tip);
            throw new ImageCodeException(tip);
        }
    }

    /**
     * 只有当用户名没有被锁定，且session没有被锁定时，才不需要进行验证码的校验
     * @param username
     * @param sessionId
     * @return
     */
    private boolean getCheckFlag(String username, String sessionId) {
        //是否需要校验验证码
        boolean checkFlag;

        boolean userCheck = false;
        boolean sessionCheck = false;

        if(null == stringRedisTemplate){
            stringRedisTemplate = SpringContextUtil.getBean("stringRedisTemplate");
        }

        if(null != username) {
            String userLockFlag = stringRedisTemplate.opsForValue().get(username + RedisConstant.LOGIN_FAIL_LOCK_USER);
            if(null != userLockFlag){
                userCheck = true;
            }else{
                userCheck = false;
            }
        }
        if(null != sessionId) {
            String sessionLockFlag = stringRedisTemplate.opsForValue().get(sessionId + RedisConstant.LOGIN_FAIL_LOCK_SESSION);
            if(null != sessionLockFlag){
                sessionCheck = true;
            }else{
                sessionCheck = false;
            }
        }

        if(!userCheck && !sessionCheck){
            checkFlag =  false;
        }else{
            checkFlag = true;
        }
        return checkFlag;
    }

    protected void doAfterPropertiesSet() throws Exception {
        Assert.notNull(this.userDetailsService, "A UserDetailsService must be set");
    }

    protected final UserDetails retrieveUser(String username,
                                             UsernamePasswordAuthenticationToken authentication)
            throws AuthenticationException {
        UserDetails loadedUser;

        try {
            loadedUser = this.getUserDetailsService().loadUserByUsername(username);
        }
        catch (UsernameNotFoundException notFound) {
            if (authentication.getCredentials() != null) {
                String presentedPassword = authentication.getCredentials().toString();
                passwordEncoder.isPasswordValid(userNotFoundEncodedPassword,
                        presentedPassword, null);
            }
            throw notFound;
        }
        catch (Exception repositoryProblem) {
            throw new InternalAuthenticationServiceException(
                    repositoryProblem.getMessage(), repositoryProblem);
        }

        if (loadedUser == null) {
            throw new InternalAuthenticationServiceException(
                    "UserDetailsService returned null, which is an interface contract violation");
        }
        return loadedUser;
    }

    /**
     * Sets the PasswordEncoder instance to be used to encode and validate passwords. If
     * not set, the password will be compared as plain text.
     * <p>
     * For systems which are already using salted password which are encoded with a
     * previous release, the encoder should be of type
     * {@code org.springframework.security.authentication.encoding.PasswordEncoder}.
     * Otherwise, the recommended approach is to use
     * {@code org.springframework.security.crypto.password.PasswordEncoder}.
     *
     * @param passwordEncoder must be an instance of one of the {@code PasswordEncoder}
     * types.
     */
    public void setPasswordEncoder(Object passwordEncoder) {
        Assert.notNull(passwordEncoder, "passwordEncoder cannot be null");

        if (passwordEncoder instanceof PasswordEncoder) {
            setPasswordEncoder((PasswordEncoder) passwordEncoder);
            return;
        }

        if (passwordEncoder instanceof org.springframework.security.crypto.password.PasswordEncoder) {
            final org.springframework.security.crypto.password.PasswordEncoder delegate = (org.springframework.security.crypto.password.PasswordEncoder) passwordEncoder;
            setPasswordEncoder(new PasswordEncoder() {
                public String encodePassword(String rawPass, Object salt) {
                    checkSalt(salt);
                    return delegate.encode(rawPass);
                }

                public boolean isPasswordValid(String encPass, String rawPass, Object salt) {
                    checkSalt(salt);
                    return delegate.matches(rawPass, encPass);
                }

                private void checkSalt(Object salt) {
                    Assert.isNull(salt,
                            "Salt value must be null when used with crypto module PasswordEncoder");
                }
            });

            return;
        }

        throw new IllegalArgumentException(
                "passwordEncoder must be a PasswordEncoder instance");
    }

    private void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        Assert.notNull(passwordEncoder, "passwordEncoder cannot be null");

        this.userNotFoundEncodedPassword = passwordEncoder.encodePassword(
                USER_NOT_FOUND_PASSWORD, null);
        this.passwordEncoder = passwordEncoder;
    }

    protected PasswordEncoder getPasswordEncoder() {
        return passwordEncoder;
    }

    /**
     * The source of salts to use when decoding passwords. <code>null</code> is a valid
     * value, meaning the <code>DaoAuthenticationProvider</code> will present
     * <code>null</code> to the relevant <code>PasswordEncoder</code>.
     * <p>
     * Instead, it is recommended that you use an encoder which uses a random salt and
     * combines it with the password field. This is the default approach taken in the
     * {@code org.springframework.security.crypto.password} package.
     *
     * @param saltSource to use when attempting to decode passwords via the
     * <code>PasswordEncoder</code>
     */
    public void setSaltSource(SaltSource saltSource) {
        this.saltSource = saltSource;
    }

    protected SaltSource getSaltSource() {
        return saltSource;
    }

    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    protected UserDetailsService getUserDetailsService() {
        return userDetailsService;
    }
}
