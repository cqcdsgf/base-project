package com.sgf.base.common;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by sgf on 2018\2\5 0005.
 */
public abstract class BaseService<T extends BaseEntity, ID extends Serializable> {
    private static final Logger logger = LoggerFactory.getLogger(BaseService.class);

    @Autowired
    protected BaseDao<T, ID> dao;

    public long count(Specification<T> spec) {
        return this.dao.count(spec);
    }

    public T find(ID id) {
        return this.dao.findOne(id);
    }

    public T findOne(Specification<T> spec) {
        return this.dao.findOne(spec);
    }

    public List<T> findAll() {
        return this.dao.findAll();
    }

    public List<T> findAll(Iterable<ID> ids) {
        return this.dao.findAll(ids);
    }

    public List<T> findAll(Sort sort) {
        return this.dao.findAll(sort);
    }

    public List<T> findAll(Specification<T> spec) {
        return this.dao.findAll(spec);
    }

    public List<T> findAll(Specification<T> spec, Sort sort) {
        return this.dao.findAll(spec, sort);
    }

    public Page<T> findAll(Pageable pageable) {
        return this.dao.findAll(pageable);
    }

    public Page<T> findAll(Specification<T> spec, Pageable pageable) {
        return this.dao.findAll(spec, pageable);
    }

    @Transactional(readOnly = false)
    public void create(T entity) {
        Validate.notNull(entity, "entity不能为空");
        Validate.isTrue(null == entity.getId(), "id必须为空");

/*        Date date = this.dateDao.getDate();
        entity.setGmtCreate(date);
        entity.setGmtModified(date);*/
        this.dao.save(entity);
    }

    @Transactional(readOnly = false)
    public void update(T entity) {
        Validate.notNull(entity, "entity不能为空");
        Validate.notNull(entity.getId(), "id不能为空");

/*        Date date = this.dateDao.getDate();
        entity.setGmtModified(date);*/

        this.dao.save(entity);
    }

/*    public Date getDate() {
        return this.dateDao.getDate();
    }*/


}
