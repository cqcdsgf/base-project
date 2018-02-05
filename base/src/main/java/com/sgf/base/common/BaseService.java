package com.sgf.base.common;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
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

    public T findOne(ID id) {
        return this.dao.findOne(id);
    }

    public T findOne(Specification<T> spec) {
        return this.dao.findOne(spec);
    }

    public T findOne(Example<T> example) {
        return this.dao.findOne(example);
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
    public T save(T entity) {
        Validate.notNull(entity, "save操作时，entity不能为空");

        return this.dao.saveAndFlush(entity);
    }

    @Transactional(readOnly = false)
    public void save(Iterable<T> entities) {
        Validate.notNull(entities, "save操作时，entities不能为空");

        this.dao.save(entities);
    }

    @Transactional(readOnly = false)
    public void delete(T entity){
        Validate.notNull(entity, "delete操作时，entity不能为空");

        this.dao.delete(entity);

    }

    @Transactional(readOnly = false)
    public void delete(ID id){
        Validate.notNull(id, "delete操作时，id不能为空");

        this.dao.delete(id);
    }

    @Transactional(readOnly = false)
    public void delete(Iterable<? extends T> entities){
        Validate.notNull(entities, "delete操作时，entities不能为空");

        this.dao.delete(entities);

    }

    @Transactional(readOnly = true)
    public boolean exists(ID id){
        Validate.notNull(id, "exists操作时，id不能为空");

        return this.dao.exists(id);
    }

    @Transactional(readOnly = true)
    public boolean exists(Example<T> example){
        Validate.notNull(example, "exists操作时，example不能为空");

        return this.dao.exists(example);
    }

}
