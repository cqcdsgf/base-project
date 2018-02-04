package com.sgf.base.demo.persist;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentDao extends JpaRepository<Student,String> {

    public Student findBySName(String sName);

}
