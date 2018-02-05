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
public class AuthRole extends BaseEntity {
    private static final long serialVersionUID = 8632534750238058622L;

    private static final Logger logger = LoggerFactory.getLogger(AuthRole.class);

    //角色名称
    private String name;

    //摘要
    private String summary;




}
