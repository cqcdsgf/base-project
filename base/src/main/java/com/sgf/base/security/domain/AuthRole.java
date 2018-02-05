package com.sgf.base.security.domain;

import com.sgf.base.common.BaseEntity;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by sgf on 2018\2\5 0005.
 */
@Entity
@NoArgsConstructor
@Data
public class AuthRole extends BaseEntity {
    private static final long serialVersionUID = 8632534750238058622L;

    private static final Logger logger = LoggerFactory.getLogger(AuthRole.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    //角色名称
    private String name;

    //摘要
    private String summary;

    @ManyToMany(cascade = {CascadeType.PERSIST,CascadeType.MERGE,CascadeType.REFRESH})
    @JoinTable(name = "auth_role_permission",
            joinColumns ={@JoinColumn(name ="role_id")},
            inverseJoinColumns={@JoinColumn(name="permission_id")})
    private Set<AuthPermission> permissions = new HashSet<AuthPermission>();




}
