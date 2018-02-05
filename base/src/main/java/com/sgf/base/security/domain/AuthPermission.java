package com.sgf.base.security.domain;

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

    private String name;

    private String url;

    private String value;

    private String summary;

    @ManyToMany(mappedBy = "permissions")
    private Set<AuthRole> roles = new HashSet<AuthRole>();






}
