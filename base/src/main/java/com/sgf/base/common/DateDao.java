package com.sgf.base.common;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.Date;

/**
 * Created by sgf on 2018\2\5 0005.
 */
public interface DateDao extends Repository<BaseEntity, String> {

    @Query(value = "SELECT NOW()", nativeQuery = true)
    Date getDate();

}
