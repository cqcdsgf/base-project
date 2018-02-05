package com.sgf.base.security.domain;

import com.sgf.base.common.BaseEntity;
import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Entity;

/**
 * Created by sgf on 2018\2\5 0005.
 */
@Entity
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class AuthUser extends BaseEntity {
    private static final long serialVersionUID = 6712090898187767157L;

    private static final Logger logger = LoggerFactory.getLogger(AuthUser.class);

    //用户名
    private String username;
    //密码
    private String password;
    //手机号
    private String phone;
    //电子邮箱
    private String email;





}
