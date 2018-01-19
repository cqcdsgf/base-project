package com.sgf.base.security.custome;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;

/**
 * Created by sgf on 2018\1\18 0018.
 */
public class CustomePreAuthenticationChecks implements UserDetailsChecker {
    private static final Logger logger = LoggerFactory.getLogger(CustomePreAuthenticationChecks.class);
    protected MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();

    public void check(UserDetails user) {
        if (!user.isAccountNonLocked()) {
            String tip = "账户 " + user.getUsername() + " 被锁定！";
            logger.debug(tip);

            throw new LockedException(messages.getMessage(
                    "AbstractUserDetailsAuthenticationProvider.locked",tip));
        }

        if (!user.isEnabled()) {
            String tip = "账户 " + user.getUsername() + " 被禁用！";
            logger.debug(tip);

            throw new DisabledException(messages.getMessage(
                    "AbstractUserDetailsAuthenticationProvider.disabled",tip));
        }

        if (!user.isAccountNonExpired()) {
            String tip = "账户 " + user.getUsername() + " 已过期！";
            logger.debug(tip);

            throw new AccountExpiredException(messages.getMessage(
                    "AbstractUserDetailsAuthenticationProvider.expired",tip));
        }
    }
}
