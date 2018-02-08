package com.sgf.app.domain;

import com.google.common.collect.Sets;
import com.sgf.base.common.BaseEntity;
import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by sgf on 2018\2\5 0005.
 */
@Entity
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(exclude = "roles")
@ToString(exclude = "roles")
public class AuthUser extends BaseEntity {
    private static final long serialVersionUID = 6712090898187767157L;

    private static final Logger logger = LoggerFactory.getLogger(AuthUser.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    //用户名
    private String username;
    //密码
    private String password;
    //手机号
    private String phone;
    //电子邮箱
    private String email;

    @ManyToMany(cascade = {CascadeType.PERSIST,CascadeType.MERGE,CascadeType.REFRESH})
    @JoinTable(name = "auth_user_role",
            joinColumns ={@JoinColumn(name ="user_id")},
            inverseJoinColumns={@JoinColumn(name="role_id")})
    public Set<AuthRole> roles = Sets.newHashSet();





}
