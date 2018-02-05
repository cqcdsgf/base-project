package com.sgf.base.security.domain;

import com.sgf.base.common.BaseEntity;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by sgf on 2018\2\5 0005.
 */
@Entity
@NoArgsConstructor
@Data
public class AuthPermission extends BaseEntity {
    private static final long serialVersionUID = 8433686206665788172L;

    private static final Logger logger = LoggerFactory.getLogger(AuthPermission.class);

    private String name;

    private String url;

    private String value;

    private String summary;






}
