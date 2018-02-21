package com.sgf.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sgf.base.common.BaseEntity;
import lombok.*;
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
@Getter
@Setter
@EqualsAndHashCode(exclude = {"permissions","users"})
@ToString(exclude = {"permissions","users"})
public class AuthRole extends BaseEntity {
    private static final long serialVersionUID = 8632534750238058622L;

    private static final Logger logger = LoggerFactory.getLogger(AuthRole.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    //角色名称
    @Column(unique = true)
    private String name;

    //摘要
    private String summary;

    @Transient
    private boolean isSelected;

    @ManyToMany(cascade = {CascadeType.PERSIST,CascadeType.MERGE,CascadeType.REFRESH})
    @JoinTable(name = "auth_role_permission",
            joinColumns ={@JoinColumn(name ="role_id")},
            inverseJoinColumns={@JoinColumn(name="permission_id")})
    private Set<AuthPermission> permissions = new HashSet<AuthPermission>();

    @JsonIgnore
    @ManyToMany(mappedBy = "roles")
    private Set<AuthUser> users = new HashSet<AuthUser>();


}
