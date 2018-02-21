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
@EqualsAndHashCode(exclude = "roles")
@ToString(exclude = "roles")
public class AuthPermission extends BaseEntity {
    private static final long serialVersionUID = 8433686206665788172L;

    private static final Logger logger = LoggerFactory.getLogger(AuthPermission.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    //权限名称
    private String name;
    //url
    @Column(unique = true)
    private String url;
    //权限值
    private String value;
    //摘要
    private String summary;

    @Transient
    private boolean isSelected;

    @JsonIgnore
    @ManyToMany(mappedBy = "permissions",fetch = FetchType.EAGER)
    private Set<AuthRole> roles = new HashSet<AuthRole>();

}
